package com.shopiroller.fragments;

import static com.shopiroller.constants.Constants.IS_PAYMENT_SUCCESS;
import static com.shopiroller.constants.Constants.ONLINE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.activities.OrderFlowActivity;
import com.shopiroller.activities.UserOrderActivity;
import com.shopiroller.constants.Constants;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.models.OrderFailedResponse;
import com.shopiroller.models.OrderResponseInner;
import com.shopiroller.models.PaymentTryAgainEvent;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.views.legacy.ShopirollerButton;
import com.shopiroller.views.legacy.ShopirollerEmptyView;
import com.shopiroller.views.legacy.ShopirollerTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderResultFragment extends OrderFlowBaseFragment {

    Unbinder unbinder;
    @BindView(R2.id.result_button)
    ShopirollerButton resultButton;
    @BindView(R2.id.continue_shopping_button)
    ShopirollerButton continueShoppingButton;
    @BindView(R2.id.failed_layout)
    ShopirollerEmptyView failedLayout;
    @BindView(R2.id.success_layout)
    ShopirollerEmptyView successLayout;
    @BindView(R2.id.extra_description_text_view)
    ShopirollerTextView extraDescriptionTextView;
    @BindView(R2.id.failed_description)
    ShopirollerTextView failedDescriptionTextView;
    @BindView(R2.id.failed_description_layout)
    LinearLayout failedDescriptionLayout;

    private OrderResponseInner orderResponse;
    private OrderFailedResponse orderFailedResponse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_e_commerce_result, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments().containsKey(Constants.ORDER_RESPONSE_MODEL))
            orderResponse = (OrderResponseInner) getArguments().getSerializable(Constants.ORDER_RESPONSE_MODEL);
        else
            orderFailedResponse = (OrderFailedResponse) getArguments().getSerializable(Constants.ORDER_FAILED_RESPONSE_MODEL);

//        if (DynamicConstants.MobiRoller_Stage) {
//            setSuccess();
//        } else {
            if (!getArguments().getBoolean(IS_PAYMENT_SUCCESS, false)) {
                setFailed();
            } else if (orderResponse.order.paymentType.equalsIgnoreCase(ONLINE) || orderResponse.order.paymentType.equalsIgnoreCase(Constants.ONLINE3DS)) {
                if (orderResponse.paymentResult.isSuccess) {
                    setSuccess();
                } else {
                    setFailed();
                }
            } else {
                setSuccess();
            }
//        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean isValid() {
        return false;
    }

    private void setSuccess() {
        resultButton.setText(getString(R.string.e_commerce_result_success_action_my_orders));
        failedLayout.setVisibility(View.GONE);
        successLayout.setVisibility(View.VISIBLE);
        continueShoppingButton.setVisibility(View.VISIBLE);
        failedDescriptionLayout.setVisibility(View.GONE);

        if (orderResponse.order.paymentType.equalsIgnoreCase(Constants.TRANSFER)) {
            successLayout.setDescription(successLayout.getDescription());
            extraDescriptionTextView.setText(getResources().getString(R.string.e_commerce_result_success_reference_number, orderResponse.order.orderCode));
            extraDescriptionTextView.setVisibility(View.VISIBLE);
        }

        if(orderResponse.order.paymentType.equalsIgnoreCase(Constants.PAYPAL)) {
            ECommerceUtil.setBadgeCount(0);
        } else {
            new ECommerceUtil().getBadge();
        }

        resultButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UserOrderActivity.class));
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        });

    }

    private void setFailed() {

        String statusCode = "";
        failedLayout.setVisibility(View.VISIBLE);
        successLayout.setVisibility(View.GONE);
        continueShoppingButton.setVisibility(View.GONE);
        if (getArguments() != null && getArguments().containsKey(Constants.ORDER_FAILED_STATUS_CODE) && getArguments().getString(Constants.ORDER_FAILED_STATUS_CODE) != null) {
            statusCode = getArguments().getString(Constants.ORDER_FAILED_STATUS_CODE);
            if (Constants.PAYMENT_STATUS_CODES.containsKey(statusCode)) {
                failedDescriptionLayout.setVisibility(View.VISIBLE);
                failedDescriptionTextView.setText(getString(Constants.PAYMENT_STATUS_CODES.get(statusCode)));
            } else {
                failedDescriptionLayout.setVisibility(View.GONE);
            }
        } else
            failedDescriptionLayout.setVisibility(View.GONE);
        resultButton.setOnClickListener(v -> {
            if (NetworkHelper.isConnected(getContext())) {
                if (orderResponse != null && orderResponse.order != null && orderResponse.order.id != null)
                    EventBus.getDefault().post(new PaymentTryAgainEvent(orderResponse.order.id));
                else if (orderFailedResponse != null && orderFailedResponse.order != null && orderFailedResponse.order.id != null)
                    EventBus.getDefault().post(new PaymentTryAgainEvent(orderFailedResponse.order.id));
                else {
                    ((OrderFlowActivity) getActivity()).loadChoosePayment(true);
                }
            } else
                DialogUtil.showNoConnectionInfo(getContext());
        });
    }

    @OnClick(R2.id.continue_shopping_button)
    public void onClickContinueShoppingButton() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

}
