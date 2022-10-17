package com.shopiroller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.viewholders.ShoppingCartCouponViewHolder

class ShoppingCartCouponAdapter (
    private var context: Context,
    private var couponString: String,
    private var listener: ShoppingCartCouponListener
        ) : RecyclerView.Adapter<ShoppingCartCouponViewHolder>() {

    interface ShoppingCartCouponListener {
        fun onClickCouponView();
        fun onClickCouponRemoveButton();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartCouponViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_e_commerce_shopping_cart_coupon, parent, false)
        return ShoppingCartCouponViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ShoppingCartCouponViewHolder, position: Int) {
        if (couponString == "") {
            holder.bindData(context.getString(R.string.e_commerce_shopping_cart_coupon_dialog_textfield_placeholder),false)
        } else
            holder.bindData(couponString,true)

    }

    override fun getItemCount(): Int {
        return 1;
    }

    fun updateString(text: String) {
        couponString = text
        notifyDataSetChanged()
    }
}