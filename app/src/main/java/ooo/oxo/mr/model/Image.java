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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Image implements Parcelable {

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String id;

    public Date createdAt;

    public String url;

    public String channel;

    public Meta meta;

    public Image() {
    }

    protected Image(Parcel in) {
        id = in.readString();
        url = in.readString();
        channel = in.readString();
    }

    public String getUTCCreatedAt() {
        return DATE_FORMAT.format(createdAt);
    }

    @Override
    public String toString() {
        return String.format("Image#%s[createdAt: %s, url: %s, channel: %s, meta: %s]",
                id, getUTCCreatedAt(), url, channel, meta);
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof Image && id.equals(((Image) o).id));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(channel);
    }

    public static class Meta implements Parcelable {

        public static final Creator<Meta> CREATOR = new Creator<Meta>() {
            @Override
            public Meta createFromParcel(Parcel in) {
                return new Meta(in);
            }

            @Override
            public Meta[] newArray(int size) {
                return new Meta[size];
            }
        };

        public String type;

        public int width;

        public int height;

        public List<Color> colors;

        public Meta() {
        }

        protected Meta(Parcel in) {
            type = in.readString();
            width = in.readInt();
            height = in.readInt();
            colors = in.createTypedArrayList(Color.CREATOR);
        }

        @Override
        public String toString() {
            return String.format("Meta[type: %s, width: %d, height: %d, colors: %s]",
                    type, width, height, colors.toString());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type);
            dest.writeInt(width);
            dest.writeInt(height);
            dest.writeTypedList(colors);
        }

    }

}
