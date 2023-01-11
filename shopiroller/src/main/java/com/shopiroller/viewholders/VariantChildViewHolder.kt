package com.shopiroller.viewholders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.adapter.ChildVariantAdapter
import com.shopiroller.adapter.VariantMainAdapter
import com.shopiroller.models.Variation

class VariantChildViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var variantVariableTextView: TextView?
    private var cardViewContainer: CardView?
    private var disabledView: View?

    init {
        variantVariableTextView = itemView.findViewById(R.id.variant_section)
        disabledView = itemView.findViewById(R.id.disabled_line_view)
        cardViewContainer = itemView.findViewById(R.id.container_card_view)
    }

    fun bind(model: Variation, isLayoutGroupActive: Boolean, isBaseVariantGroup: Boolean) {

        if (!isLayoutGroupActive) {
            variantVariableTextView?.alpha = 0.3F
        } else {
            variantVariableTextView?.alpha = 1F
            disabledView?.visibility = View.GONE
        }

        if (!isBaseVariantGroup) {
            if (model.isAvailable == false) {
                disabledView?.visibility = View.VISIBLE
                variantVariableTextView?.alpha = 0.3F
            } else {
                disabledView?.visibility = View.GONE
                variantVariableTextView?.alpha = 1F
            }
        } else {
            disabledView?.visibility = View.GONE
            variantVariableTextView?.alpha = 1F
        }

        variantVariableTextView?.text = model.value
        //if (!isActive) {
          //  disabledImage?.visibility = View.VISIBLE
            //cardViewContainer?.isEnabled = false
            //variantVariableButton?.isEnabled = false
       // }
        if (model.isSelected == true) {
            variantVariableTextView?.background = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.e_commerce_variant_selected
            )
        } else {
            variantVariableTextView?.background = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.e_commerce_bank_unselected
            )
        }
    }

}