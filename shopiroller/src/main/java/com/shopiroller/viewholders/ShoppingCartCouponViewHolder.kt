package com.shopiroller.viewholders

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.R
import com.shopiroller.adapter.ShoppingCartCouponAdapter

class ShoppingCartCouponViewHolder(itemView: View, listener: ShoppingCartCouponAdapter.ShoppingCartCouponListener) : RecyclerView.ViewHolder(itemView) {

    private var couponEditText: TextView?
    private var clearCouponbutton: AppCompatButton?
    private var shoppingCartCouponListener: ShoppingCartCouponAdapter.ShoppingCartCouponListener?

    init {
        couponEditText = itemView.findViewById(R.id.coupon_text)
        clearCouponbutton = itemView.findViewById(R.id.clear_coupon_button)
        shoppingCartCouponListener = listener
    }

    fun bindData(coupon: String, isCouponRemovable: Boolean) {
        if (isCouponRemovable) {
            clearCouponbutton?.visibility = View.VISIBLE
        } else {
            clearCouponbutton?.visibility = View.GONE
        }
        couponEditText?.setOnClickListener {
            shoppingCartCouponListener?.onClickCouponView()
        }
        clearCouponbutton?.setOnClickListener {
            shoppingCartCouponListener?.onClickCouponRemoveButton()
        }
        couponEditText?.setText(coupon)
    }

}