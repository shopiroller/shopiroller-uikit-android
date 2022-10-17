package com.shopiroller.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.Shopiroller;
import com.shopiroller.adapter.ShoppingCartCouponAdapter;
import com.shopiroller.adapter.ShoppingCartListAdapter;
import com.shopiroller.adapter.ShoppingInvalidCartListAdapter;
import com.shopiroller.constants.Constants;
import com.shopiroller.enums.MobirollerDialogType;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.LegacyToolbarHelper;
import com.shopiroller.interfaces.ShoppingCartListener;
import com.shopiroller.models.BuyerOrderModel;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.MakeOrder;
import com.shopiroller.models.ShoppingCartResponse;
import com.shopiroller.models.UpdateCartItemQuantity;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.legacy.ShopirollerButton;
import com.shopiroller.views.legacy.ShopirollerClickableLayout;
import com.shopiroller.views.legacy.ShopirollerDialog;
import com.shopiroller.views.legacy.ShopirollerTextView;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends ECommerceBaseActivity implements ShoppingCartListener, ShoppingCartCouponAdapter.ShoppingCartCouponListener {

    @BindView(R2.id.toolbar)
    ShopirollerToolbar toolbar;
    @BindView(R2.id.empty_layout)
    ConstraintLayout emptyLayout;
    @BindView(R2.id.content_layout)
    ConstraintLayout contentLayout;
    @BindView(R2.id.item_count_text_view)
    ShopirollerTextView itemCountTextView;
    @BindView(R2.id.confirm_button)
    ShopirollerButton confirmButton;
    @BindView(R2.id.campaign_layout)
    ShopirollerClickableLayout campaignLayout;
    @BindView(R2.id.product_total_description_text_view)
    ShopirollerTextView productSubTotalTextView;
    @BindView(R2.id.shipping_total_description_text_view)
    ShopirollerTextView shippingTotalTextView;
    @BindView(R2.id.total)
    TextView totalTextView;
    @BindView(R2.id.info_text_view)
    ShopirollerTextView infoTextView;
    @BindView(R2.id.list)
    RecyclerView list;
    @BindView(R2.id.coupon_recycler_view)
    RecyclerView couponRecyclerView;
    @BindView(R2.id.bottom_layout)
    ConstraintLayout bottomLayout;
    @BindView(R2.id.coupon_discount_price_description)
    ShopirollerTextView couponDiscountPriceTextView;
    @BindView(R2.id.coupon_discount_price_text_view)
    ShopirollerTextView couponDiscountPrice;

    private Parcelable recyclerViewState;

    private String couponString;

    final static int SHOPPING_CART_REQUEST_CODE = 11;

    private ShoppingCartListAdapter adapter;
    private ShoppingCartCouponAdapter shoppingCartCouponAdapter;
    private final ECommerceRequestHelper eCommerceRequestHelper = new ECommerceRequestHelper();
    ShoppingCartResponse shoppingCart;
    LegacyProgressViewHelper legacyProgressViewHelper;
    MaterialDialog invalidDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Shopiroller.getUserLoginStatus())
            Shopiroller.getListener().loginNeeded();

        setContentView(R.layout.activity_e_commerce_shopping_cart);
        ButterKnife.bind(this);
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);

        new LegacyToolbarHelper().setStatusBar(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);

        toolbar.setTitleTypeface();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setTitle(getString(R.string.e_commerce_shopping_cart_title));

        if (getIntent().hasExtra(Constants.SHOPPING_CART)) {
            shoppingCart = (ShoppingCartResponse) getIntent().getSerializableExtra(Constants.SHOPPING_CART);
            setContent();
        } else {
            getShoppingCart();
        }
    }

    void setEmpty() {
        contentLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    void setContent() {
        if (invalidDialog != null)
            invalidDialog.dismiss();
        contentLayout.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        if (shoppingCart.items != null && shoppingCart.items.size() != 0) {
            setAdapter();
            itemCountTextView.setText(getString(R.string.e_commerce_shopping_cart_item_count, shoppingCart.items.size()));
            productSubTotalTextView.setText(String.format(": %s", ECommerceUtil.getFormattedPrice(shoppingCart.subTotalPrice, shoppingCart.currency)));
            shippingTotalTextView.setText(String.format(": %s", ECommerceUtil.getFormattedPrice(shoppingCart.shippingPrice, shoppingCart.currency)));
            totalTextView.setText(ECommerceUtil.getFormattedPrice(shoppingCart.totalPrice, shoppingCart.currency));

            bottomLayout.setVisibility(View.VISIBLE);
            updateBottomView();

            if (shoppingCart.messages != null && shoppingCart.messages.size() != 0) {
                boolean flag = false;
                for (int i = 0; i < shoppingCart.messages.size(); i++) {
                    if (shoppingCart.messages.get(i).type.equalsIgnoreCase("Campaign")) {
                        campaignLayout.setVisibility(View.VISIBLE);
                        infoTextView.setText(shoppingCart.messages.get(i).message);
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    campaignLayout.setVisibility(View.GONE);
            } else
                campaignLayout.setVisibility(View.GONE);
        } else {
            setEmpty();
        }

        if (shoppingCart.invalidItems != null && shoppingCart.invalidItems.size() > 0) {
            ShoppingInvalidCartListAdapter adapter = new ShoppingInvalidCartListAdapter(this,
                    shoppingCart.invalidItems,
                    this);
            if (invalidDialog != null)
                invalidDialog.dismiss();
            invalidDialog = new ShopirollerDialog.Builder()
                    .setContext(this)
                    .setIconResource(R.drawable.ic_edit_white_24dp)
                    .setColor(Color.parseColor("#F8E7D8"))
                    .setTitle(getString(R.string.e_commerce_shopping_cart_updating_popup_title))
                    .setAutoDismiss(false)
                    .setListener(() -> {
                        if (shoppingCart.items != null && shoppingCart.items.size() != 0)
                            invalidDialog.dismiss();
                        validateShopping();
                    })
                    .setDescription(getString(R.string.e_commerce_shopping_cart_updating_popup_description))
                    .setType(MobirollerDialogType.LIST_WITH_BUTTON)
                    .setButtonText(getString(R.string.e_commerce_shopping_cart_invalid_popup_button))
                    .setAdapter(adapter)
                    .show();
        }
    }

    private void checkDiscount() {
        if (!shoppingCart.couponId.equals("") && shoppingCart.couponPrice != 0.0) {
            couponString = shoppingCart.couponName;
        } else {
            couponString = "";
        }
        setContent();
        updateBottomView();
    }

    void setAdapter() {
        adapter = new ShoppingCartListAdapter(this, shoppingCart.items, this);
        shoppingCartCouponAdapter = new ShoppingCartCouponAdapter(this, couponString, this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        couponRecyclerView.setLayoutManager(linearLayoutManager1);
        couponRecyclerView.setAdapter(shoppingCartCouponAdapter);
        list.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(linearLayoutManager);

        if (0 == list.getItemDecorationCount()) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(),
                    linearLayoutManager.getOrientation());
            list.addItemDecoration(dividerItemDecoration);
        }

        if (recyclerViewState != null) {
            list.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }

    }

    void getShoppingCart() {
        if (adapter != null)
            recyclerViewState = list.getLayoutManager().onSaveInstanceState();
        legacyProgressViewHelper.show();
        Call<ECommerceResponse<ShoppingCartResponse>> responseCall = eCommerceRequestHelper.getApiService().getShoppingCart(Shopiroller.getUserId());
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<ShoppingCartResponse>() {
            @Override
            public void done() {
                if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
            }

            @Override
            public void onSuccess(ShoppingCartResponse result) {
                shoppingCart = result;
                if (shoppingCart != null && shoppingCart.items != null) {
                    ECommerceUtil.setBadgeCount(shoppingCart.items.size());
                }
                if (shoppingCart.couponId != null && !shoppingCart.couponId.equals("")) {
                    checkDiscount();
                }
                setCouponString();
                setContent();
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                ErrorUtils.showErrorToast(ShoppingCartActivity.this);
            }

            @Override
            public void onNetworkError(String result) {
                ErrorUtils.showErrorToast(ShoppingCartActivity.this);
            }
        });
    }

    void validateShopping() {
        legacyProgressViewHelper.show();
        Call<ECommerceResponse<ShoppingCartResponse>> responseCall = eCommerceRequestHelper.getApiService().validateShoppingCart(Shopiroller.getUserId());
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<ShoppingCartResponse>() {
            @Override
            public void done() {
                if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
            }

            @Override
            public void onSuccess(ShoppingCartResponse result) {
                shoppingCart = result;
                setContent();
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                ErrorUtils.showErrorToast(ShoppingCartActivity.this);
            }

            @Override
            public void onNetworkError(String result) {
                ErrorUtils.showErrorToast(ShoppingCartActivity.this);
            }
        });
    }

    private void removeItemFromShoppingCart(String shoppingCartItemId) {
        Call<ECommerceResponse> responseCall = eCommerceRequestHelper.getApiService().removeItemFromShoppingCart(Shopiroller.getUserId(), shoppingCartItemId);
        enqueue(responseCall);
    }

    private void updateCartItemQuantity(String shoppingCartItemId, int quantity) {
        Call<ECommerceResponse> responseCall = eCommerceRequestHelper.getApiService().updateItemQuantity(Shopiroller.getUserId(), shoppingCartItemId, new UpdateCartItemQuantity(quantity));
        enqueue(responseCall);
    }

    private void clearShoppingCart() {
        Call<ECommerceResponse> responseCall = eCommerceRequestHelper.getApiService().clearShoppingCart(Shopiroller.getUserId());
        enqueue(responseCall);
    }

    private void enqueue(Call<ECommerceResponse> responseCall) {
        legacyProgressViewHelper.show();
        responseCall.enqueue(new Callback<ECommerceResponse>() {
            @Override
            public void onResponse(Call<ECommerceResponse> call, Response<ECommerceResponse> response) {
                if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
                if (response.isSuccessful()) {
                    getShoppingCart();
                }
            }

            @Override
            public void onFailure(Call<ECommerceResponse> call, Throwable t) {

                if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
                ErrorUtils.showErrorToast(ShoppingCartActivity.this);
            }
        });
    }

    private void couponEnqueue(Call<ECommerceResponse<ShoppingCartResponse>> responseCall) {
        legacyProgressViewHelper.show();
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<ShoppingCartResponse>() {
            @Override
            public void done() {
                if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
            }

            @Override
            public void onSuccess(ShoppingCartResponse result) {
                shoppingCart = result;
                setCouponString();
                setContent();
                updateBottomView();
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                if (result.isUserFriendlyMessage) showCouponErrorDialog(result.message);
                else ErrorUtils.showErrorToast(ShoppingCartActivity.this);
            }

            @Override
            public void onNetworkError(String result) {
                ErrorUtils.showErrorToast(ShoppingCartActivity.this);
            }
        });
    }

    private void showCouponErrorDialog(String couponErrorMessage) {
        new ShopirollerDialog.Builder()
                .setContext(this)
                .setTitle(getString(R.string.e_commerce_shopping_cart_coupon_dialog_title))
                .setIconResource(R.drawable.ic_document_icon)
                .setDescription(couponErrorMessage)
                .setButtonText(getString(R.string.e_commerce_product_detail_terms_delivery_conditions_popup_button))
                .setType(MobirollerDialogType.BASIC)
                .show();
    }

    private void setCouponString() {
        if (shoppingCart.couponName != null && !shoppingCart.couponName.equals("")) {
            couponString = shoppingCart.couponName;
        } else {
            couponString = "";
        }
    }

    private void updateBottomView() {
        shoppingCartCouponAdapter.updateString(couponString);
        if (bottomLayout.getVisibility() == View.VISIBLE) {
            if (shoppingCart.couponPrice != null && shoppingCart.couponPrice != 0.0 && !shoppingCart.couponId.equals("")) {
                couponDiscountPriceTextView.setVisibility(View.VISIBLE);
                couponDiscountPrice.setVisibility(View.VISIBLE);
                couponDiscountPrice.setText(String.format(": -%s", ECommerceUtil.getFormattedPrice(shoppingCart.couponPrice, shoppingCart.currency)));
            } else {
                couponDiscountPriceTextView.setVisibility(View.GONE);
                couponDiscountPrice.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClickRemoveItem(String shoppingCartItemId) {
        removeItemFromShoppingCart(shoppingCartItemId);
    }

    @Override
    public void onClickUpdateQuantity(String shoppingCartItemId, int quantity) {
        updateCartItemQuantity(shoppingCartItemId, quantity);
    }

    @OnClick(R2.id.confirm_button)
    public void onClickCheckout() {
        MakeOrder makeOrder = new MakeOrder();
        makeOrder.userId = Shopiroller.getUserId();
        makeOrder.buyer = new BuyerOrderModel();
        OrderFlowActivity.startActivityForResult(makeOrder, this);
    }

    @OnClick(R2.id.start_shopping_button)
    public void onClickStartShoppingButton() {
        finish();
    }

    @OnClick(R2.id.clear_cart_text_view)
    public void onClickClearShoppingCart() {
        new ShopirollerDialog.Builder()
                .setContext(this)
                .setType(MobirollerDialogType.BUTTON)
                .setColor(Color.parseColor("#F8E7D8"))
                .setButtonText(getString(R.string.e_commerce_shopping_cart_clear_cart_button))
                .setListener(this::clearShoppingCart)
                .setIconResource(R.drawable.ic_trash_icon)
                .setTitle(getString(R.string.e_commerce_shopping_cart_clear_cart_title))
                .setDescription(getString(R.string.e_commerce_shopping_cart_clear_cart_description))
                .show();

    }

    private void showCouponDialog(String title, String buttonText) {
        new ShopirollerDialog.Builder()
                .setContext(ShoppingCartActivity.this)
                .setTitle(title)
                .setCouponString(couponString)
                .setType(MobirollerDialogType.TEXT_INPUT)
                .setTextInputDialogListener(new ShopirollerDialog.TextInputDialogButtonCallBack() {
                    @Override
                    public void onClickButton(String textInput) {
                        couponString = textInput;
                        applyCoupon();
                    }
                })
                .setColor(Color.parseColor("#F8E7D8"))
                .setIconResource(R.drawable.ic_coupon)
                .setButtonText(buttonText)
                .show();
    }

    private void applyCoupon() {
        Call<ECommerceResponse<ShoppingCartResponse>> responseCall = eCommerceRequestHelper.getApiService().insertCoupon(Shopiroller.getUserId(), couponString);
        couponEnqueue(responseCall);
    }

    private void removeCoupon() {
        Call<ECommerceResponse<ShoppingCartResponse>> responseCall = eCommerceRequestHelper.getApiService().deleteCoupon(Shopiroller.getUserId(), couponString);
        couponEnqueue(responseCall);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHOPPING_CART_REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onClickCouponView() {
        showCouponDialog(getString(R.string.e_commerce_shopping_cart_coupon_dialog_title), getString(R.string.e_commerce_shopping_cart_coupon_apply_discount));
    }

    @Override
    public void onClickCouponRemoveButton() {
        removeCoupon();
    }
}
