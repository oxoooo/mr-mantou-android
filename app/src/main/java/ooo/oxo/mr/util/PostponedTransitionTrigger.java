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

package ooo.oxo.mr.util;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Starts the postponed transition of an Activity once image ready or time-outed
 */
public class PostponedTransitionTrigger implements RequestListener<String, GlideDrawable> {

    private static final String TAG = "PostponedStarter";

    private static final long TIMEOUT = 200;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private AppCompatActivity activity;

    private boolean isExecuted;

    public PostponedTransitionTrigger(AppCompatActivity activity) {
        this.activity = activity;
        this.handler.postDelayed(() -> {
            Log.d(TAG, "start transition after timeout");
            supportStartPostponedEnterTransition();
        }, TIMEOUT);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                               boolean isFirstResource) {
        Log.d(TAG, "start transition on exception");
        supportStartPostponedEnterTransition();
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model,
                                   Target<GlideDrawable> target, boolean isFromMemoryCache,
                                   boolean isFirstResource) {
        Log.d(TAG, "start transition on resource ready");
        supportStartPostponedEnterTransition();
        return false;
    }

    private void supportStartPostponedEnterTransition() {
        if (!isExecuted) {
            isExecuted = true;
            if (activity != null) {
                activity.supportStartPostponedEnterTransition();
            }
        }

        cancel();
    }

    public void cancel() {
        handler.removeCallbacksAndMessages(null);
        activity = null;
    }

}
