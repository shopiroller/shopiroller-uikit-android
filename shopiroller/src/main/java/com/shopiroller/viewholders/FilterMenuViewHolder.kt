package com.shopiroller.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.adapter.FilterMenuAdapter
import kotlinx.android.synthetic.main.e_commerce_item_filter.view.*
import kotlinx.android.synthetic.main.e_commerce_item_filter_multi_choice.view.name_text_view

class FilterMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var listener: FilterMenuAdapter.FilterMenuListener? = null

    fun bind(
        item: com.shopiroller.models.VariationGroupsItem?,
        listener: FilterMenuAdapter.FilterMenuListener?
    ) {
        this.listener = listener
        if (item?.selectedItems?.isNotEmpty() == true) {
            itemView.selected_items_text_view.text = item.selectedItems
            itemView.selected_items_text_view.visibility = View.VISIBLE
        } else
            itemView.selected_items_text_view.visibility = View.GONE

        itemView.name_text_view.text = item?.name
        itemView.setOnClickListener {
            listener?.onMenuItemSelected(adapterPosition)
        }
    }
}