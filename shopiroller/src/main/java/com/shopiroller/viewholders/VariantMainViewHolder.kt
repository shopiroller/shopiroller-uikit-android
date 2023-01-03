package com.shopiroller.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.adapter.ChildVariantAdapter

class VariantMainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var sectionNameTextView: TextView
    var childRecyclerView: RecyclerView

    init {
        sectionNameTextView = itemView.findViewById(R.id.header)
        childRecyclerView = itemView.findViewById(R.id.child_recycler_view)
    }

    fun bind(headerTitle: String?, variantChildAdapter: ChildVariantAdapter?) {
        sectionNameTextView.text =  headerTitle
        childRecyclerView.adapter = variantChildAdapter
    }
}