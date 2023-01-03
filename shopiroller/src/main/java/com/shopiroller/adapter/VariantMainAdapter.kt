package com.shopiroller.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.models.Variation
import com.shopiroller.models.VariationGroupsModel
import com.shopiroller.viewholders.VariantMainViewHolder

class VariantMainAdapter(
    private var mainList: List<VariationGroupsModel>,
    private var variantSelectionListener: VariantSelectionListener) :
    RecyclerView.Adapter<VariantMainViewHolder>() {

    interface VariantSelectionListener {
        fun clickedVariantSection(variantIndex: Int?, variantGroupIndex: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantMainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.item_main_variant, parent, false)
        return VariantMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: VariantMainViewHolder, position: Int) {
        val section: List<Variation> = mainList[position].variations
        val sectionName: String = mainList[position].name
        holder.bind(sectionName, ChildVariantAdapter(section,variantSelectionListener,position))
    }

    override fun getItemCount(): Int {
        return mainList.size
    }
}