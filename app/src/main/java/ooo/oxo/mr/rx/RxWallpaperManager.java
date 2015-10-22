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

package ooo.oxo.mr.rx;

import android.app.WallpaperManager;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import rx.Observable;

public class RxWallpaperManager {

    public static Observable<Void> setStream(Context context, InputStream stream) {
        return Observable.defer(() -> {
            try {
                WallpaperManager.getInstance(context).setStream(stream);
                return Observable.just(null);
            } catch (IOException e) {
                return Observable.error(e);
            }
        });
    }

}
