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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import ooo.oxo.mr.databinding.ViewerFragmentBinding;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.net.GlideRequestListenerAdapter;
import ooo.oxo.mr.rx.RxFileInputStream;
import ooo.oxo.mr.rx.RxFiles;
import ooo.oxo.mr.rx.RxGlide;
import ooo.oxo.mr.rx.RxWallpaperManager;
import ooo.oxo.mr.util.EnterTransitionCompat;
import ooo.oxo.mr.util.SimpleTransitionListener;
import ooo.oxo.mr.util.ToastUtil;
import ooo.oxo.mr.widget.RxBindingFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
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

    private static Observable<File> ensureExternalDirectory(String name) {
        return RxFiles.mkdirsIfNotExists(new File(Environment.getExternalStorageDirectory(), name));
    }

    private static String makeFileName(Image image) {
        return String.format("%d.%s", image.createdAt.getTime(), image.meta.type);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        image = getArguments().getParcelable("image");
        thumbnail = getArguments().getString("thumbnail");

        hasSharedElementTransition = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                !TextUtils.isEmpty(thumbnail);

        setHasOptionsMenu(true);

        observableDownload = RxGlide.download(Glide.with(this), image.url);

        observableSave = ensureExternalDirectory("Mr.Mantou")
                .map(directory -> new File(directory, makeFileName(image)))
                .withLatestFrom(observableDownload, (Func2<File, File, Pair<File, File>>) Pair::new)
                .flatMap(pair -> RxFiles.copy(pair.second, pair.first));
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
                    ToastUtil.shorts(getContext(), R.string.save_success, file.getPath());
                }, e -> {
                    Log.e(TAG, "Failed to save picture", e);
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
                }, e -> {
                    Log.e(TAG, "Failed to save picture", e);
                });
    }

    private void setWallpaper() {
        observableDownload
                .flatMap(RxFileInputStream::create)
                .flatMap(stream -> RxWallpaperManager.setStream(getContext(), stream))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(avoid -> {
                    ToastUtil.shorts(getContext(), R.string.set_wallpaper_success);
                }, e -> {
                    Log.e(TAG, "Failed to save picture", e);
                });
    }

    private void notifyMediaScanning(File file) {
        MediaScannerConnection.scanFile(
                getContext().getApplicationContext(),
                new String[]{file.getPath()}, null, null);
    }

}
