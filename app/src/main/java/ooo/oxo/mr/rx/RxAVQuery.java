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

package ooo.oxo.mr.rx;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.List;

import rx.Observable;

public class RxAVQuery {

    public static <T extends AVObject> Observable<List<T>> find(AVQuery<T> query) {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }
            } catch (AVException e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
