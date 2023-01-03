package com.shopiroller.viewholders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.adapter.VariantMainAdapter
import com.shopiroller.models.Variation

class VariantChildViewHolder(itemView: View,
                             listener: VariantMainAdapter.VariantSelectionListener?,
                             variantGroupIndex: Int?): RecyclerView.ViewHolder(itemView) {
    private var variantVariableButton: TextView?
    private var cardViewContainer: CardView?
    private var disabledImage: ImageView?

    private var listener: VariantMainAdapter.VariantSelectionListener? = null

    private var variantGroupIndex = 0


    init {
        variantVariableButton = itemView.findViewById(R.id.variant_section)
        disabledImage = itemView.findViewById(R.id.disabled_image_view)
        cardViewContainer = itemView.findViewById(R.id.container_card_view)
        this.variantGroupIndex = variantGroupIndex ?: 0
        this.listener = listener
    }

    fun bind(model: Variation) {
        variantVariableButton?.text = model.value
        //if (!isActive) {
          //  disabledImage?.visibility = View.VISIBLE
            //cardViewContainer?.isEnabled = false
            //variantVariableButton?.isEnabled = false
       // }
        if (model.isSelected) {
            variantVariableButton?.setBackground(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.e_commerce_bank_selected
                )
            )
        } else {
            variantVariableButton?.setBackground(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.e_commerce_bank_unselected
                )
            )
        }
        variantVariableButton?.setOnClickListener {
            listener?.clickedVariantSection(absoluteAdapterPosition,variantGroupIndex)
        }
    }

}