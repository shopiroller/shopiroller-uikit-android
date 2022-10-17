package com.shopiroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.models.CategoriesMobileSettings;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.viewholders.CategoryListViewHolder;

import java.util.List;


public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<CategoryResponseModel> dataList;
    Context activity;
    CategoriesMobileSettings mobileSettings;

    public CategoryListAdapter(Context activity, List<CategoryResponseModel> dataList, CategoriesMobileSettings mobileSettings) {
        this.dataList = dataList;
        this.activity = activity;
        this.mobileSettings = mobileSettings;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_category_item, parent,false);
        return new CategoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryListViewHolder categoryListViewHolder = (CategoryListViewHolder)holder;
        categoryListViewHolder.bind(dataList.get(position), mobileSettings);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

}
