package com.shopiroller.activities;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shopiroller.R;
import com.shopiroller.Shopiroller;

public class ECommerceBaseActivity extends AppCompatActivity {

    public void startShoppingCartActivity() {
        if (!Shopiroller.getUserLoginStatus()) {
            if (Shopiroller.getListener() != null)
                Shopiroller.getListener().loginNeeded();
        } else
            startActivity(new Intent(this, ShoppingCartActivity.class));
    }


    public void setBadgeCount(Toolbar toolbar, int count) {
        if (toolbar != null &&
                toolbar.getMenu() != null &&
                toolbar.getMenu().findItem(R.id.action_shopping_cart) != null &&
                toolbar.getMenu().findItem(R.id.action_shopping_cart).getActionView() != null) {
            View actionView = toolbar.getMenu().findItem(R.id.action_shopping_cart).getActionView();
            TextView titleBadge = actionView.findViewById(R.id.cart_badge);
            if (count > 0) {
                titleBadge.setText(String.valueOf(count));
                titleBadge.setVisibility(View.VISIBLE);
            } else {
                titleBadge.setVisibility(View.GONE);
            }
        }
    }
}
