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
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import ooo.oxo.mr.databinding.ViewerActivityBinding;
import ooo.oxo.mr.util.SimpleTransitionListener;
import ooo.oxo.mr.widget.PullBackLayout;

public class ViewerActivity extends RxAppCompatActivity implements PullBackLayout.Callback {

    private static final int SYSTEM_UI_BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private static final int SYSTEM_UI_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    private ViewerActivityBinding binding;

    private ColorDrawable background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.viewer_activity);

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
        getWindow().getDecorView().setBackground(background);
    }

    void fadeIn() {
        binding.toolbar.animate().alpha(1).start();
        showSystemUi();
    }

    void fadeOut() {
        binding.toolbar.animate().alpha(0).start();
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
        binding.getRoot().setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY);
    }

    private void hideSystemUi() {
        binding.getRoot().setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY | SYSTEM_UI_IMMERSIVE);
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

    @Override
    public boolean canPullDown() {
        ViewerFragment current = getCurrent();
        return current != null && !current.canScroll();
    }

    @Nullable
    private ViewerFragment getCurrent() {
        return (ViewerFragment) getSupportFragmentManager().findFragmentById(R.id.viewer);
    }

}
