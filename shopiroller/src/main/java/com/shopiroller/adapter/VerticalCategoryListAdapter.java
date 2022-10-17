package com.shopiroller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.models.CategoriesMobileSettings;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.viewholders.VerticalCategoryListViewHolder;

import java.util.List;

public class VerticalCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CategoryResponseModel> dataList;
    private CategoriesMobileSettings mobileSettings;

    public VerticalCategoryListAdapter(Context context, List<CategoryResponseModel> dataList, CategoriesMobileSettings mobileSettings) {
        this.context = context;
        this.dataList = dataList;
        this.mobileSettings = mobileSettings;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vertical_category_list_item, parent, false);

        return new VerticalCategoryListViewHolder(view, ((Activity) context));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VerticalCategoryListViewHolder verticalCategoryListViewHolder = (VerticalCategoryListViewHolder) holder;
        verticalCategoryListViewHolder.bind(dataList.get(position), mobileSettings);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

}
