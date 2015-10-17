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

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ooo.oxo.library.databinding.support.widget.BindingRecyclerView;
import ooo.oxo.mr.databinding.AboutActivityBinding;
import ooo.oxo.mr.databinding.AboutHeaderBinding;
import ooo.oxo.mr.databinding.AboutLibraryItemBinding;

public class AboutActivity extends AppCompatActivity {

    private final ArrayMap<String, String> libraries = new ArrayMap<>();

    @Override
    @SuppressWarnings("SpellCheckingInspection")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AboutActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.about_activity);

        binding.toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());

        libraries.put("square / okhttp", "https://square.github.io/okhttp/");
        libraries.put("square / retrofit", "https://square.github.io/retrofit/");
        libraries.put("google / gson", "https://github.com/google/gson");
        libraries.put("bumptech / glide", "https://github.com/bumptech/glide");
        libraries.put("sephiroth74 / ImageViewZoom", "https://github.com/sephiroth74/ImageViewZoom");
        libraries.put("ReactiveX / RxJava", "https://github.com/ReactiveX/RxJava");
        libraries.put("ReactiveX / RxAndroid", "https://github.com/ReactiveX/RxAndroid");
        libraries.put("JakeWharton / RxBinding", "https://github.com/JakeWharton/RxBinding");
        libraries.put("trello / RxLifecycle", "https://github.com/trello/RxLifecycle");

        binding.libraries.setAdapter(new LibrariesAdapter());
    }

    private void open(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    class LibrariesAdapter extends RecyclerView.Adapter<BindingRecyclerView.ViewHolder> {

        private final LayoutInflater inflater = getLayoutInflater();

        @Override
        public BindingRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return viewType == 0
                    ? new HeaderViewHolder(AboutHeaderBinding.inflate(inflater, parent, false))
                    : new ItemViewHolder(AboutLibraryItemBinding.inflate(inflater, parent, false));
        }

        @Override
        public void onBindViewHolder(BindingRecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == 0) {
                ((HeaderViewHolder) holder).binding.setName(position == 0
                        ? R.string.fork_me_on_github
                        : R.string.libraries_used);
            } else {
                ItemViewHolder itemHolder = (ItemViewHolder) holder;
                if (position == 1) {
                    itemHolder.binding.setName("oxoooo / mr-mantou-android");
                } else {
                    itemHolder.binding.setName(libraries.keyAt(position - 3));
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 || position == 2 ? 0 : 1;
        }

        @Override
        public int getItemCount() {
            return libraries.size() + 3;
        }

        private void handleItemClick(int position) {
            if (position == 1) {
                open("https://github.com/oxoooo/mr-mantou-android");
            } else {
                open(libraries.valueAt(position - 3));
            }
        }

        class HeaderViewHolder extends BindingRecyclerView.ViewHolder<AboutHeaderBinding> {

            public HeaderViewHolder(AboutHeaderBinding binding) {
                super(binding);
            }

        }

        class ItemViewHolder extends BindingRecyclerView.ViewHolder<AboutLibraryItemBinding> {

            public ItemViewHolder(AboutLibraryItemBinding binding) {
                super(binding);
                itemView.setOnClickListener(v -> handleItemClick(getAdapterPosition()));
            }

        }

    }

}
