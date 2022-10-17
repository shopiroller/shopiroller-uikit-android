package com.shopiroller.viewholders;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.constants.Constants;
import com.shopiroller.helpers.UtilManager;
import com.shopiroller.interfaces.ShoppingCartListener;
import com.shopiroller.models.ShoppingCartItem;
import com.shopiroller.views.legacy.ShopirollerClickableLayout;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ealtaca on 8/27/17.
 */

public class ShoppingInvalidCartViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.image_view)
    public ImageView image;
    @BindView(R2.id.title_text_view)
    ShopirollerTextView title;
    @BindView(R2.id.badge_text)
    ShopirollerTextView badgeTextView;

    @BindView(R2.id.quantity_layout)
    ConstraintLayout quantityLayout;
    @BindView(R2.id.count_text_view)
    ShopirollerTextView countTextView;
    @BindView(R2.id.minus_text_view)
    ShopirollerClickableLayout minusLayout;
    @BindView(R2.id.plus_text_view)
    ShopirollerClickableLayout plusLayout;
    @BindView(R2.id.badge_view)
    CardView badgeView;

    private ShoppingCartListener shoppingCartListener;
    private ShoppingCartItem shoppingCartItem;

    public ShoppingInvalidCartViewHolder(View itemView, ShoppingCartListener shoppingCartListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.shoppingCartListener = shoppingCartListener;
    }

    public void bind(ShoppingCartItem shoppingCartItem) {
        this.shoppingCartItem = shoppingCartItem;
        if (shoppingCartItem.product.featuredImage != null && shoppingCartItem.product.featuredImage.n != null)
            Glide.with(itemView)
                    .load(shoppingCartItem.product.featuredImage.n + "?width=" + image.getWidth())
                    .placeholder(R.drawable.no_image_e_commerce)
                    .error(
                            Glide.with(itemView)
                                    .load(R.drawable.no_image_e_commerce))
                    .into(image);
        else
            Glide.with(itemView).load(R.drawable.no_image_e_commerce).into(image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.setTransitionName("featuredImage");
        }

        title.setText(UtilManager.localizationHelper().getLocalizedTitle(shoppingCartItem.product.title));

        minusLayout.setEnabled(shoppingCartItem.quantity != 1);

        if (shoppingCartItem.quantity >= shoppingCartItem.product.maxQuantityPerOrder || shoppingCartItem.quantity >= shoppingCartItem.product.stock) {
            plusLayout.setEnabled(false);
        } else {
            plusLayout.setEnabled(true);
        }

        if (shoppingCartItem.messages != null && shoppingCartItem.messages.size() != 0) {
            String text = "";

            if (Constants.INVALID_CART_ITEM_STATUS.containsKey(shoppingCartItem.messages.get(0).key)) {
                badgeTextView.setTextColor(Color.parseColor("#fa2e2e"));
                badgeTextView.setAllCaps(true);
                int textResource = Constants.INVALID_CART_ITEM_STATUS.get(shoppingCartItem.messages.get(0).key);
                if (Constants.SHOPPING_CART_NOT_ENOUGH_STOCK.equalsIgnoreCase(shoppingCartItem.messages.get(0).key)) {
                    text = itemView.getContext().getString(textResource, shoppingCartItem.product.stock);
                    countTextView.setText(String.valueOf(shoppingCartItem.product.stock));
                    quantityLayout.setVisibility(View.VISIBLE);
                } else {
                    text = itemView.getContext().getString(textResource);
                    quantityLayout.setVisibility(View.GONE);
                }
            }

            if (text != null) {
                badgeView.setVisibility(View.VISIBLE);
                badgeTextView.setText(text);
            } else
                badgeView.setVisibility(View.GONE);
        } else
            badgeView.setVisibility(View.GONE);

    }

    @OnClick(R2.id.minus_text_view)
    public void onClickMinus() {
        if (shoppingCartItem.quantity != 1)
            shoppingCartListener.onClickUpdateQuantity(shoppingCartItem.id, shoppingCartItem.quantity - 1);
    }

    @OnClick(R2.id.plus_text_view)
    public void onClickPlusItem() {
        shoppingCartListener.onClickUpdateQuantity(shoppingCartItem.id, shoppingCartItem.quantity + 1);
    }

}
