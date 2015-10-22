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

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;
import java.util.Map;

import ooo.oxo.library.widget.PullBackLayout;
import ooo.oxo.mr.databinding.ViewerActivityBinding;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.util.SimpleTransitionListener;
import ooo.oxo.mr.widget.ImmersiveUtil;

public class ViewerActivity extends RxAppCompatActivity implements PullBackLayout.Callback {

    private final ObservableArrayList<Image> images = MrSharedState.getInstance().getImages();

    private ViewerActivityBinding binding;

    private Adapter adapter;

    private final ObservableList.OnListChangedCallback<ObservableList<Image>> listener =
            new ObservableList.OnListChangedCallback<ObservableList<Image>>() {
                @Override
                public void onChanged(ObservableList<Image> sender) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(ObservableList<Image> sender,
                                               int positionStart, int itemCount) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeInserted(ObservableList<Image> sender,
                                                int positionStart, int itemCount) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeMoved(ObservableList<Image> sender,
                                             int fromPosition, int toPosition, int itemCount) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeRemoved(ObservableList<Image> sender,
                                               int positionStart, int itemCount) {
                    adapter.notifyDataSetChanged();
                }
            };

    private ColorDrawable background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.viewer_activity);

        setTitle(null);
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());

        binding.puller.setCallback(this);

        supportPostponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new SimpleTransitionListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                public void onTransitionEnd(Transition transition) {
                    getWindow().getEnterTransition().removeListener(this);
                    fadeIn();
                }
            });
        } else {
            fadeIn();
        }

        background = new ColorDrawable(Color.BLACK);
        binding.getRoot().setBackground(background);

        adapter = new Adapter();

        binding.pager.setAdapter(adapter);
        binding.pager.setCurrentItem(getIntent().getIntExtra("index", 0));
        binding.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    fadeOut();
                }
            }
        });

        images.addOnListChangedCallback(listener);

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                Image image = images.get(binding.pager.getCurrentItem());
                sharedElements.clear();
                sharedElements.put(String.format("%s.image", image.id), getCurrent().getSharedElement());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        images.removeOnListChangedCallback(listener);
    }

    void fadeIn() {
        binding.toolbar.fadeIn();
        showSystemUi();
    }

    void fadeOut() {
        binding.toolbar.fadeOut();
        hideSystemUi();
    }

    void toggleFade() {
        if (binding.toolbar.getAlpha() == 0) {
            fadeIn();
        } else {
            fadeOut();
        }
    }

    private void showSystemUi() {
        ImmersiveUtil.exit(binding.getRoot());
    }

    private void hideSystemUi() {
        ImmersiveUtil.enter(binding.getRoot());
    }

    @Override
    public void onPullStart() {
        fadeOut();
        showSystemUi();
    }

    @Override
    public void onPull(float progress) {
        progress = Math.min(1f, progress * 3f);
        background.setAlpha((int) (0xff * (1f - progress)));
    }

    @Override
    public void onPullCancel() {
        fadeIn();
    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }

    public ViewerFragment getCurrent() {
        return (ViewerFragment) adapter.instantiateItem(binding.pager, binding.pager.getCurrentItem());
    }

    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", binding.pager.getCurrentItem());
        setResult(RESULT_OK, data);

        showSystemUi();

        super.supportFinishAfterTransition();
    }

    private class Adapter extends FragmentStatePagerAdapter {

        public Adapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            Image image = images.get(position);

            Bundle arguments = new Bundle();
            arguments.putParcelable("image", image);

            if (position == getIntent().getIntExtra("index", 0)) {
                arguments.putString("thumbnail", getIntent().getStringExtra("thumbnail"));
            }

            Fragment fragment = new ViewerFragment();
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public int getCount() {
            return images.size();
        }

    }

}
