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
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import ooo.oxo.mr.R;

public class OhRecyclerView extends RecyclerView {

    public OhRecyclerView(Context context) {
        this(context, null);
    }

    public OhRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OhRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final int padding;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OhRecyclerView);
        padding = a.getDimensionPixelOffset(R.styleable.OhRecyclerView_padding, 0);
        a.recycle();

        setPadding(padding, padding, padding, padding);

        ViewCompat.setOnApplyWindowInsetsListener(this, (v, insets) -> {
            final int l = padding + insets.getSystemWindowInsetLeft();
            final int r = padding + insets.getSystemWindowInsetRight();
            final int b = padding + insets.getSystemWindowInsetBottom();
            setPadding(l, padding, r, b);
            return insets.consumeSystemWindowInsets();
        });
    }

}
