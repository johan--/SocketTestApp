package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.data.rx.transformer.ReceivedDataTransformer.ChangesBundle;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;
import com.surinov.alexander.sockettestapp.utils.CollectionUtils;

import java.util.Collection;
import java.util.List;

class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportViewHolder> {

    private final SortedList<SportItem> mSportItems = new SortedList<>(SportItem.class, new SortedListAdapterCallback<SportItem>(this) {
        @Override
        public int compare(SportItem item1, SportItem item2) {
            return Integer.compare(item1.getId(), item2.getId());
        }

        @Override
        public boolean areContentsTheSame(SportItem oldItem, SportItem newItem) {
            // always return false and skip comparison of two items
            // because we know that if we receive new item with same id, it's definitely has new content
            return false;
        }

        @Override
        public boolean areItemsTheSame(SportItem item1, SportItem item2) {
            return item1.getId().equals(item2.getId());
        }
    });

    @Override
    public SportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sport, parent, false);
        return new SportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SportViewHolder holder, int position) {
        SportItem sportItem = mSportItems.get(position);

        holder.mNameView.setText(sportItem.getName());
        holder.mGameCountView.setText(String.valueOf(sportItem.getGameCount()));
    }

    @Override
    public int getItemCount() {
        return mSportItems.size();
    }

    public void setData(Collection<SportItem> items) {
        mSportItems.addAll(items);
    }

    void onDataChanged(ChangesBundle<SportItem> changesBundle) {
        mSportItems.beginBatchedUpdates();

        List<SportItem> newItems = changesBundle.getNewItems();
        if (!CollectionUtils.isNullOrEmpty(newItems)) {
            mSportItems.addAll(newItems);
        }

        List<SportItem> deletedItems = changesBundle.getDeletedItems();
        if (!CollectionUtils.isNullOrEmpty(deletedItems)) {
            for (SportItem deletedItem : deletedItems) {
                mSportItems.remove(deletedItem);
            }
        }

        List<SportItem> updatedItems = changesBundle.getUpdatedItems();
        if (!CollectionUtils.isNullOrEmpty(updatedItems)) {
            mSportItems.addAll(updatedItems);
        }

        mSportItems.endBatchedUpdates();
    }

    static class SportViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNameView;
        private final TextView mGameCountView;

        SportViewHolder(View itemView) {
            super(itemView);
            mNameView = (TextView) itemView.findViewById(R.id.tvName);
            mGameCountView = (TextView) itemView.findViewById(R.id.tvGameCount);
        }
    }
}