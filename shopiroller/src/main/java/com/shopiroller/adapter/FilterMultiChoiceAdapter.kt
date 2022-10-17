package com.shopiroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.viewholders.FilterMultiChoiceViewHolder

class FilterMultiChoiceAdapter(
    private var data: ArrayList<com.shopiroller.models.MultiChoiceItem?>?,
    private var listener: FilterMultiChoiceItemSelectedListener
) : RecyclerView.Adapter<FilterMultiChoiceViewHolder>() {

    interface FilterMultiChoiceItemSelectedListener {
        fun onItemSelected(position: Int, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMultiChoiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.e_commerce_item_filter_multi_choice, parent, false)

        return FilterMultiChoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterMultiChoiceViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
}