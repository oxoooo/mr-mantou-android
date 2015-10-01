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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ooo.oxo.mr.databinding.MainGridItemBinding;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.widget.RecyclerViewBindingHolder;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Image> data;
    private final Listener listener;

    public MainAdapter(Context context, List<Image> data, Listener listener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
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
        holder.binding.image.setOriginalSize(image.meta.width, image.meta.height);
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface Listener {

        void onImageClick(ViewHolder holder);

    }

    public class ViewHolder extends RecyclerViewBindingHolder<MainGridItemBinding> {

        public ViewHolder(MainGridItemBinding binding) {
            super(binding);
            itemView.setOnClickListener(v -> listener.onImageClick(this));
        }

    }

}
