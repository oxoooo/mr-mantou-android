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

package ooo.oxo.mr.util;

import android.annotation.SuppressLint;
import android.support.v4.view.WindowInsetsCompat;
import android.view.Gravity;

public class WindowInsetsCompatUtil {

    public static WindowInsetsCompat copy(WindowInsetsCompat source) {
        return copyExcluded(source, Gravity.NO_GRAVITY);
    }

    @SuppressLint("RtlHardcoded")
    public static WindowInsetsCompat copyExcluded(WindowInsetsCompat source, int gravity) {
        int l = (gravity & Gravity.LEFT) == Gravity.LEFT ? 0 : source.getSystemWindowInsetLeft();
        int t = (gravity & Gravity.TOP) == Gravity.TOP ? 0 : source.getSystemWindowInsetTop();
        int r = (gravity & Gravity.RIGHT) == Gravity.RIGHT ? 0 : source.getSystemWindowInsetRight();
        int b = (gravity & Gravity.BOTTOM) == Gravity.BOTTOM ? 0 : source.getSystemWindowInsetBottom();
        return source.replaceSystemWindowInsets(l, t, r, b);
    }

}
