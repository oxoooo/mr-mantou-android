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

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;
import java.util.Map;

import ooo.oxo.mr.api.ImageApi;
import ooo.oxo.mr.api.VersionApi;
import ooo.oxo.mr.databinding.MainActivityBinding;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.model.Version;
import ooo.oxo.mr.net.QiniuImageQueryBuilder;
import ooo.oxo.mr.rx.RxEndlessRecyclerView;
import ooo.oxo.mr.rx.RxList;
import ooo.oxo.mr.rx.RxNetworking;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements MainAdapter.Listener {

    private static final String TAG = "MainActivity";

    private ObservableArrayList<Image> images;

    private MainActivityBinding binding;

    private ImageApi imageApi;
    private VersionApi versionApi;

    private Observable<List<Image>> observableLoadLatest;
    private Observable<List<Image>> observableLoadBefore;
    private Observable<Version> observableCheckUpdate;

    private Bundle reenterState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        setSupportActionBar(binding.toolbar);

        images = MrSharedState.getInstance().getImages();

        binding.content.setAdapter(new MainAdapter(this, images, this));

        MrApplication application = MrApplication.from(this);

        imageApi = application.createApi(ImageApi.class);
        versionApi = application.createApi(VersionApi.class);

        createObservables();
        attachObservables();

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (reenterState != null) {
                    int index = reenterState.getInt("index", 0);

                    Image image = images.get(index);

                    MainAdapter.ViewHolder holder = (MainAdapter.ViewHolder) binding.content
                            .findViewHolderForLayoutPosition(index);

                    sharedElements.clear();
                    sharedElements.put(String.format("%s.image", image.id), holder.binding.image);

                    reenterState = null;
                }
            }
        });
    }

    private void createObservables() {
        Observable.Transformer<List<Image>, List<Image>> networkingIndicator =
                RxNetworking.bindRefreshing(binding.refresher);

        observableLoadLatest = Observable
                .defer(() -> images.isEmpty()
                        ? imageApi.latest(null)
                        : imageApi.since(null, images.get(0).getUTCCreatedAt()))
                .doOnUnsubscribe(() -> Log.d("RxJava", "unsubscribe load latest"))
                .map(images -> {
                    // for a strange bug of pg sql
                    if (!images.isEmpty() && images.get(images.size() - 1).equals(images.get(0))) {
                        images.remove(images.size() - 1);
                    }

                    return images;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(networkingIndicator);

        observableLoadBefore = Observable
                .defer(() -> imageApi.before(null, images.get(images.size() - 1).getUTCCreatedAt()))
                .doOnUnsubscribe(() -> Log.d("RxJava", "unsubscribe load before"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(networkingIndicator);

        observableCheckUpdate = versionApi.check()
                .doOnUnsubscribe(() -> Log.d("RxJava", "unsubscribe check update"))
                .filter(version -> version.versionCode > BuildConfig.VERSION_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void attachObservables() {
        RxEndlessRecyclerView.reachesEnd(binding.content)
                .doOnUnsubscribe(() -> Log.d("RxJava", "unsubscribe recycler view"))
                .flatMap(avoid -> observableLoadBefore)
                .compose(bindToLifecycle())
                .subscribe(RxList.appendTo(images), this::showError);

        RxSwipeRefreshLayout.refreshes(binding.refresher)
                .doOnUnsubscribe(() -> Log.d("RxJava", "unsubscribe swipe refresh layout"))
                .flatMap(avoid -> observableLoadLatest)
                .compose(bindToLifecycle())
                .subscribe(RxList.prependTo(images), this::showError);

        observableLoadLatest
                .compose(bindToLifecycle())
                .subscribe(RxList.appendTo(images), this::showError);

        observableCheckUpdate
                .compose(bindToLifecycle())
                .subscribe(this::promptUpdate, error -> Log.e(TAG, "fail to check update", error));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.content.setAdapter(null);
    }

    private void showError(Throwable error) {
        Log.e(TAG, "error", error);
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void promptUpdate(Version version) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.update_available, version.versionName))
                .setMessage(TextUtils.isEmpty(version.changelog) ? null : version.changelog)
                .setNegativeButton(R.string.update_cancel, null)
                .setPositiveButton(R.string.update_confirm, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(version.url));
                    startActivity(intent);
                })
                .show();
    }

    @Override
    public void onImageClick(MainAdapter.ViewHolder holder) {
        Image image = images.get(holder.getAdapterPosition());

        Intent intent = new Intent(this, ViewerActivity.class);
        intent.putExtra("index", holder.getAdapterPosition());
        intent.putExtra("thumbnail", QiniuImageQueryBuilder.build(
                image.url, holder.binding.image.getWidth()));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, holder.binding.image, String.format("%s.image", image.id));

        startActivity(intent, options.toBundle());
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

        supportPostponeEnterTransition();

        reenterState = new Bundle(data.getExtras());

        final int index = reenterState.getInt("index", 0);

        binding.content.scrollToPosition(index);
        binding.content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                binding.content.getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
