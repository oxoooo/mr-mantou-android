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
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

@SuppressWarnings("unused")
public class RecyclerViewBindingUtil {

    @BindingAdapter("bind:data")
    @SuppressWarnings("unchecked")
    public static void bindData(RecyclerView view, ObservableList data) {
        data.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList>() {
            @Override
            public void onChanged(ObservableList sender) {
                if (view.getAdapter() != null) {
                    view.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                if (view.getAdapter() != null) {
                    view.getAdapter().notifyItemRangeChanged(positionStart, itemCount);
                }
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                if (view.getAdapter() != null) {
                    view.getAdapter().notifyItemRangeInserted(positionStart, itemCount);
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                if (view.getAdapter() != null) {
                    for (int i = 0; i < itemCount; i++) {
                        view.getAdapter().notifyItemMoved(fromPosition + i, toPosition + i);
                    }
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                if (view.getAdapter() != null) {
                    view.getAdapter().notifyItemRangeRemoved(positionStart, itemCount);
                }
            }
        });
    }

}
