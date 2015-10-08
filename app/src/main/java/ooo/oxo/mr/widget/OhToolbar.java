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

package ooo.oxo.mr.widget;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

public class OhToolbar extends Toolbar {

    public OhToolbar(Context context) {
        super(context);
    }

    public OhToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OhToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void fadeIn() {
        animate().alpha(1).start();
    }

    public void fadeOut() {
        animate().alpha(0).start();
    }

}
