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

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxNetworking {

    public static <T> Observable.Transformer<T, T> bindNetworking(
            @Nullable RxAppCompatActivity activity, @Nullable SwipeRefreshLayout indicator) {
        return observable -> {
            observable = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            if (activity != null) {
                observable = observable.compose(activity.bindToLifecycle());
            }

            if (indicator != null) {
                observable = observable.compose(bindRefreshing(indicator));
            }

            return observable;
        };
    }

    public static <T> Observable.Transformer<T, T> bindRefreshing(SwipeRefreshLayout indicator) {
        return observable -> observable
                .doOnSubscribe(() -> indicator.post(() -> indicator.setRefreshing(true)))
                .doOnCompleted(() -> indicator.post(() -> indicator.setRefreshing(false)));
    }

}
