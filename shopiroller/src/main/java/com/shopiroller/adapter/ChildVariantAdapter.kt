package com.shopiroller.adapter

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_variant,parent,false)
        return VariantChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: VariantChildViewHolder, position: Int) {
        var isBase = false
        if (variantGroupIndex == 0) {
            isBase = true
        }

        holder.bind(model.variationList?.get(position) ?: Variation(), model.variantGroupIsActive, isBase)
        val variantSectionTextView = holder.itemView.findViewById<TextView>(R.id.variant_section)
        if (variantGroupIndex != 0) {
            variantSectionTextView.isEnabled =
                model.variationList?.get(position)?.isAvailable == true
        }
        variantSectionTextView.setOnClickListener {
            setSelectedPosition(position)
        }
    }

    private fun setSelectedPosition(position: Int) {
        listener.clickedVariantSection(position,variantGroupIndex)
        model.variationList?.let {
            for (variation in it) {
                variation.isSelected = false
            }
        }
        model.variationList?.get(position)?.isSelected = true
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return model.variationList?.size ?: 0
    }
}