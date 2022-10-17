package com.shopiroller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.models.Order;
import com.shopiroller.viewholders.OrderViewHolder;

import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity context;
    List<Order> dataList;

    public UserOrderAdapter(AppCompatActivity context, List<Order> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_order_list_item, parent, false);
        return new OrderViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderViewHolder viewHolder = (OrderViewHolder) holder;
        viewHolder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
