package com.shopiroller.viewholders;

import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R2;
import com.shopiroller.activities.OrderDetailsActivity;
import com.shopiroller.models.Order;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ealtaca on 8/27/17.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {


    @BindView(R2.id.order_number)
    ShopirollerTextView orderNumber;
    @BindView(R2.id.order_date)
    ShopirollerTextView orderDate;
    @BindView(R2.id.order_time)
    ShopirollerTextView orderTime;
    @BindView(R2.id.order_paid)
    ShopirollerTextView orderPaid;
    @BindView(R2.id.status_background_view)
    ConstraintLayout orderStatusBackground;
    @BindView(R2.id.status_text_view)
    ShopirollerTextView orderStatusTextView;

    private AppCompatActivity context;
    private Order order;


    public OrderViewHolder(View itemView, AppCompatActivity activity) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = activity;
    }

    public void bind(Order order) {
        this.order = order;
        orderNumber.setText(String.valueOf(order.orderCode));
        orderDate.setText(new ECommerceUtil().getFormattedDateTime(order.createdDate,ECommerceUtil.dateFormatOrderDateString));
        orderTime.setText(new ECommerceUtil().getFormattedDateTime(order.createdDate,ECommerceUtil.dateFormatOrderTimeString));
        orderPaid.setText(ECommerceUtil.getFormattedPrice(order.totalPrice, order.currency));
        orderStatusTextView.setText(ECommerceUtil.getOrderStatus(order.currentStatus, context));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            orderStatusBackground.setBackgroundTintList(ColorStateList.valueOf(ECommerceUtil.getOrderStatusColor(order.currentStatus, context)));
        }
    }

    @OnClick(R2.id.main_layout)
    public void onClickOrderDetail() {
        OrderDetailsActivity.startActivity(context, order.id);
    }


}
