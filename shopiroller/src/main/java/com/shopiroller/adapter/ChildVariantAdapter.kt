package com.shopiroller.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.models.VariantSelectionModel
import com.shopiroller.models.Variation
import com.shopiroller.viewholders.VariantChildViewHolder


class ChildVariantAdapter(private var model: VariantSelectionModel,
                          private var listener: VariantMainAdapter.VariantSelectionListener,
                          private var variantGroupIndex: Int?): RecyclerView.Adapter<VariantChildViewHolder>() {

    private var selectedPosition = 0
    private var lastSelectedVariantGroupId = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_variant,parent,false)
        return VariantChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: VariantChildViewHolder, position: Int) {
        holder.bind(model.variationList?.get(position) ?: Variation(), model.variantGroupIsActive)
        lastSelectedVariantGroupId = model.variantGroupId ?: ""
        val variantSectionTextView = holder.itemView.findViewById<TextView>(R.id.variant_section)
        variantSectionTextView.isEnabled = model.variationList?.get(position)?.isAvailable == true
        variantSectionTextView.setOnClickListener {
            setSelectedPosition(position)
        }
    }

    private fun setSelectedPosition(position: Int) {
        if (selectedPosition >= 0) model.variationList?.get(selectedPosition)?.isSelected = false
        listener.clickedVariantSection(position,variantGroupIndex)
        model.variationList?.get(position)?.isSelected = true
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return model.variationList?.size ?: 0
    }
}