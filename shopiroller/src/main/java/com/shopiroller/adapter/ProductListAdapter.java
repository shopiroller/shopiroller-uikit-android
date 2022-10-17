package com.shopiroller.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.activities.ProductDetailActivity;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.models.ProductDetailModel;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.viewholders.ProductViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    List<ProductDetailModel> data;
    ECommerceRequestHelper eCommerceRequestHelper;
    LegacyProgressViewHelper legacyProgressViewHelper;

    private long mLastClickTime = 0;
    private static final long CLICK_TIME_INTERVAL = 1000;

    public ProductListAdapter(Activity context, List<ProductDetailModel> data) {
        this.context = context;
        this.data = data;
        this.legacyProgressViewHelper = new LegacyProgressViewHelper(context);
        eCommerceRequestHelper = new ECommerceRequestHelper();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ecommerce_list_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductViewHolder viewHolder = (ProductViewHolder) holder;
        viewHolder.bind(data.get(position));
        viewHolder.itemView.setOnClickListener(v -> {

                long now = System.currentTimeMillis();
                if (mLastClickTime != 0 && (now - mLastClickTime) < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = System.currentTimeMillis();

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context,
                        viewHolder.image,
                        "featuredImage");
                ProductDetailActivity.startActivity(viewHolder.image.getContext(), data.get(position).id, null, "featuredImage", options);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<ProductDetailModel> newItems) {
        if(newItems == null) return;
        int oldPosition = data.size();
        data.addAll(newItems);
        int newPosition = data.size();
        notifyItemRangeChanged(oldPosition, newPosition - oldPosition);
    }

    public void update(List<ProductDetailModel> newItems) {
        data.clear();
        if (newItems != null) {
            data.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void deleteAll() {
        int oldPosition = data.size();
        data = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
