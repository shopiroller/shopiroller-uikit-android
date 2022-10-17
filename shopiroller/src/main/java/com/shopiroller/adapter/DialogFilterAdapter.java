package com.shopiroller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.viewholders.PopupCountryViewHolder;

import java.util.ArrayList;

/**
 * Created by ealtaca on 6/30/18.
 */

public class DialogFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<Object> dataList;
    private ArrayList<Object> dataListFiltered;
    private Integer selectedVariantPosition;

    public DialogFilterAdapter(ArrayList<Object> data ) {
        dataListFiltered = data;
        dataList = data;
    }

    public DialogFilterAdapter(ArrayList<Object> data , Integer selectedVariantPosition) {
        dataListFiltered = data;
        dataList = data;
        this.selectedVariantPosition = selectedVariantPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_dialog_list_filter_item, parent, false);
        return new PopupCountryViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final String string = dataListFiltered.get(position).toString();
        if (selectedVariantPosition != null && selectedVariantPosition == position) {
            ((PopupCountryViewHolder) holder).bindTextWithCheckedImage(string);
        } else  {
            ((PopupCountryViewHolder) holder).bindText(string);
        }
    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
                    ArrayList<Object> filteredList = new ArrayList<>();
                    for (Object row : dataList) {
                        if (row.toString().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList<Object>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Object getItemAtPosition(int position) {
        return dataListFiltered.get(position);
    }
}
