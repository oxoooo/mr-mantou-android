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

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class GlideRequestListenerAdapter<T, R> implements RequestListener<T, R> {

    @Override
    public boolean onException(Exception e, T model, Target<R> target, boolean isFirstResource) {
        onComplete();
        return false;
    }

    @Override
    public boolean onResourceReady(R resource, T model, Target<R> target,
                                   boolean isFromMemoryCache, boolean isFirstResource) {
        onComplete();
        onSuccess(resource);
        return false;
    }

    protected void onComplete() {
    }

    protected void onSuccess(R resource) {
    }

}
