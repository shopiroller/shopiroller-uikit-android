package com.shopiroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.viewholders.FilterSelectionViewHolder

class FilterSelectionAdapter(
    private var data: ArrayList<com.shopiroller.models.CategoriesItem?>?,
    private var listener: FilterSelectionItemSelectedListener?
) : RecyclerView.Adapter<FilterSelectionViewHolder>() {

    interface FilterSelectionItemSelectedListener {
        fun onItemSelected(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterSelectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.e_commerce_item_filter_selection, parent, false)

        return FilterSelectionViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: FilterSelectionViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
}