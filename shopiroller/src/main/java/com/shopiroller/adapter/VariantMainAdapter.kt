package com.shopiroller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.models.VariantSelectionModel
import com.shopiroller.models.Variation
import com.shopiroller.viewholders.VariantMainViewHolder

class VariantMainAdapter(
    private var mainList: ArrayList<VariantSelectionModel>,
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
        holder.bind(mainList[position], ChildVariantAdapter(mainList[position],variantSelectionListener,position))
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    fun updateVariantModel(mainList: ArrayList<VariantSelectionModel>, position: Int?) {
        mainList[position ?: 0].variantGroupIsActive = true
        notifyDataSetChanged()
    }
}