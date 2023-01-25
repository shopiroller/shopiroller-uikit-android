package com.shopiroller.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.Shopiroller;
import com.shopiroller.activities.OrderFlowActivity;
import com.shopiroller.activities.ShoppingCartActivity;
import com.shopiroller.constants.Constants;
import com.shopiroller.enums.MobirollerDialogType;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.helpers.UtilManager;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.MakeOrder;
import com.shopiroller.models.OrderFailedResponse;
import com.shopiroller.models.OrderResponseEvent;
import com.shopiroller.models.OrderResponseInner;
import com.shopiroller.models.PaymentSettings;
import com.shopiroller.models.ShoppingCartResponse;
import com.shopiroller.models.StripeCancelEvent;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.network.ECommerceServiceInterface;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.legacy.ShopirollerButton;
import com.shopiroller.views.legacy.ShopirollerDialog;
import com.shopiroller.views.legacy.ShopirollerTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class OrderSummaryFragment extends OrderFlowBaseFragment {

    Unbinder unbinder;

    @BindView(R2.id.agreement_description)
    ShopirollerTextView agreementDescription;
    @BindView(R2.id.orderNoteTextInputEditText)
    TextInputEditText orderNoteTextInputEditText;
    @BindView(R2.id.note_layout)
    CardView noteLayout;
    @BindView(R2.id.agreement)
    CheckBox agreement;
    @BindView(R2.id.confirm_button)
    ShopirollerButton confirmButton;
    @BindView(R2.id.main_layout)
    ConstraintLayout mainLayout;
    @BindView(R2.id.bottom_layout)
    ConstraintLayout bottomLayout;
    @BindView(R2.id.loading_animation)
    LottieAnimationView loadingAnimation;
    @BindView(R2.id.product_total_description_text_view)
    ShopirollerTextView productSubTotalTextView;
    @BindView(R2.id.shipping_total_description_text_view)
    ShopirollerTextView shippingTotalTextView;
    @BindView(R2.id.total)
    ShopirollerTextView totalTextView;
    @BindView(R2.id.payment_description_text_view)
    ShopirollerTextView paymentDescriptionTextView;
    @BindView(R2.id.billing_description_text_view)
    ShopirollerTextView billingDescriptionTextView;
    @BindView(R2.id.shipping_description_text_view)
    ShopirollerTextView shippingDescriptionTextView;
    @BindView(R2.id.cart_description_text_view)
    ShopirollerTextView cardDescriptionTExtView;
    @BindView(R2.id.coupon_discount_price_description)
    ShopirollerTextView couponDiscountPriceTextView;
    @BindView(R2.id.coupon_discount_price_text_view)
    ShopirollerTextView couponDiscountPrice;

    private LegacyProgressViewHelper legacyProgressViewHelper;
    private ECommerceServiceInterface applyzeECommerceService;
    private ECommerceRequestHelper eCommerceRequestHelper;

    private MakeOrder makeOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_order_summary, container, false);
        unbinder = ButterKnife.bind(this, view);
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        eCommerceRequestHelper = new ECommerceRequestHelper();
        applyzeECommerceService = eCommerceRequestHelper.getApiService();
        makeOrder = (MakeOrder) getArguments().getSerializable(Constants.MAKE_ORDER_MODEL);
        agreementDescription.setText(Html.fromHtml(getString(R.string.e_commerce_order_summary_agreement_description)));
        agreementDescription.setOnClickListener(view1 -> getTerms());
        setUpView();
        setConfirmButton(false);
        agreement.setOnCheckedChangeListener((compoundButton, b) -> setConfirmButton(b));
        setOnTouchToNoteField();

        mainLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);

        getShoppingCart(false);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchToNoteField() {
        orderNoteTextInputEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (orderNoteTextInputEditText.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void setUpView() {
//        if(DynamicConstants.MobiRoller_Stage) {
//            total.setText(ECommerceUtil.getPriceString(120) + " " + ECommerceUtil.getCurrency("TRY"));
//            productTotal.setText(ECommerceUtil.getPriceString(115) + " " + ECommerceUtil.getCurrency("TRY"));
//            shippingTotal.setText(ECommerceUtil.getPriceString(5) + " " + ECommerceUtil.getCurrency("TRY"));
//        } else {
//            total.setText(ECommerceUtil.getPriceString((makeOrder.shippingPrice + makeOrder.productPriceTotal)) + " " + ECommerceUtil.getCurrency(makeOrder.currency));
//            productTotal.setText(ECommerceUtil.getPriceString((makeOrder.productPriceTotal)) + " " + ECommerceUtil.getCurrency(makeOrder.currency));
//            shippingTotal.setText(ECommerceUtil.getPriceString((makeOrder.shippingPrice)) + " " + ECommerceUtil.getCurrency(makeOrder.currency));
//        }
//        if (DynamicConstants.MobiRoller_Stage) {
////
//            shippingDescriptionTextView.setText(getString(R.string.preview_e_commerce_shipping_address_sample));
//            billingDescriptionTextView.setText(getString(R.string.preview_e_commerce_billing_address_sample));
////
//            paymentDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_credit_card));
//
//            productSubTotalTextView.setText(": " + 115 + " " + ECommerceUtil.getCurrency("TRY"));
//            shippingTotalTextView.setText(": " + 5 + " " + ECommerceUtil.getCurrency("TRY"));
//            totalTextView.setText(20 + " " + ECommerceUtil.getCurrency("TRY"));
//        } else {
        shippingDescriptionTextView.setText(makeOrder.userShippingAddressModel.getSummaryDescriptionArea());
        billingDescriptionTextView.setText(makeOrder.userBillingAddressModel.getSummaryDescriptionArea());
        if (makeOrder.paymentType.equalsIgnoreCase(Constants.PAY_AT_DOOR)) {
            paymentDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_pay_at_door));
        } else if (makeOrder.paymentType.equalsIgnoreCase(Constants.ONLINE) || makeOrder.paymentType.equalsIgnoreCase(Constants.ONLINE3DS)) {
            paymentDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_credit_card) + "\n" + Html.fromHtml("**** " + makeOrder.card.cardNumber.substring(12, 16)));
        } else if (makeOrder.paymentType.equalsIgnoreCase(Constants.TRANSFER)) {
            paymentDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_transfer) + "\n" + makeOrder.bankAccountModel.toString());
        } else if (makeOrder.paymentType.equalsIgnoreCase(Constants.PAYPAL)) {
            paymentDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_paypal));
        } else if (makeOrder.paymentType.equalsIgnoreCase(Constants.STRIPE)) {
            paymentDescriptionTextView.setText(getString(R.string.e_commerce_payment_method_selection_stripe));
        }
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean isValid() {
        return agreement.isChecked();
    }

    @Subscribe
    public void onPostStripeCancelled(StripeCancelEvent e) {
        bottomLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.confirm_button)
    public void onClickConfirmButton() {
        if (isValid()) {
            getShoppingCart(true);
        } else {
            new ShopirollerDialog.Builder()
                    .setContext(getActivity())
                    .setType(MobirollerDialogType.BASIC)
                    .setTitle(getString(R.string.e_commerce_order_summary_agreement_popup_title))
                    .setDescription(getString(R.string.e_commerce_order_summary_agreement_popup_description))
                    .setIconResource(R.drawable.ic_outline_info_24)
                    .setColor(Color.parseColor("#F8E7D8"))
                    .setListener(new ShopirollerDialog.DialogButtonCallback() {
                        @Override
                        public void onClickButton() {
                            if (agreement != null)
                                agreement.setChecked(true);
                        }
                    })
                    .setButtonText(getString(R.string.e_commerce_order_summary_agreement_popup_button))
                    .show();
        }
    }

    private void showUpdateShoppingCartDialog() {
        new ShopirollerDialog.Builder()
                .setContext(getActivity())
                .setType(MobirollerDialogType.BASIC)
                .setTitle(getString(R.string.e_commerce_order_summary_update_order_popup_title))
                .setDescription(getString(R.string.e_commerce_order_summary_update_order_popup_description))
                .setIconResource(R.drawable.ic_edit_white_24dp)
                .setColor(Color.parseColor("#F8E7D8"))
                .setListener(new ShopirollerDialog.DialogButtonCallback() {
                    @Override
                    public void onClickButton() {
                        startActivity(new Intent(getActivity(), ShoppingCartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();
                    }
                })
                .setButtonText(getString(R.string.e_commerce_order_summary_update_order_popup_button))
                .show();
    }

    private void getShoppingCart(boolean isCheckout) {
        legacyProgressViewHelper.show();
        Call<ECommerceResponse<ShoppingCartResponse>> responseCall = eCommerceRequestHelper.getApiService().getShoppingCart(Shopiroller.getUserId());
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<ShoppingCartResponse>() {
            @Override
            public void done() {
                if (!getActivity().isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
            }

            @Override
            public void onSuccess(ShoppingCartResponse result) {
                mainLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.VISIBLE);
                if (isCheckout) {
                    if (result.invalidItems != null && result.invalidItems.size() != 0) {
                        showUpdateShoppingCartDialog();
                    } else {
                        makeOrder();
                    }
                } else {
                    setCartLayout(result);
                }
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                ErrorUtils.showErrorToast(getActivity());
            }

            @Override
            public void onNetworkError(String result) {
                ErrorUtils.showErrorToast(getActivity());
            }
        });
    }

    private void setCartLayout(ShoppingCartResponse response) {
        cardDescriptionTExtView.setText(getString(R.string.e_commerce_order_summary_cart_description, String.valueOf(response.items.size()), ECommerceUtil.getFormattedPrice(response.subTotalPrice, response.currency)));
        productSubTotalTextView.setText(": " + ECommerceUtil.getFormattedPrice(response.subTotalPrice, response.currency));
        shippingTotalTextView.setText(": " + ECommerceUtil.getFormattedPrice(response.shippingPrice, response.currency));
        totalTextView.setText(ECommerceUtil.getFormattedPrice(response.totalPrice, response.currency));
        if (response.couponPrice != null && response.couponPrice != 0.0 && !response.couponId.equals("")) {
            couponDiscountPriceTextView.setVisibility(View.VISIBLE);
            couponDiscountPrice.setVisibility(View.VISIBLE);
            couponDiscountPrice.setText(String.format(": -%s", ECommerceUtil.getFormattedPrice(response.couponPrice, response.currency)));
        } else  {
            couponDiscountPriceTextView.setVisibility(View.GONE);
            couponDiscountPrice.setVisibility(View.GONE);
        }

    }

    private void setConfirmButton(boolean isEnabled) {
        confirmButton.setOpacity(isEnabled);
    }

    private void makeOrder() {

//        if (DynamicConstants.MobiRoller_Stage) {
//
//            long time = System.currentTimeMillis();
//            loadingAnimation.setVisibility(View.VISIBLE);
//            bottomLayout.setVisibility(View.GONE);
//            loadingAnimation.loop(true);
//            if (!loadingAnimation.isAnimating())
//                loadingAnimation.playAnimation();
//            long delay = 0;
//            long responseTime = System.currentTimeMillis() - time;
//            if (responseTime < 1500) {
//                delay = 1500 - responseTime;
//            }
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (!getActivity().isFinishing()) {
//                        loadingAnimation.setVisibility(View.GONE);
//                        bottomLayout.setVisibility(View.VISIBLE);
//                        loadingAnimation.cancelAnimation();
//                        EventBus.getDefault().post(new OrderResponseEvent(new OrderResponseInner()));
//                    }
//                }
//            }, delay);
//            mainLayout.setVisibility(View.GONE);
//            return;
//        }
        if (NetworkHelper.isConnected(getContext())) {
            long time = System.currentTimeMillis();
            loadingAnimation.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
            loadingAnimation.loop(true);
            if (!loadingAnimation.isAnimating())
                loadingAnimation.playAnimation();
//        progressViewHelper.show();
            makeOrder.buyer.email = Shopiroller.getUserEmail();
            makeOrder.userNote = orderNoteTextInputEditText.getText().toString();
            String[] names = makeOrder.userBillingAddressModel.contact.nameSurname.split(" ");
            if (names.length != 0) {
                makeOrder.buyer.name = names[0];
                makeOrder.buyer.surname = "";
                if (names.length > 1) {
                    for (int i = 1; i < names.length; i++) {
                        if (i == 1)
                            makeOrder.buyer.surname += names[i];
                        else
                            makeOrder.buyer.surname += " " + names[i];

                    }
                }
            }
            mainLayout.setVisibility(View.GONE);
            if (makeOrder.tryAgain) {
                Call<ECommerceResponse<OrderResponseInner>> responseCall = eCommerceRequestHelper.getApiService().tryAgain(makeOrder.getCompleteOrderModel());
                eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<OrderResponseInner>() {
                    @Override
                    public void done() {
                        if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                            legacyProgressViewHelper.dismiss();
                    }

                    @Override
                    public void onSuccess(OrderResponseInner result) {
                        long delay = 0;
                        long responseTime = System.currentTimeMillis() - time;
                        if (responseTime < 1500) {
                            delay = 1500 - responseTime;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!getActivity().isFinishing()) {
                                    EventBus.getDefault().post(new OrderResponseEvent(result));
                                    if (!result.order.paymentType.equalsIgnoreCase(Constants.PAYPAL)) {
                                        loadingAnimation.setVisibility(View.GONE);
                                        loadingAnimation.cancelAnimation();
                                    }
                                }
                            }
                        }, delay);
                    }

                    @Override
                    public void onFailure(ECommerceErrorResponse result) {
                        long delay = 0;
                        long responseTime = System.currentTimeMillis() - time;
                        if (responseTime < 1500) {
                            delay = 1500 - responseTime;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(new OrderResponseEvent(new OrderFailedResponse(result)));
                                loadingAnimation.setVisibility(View.GONE);
                                loadingAnimation.cancelAnimation();
                            }
                        }, delay);
                    }

                    @Override
                    public void onNetworkError(String result) {
                        long delay = 0;
                        long responseTime = System.currentTimeMillis() - time;
                        if (responseTime < 1500) {
                            delay = 1500 - responseTime;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAnimation.setVisibility(View.GONE);
                                loadingAnimation.cancelAnimation();
                                mainLayout.setVisibility(View.VISIBLE);
                                bottomLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), getString(R.string.common_error), Toast.LENGTH_LONG).show();

                            }
                        }, delay);
                    }
                });
            } else {
                Call<ECommerceResponse<OrderResponseInner>> responseCall = eCommerceRequestHelper.getApiService().makeOrder(Shopiroller.getUserId(), makeOrder);
                eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<OrderResponseInner>() {
                    @Override
                    public void done() {
                        if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                            legacyProgressViewHelper.dismiss();
                    }

                    @Override
                    public void onSuccess(OrderResponseInner result) {
                        long delay = 0;
                        long responseTime = System.currentTimeMillis() - time;
                        if (responseTime < 1500) {
                            delay = 1500 - responseTime;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!getActivity().isFinishing()) {
                                    EventBus.getDefault().post(new OrderResponseEvent(result));
                                    if (!result.order.paymentType.equalsIgnoreCase(Constants.PAYPAL)) {
                                        loadingAnimation.setVisibility(View.GONE);
                                        loadingAnimation.cancelAnimation();
                                    }
                                }
                            }
                        }, delay);
                    }

                    @Override
                    public void onFailure(ECommerceErrorResponse result) {
                        long delay = 0;
                        long responseTime = System.currentTimeMillis() - time;
                        if (responseTime < 1500) {
                            delay = 1500 - responseTime;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(new OrderResponseEvent(new OrderFailedResponse(result)));
                                loadingAnimation.setVisibility(View.GONE);
                                loadingAnimation.cancelAnimation();
                            }
                        }, delay);
                    }

                    @Override
                    public void onNetworkError(String result) {
                        long delay = 0;
                        long responseTime = System.currentTimeMillis() - time;
                        if (responseTime < 1500) {
                            delay = 1500 - responseTime;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAnimation.setVisibility(View.GONE);
                                loadingAnimation.cancelAnimation();
                                mainLayout.setVisibility(View.VISIBLE);
                                bottomLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), getString(R.string.common_error), Toast.LENGTH_LONG).show();

                            }
                        }, delay);
                    }
                });
            }
        } else
            DialogUtil.showNoConnectionInfo(getContext());
    }

    private void getTerms() {
        if (ECommerceUtil.getPaymentSettings() == null) {

            if (NetworkHelper.isConnected(getContext())) {
                legacyProgressViewHelper.show();

                new ECommerceUtil().getPaymentSettings(new ECommerceRequestHelper.ECommerceCallBack<PaymentSettings>() {
                    @Override
                    public void done() {
                        if (!getActivity().isFinishing() && legacyProgressViewHelper.isShowing())
                            legacyProgressViewHelper.dismiss();
                    }

                    @Override
                    public void onSuccess(PaymentSettings result) {
                        openTermLink();
                    }

                    @Override
                    public void onFailure(ECommerceErrorResponse result) {
                        ErrorUtils.showErrorToast(getContext());
                    }

                    @Override
                    public void onNetworkError(String result) {
                        ErrorUtils.showErrorToast(getContext());
                    }
                });
            } else
                DialogUtil.showNoConnectionInfo(getContext());
        } else
            openTermLink();
    }

    private void openTermLink() {
        if (ECommerceUtil.getPaymentSettings() != null && ECommerceUtil.getPaymentSettings().getDistanceSalesContract() != null) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            try {
                customTabsIntent.launchUrl(getActivity(), Uri.parse(URLUtil.guessUrl(UtilManager.localizationHelper().getLocalizedTitle(ECommerceUtil.getPaymentSettings().getDistanceSalesContract()))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R2.id.cart_grid_layout)
    public void onClickCartLayout() {
        startActivity(new Intent(getActivity(), ShoppingCartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        getActivity().finish();
    }

    @OnClick(R2.id.payment_grid_layout)
    public void onClickPaymentLayout() {
        ((OrderFlowActivity) getActivity()).loadChoosePayment(false);
    }

    @OnClick({R2.id.billing_address_grid_layout, R2.id.delivery_address_layout})
    public void onClickAddressLayout() {
        ((OrderFlowActivity) getActivity()).loadChooseAddress(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadingAnimation.setVisibility(View.GONE);
        loadingAnimation.cancelAnimation();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
