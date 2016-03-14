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
import android.support.annotation.Keep;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import ooo.oxo.mr.util.WindowInsetsCompatUtil;

@Keep
@SuppressWarnings("unused")
public class InsetsScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {

    public InsetsScrollingViewBehavior() {
    }

    public InsetsScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout layout,
                                                  View child, WindowInsetsCompat insets) {
        return ViewCompat.onApplyWindowInsets(child, WindowInsetsCompatUtil.copyExcluded(
                insets, Gravity.TOP));
    }

}
