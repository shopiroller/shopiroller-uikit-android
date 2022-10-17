package com.shopiroller.viewholders;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.models.user.UserBillingAddressModel;
import com.shopiroller.models.user.UserShippingAddressModel;
import com.shopiroller.views.legacy.ShopirollerTextView;

/**
 * Created by ealtaca on 8/27/17.
 */

public class PopupAddressViewHolder extends RecyclerView.ViewHolder {

    ShopirollerTextView addressTitle;
    ShopirollerTextView addressDescriptionFirstLine;
    ShopirollerTextView addressDescriptionSecondLine;
    ShopirollerTextView addressDescriptionThirdLine;
    ConstraintLayout addressContentInnerLayout;
    ConstraintLayout addressMainLayout;

    private AppCompatActivity context;
    private UserShippingAddressModel shippingAddressModel;
    private UserBillingAddressModel billingAddressModel;

    public PopupAddressViewHolder(View itemView, AppCompatActivity activity) {
        super(itemView);
        addressTitle = itemView.findViewById(R.id.address_title);
        addressDescriptionFirstLine = itemView.findViewById(R.id.address_description_first);
        addressDescriptionSecondLine = itemView.findViewById(R.id.address_description_second);
        addressDescriptionThirdLine = itemView.findViewById(R.id.address_description_third);
        addressContentInnerLayout = itemView.findViewById(R.id.address_content_inner_layout);
        addressMainLayout = itemView.findViewById(R.id.address_main_layout);
        context = activity;
    }

    public void bindShipping(UserShippingAddressModel addressModel) {
        shippingAddressModel = addressModel;
        addressTitle.setText(addressModel.title);
        addressDescriptionFirstLine.setText(
                addressModel.getPopupAddressFirstLine());
        addressDescriptionSecondLine.setText(
                addressModel.getPopupAddressSecondLine());
        addressDescriptionThirdLine.setText(
                addressModel.getPopupAddressThirdLine());
    }

    public void bindBilling(UserBillingAddressModel addressModel) {
        billingAddressModel = addressModel;
        addressTitle.setText(addressModel.title);
        addressDescriptionFirstLine.setText(
                addressModel.getPopupAddressFirstLine());
        addressDescriptionSecondLine.setText(
                addressModel.getPopupAddressSecondLine());
        addressDescriptionThirdLine.setText(
                addressModel.getPopupAddressThirdLine());
    }

}
