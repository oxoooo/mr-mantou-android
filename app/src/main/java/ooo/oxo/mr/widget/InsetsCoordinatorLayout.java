/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015-2016  XiNGRZ <xxx@oxo.ooo>
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

package ooo.oxo.mr.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import ooo.oxo.mr.util.WindowInsetsCompatUtil;

public class InsetsCoordinatorLayout extends CoordinatorLayout {

    public InsetsCoordinatorLayout(Context context) {
        this(context, null);
    }

    public InsetsCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetsCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewCompat.setOnApplyWindowInsetsListener(this, (v, insets) -> {
            if (insets.isConsumed()) {
                return insets;
            }

            for (int i = 0, count = getChildCount(); i < count; i++) {
                final View child = getChildAt(i);
                final Behavior b = getBehavior(child);

                if (b == null) {
                    continue;
                }

                //noinspection unchecked
                b.onApplyWindowInsets(this, child, WindowInsetsCompatUtil.copy(insets));
            }

            return insets;
        });
    }

    private static Behavior getBehavior(View child) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return lp.getBehavior();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= 21) {
            return super.fitSystemWindows(insets);
        }

        boolean consumed = false;

        for (int i = 0, count = getChildCount(); i < count; i++) {
            final View child = getChildAt(i);
            final Behavior b = getBehavior(child);

            if (b == null) {
                continue;
            }

            if (!(b instanceof WindowInsetsHandlingBehavior)) {
                continue;
            }

            if (((WindowInsetsHandlingBehavior) b).onApplyWindowInsets(this, child, insets)) {
                consumed = true;
            }
        }

        return consumed;
    }

    public interface WindowInsetsHandlingBehavior {

        boolean onApplyWindowInsets(CoordinatorLayout layout, View child, Rect insets);

    }

}
