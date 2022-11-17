package com.shopiroller.activities;

import static com.shopiroller.activities.ShoppingCartActivity.SHOPPING_CART_REQUEST_CODE;
import static com.shopiroller.constants.Constants.IS_PAYMENT_SUCCESS;
import static com.shopiroller.constants.Constants.MAKE_ORDER_MODEL;
import static com.shopiroller.constants.Constants.ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED_STATUS_CODE;
import static com.shopiroller.constants.Constants.ONLINE_PAYMENT_3D_HTML_REQUEST_SUCCESS;
import static com.shopiroller.constants.Constants.ORDER_FAILED_RESPONSE_MODEL;
import static com.shopiroller.constants.Constants.ORDER_FAILED_STATUS_CODE;
import static com.shopiroller.constants.Constants.ORDER_RESPONSE_MODEL;
import static com.shopiroller.constants.Constants.PAY_PAL_REQUEST_SUCCESS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.Shopiroller;
import com.shopiroller.constants.Constants;
import com.shopiroller.enums.FontTypeEnum;
import com.shopiroller.fragments.OrderChooseAddressFragment;
import com.shopiroller.fragments.OrderChoosePaymentFragment;
import com.shopiroller.fragments.OrderFlowBaseFragment;
import com.shopiroller.fragments.OrderResultFragment;
import com.shopiroller.fragments.OrderSummaryFragment;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.LegacyToolbarHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.MakeOrder;
import com.shopiroller.models.OrderAddressesEvent;
import com.shopiroller.models.OrderFailedResponse;
import com.shopiroller.models.OrderPaymentEvent;
import com.shopiroller.models.OrderResponseEvent;
import com.shopiroller.models.OrderResponseInner;
import com.shopiroller.models.PaymentContinueEvent;
import com.shopiroller.models.PaymentSettings;
import com.shopiroller.models.PaymentTryAgainEvent;
import com.shopiroller.models.StripeCancelEvent;
import com.shopiroller.models.StripeOrderStatusModel;
import com.shopiroller.models.events.AddressContinueEvent;
import com.shopiroller.models.events.AnimationFinishedEvent;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.StatusViewScroller;
import com.shopiroller.views.legacy.ShopirollerToolbar;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFlowActivity extends ECommerceBaseActivity {

    @BindView(R2.id.step_view)
    StatusViewScroller stepView;
    @BindView(R2.id.step_view_main_layout)
    ConstraintLayout stepViewMainLayout;
    @BindView(R2.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R2.id.top_title_layout)
    RelativeLayout topTitleLayout;
    @BindView(R2.id.toolbar)
    ShopirollerToolbar toolbar;
    @BindView(R2.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R2.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    private int currentStep = 1;
    private OrderFlowBaseFragment currentFragment;
    private MakeOrder makeOrder;
    private OrderResponseEvent orderResponseEvent;
    ECommerceRequestHelper eCommerceRequestHelper;
    LegacyProgressViewHelper legacyProgressViewHelper;
    private boolean mIsSuccess;

    private OrderChooseAddressFragment chooseAddressFragment;
    private OrderChoosePaymentFragment choosePaymentFragment;
    private OrderSummaryFragment orderSummaryFragment;
    private OrderResultFragment orderResultFragment;

    private PaymentSheet paymentSheet;
    private String paymentClientSecret;
    private String paymentId;
    private StripeOrderStatusModel stripeOrderStatusModel;
    private String stripeErrorCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_commerce_order_flow);
        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        new LegacyToolbarHelper().setStatusBar(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);
        toolbar.setTitleTypeface();

        stripeOrderStatusModel = new StripeOrderStatusModel();

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        legacyProgressViewHelper = new LegacyProgressViewHelper(this);
        eCommerceRequestHelper = new ECommerceRequestHelper();
        if (getIntent().hasExtra(MAKE_ORDER_MODEL)) {
            makeOrder = (MakeOrder) getIntent().getSerializableExtra(MAKE_ORDER_MODEL);
        }
        loadChooseAddress(true);
        stepView.getStatusView().setLabelsTypeface(FontTypeEnum.getResIdByResOrder(3, getApplicationContext()));
        stepView.getStatusView().setStatusTypeface(FontTypeEnum.getResIdByResOrder(3, getApplicationContext()));
        stepView.getStatusView().setLineColor(Color.BLACK);
        stepView.getStatusView().setLineColorIncomplete(Color.parseColor("#19000000"));
        stepView.getStatusView().setCircleFillColorIncomplete(Color.parseColor("#19000000"));
        stepView.getStatusView().setCircleStrokeColorIncomplete(Color.parseColor("#19000000"));
    }

    public void loadChooseAddress(boolean fromRight) {
        stepViewMainLayout.setVisibility(View.VISIBLE);
        currentStep = 1;
        nestedScrollView.scrollTo(0, 0);

        if (chooseAddressFragment == null) {
            chooseAddressFragment = new OrderChooseAddressFragment();
        }
        currentFragment = chooseAddressFragment;

        setFragment(chooseAddressFragment, fromRight);
        setTitle(getString(R.string.e_commerce_address_selection_title));
    }

    public void loadChoosePayment(boolean fromRight) {
        if (ECommerceUtil.getPaymentSettings() == null) {
            getPaymentOptions(fromRight);
            return;
        }
        stepViewMainLayout.setVisibility(View.VISIBLE);
        setDefaultAddresses();
        currentStep = 2;
        nestedScrollView.scrollTo(0, 0);
        if (choosePaymentFragment == null) {
            choosePaymentFragment = new OrderChoosePaymentFragment();
        }
        currentFragment = choosePaymentFragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MAKE_ORDER_MODEL, makeOrder);
        choosePaymentFragment.setArguments(bundle);
        setFragment(choosePaymentFragment, fromRight);
        setTitle(getString(R.string.e_commerce_payment_method_selection_title));
    }

    private void setFragment(Fragment fragment, boolean fromRight) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setTransactionAnimation(fragmentTransaction, fromRight);
        replaceFragment(fragmentTransaction, fragment);
    }

    private void replaceFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (fragment.isAdded())
            fragmentTransaction.show(fragment);
        else
            fragmentTransaction.add(R.id.frame_layout, fragment);

        for (Fragment fra :
                getSupportFragmentManager().getFragments()) {
            if (fra != fragment && fra.isAdded())
                fragmentTransaction.hide(fra);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Subscribe
    public void onPostAnimationFinishedEvent(AnimationFinishedEvent e) {
        if (currentStep != 4 || mIsSuccess)
            stepView.getStatusView().setCurrentCount(currentStep);
    }

    private void loadOrderSummary(boolean fromRight) {
        stepViewMainLayout.setVisibility(View.VISIBLE);
        currentStep = 3;
        nestedScrollView.scrollTo(0, 0);
        try {
            if (orderSummaryFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.remove(orderResultFragment).commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderSummaryFragment = new OrderSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MAKE_ORDER_MODEL, makeOrder);
        orderSummaryFragment.setArguments(bundle);
        currentFragment = orderSummaryFragment;
        setFragment(orderSummaryFragment, fromRight);
        setTitle(getString(R.string.e_commerce_order_summary_title));
    }

    private void loadOrderResultSuccess(OrderResponseInner orderResponse, boolean fromRight) {
        stepViewMainLayout.setVisibility(View.GONE);
        currentStep = 4;
        nestedScrollView.scrollTo(0, 0);
        if (orderResultFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(orderResultFragment).commitAllowingStateLoss();
        }
        orderResultFragment = new OrderResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER_RESPONSE_MODEL, orderResponse);
        bundle.putBoolean(IS_PAYMENT_SUCCESS, true);
        mIsSuccess = true;
        orderResultFragment.setArguments(bundle);
        currentFragment = orderResultFragment;
        setFragment(orderResultFragment, fromRight);
        setTitle("");
    }

    private void loadOrderResultFailed(OrderFailedResponse orderResponse, boolean fromRight) {
        stepViewMainLayout.setVisibility(View.GONE);
        currentStep = 4;
        nestedScrollView.scrollTo(0, 0);
        if (orderResultFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(orderResultFragment).commitAllowingStateLoss();
        }
        orderResultFragment = new OrderResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER_FAILED_RESPONSE_MODEL, orderResponse);
        bundle.putBoolean(IS_PAYMENT_SUCCESS, false);
        mIsSuccess = false;
        orderResultFragment.setArguments(bundle);
        currentFragment = orderResultFragment;
        setFragment(orderResultFragment, fromRight);
        setTitle("");
    }

    private void loadOrderResultFailed(OrderResponseInner orderResponse, String statusCode, boolean fromRight) {
        stepViewMainLayout.setVisibility(View.GONE);
        currentStep = 4;
        nestedScrollView.scrollTo(0, 0);
        orderResultFragment = new OrderResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER_RESPONSE_MODEL, orderResponse);
        bundle.putBoolean(IS_PAYMENT_SUCCESS, false);
        bundle.putString(ORDER_FAILED_STATUS_CODE, statusCode);
        mIsSuccess = false;
        orderResultFragment.setArguments(bundle);
        currentFragment = orderResultFragment;
        setFragment(orderResultFragment, fromRight);
        setTitle("");
    }

    private void setTransactionAnimation(FragmentTransaction fragmentTransaction, boolean right) {
        if (right)
            fragmentTransaction.setCustomAnimations(R.anim.right_to_left_enter, R.anim.right_to_left_exit, R.anim.right_to_left_enter, R.anim.right_to_left_exit);
        else
            fragmentTransaction.setCustomAnimations(R.anim.left_to_right_enter, R.anim.left_to_right_exit, R.anim.left_to_right_enter, R.anim.left_to_right_exit);
    }

    public void onClickContinueButton() {
        if (NetworkHelper.isConnected(this)) {
            if (validateStep()) {
                if (currentStep == 1)
                    getPaymentOptions(true);
                else if (currentStep == 2) {
                    loadOrderSummary(true);
                }
            }
        } else {
            DialogUtil.showNoConnectionInfo(this);
        }
    }

    public boolean validateStep() {
        return currentFragment.isValid();
    }

    public static void startActivity(MakeOrder order, Context context) {
        Intent intent = new Intent(context, OrderFlowActivity.class);
        intent.putExtra(MAKE_ORDER_MODEL, order);
        context.startActivity(intent);
    }

    public static void startActivityForResult(MakeOrder order, AppCompatActivity context) {
        Intent intent = new Intent(context, OrderFlowActivity.class);
        intent.putExtra(MAKE_ORDER_MODEL, order);
        context.startActivityForResult(intent, SHOPPING_CART_REQUEST_CODE);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OrderFlowActivity.class));
    }

    @Subscribe
    public void onPostPaymentTryAgainEvent(PaymentTryAgainEvent e) {
        makeOrder.tryAgain = true;
        makeOrder.orderId = e.orderId;
        getPaymentOptions(false);
    }

    @Subscribe
    public void onPostOrderPaymentEvent(OrderPaymentEvent e) {
        makeOrder.paymentType = e.paymentType;
        makeOrder.bankAccount = null;
        makeOrder.paymentAccount = null;
        makeOrder.bankAccountModel = null;
        makeOrder.card = null;
        if (e.paymentType.equalsIgnoreCase(Constants.ONLINE) || e.paymentType.equalsIgnoreCase(Constants.ONLINE3DS))
            makeOrder.card = e.orderCard;
        else if (e.paymentType.equalsIgnoreCase(Constants.TRANSFER)) {
            makeOrder.bankAccount = e.bankAccount.toString();
            makeOrder.bankAccountModel = e.bankAccount;
            makeOrder.paymentAccount = e.bankAccount;
        }
    }

    @Subscribe
    public void onPostOrderResponseEvent(OrderResponseEvent e) {
        orderResponseEvent = e;
        if (!(currentFragment instanceof OrderResultFragment)) {
            if (e.orderResponse != null && e.orderResponse.order.paymentType.equalsIgnoreCase(Constants.PAYPAL) && e.orderResponse.paymentResult != null && e.orderResponse.paymentResult.token != null) {
                startActivityForResult(
                        new Intent(this, PayPalActivity.class)
                                .putExtra(PayPalActivity.PAYPAL_AUTH_TOKEN_KEY, e.orderResponse.paymentResult.token)
                                .putExtra(PayPalActivity.PAYPAL_AMOUNT_KEY, e.orderResponse.order.totalPrice)
                                .putExtra(PayPalActivity.PAYPAL_CURRENCY_CODE_KEY, e.orderResponse.order.currency)
                                .putExtra(PayPalActivity.PAYPAL_ORDER_ID_KEY, e.orderResponse.order.id)
                                .putExtra(PayPalActivity.PAYPAL_DISPLAY_NAME_KEY, e.orderResponse.order.orderCode),
                        Constants.PAYPAL_REQUEST_CODE);
            } else if (e.orderResponse != null && e.orderResponse.order.paymentType.equalsIgnoreCase(Constants.ONLINE3DS) && e.orderResponse.paymentResult != null && e.orderResponse.paymentResult._3DSecureHtml != null) {
                startActivityForResult(
                        new Intent(this, Online3DSecureGateway.class)
                                .putExtra(Constants.ONLINE_PAYMENT_3D_HTML, e.orderResponse.paymentResult._3DSecureHtml),
                        Constants.ONLINE_PAYMENT_3D_HTML_REQUEST_CODE);
            } else if (e.orderResponse != null && e.orderResponse.order.paymentType.equalsIgnoreCase(Constants.STRIPE) && e.orderResponse.paymentResult != null && e.orderResponse.paymentResult.token != null) {
                paymentClientSecret = e.orderResponse.paymentResult.token;
                paymentId = paymentClientSecret.substring(0, 27);
                stripeOrderStatusModel.setOrderId(e.orderResponse.order.id);
                stripeOrderStatusModel.setPaymentId(paymentId);
                PaymentConfiguration.init(getApplicationContext(), e.orderResponse.paymentResult.publishableKey);
                presentPaymentSheet();
            } else {
                if (e.orderResponse == null && e.failedResponse != null) {
                    loadOrderResultFailed(e.failedResponse, true);
                } else
                    loadOrderResultSuccess(e.orderResponse, true);
            }
        }
    }

    @Subscribe
    public void onPostOrderAddressesEvent(OrderAddressesEvent e) {
        makeOrder.userBillingAddressModel = e.userBillingAddressModel;
        makeOrder.userShippingAddressModel = e.userShippingAddressModel;
        makeOrder.billingAddress = ECommerceUtil.getOrderAddress(e.userBillingAddressModel);
        makeOrder.shippingAddress = ECommerceUtil.getOrderAddress(e.userShippingAddressModel);
    }

    @Subscribe
    public void onPostAddressContinueEvent(AddressContinueEvent e) {
        onClickContinueButton();
    }

    @Subscribe
    public void onPostPaymentContinueEvent(PaymentContinueEvent e) {
        onClickContinueButton();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (orderSummaryFragment != null) {
            orderSummaryFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == Constants.ONLINE_PAYMENT_3D_HTML_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getBooleanExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_SUCCESS, false))
                    loadOrderResultSuccess(orderResponseEvent.orderResponse, true);
                else if (data != null && data.hasExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED_STATUS_CODE)) {
                    String statusCode = data.getStringExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED_STATUS_CODE);
                    loadOrderResultFailed(orderResponseEvent.orderResponse, statusCode, true);
                } else {
                    loadOrderResultFailed(orderResponseEvent.orderResponse, "0", true);
                }
            }
        } else if (requestCode == Constants.PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null && data.hasExtra(PAY_PAL_REQUEST_SUCCESS) && data.getBooleanExtra(PAY_PAL_REQUEST_SUCCESS, false)) {
                loadOrderResultSuccess(orderResponseEvent.orderResponse, true);
            } else if (data != null && data.hasExtra(Constants.PAY_PAL_REQUEST_FAILED_STATUS_CODE)) {
                loadOrderResultFailed(orderResponseEvent.orderResponse, data.getStringExtra(Constants.PAY_PAL_REQUEST_FAILED_STATUS_CODE), true);
            } else {
                loadOrderResultFailed(orderResponseEvent.orderResponse, "0", true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (currentStep == 1) {
            finish();
        } else if (currentStep == 2) {
            loadChooseAddress(false);
        } else if (currentStep == 3) {
            loadChoosePayment(false);
        } else if (currentStep == 4) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void setDefaultAddresses() {
        if(Shopiroller.getListener() != null)
            Shopiroller.getListener().onDefaultAddressChanged(makeOrder.userBillingAddressModel.id, makeOrder.userShippingAddressModel.id);
    }

    private void getPaymentOptions(boolean isFromRight) {
        if (ECommerceUtil.getPaymentSettings() != null) {
            if (orderResultFragment != null)
                getSupportFragmentManager().beginTransaction().remove(orderResultFragment).commitAllowingStateLoss();
            loadChoosePayment(isFromRight);
            return;
        }

        if (!isFinishing() && !legacyProgressViewHelper.isShowing())
            legacyProgressViewHelper.show();

        new ECommerceUtil().getPaymentSettings(new ECommerceRequestHelper.ECommerceCallBack<PaymentSettings>() {
            @Override
            public void done() {
                if (!isFinishing() && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
            }

            @Override
            public void onSuccess(PaymentSettings result) {
                if (orderResultFragment != null)
                    getSupportFragmentManager().beginTransaction().remove(orderResultFragment).commitAllowingStateLoss();
                loadChoosePayment(isFromRight);
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                ErrorUtils.showErrorToast(OrderFlowActivity.this);
            }

            @Override
            public void onNetworkError(String result) {
                ErrorUtils.showErrorToast(OrderFlowActivity.this);
            }
        });
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            EventBus.getDefault().post(new StripeCancelEvent());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            stripeErrorCode = ((PaymentSheetResult.Failed) paymentSheetResult).getError().toString();
            setFailRequest();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            setSuccessRequest();
        }
    }

    private void setSuccessRequest() {
        Call<ECommerceResponse> responseCall = eCommerceRequestHelper.getApiService().stripeCompletePayment(stripeOrderStatusModel);
        enqueue(responseCall, true);
    }

    private void enqueue(Call<ECommerceResponse> responseCall, Boolean mIsSuccess) {
        legacyProgressViewHelper.show();
        responseCall.enqueue(new Callback<ECommerceResponse>() {
            @Override
            public void onResponse(Call<ECommerceResponse> call, Response<ECommerceResponse> response) {
                if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
                if (mIsSuccess) {
                    loadOrderResultSuccess(orderResponseEvent.orderResponse, true);
                } else {
                    loadOrderResultFailed(orderResponseEvent.orderResponse, stripeErrorCode, true);
                }
            }

            @Override
            public void onFailure(Call<ECommerceResponse> call, Throwable t) {
                if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
                ErrorUtils.showErrorToast(OrderFlowActivity.this);
                loadOrderResultFailed(orderResponseEvent.orderResponse, stripeErrorCode, true);
            }
        });
    }

    private void setFailRequest() {
        Call<ECommerceResponse> responseCall = eCommerceRequestHelper.getApiService().stripeFailurePayment(stripeOrderStatusModel);
        enqueue(responseCall, false);
    }

    private void presentPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("ShopirollerSDK");
        paymentSheet.presentWithPaymentIntent(paymentClientSecret, configuration);
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}