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

package ooo.oxo.mr;

import android.content.Context;
import android.databinding.ObservableList;
import android.view.ViewGroup;

import ooo.oxo.mr.databinding.MainGridItemBinding;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.widget.BindingRecyclerView;

public class MainAdapter extends BindingRecyclerView.ListAdapter<Image, MainAdapter.ViewHolder> {

    private final Listener listener;

    public MainAdapter(Context context, ObservableList<Image> data, Listener listener) {
        super(context, data);
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(MainGridItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = data.get(position);
        holder.binding.setImage(image);

        // execute the binding immediately to ensure the original size of RatioImageView is set
        // before layout
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        Image image = data.get(position);
        return Math.round((float) image.meta.width / (float) image.meta.height * 10f);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id.hashCode();
    }

    public interface Listener {

        void onImageClick(ViewHolder holder);

    }

    public class ViewHolder extends BindingRecyclerView.ViewHolder<MainGridItemBinding> {

        public ViewHolder(MainGridItemBinding binding) {
            super(binding);
            itemView.setOnClickListener(v -> listener.onImageClick(this));
        }

    }

}
