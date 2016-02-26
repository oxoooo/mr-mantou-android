package ooo.oxo.mr.util;

import android.databinding.ObservableList;
import android.support.v4.view.PagerAdapter;

public class ObservableListPagerAdapterCallback<T extends ObservableList> extends ObservableList.OnListChangedCallback<T> {

    private final PagerAdapter adapter;

    public ObservableListPagerAdapterCallback(PagerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChanged(T sender) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(T sender, int positionStart, int itemCount) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeInserted(T sender, int positionStart, int itemCount) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeMoved(T sender, int fromPosition, int toPosition, int itemCount) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeRemoved(T sender, int positionStart, int itemCount) {
        adapter.notifyDataSetChanged();
    }

}
