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
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class StatusBarHolderView extends View {

    private final int height;

    public StatusBarHolderView(Context context) {
        this(context, null);
    }

    public StatusBarHolderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        height = determineStatusBarHeight();
    }

    private int determineStatusBarHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }

        if (isInEditMode()) {
            return (int) (25 * getContext().getResources().getDisplayMetrics().density);
        }

        boolean hasStatusBar = false;

        TypedArray a = getContext().obtainStyledAttributes(new int[]{
                android.R.attr.windowTranslucentStatus
        });

        try {
            hasStatusBar = a.getBoolean(0, false);
        } finally {
            a.recycle();
        }

        int resId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (hasStatusBar && resId > 0) {
            return getContext().getResources().getDimensionPixelSize(resId);
        } else {
            return 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

}
