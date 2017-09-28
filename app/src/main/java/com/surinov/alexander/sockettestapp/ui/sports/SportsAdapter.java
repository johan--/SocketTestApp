package com.surinov.alexander.sockettestapp.ui.sports;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surinov.alexander.sockettestapp.R;
import com.surinov.alexander.sockettestapp.data.rx.transformer.ReceivedDataTransformer.ItemWithPosition;
import com.surinov.alexander.sockettestapp.data.source.response.SportsResponse.SportItem;

import java.util.ArrayList;
import java.util.List;

class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportViewHolder> {

    private final List<SportItem> mSportItems = new ArrayList<>();

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

    void addItems(@NonNull List<SportItem> newItems) {
        if (newItems.isEmpty()) {
            return;
        }

        mSportItems.addAll(newItems);
        notifyItemRangeInserted(mSportItems.size() - newItems.size(), newItems.size());
    }

    void updateItem(@NonNull ItemWithPosition<SportItem> itemWithPosition) {
        mSportItems.set(itemWithPosition.getPosition(), itemWithPosition.getItem());
        notifyItemChanged(itemWithPosition.getPosition());
    }

    void deleteItem(int position) {
        mSportItems.remove(position);
        notifyItemRemoved(position);
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