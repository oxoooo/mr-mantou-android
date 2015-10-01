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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import rx.Observable;

public class RxEndlessRecyclerView {

    public static Observable<Integer> reachesEnd(RecyclerView view) {
        return RxRecyclerView.scrollEvents(view)
                .filter(i -> view.getLayoutManager() != null)
                .concatMap(event -> {
                    RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) { // also GridLayoutManager
                        return lastVisibleItemPosition((LinearLayoutManager) layoutManager);
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        return lastVisibleItemPositions((StaggeredGridLayoutManager) layoutManager);
                    } else {
                        return Observable.empty();
                    }
                })
                .filter(i -> i >= view.getLayoutManager().getItemCount() - 1)
                .distinctUntilChanged();
    }

    public static Observable<Integer> lastVisibleItemPosition(LinearLayoutManager lm) {
        return Observable.just(lm.findLastVisibleItemPosition());
    }

    public static Observable<Integer> lastVisibleItemPositions(StaggeredGridLayoutManager lm) {
        return Observable.create(subscriber -> {
            int[] positions = new int[lm.getSpanCount()];
            lm.findLastVisibleItemPositions(positions);

            for (int i : positions) {
                if (subscriber.isUnsubscribed()) {
                    break;
                } else {
                    subscriber.onNext(i);
                }
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        });
    }

}
