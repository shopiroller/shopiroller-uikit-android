package com.shopiroller.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.adapter.OrderDetailProductListAdapter;
import com.shopiroller.constants.Constants;
import com.shopiroller.helpers.LocalizationHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.helpers.ProgressViewHelper;
import com.shopiroller.helpers.ToolbarHelper;
import com.shopiroller.helpers.UtilManager;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.OrderDetailModel;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.legacy.ShopirollerTextView;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class OrderDetailsActivity extends ECommerceBaseActivity {

    @BindView(R2.id.order_title)
    ShopirollerTextView orderTitle;
    @BindView(R2.id.order_status)
    ShopirollerTextView orderStatus;
    @BindView(R2.id.order_user_note)
    ShopirollerTextView orderUserNote;
    @BindView(R2.id.order_status_layout)
    ConstraintLayout orderStatusLayout;
    @BindView(R2.id.order_date)
    ShopirollerTextView orderDate;
    @BindView(R2.id.order_code)
    ShopirollerTextView orderCodeTextView;
    @BindView(R2.id.order_layout)
    CardView orderLayout;
    @BindView(R2.id.payment_title)
    ShopirollerTextView paymentTitle;
    @BindView(R2.id.payment_method_title)
    ShopirollerTextView paymentMethodTitle;
    @BindView(R2.id.payment_method_description)
    ShopirollerTextView paymentMethodDescription;
    @BindView(R2.id.payment_layout)
    CardView paymentLayout;
    @BindView(R2.id.cargo_title)
    ShopirollerTextView cargoTitle;
    @BindView(R2.id.delivery_address_title)
    ShopirollerTextView deliveryAddressTitle;
    @BindView(R2.id.delivery_address_description)
    ShopirollerTextView deliveryAddressDescription;
    @BindView(R2.id.billing_address_title)
    ShopirollerTextView billingAddressTitle;
    @BindView(R2.id.billing_address_description)
    ShopirollerTextView billingAddressDescription;
    @BindView(R2.id.cargo_layout)
    CardView cargoLayout;
    @BindView(R2.id.order_status_image)
    ImageView orderStatusImage;
    @BindView(R2.id.product_layout)
    CardView productLayout;
    @BindView(R2.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.cargo_main_layout)
    LinearLayout cargoMainLayout;
    @BindView(R2.id.main_layout)
    ConstraintLayout mainLayout;
    @BindView(R2.id.cargo_tracking_number)
    TextView cargoTrackingNumberTextView;
    @BindView(R2.id.cargo_company_name)
    TextView cargoCompanyNameTextView;
    @BindView(R2.id.preview_layout)
    CardView previewLayout;
    @BindView(R2.id.toolbar)
    ShopirollerToolbar toolbar;
    @BindView(R2.id.product_list)
    RecyclerView productList;
    @BindView(R2.id.bottom_layout)
    ConstraintLayout bottomLayout;
    @BindView(R2.id.bank_transfer_layout)
    LinearLayout bankTransferLayout;
    @BindView(R2.id.bank_name)
    ShopirollerTextView bankNameTextView;
    @BindView(R2.id.receiver_name)
    ShopirollerTextView receiverNameTextView;
    @BindView(R2.id.bank_account)
    ShopirollerTextView bankAccountTextView;
    @BindView(R2.id.bank_iban)
    ShopirollerTextView bankIbanTextView;

    @BindView(R2.id.product_total_description_text_view)
    ShopirollerTextView productSubTotalTextView;
    @BindView(R2.id.shipping_total_description_text_view)
    ShopirollerTextView shippingTotalTextView;
    @BindView(R2.id.total)
    ShopirollerTextView totalTextView;
    @BindView(R2.id.coupon_discount_price_description)
    ShopirollerTextView couponDiscountPriceTextView;
    @BindView(R2.id.coupon_discount_price_text_view)
    ShopirollerTextView couponDiscountPrice;

    ECommerceRequestHelper eCommerceRequestHelper;
    LocalizationHelper localizationHelper;
    private String mOrderId;
    OrderDetailModel mOrderDetailModel;
    private ProgressViewHelper progressViewHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        new ToolbarHelper().setStatusBar(this);
        progressViewHelper = new ProgressViewHelper(this);
        localizationHelper = UtilManager.localizationHelper();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);

        toolbar.setTitleTypeface();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setTitle(getString(R.string.e_commerce_my_orders_title));
        eCommerceRequestHelper = new ECommerceRequestHelper();
        if (NetworkHelper.isConnected(this)) {
            progressViewHelper.show();

            if (getIntent().hasExtra(Constants.ORDER_ID)) {
                mOrderId = getIntent().getStringExtra(Constants.ORDER_ID);
                getOrder();
            } else if (getIntent().hasExtra(Constants.ORDER_DETAIL_MODEL)) {
                mOrderDetailModel = (OrderDetailModel) getIntent().getSerializableExtra(Constants.ORDER_DETAIL_MODEL);
                mOrderId = mOrderDetailModel.id;
                setupView();
            } else {
                finish();
                progressViewHelper.dismiss();
            }
            swipeRefreshLayout.setOnRefreshListener(this::getOrder);
        } else
            DialogUtil.showNoConnectionError(this);
    }

    void getOrder() {

        Call<ECommerceResponse<OrderDetailModel>> responseCall = eCommerceRequestHelper.getApiService().getOrder(mOrderId);
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<OrderDetailModel>() {
            @Override
            public void done() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(OrderDetailModel result) {
                mOrderDetailModel = result;
                setupView();
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                ErrorUtils.showErrorToast(OrderDetailsActivity.this);
            }

            @Override
            public void onNetworkError(String result) {

            }
        });
    }

    void setupView() {
        orderStatusImage.setImageDrawable(ContextCompat.getDrawable(this, ECommerceUtil.getOrderStatusIcon(mOrderDetailModel.currentStatus)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            orderStatusImage.setBackgroundTintList(ColorStateList.valueOf(ECommerceUtil.getOrderStatusColor(mOrderDetailModel.currentStatus, this)));
        }

//        setTitle(getString(R.string.e_commerce_order_details_title, String.valueOf(mOrderDetailModel.orderCode)));
//        orderStatus.setTextColor(ECommerceUtil.getOrderStatusColor(mOrderDetailModel.currentStatus, this));
        orderStatus.setText(ECommerceUtil.getOrderStatus(mOrderDetailModel.currentStatus, this));
        orderStatus.setText(ECommerceUtil.getOrderStatus(mOrderDetailModel.currentStatus, this));

        if (!ECommerceUtil.getOrderStatus(mOrderDetailModel.userNote, this).equals("")) {
            orderUserNote.setVisibility(View.VISIBLE);
            orderUserNote.setText(getString(R.string.e_commerce_order_details_user_note_sub_title) + " " +ECommerceUtil.getOrderStatus(mOrderDetailModel.userNote, this));
        }

        if ((mOrderDetailModel.shippingTrackingCode != null || mOrderDetailModel.shippingTrackingCompany != null) &&
                (mOrderDetailModel.currentStatus.equalsIgnoreCase(Constants.SHIPPED) || mOrderDetailModel.currentStatus.equalsIgnoreCase(Constants.DELIVERED))) {
            cargoMainLayout.setVisibility(View.VISIBLE);
            if (mOrderDetailModel.shippingTrackingCompany != null)
                cargoCompanyNameTextView.setText(mOrderDetailModel.shippingTrackingCompany);
            else
                cargoCompanyNameTextView.setVisibility(View.GONE);
            if (mOrderDetailModel.shippingTrackingCode != null)
                cargoTrackingNumberTextView.setText(mOrderDetailModel.shippingTrackingCode);
            else
                cargoTrackingNumberTextView.setVisibility(View.GONE);
        } else {
            cargoMainLayout.setVisibility(View.GONE);
        }

        orderDate.setText(new ECommerceUtil().getFormattedDateTime(mOrderDetailModel.createdDate,ECommerceUtil.dateFormatOrderDetailString));

        orderCodeTextView.setText(mOrderDetailModel.orderCode);
        deliveryAddressDescription.setText(String.format(mOrderDetailModel.shippingAddress.getDescriptionArea(), mOrderDetailModel.buyer.getFullName()));
        billingAddressDescription.setText(String.format(mOrderDetailModel.billingAddress.getBillingDescriptionArea(), mOrderDetailModel.buyer.getFullName()));

        if (mOrderDetailModel.paymentType != null) {
            if (mOrderDetailModel.paymentType.equalsIgnoreCase(Constants.PAY_AT_DOOR)) {
                paymentMethodTitle.setText(getString(R.string.e_commerce_payment_method_selection_pay_at_door));
                paymentMethodDescription.setVisibility(View.GONE);
                paymentLayout.requestLayout();
            } else if (mOrderDetailModel.paymentType.equalsIgnoreCase(Constants.ONLINE) || mOrderDetailModel.paymentType.equalsIgnoreCase(Constants.ONLINE3DS)) {
                paymentMethodTitle.setText(getString(R.string.e_commerce_payment_method_selection_credit_card));
                if (mOrderDetailModel.cardNumber != null) {
                    paymentMethodDescription.setText(mOrderDetailModel.cardNumber.replaceAll("[^0-9]", "") + " **** **** ****");
                    paymentMethodDescription.setVisibility(View.VISIBLE);
                }
            } else if (mOrderDetailModel.paymentType.equalsIgnoreCase(Constants.TRANSFER)) {
                paymentMethodTitle.setText(getString(R.string.e_commerce_payment_method_selection_transfer));
                if (mOrderDetailModel.paymentAccount != null) {
                    bankTransferLayout.setVisibility(View.VISIBLE);
                    bankNameTextView.setText(mOrderDetailModel.paymentAccount.name + " - " + mOrderDetailModel.paymentAccount.accountName + " / " + mOrderDetailModel.paymentAccount.accountCode);
                    receiverNameTextView.setText(Html.fromHtml(getString(R.string.e_commerce_order_details_bank_receiver, mOrderDetailModel.paymentAccount.nameSurname)));
                    bankAccountTextView.setText(Html.fromHtml(getString(R.string.e_commerce_order_details_bank_account, mOrderDetailModel.paymentAccount.accountNumber)));
                    bankIbanTextView.setText(Html.fromHtml(getString(R.string.e_commerce_order_details_bank_iban, mOrderDetailModel.paymentAccount.accountAddress)));
                    paymentMethodDescription.setVisibility(View.GONE);
                } else if (mOrderDetailModel.bankAccount != null) {
                    paymentMethodDescription.setText(mOrderDetailModel.bankAccount);
                    paymentMethodDescription.setVisibility(View.VISIBLE);
                } else
                    paymentMethodDescription.setVisibility(View.GONE);
            } else if (mOrderDetailModel.paymentType.equalsIgnoreCase(Constants.PAYPAL)) {
                paymentMethodTitle.setText(getString(R.string.e_commerce_payment_method_selection_paypal));
            } else if (mOrderDetailModel.paymentType.equalsIgnoreCase(Constants.STRIPE)) {
                paymentTitle.setText(getString(R.string.e_commerce_payment_method_selection_stripe));
            }
        }

        productSubTotalTextView.setText(": " + ECommerceUtil.getFormattedPrice(getSubTotal(), mOrderDetailModel.currency));
        shippingTotalTextView.setText(": " + ECommerceUtil.getFormattedPrice(mOrderDetailModel.shippingPrice, mOrderDetailModel.currency));
        totalTextView.setText(ECommerceUtil.getFormattedPrice(mOrderDetailModel.totalPrice, mOrderDetailModel.currency));
        if (mOrderDetailModel.appliedCouponModel.getDiscountPrice() != null && mOrderDetailModel.appliedCouponModel.getDiscountPrice() != 0.0 && !mOrderDetailModel.appliedCouponModel.getId().equals("")) {
            couponDiscountPriceTextView.setVisibility(View.VISIBLE);
            couponDiscountPrice.setVisibility(View.VISIBLE);
            couponDiscountPrice.setText(String.format(": -%s", ECommerceUtil.getFormattedPrice(mOrderDetailModel.appliedCouponModel.getDiscountPrice(), mOrderDetailModel.appliedCouponModel.getCurrency())));
        } else  {
            couponDiscountPriceTextView.setVisibility(View.GONE);
            couponDiscountPrice.setVisibility(View.GONE);
        }

        OrderDetailProductListAdapter productAdapter = new OrderDetailProductListAdapter(this, mOrderDetailModel.productList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(linearLayoutManager);
        productList.setAdapter(productAdapter);

        if (0 == productList.getItemDecorationCount()) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(productList.getContext(),
                    linearLayoutManager.getOrientation());
            productList.addItemDecoration(dividerItemDecoration);
        }

        mainLayout.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        progressViewHelper.dismiss();
    }

    private double getSubTotal() {
        if(mOrderDetailModel.appliedCouponModel != null) return mOrderDetailModel.appliedCouponModel.getDiscountPrice() + mOrderDetailModel.totalPrice - mOrderDetailModel.shippingPrice;
        else return mOrderDetailModel.totalPrice - mOrderDetailModel.shippingPrice;
    }

    public static void startActivity(Context context, String orderId) {
        context.startActivity(new Intent(context, OrderDetailsActivity.class).putExtra(Constants.ORDER_ID, orderId));
    }

    public static void startActivity(Context context, OrderDetailModel orderDetailModel) {
        context.startActivity(new Intent(context, OrderDetailsActivity.class).putExtra(Constants.ORDER_DETAIL_MODEL, orderDetailModel));
    }

    @OnClick(R2.id.cargo_tracking_number)
    public void onClickCargoText() {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("cargo", mOrderDetailModel.shippingTrackingCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.e_commerce_order_details_cargo_copy_toast_message), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R2.id.bank_iban)
    public void onClickIbanText() {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("iban", mOrderDetailModel.paymentAccount.accountAddress);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.e_commerce_order_details_bank_copy_toast_message), Toast.LENGTH_SHORT).show();
    }

}
