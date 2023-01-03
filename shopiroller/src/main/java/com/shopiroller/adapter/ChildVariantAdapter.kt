package com.shopiroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.models.Variation
import com.shopiroller.viewholders.VariantChildViewHolder


class ChildVariantAdapter(private var variationList: List<Variation>,
                          private var listener: VariantMainAdapter.VariantSelectionListener,
                          private var variantGroupIndex: Int?): RecyclerView.Adapter<VariantChildViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_variant,parent,false)
        return VariantChildViewHolder(view,listener,variantGroupIndex)
    }

    override fun onBindViewHolder(holder: VariantChildViewHolder, position: Int) {
        holder.bind(variationList[position])
        val variantSectionTextView = holder.itemView.findViewById<TextView>(R.id.variant_section)
        variantSectionTextView.setOnClickListener {
            setSelectedPosition(position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!recyclerView.isComputingLayout) {
            setSelectedPosition(0)
        }
    }

    private fun setSelectedPosition(position: Int) {
        if (selectedPosition >= 0) variationList[selectedPosition].isSelected = false
        variationList[position].isSelected = true
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return variationList.size
    }
}