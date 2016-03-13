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

package ooo.oxo.mr.net;

import android.content.Context;

import com.avos.avoscloud.AVFile;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

import java.io.InputStream;

public class AVFileLoader extends BaseGlideUrlLoader<AVFile> {

    public AVFileLoader(ModelLoader<GlideUrl, InputStream> concreteLoader) {
        super(concreteLoader);
    }

    @Override
    protected String getUrl(AVFile model, int width, int height) {
        return QiniuUtil.getUrl(model, width);
    }

    public static class Factory implements ModelLoaderFactory<AVFile, InputStream> {

        @Override
        public ModelLoader<AVFile, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new AVFileLoader(factories.buildModelLoader(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
        }

    }

}
