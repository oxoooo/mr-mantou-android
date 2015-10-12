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

package ooo.oxo.mr.widget;

import android.content.Context;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.concurrent.atomic.AtomicInteger;

public class BindingRecyclerView {

    public static abstract class ListAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

        protected final LayoutInflater inflater;
        protected final ObservableList<T> data;

        private final AtomicInteger recyclerViewsAttached = new AtomicInteger();

        private final ObservableList.OnListChangedCallback<ObservableList<T>> callback =
                new ObservableList.OnListChangedCallback<ObservableList<T>>() {
                    @Override
                    public void onChanged(ObservableList<T> sender) {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onItemRangeChanged(ObservableList<T> sender,
                                                   int positionStart, int itemCount) {
                        notifyItemRangeChanged(positionStart, itemCount);
                    }

                    @Override
                    public void onItemRangeInserted(ObservableList<T> sender,
                                                    int positionStart, int itemCount) {
                        notifyItemRangeInserted(positionStart, itemCount);
                    }

                    @Override
                    public void onItemRangeMoved(ObservableList<T> sender,
                                                 int fromPosition, int toPosition, int itemCount) {
                        for (int i = 0; i < itemCount; i++) {
                            notifyItemMoved(fromPosition + i, toPosition + i);
                        }
                    }

                    @Override
                    public void onItemRangeRemoved(ObservableList<T> sender,
                                                   int positionStart, int itemCount) {
                        notifyItemRangeRemoved(positionStart, itemCount);
                    }
                };

        public ListAdapter(Context context, ObservableList<T> data) {
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            if (recyclerViewsAttached.getAndIncrement() == 0) {
                data.addOnListChangedCallback(callback);
            }
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            if (recyclerViewsAttached.decrementAndGet() == 0) {
                data.removeOnListChangedCallback(callback);
            }
        }

    }

    public static class ViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public final V binding;

        public ViewHolder(V binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
