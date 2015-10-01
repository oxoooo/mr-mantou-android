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

package ooo.oxo.mr.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;

import ooo.oxo.mr.model.Image;

@SuppressWarnings("unused")
public class ImageViewBindingUtil {

    @BindingAdapter("bind:image")
    public static void loadUrl(ImageView view, Image image) {
        Glide.with(view.getContext())
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    @BindingAdapter({"bind:originalImage", "bind:thumbnail", "bind:listener"})
    public static void loadUrlWithThumbnail(ImageView view, Image image, String thumbnail,
                                            RequestListener listener) {
        DrawableRequestBuilder<String> thumbnailRequest = Glide.with(view.getContext())
                .load(thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

        //noinspection unchecked
        Glide.with(view.getContext())
                .load(image)
                .thumbnail(thumbnailRequest)
                .listener(listener)
                .into(new GlideDrawableImageViewTarget(view) {
                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL);
                    }
                });
    }

}
