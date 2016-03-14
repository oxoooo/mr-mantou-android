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

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtil {

    public static void show(Context context, int duration, @StringRes int resId, Object... args) {
        Toast.makeText(context, context.getString(resId, args), duration).show();
    }

    public static void show(Context context, int duration, String text, Object... args) {
        Toast.makeText(context, String.format(text, args), duration).show();
    }

    public static void shorts(Context context, @StringRes int resId, Object... args) {
        show(context, Toast.LENGTH_SHORT, resId, args);
    }

    public static void shorts(Context context, String text, Object... args) {
        show(context, Toast.LENGTH_SHORT, text, args);
    }

    public static void longs(Context context, @StringRes int resId, Object... args) {
        show(context, Toast.LENGTH_LONG, resId, args);
    }

    public static void longs(Context context, String text, Object... args) {
        show(context, Toast.LENGTH_LONG, text, args);
    }

}
