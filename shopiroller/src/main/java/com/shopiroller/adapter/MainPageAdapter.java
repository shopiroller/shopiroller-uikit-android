package com.shopiroller.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.activities.ProductDetailActivity;
import com.shopiroller.helpers.ProgressViewHelper;
import com.shopiroller.models.CategoriesMobileSettings;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.HeaderModel;
import com.shopiroller.models.ProductDetailModel;
import com.shopiroller.models.ShowcaseResponseModel;
import com.shopiroller.models.SliderDataModel;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.viewholders.HeaderViewHolder;
import com.shopiroller.viewholders.ProductViewHolder;

import java.util.List;

public class MainPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    Fragment fragment;
    List<Object> data;
    ECommerceRequestHelper eCommerceRequestHelper;
    ProgressViewHelper progressViewHelper;

    private long mLastClickTime = 0;

    public static final int HEADER_VIEW = 1;
    private static final int PRODUCT_VIEW = 0;
    private static final long CLICK_TIME_INTERVAL = 1000;

    public MainPageAdapter(Activity context, List<Object> data, Fragment fragment) {
        this.context = context;
        this.data = data;
        this.fragment = fragment;
        this.progressViewHelper = new ProgressViewHelper(context);
        eCommerceRequestHelper = new ECommerceRequestHelper();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW) {
            HeaderViewHolder viewHolder = new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_ecommerce_header_item, parent, false));
            return viewHolder;
        } else {
            ProductViewHolder viewHolder = new ProductViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_ecommerce_list_item, parent, false));
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("onBindViewHolder", "position " + position);
        if (data.get(position) instanceof ProductDetailModel) {
            ProductViewHolder viewHolder = (ProductViewHolder) holder;
            viewHolder.bind(((ProductDetailModel) data.get(position)));
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
                ProductDetailActivity.startActivity(viewHolder.image.getContext(), ((ProductDetailModel) data.get(position)).id, null, "featuredImage", options);
            });
        } else {
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            viewHolder.bind(((HeaderModel) data.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<ProductDetailModel> newItems) {
        if(newItems == null) return;
        int oldPosition = data.size() + 1;
        data.addAll(newItems);
        notifyItemRangeInserted(oldPosition, newItems.size());
    }

    public void addSlider(List<SliderDataModel> slider) {
        if (data.size() > 0 && data.get(0) instanceof HeaderModel) {
            HeaderModel headerModel = (HeaderModel) data.get(0);
            headerModel.sliderList = slider;
            notifyItemChanged(0);
        }
    }

    public void addCategory(List<CategoryResponseModel> categoryList, CategoriesMobileSettings categoriesMobileSettings) {
        if (data.size() > 0 && data.get(0) instanceof HeaderModel) {
            HeaderModel headerModel = (HeaderModel) data.get(0);
            headerModel.categoryList = categoryList;
            headerModel.categoriesMobileSettings = categoriesMobileSettings;
            notifyItemChanged(0);
        }
    }

    public void addShowcase(List<ShowcaseResponseModel> showcaseList) {
        if (data.size() > 0 && data.get(0) instanceof HeaderModel) {
            HeaderModel headerModel = (HeaderModel) data.get(0);
            headerModel.showcaseList = showcaseList;
            notifyItemChanged(0);
        }
    }

    public void deleteAll() {
        Object first = data.get(0);
        int size = data.size() - 1;
        data.clear();
        data.add(first);
        notifyItemRangeRemoved(1, size);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof ProductDetailModel)
            return PRODUCT_VIEW;
        else
            return HEADER_VIEW;
    }

}
