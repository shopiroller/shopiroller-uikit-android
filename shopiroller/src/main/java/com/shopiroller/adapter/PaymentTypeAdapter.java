package com.shopiroller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.models.SupportedPaymentType;
import com.shopiroller.viewholders.PaymentTypeViewHolder;

import java.util.List;

public class PaymentTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SupportedPaymentType> data;

    public PaymentTypeAdapter(List<SupportedPaymentType> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_e_commerce_payment_type, parent, false);
        return new PaymentTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PaymentTypeViewHolder viewHolder = (PaymentTypeViewHolder) holder;
        viewHolder.bind(data.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
