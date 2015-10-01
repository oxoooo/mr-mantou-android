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

package ooo.oxo.mr.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

import static android.graphics.Color.parseColor;

public class Color implements Parcelable {

    public static final Creator<Color> CREATOR = new Creator<Color>() {
        @Override
        public Color createFromParcel(Parcel in) {
            return new Color(in);
        }

        @Override
        public Color[] newArray(int size) {
            return new Color[size];
        }
    };

    @ColorInt
    public final int value;

    public Color(@ColorInt int value) {
        this.value = value;
    }

    protected Color(Parcel in) {
        value = in.readInt();
    }

    public static Color parse(String colorString) {
        return new Color(parseColor(colorString));
    }

    @Override
    public String toString() {
        return String.format("#%06x", value & 0xFFFFFF);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
    }

}
