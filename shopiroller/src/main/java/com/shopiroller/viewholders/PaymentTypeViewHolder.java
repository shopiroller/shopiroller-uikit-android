package com.shopiroller.viewholders;

import static com.shopiroller.constants.Constants.ONLINE;
import static com.shopiroller.constants.Constants.ONLINE3DS;
import static com.shopiroller.constants.Constants.PAYPAL;
import static com.shopiroller.constants.Constants.PAY_AT_DOOR;
import static com.shopiroller.constants.Constants.STRIPE;
import static com.shopiroller.constants.Constants.TRANSFER;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.models.SupportedPaymentType;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 8/27/17.
 */

public class PaymentTypeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.payment_type_icon)
    ImageView paymentTypeIcon;
    @BindView(R2.id.payment_type_name)
    ShopirollerTextView paymentTypeName;

    public View itemView;

    public PaymentTypeViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bind(SupportedPaymentType supportedPaymentType) {

        if (supportedPaymentType.paymentType.equalsIgnoreCase(ONLINE)) {
            paymentTypeName.setText(itemView.getContext().getString(R.string.e_commerce_payment_method_selection_credit_card));
            paymentTypeIcon.setImageResource(R.drawable.ic_credit_card_icon);
        } else if (supportedPaymentType.paymentType.equalsIgnoreCase(ONLINE3DS)) {
            paymentTypeName.setText(itemView.getContext().getString(R.string.e_commerce_payment_method_selection_credit_card));
            paymentTypeIcon.setImageResource(R.drawable.ic_credit_card_icon);
        } else if (supportedPaymentType.paymentType.equalsIgnoreCase(TRANSFER)) {
            paymentTypeName.setText(itemView.getContext().getString(R.string.e_commerce_payment_method_selection_transfer));
            paymentTypeIcon.setImageResource(R.drawable.ic_bank_iconn);
        } else if (supportedPaymentType.paymentType.equalsIgnoreCase(PAY_AT_DOOR)) {
            paymentTypeName.setText(itemView.getContext().getString(R.string.e_commerce_payment_method_selection_pay_at_door));
            paymentTypeIcon.setImageResource(R.drawable.ic_pay_at_door_icon);
        }else if (supportedPaymentType.paymentType.equalsIgnoreCase(PAYPAL)) {
            paymentTypeName.setText(itemView.getContext().getString(R.string.e_commerce_payment_method_selection_paypal));
            paymentTypeIcon.setImageResource(R.drawable.ic_paypal);
        } else if (supportedPaymentType.paymentType.equalsIgnoreCase(STRIPE)) {
            paymentTypeName.setText(itemView.getContext().getString(R.string.e_commerce_payment_method_selection_stripe));
            paymentTypeIcon.setImageResource(R.drawable.ic_stripe);
        }
    }

}
