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

package ooo.oxo.mr.model;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.Date;
import java.util.Map;

import ooo.oxo.mr.net.QiniuUtil;

@AVClassName("Image")
public class Image extends AVObject {

    public static final Creator CREATOR = AVObjectCreator.instance;

    private static final String PUBLISHED_AT = "publishedAt";

    private static final String FILE = "file";

    private static final String META = "meta";

    private static final String META_TYPE = "type";
    private static final String META_WIDTH = "width";
    private static final String META_HEIGHT = "height";

    @SuppressWarnings("unused")
    public Image() {
        super();
    }

    @SuppressWarnings("unused")
    public Image(Parcel in) {
        super(in);
    }

    public static AVQuery<Image> all() {
        return AVObject.getQuery(Image.class)
                .orderByDescending(PUBLISHED_AT);
    }

    public static AVQuery<Image> since(Image image) {
        return AVObject.getQuery(Image.class)
                .whereGreaterThan(PUBLISHED_AT, image.getPublishedAt())
                .orderByDescending(PUBLISHED_AT);
    }

    public Date getPublishedAt() {
        return getDate(PUBLISHED_AT);
    }

    public AVFile getFile() {
        return getAVFile(FILE);
    }

    public String getUrl() {
        return getFile().getUrl();
    }

    public String getUrl(int width) {
        return QiniuUtil.getUrl(getFile(), width);
    }

    public String getMime() {
        return getString("mime");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMeta() {
        return getMap(META);
    }

    public String getType() {
        return (String) getMeta().get(META_TYPE);
    }

    public int getWidth() {
        return (Integer) getMeta().get(META_WIDTH);
    }

    public int getHeight() {
        return (Integer) getMeta().get(META_HEIGHT);
    }

}
