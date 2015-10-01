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

package ooo.oxo.mr.api;

import java.util.List;

import ooo.oxo.mr.model.Image;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ImageApi {

    @GET("v1/channels/images.json")
    Observable<List<Image>> latest(@Query("tags") String tags);

    @GET("v1/channels/images.json")
    Observable<List<Image>> before(@Query("tags") String tags,
                                   @Query("before") String before);

    @GET("v1/channels/images.json")
    Observable<List<Image>> since(@Query("tags") String tags,
                                  @Query("since") String since);

}
