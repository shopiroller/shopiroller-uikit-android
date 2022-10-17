package com.shopiroller.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.interfaces.ShoppingCartListener;
import com.shopiroller.models.ShoppingCartItem;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.viewholders.ShoppingInvalidCartViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ShoppingInvalidCartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    List<ShoppingCartItem> data;
    ECommerceRequestHelper eCommerceRequestHelper;
    LegacyProgressViewHelper legacyProgressViewHelper;
    ShoppingCartListener shoppingCartListener;

    public ShoppingInvalidCartListAdapter(Activity context, List<ShoppingCartItem> data, ShoppingCartListener shoppingCartListener) {
        this.context = context;
        this.data = data;
        this.legacyProgressViewHelper = new LegacyProgressViewHelper(context);
        eCommerceRequestHelper = new ECommerceRequestHelper();
        this.shoppingCartListener = shoppingCartListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_e_commerce_shopping_invalid_cart_item, parent, false);
        return new ShoppingInvalidCartViewHolder(itemView, shoppingCartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShoppingInvalidCartViewHolder viewHolder = (ShoppingInvalidCartViewHolder) holder;
        viewHolder.bind(data.get(position));
        viewHolder.itemView.setOnClickListener(v -> {

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context,
                    viewHolder.image,
                    "featuredImage");
//                ProductDetailActivity.startActivity(viewHolder.image.getContext(), data.get(position), null, "featuredImage", options);
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<ShoppingCartItem> newItems) {
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
