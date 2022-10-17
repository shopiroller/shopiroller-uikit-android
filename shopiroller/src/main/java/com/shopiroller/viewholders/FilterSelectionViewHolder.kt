package com.shopiroller.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.Shopiroller
import com.shopiroller.adapter.FilterSelectionAdapter
import com.shopiroller.adapter.FilterSubCategoryAdapter
import com.shopiroller.models.CategoriesItem
import kotlinx.android.synthetic.main.e_commerce_item_filter_multi_choice.view.name_text_view
import kotlinx.android.synthetic.main.e_commerce_item_filter_selection.view.*

class FilterSelectionViewHolder(
    itemView: View,
    val listener: FilterSelectionAdapter.FilterSelectionItemSelectedListener?
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: CategoriesItem?) {
        if (item?.isSelected == true)
            itemView.selected_image_view.visibility = View.VISIBLE
        else
            itemView.selected_image_view.visibility = View.INVISIBLE

        if (item?.subCategories?.isNotEmpty() == true) {
            val adapter = FilterSubCategoryAdapter(
                item.subCategories as ArrayList<CategoriesItem?>?,
                listener
            )
            itemView.sub_category_list.adapter = adapter
        }

        itemView.name_text_view.text =
            item?.name?.get(Shopiroller.adapter?.getLocale()) ?: ""
        itemView.setOnClickListener {
            item?.categoryId?.let { it1 -> listener?.onItemSelected(it1) }
        }
    }
}