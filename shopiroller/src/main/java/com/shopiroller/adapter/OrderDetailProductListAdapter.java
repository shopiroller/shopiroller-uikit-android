package com.shopiroller.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.models.OrderProduct;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.viewholders.OrderProductViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    List<OrderProduct> data;
    ECommerceRequestHelper eCommerceRequestHelper;
    LegacyProgressViewHelper legacyProgressViewHelper;

    public OrderDetailProductListAdapter(Activity context, List<OrderProduct> data) {
        this.context = context;
        this.data = data;
        this.legacyProgressViewHelper = new LegacyProgressViewHelper(context);
        eCommerceRequestHelper = new ECommerceRequestHelper();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ecommerce_order_product_list_item, parent, false);
        return new OrderProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderProductViewHolder viewHolder = (OrderProductViewHolder) holder;
        viewHolder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<OrderProduct> newItems) {
        int oldPosition = data.size();
        data.addAll(newItems);
        int newPosition = data.size();
        notifyItemRangeChanged(oldPosition, newPosition - oldPosition);
    }

    public void deleteAll() {
        data = new ArrayList<>();
        notifyDataSetChanged();
    }

}
