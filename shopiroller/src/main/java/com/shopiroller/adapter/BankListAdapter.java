package com.shopiroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.models.events.BankSelectedEvent;
import com.shopiroller.R;
import com.shopiroller.models.BankAccount;
import com.shopiroller.viewholders.BankAccountViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<BankAccount> data;
    private int selectedPosition = -1;

    public BankListAdapter(Context context, List<BankAccount> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_e_commerce_bank_item, parent, false);
        return new BankAccountViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BankAccountViewHolder viewHolder = (BankAccountViewHolder) holder;
        viewHolder.bind(data.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedPosition(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<BankAccount> newItems) {
        int oldPosition = data.size();
        int newPosition = data.size();
        notifyItemRangeChanged(oldPosition, newPosition - oldPosition);
    }

    private void setSelectedPosition(int position) {
        if (selectedPosition == position)
            return;
        if (selectedPosition >= 0)
            data.get(selectedPosition).isSelected = false;
        data.get(position).isSelected = true;
        selectedPosition = position;
        EventBus.getDefault().post(new BankSelectedEvent());
        notifyDataSetChanged();
    }

    public void clearSelectedPosition() {
        if (selectedPosition >= 0)
            data.get(selectedPosition).isSelected = false;
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public BankAccount getSelectedBank() {
        if (data != null && selectedPosition != -1 && data.size() > selectedPosition)
            return data.get(selectedPosition);
        else
            return null;
    }
}
