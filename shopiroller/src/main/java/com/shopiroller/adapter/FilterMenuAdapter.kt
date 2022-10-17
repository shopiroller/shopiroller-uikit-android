package com.shopiroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.viewholders.FilterMenuViewHolder

class FilterMenuAdapter(
    private var data: ArrayList<com.shopiroller.models.VariationGroupsItem?>?,
    private var listener: FilterMenuListener?
) : RecyclerView.Adapter<FilterMenuViewHolder>() {

    interface FilterMenuListener {
        fun onMenuItemSelected(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.e_commerce_item_filter, parent, false)

        return FilterMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterMenuViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
}