package com.shopiroller.viewholders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.adapter.ChildVariantAdapter
import com.shopiroller.models.VariantSelectionModel

class VariantMainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var sectionNameTextView: TextView
    private var childRecyclerView: RecyclerView
    private var variantMainLayout: ConstraintLayout
    private var clickableState: View

    init {
        sectionNameTextView = itemView.findViewById(R.id.header)
        childRecyclerView = itemView.findViewById(R.id.child_recycler_view)
        variantMainLayout = itemView.findViewById(R.id.variant_main_layout)
        clickableState = itemView.findViewById(R.id.clickable_state)
    }

    fun bind(variantSelectionModel: VariantSelectionModel, variantChildAdapter: ChildVariantAdapter?) {
        if (!variantSelectionModel.variantGroupIsActive) {
            sectionNameTextView.alpha = 0.3F
            clickableState.visibility = View.VISIBLE
        } else {
            sectionNameTextView.alpha = 1F
            clickableState.visibility = View.GONE
        }
        sectionNameTextView.text =  variantSelectionModel.variantGroupName
        childRecyclerView.adapter = variantChildAdapter
    }
}