package com.shopiroller.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.Shopiroller;
import com.shopiroller.adapter.UserOrderAdapter;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.LegacyToolbarHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.Order;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.legacy.ShopirollerButton;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class UserOrderActivity extends AppCompatActivity {

    @BindView(R2.id.empty_layout)
    ConstraintLayout emptyView;
    @BindView(R2.id.start_shopping_button)
    ShopirollerButton startShoppingButton;
    @BindView(R2.id.toolbar)
    ShopirollerToolbar toolbar;
    @BindView(R2.id.list)
    RecyclerView list;
    @BindView(R2.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.preview_layout)
    CardView previewLayout;

    ECommerceRequestHelper eCommerceRequestHelper;
    private List<Order> orderListResponse;
    private UserOrderAdapter adapter;
    private LegacyProgressViewHelper legacyProgressViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setTitleTypeface();

        new LegacyToolbarHelper().setStatusBar(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (NetworkHelper.isConnected(this)) {
            legacyProgressViewHelper = new LegacyProgressViewHelper(this);
            getSupportActionBar().setTitle(getString(R.string.e_commerce_my_orders_title));
            eCommerceRequestHelper = new ECommerceRequestHelper();
            swipeRefreshLayout.setOnRefreshListener(() -> getUserOrders());
            getUserOrders();
        } else {
            DialogUtil.showNoConnectionError(this);
        }
    }

    private void getUserOrders() {


        legacyProgressViewHelper.show();

        Call<ECommerceResponse<List<Order>>> responseCall = eCommerceRequestHelper.getApiService().getOrderList(Shopiroller.getUserId());
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<List<Order>>() {
            @Override
            public void done() {
                swipeRefreshLayout.setRefreshing(false);
                if (!isFinishing() && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
            }

            @Override
            public void onSuccess(List<Order> result) {
                if (result != null && result.size() != 0) {
                    orderListResponse = result;
                    setupView();
                } else {
                    swipeRefreshLayout.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                ErrorUtils.showErrorToast(UserOrderActivity.this);
            }

            @Override
            public void onNetworkError(String result) {
                ErrorUtils.showErrorToast(UserOrderActivity.this);
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                if (!isFinishing() && legacyProgressViewHelper.isShowing())
                    legacyProgressViewHelper.dismiss();
            }
        });
    }

    private void setupView() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        adapter = new UserOrderAdapter(this, orderListResponse);
        list.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (legacyProgressViewHelper.isShowing())
            legacyProgressViewHelper.dismiss();
    }

    @OnClick(R2.id.start_shopping_button)
    public void onClickStartShopping() {
        finish();
        Shopiroller.getListener().onStartShoppingClicked();
    }
}
