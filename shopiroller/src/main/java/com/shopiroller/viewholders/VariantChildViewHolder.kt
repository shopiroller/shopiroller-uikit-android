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

class VariantChildViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var variantVariableButton: TextView?
    private var cardViewContainer: CardView?
    private var disabledImage: ImageView?


    init {
        variantVariableButton = itemView.findViewById(R.id.variant_section)
        disabledImage = itemView.findViewById(R.id.disabled_image_view)
        cardViewContainer = itemView.findViewById(R.id.container_card_view)
    }

    fun bind(model: Variation, isLayoutGroupActive: Boolean) {
        if (!isLayoutGroupActive) {
            variantVariableButton?.alpha = 0.3F
        } else {
            variantVariableButton?.alpha = 1F
        }

        if (model.isAvailable == false) {
            disabledImage?.visibility = View.VISIBLE
        } else {
            disabledImage?.visibility = View.GONE
        }

        variantVariableButton?.text = model.value
        //if (!isActive) {
          //  disabledImage?.visibility = View.VISIBLE
            //cardViewContainer?.isEnabled = false
            //variantVariableButton?.isEnabled = false
       // }
        if (model.isSelected == true) {
            variantVariableButton?.background = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.e_commerce_bank_selected
            )
        } else {
            variantVariableButton?.background = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.e_commerce_bank_unselected
            )
        }
    }

}