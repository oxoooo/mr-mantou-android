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

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;

public class ImmersiveUtil {

    public static void enter(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            ImmersiveUtil19.enter(activity.getWindow().getDecorView());
        }
    }

    public static void exit(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            ImmersiveUtil19.exit(activity.getWindow().getDecorView());
        }
    }

    @TargetApi(19)
    private static class ImmersiveUtil19 {

        private static final int FLAG_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        public static void enter(View decor) {
            SystemUiVisibilityUtil.addFlags(decor, FLAG_IMMERSIVE);
        }

        public static void exit(View decor) {
            SystemUiVisibilityUtil.clearFlags(decor, FLAG_IMMERSIVE);
        }

    }

}
