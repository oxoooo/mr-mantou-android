/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015  XiNGRZ <xxx@oxo.ooo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ooo.oxo.mr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;

import ooo.oxo.mr.databinding.ViewerFragmentBinding;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.net.GlideRequestListenerAdapter;
import ooo.oxo.mr.util.EnterTransitionCompat;
import ooo.oxo.mr.util.SimpleTransitionListener;
import ooo.oxo.mr.widget.RxBindingFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewerFragment extends RxBindingFragment<ViewerFragmentBinding> {

    private static final String TAG = "ViewerFragment";

    private Image image;
    private String thumbnail;

    private boolean hasSharedElementTransition = false;

    private boolean isTransitionExecuted = false;

    private View sharedElement;

    private Observable<File> observableDownload;
    private Observable<File> observableSave;

    public ViewerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = getArguments().getParcelable("image");
        thumbnail = getArguments().getString("thumbnail");

        hasSharedElementTransition = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                !TextUtils.isEmpty(thumbnail);

        setHasOptionsMenu(true);

        observableDownload = Observable.just(Glide.with(this).load(image.url))
                .map(request -> {
                    try {
                        return request.downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    } catch (InterruptedException | ExecutionException e) {
                        Log.e(TAG, "Failed to download photo", e);
                        return null;
                    }
                })
                .filter(file -> file != null);

        observableSave = observableDownload
                .map(src -> {
                    try {
                        return copy(src, makeExternalFile(String.format("%d.%s",
                                image.createdAt.getTime(), image.meta.type)));
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to save photo", e);
                        return null;
                    }
                })
                .filter(file -> file != null);
    }

    @Nullable
    @Override
    public ViewerFragmentBinding onCreateBinding(LayoutInflater inflater,
                                                 @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        return ViewerFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        binding.image.setSingleTapListener(((ViewerActivity) getActivity())::toggleFade);
        binding.image.setDoubleTapListener(((ViewerActivity) getActivity())::fadeOut);

        binding.setImage(image);

        sharedElement = binding.thumbnail;

        if (savedInstanceState != null) {
            isTransitionExecuted = savedInstanceState.getBoolean("transition_executed", false);
        }

        loadImage();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("transition_executed", isTransitionExecuted);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.viewer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                share();
                return true;
            case R.id.save:
                save();
                return true;
            case R.id.set_wallpaper:
                setWallpaper();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View getSharedElement() {
        return sharedElement;
    }

    private void startPostponedEnterTransition() {
        if (hasSharedElementTransition) {
            getActivity().supportStartPostponedEnterTransition();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void loadImage() {
        if (hasSharedElementTransition && !isTransitionExecuted) {
            isTransitionExecuted = true;
            loadThumbnail();
            EnterTransitionCompat.addListener(getActivity().getWindow(), new SimpleTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    EnterTransitionCompat.removeListener(getActivity().getWindow(), this);
                    loadFullImage();
                }
            });
        } else {
            loadFullImage();
        }
    }

    private void loadThumbnail() {
        Glide.with(this).load(thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade(0)
                .listener(new GlideRequestListenerAdapter<String, GlideDrawable>() {
                    @Override
                    protected void onComplete() {
                        startPostponedEnterTransition();
                    }
                })
                .into(binding.thumbnail);
    }

    private void loadFullImage() {
        Glide.with(this).load(image.url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade(0)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .listener(new GlideRequestListenerAdapter<String, GlideDrawable>() {
                    @Override
                    protected void onSuccess(GlideDrawable resource) {
                        sharedElement = binding.image;
                        fadeInFullImage();
                    }
                })
                .into(binding.image);
    }

    private void fadeInFullImage() {
        binding.image.animate().alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.thumbnail.setVisibility(View.GONE);
            }
        }).start();
    }

    private void save() {
        observableSave
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(file -> {
                    notifyMediaScanning(file);
                    Toast.makeText(
                            getContext(),
                            getString(R.string.save_success, file.getPath()),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void share() {
        observableSave
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(file -> {
                    notifyMediaScanning(file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/" + image.meta.type);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
                });
    }

    private void setWallpaper() {
        observableDownload
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(file -> {
                    try {
                        WallpaperManager.getInstance(getContext())
                                .setStream(new FileInputStream(file));
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to set wallpaper", e);
                    }
                });
    }

    private File copy(File from, File to) throws IOException {
        FileInputStream input = new FileInputStream(from);
        FileOutputStream output = new FileOutputStream(to);

        FileChannel inputChannel = input.getChannel();
        FileChannel outputChannel = output.getChannel();

        inputChannel.transferTo(0, inputChannel.size(), outputChannel);

        input.close();
        output.close();

        return to;
    }

    private File makeExternalFile(String name) throws IOException {
        File directory = new File(Environment.getExternalStorageDirectory(), "Mr.Mantou");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create external directory");
        }

        return new File(directory, name);
    }

    private void notifyMediaScanning(File file) {
        MediaScannerConnection.scanFile(getContext(), new String[]{file.getPath()}, null, null);
    }

}
