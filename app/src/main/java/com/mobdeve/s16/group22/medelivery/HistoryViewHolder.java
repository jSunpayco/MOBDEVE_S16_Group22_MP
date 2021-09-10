package com.mobdeve.s16.group22.medelivery;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView historyDate,TransactionID,historyStatus;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        this.historyDate = (TextView) itemView.findViewById(R.id.historyDate);
        this.TransactionID = (TextView) itemView.findViewById(R.id.TransactionID);
        this.historyStatus = (TextView) itemView.findViewById(R.id.historyStatus);
    }
}
