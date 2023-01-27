package com.shopiroller.viewholders;

import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.SharedApplication;
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

public class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.image_view)
    public ImageView image;
    @BindView(R2.id.title_text_view)
    ShopirollerTextView title;
    @BindView(R2.id.price_text_view)
    ShopirollerTextView price;
    @BindView(R2.id.price_sale_text_view)
    ShopirollerTextView campaignPrice;

    @BindView(R2.id.count_text_view)
    ShopirollerTextView countTextView;
    @BindView(R2.id.remove_image_view)
    ImageView removeImageView;
    @BindView(R2.id.minus_text_view)
    ShopirollerClickableLayout minusLayout;
    @BindView(R2.id.plus_text_view)
    ShopirollerClickableLayout plusLayout;
    @BindView(R2.id.cargo_layout)
    CardView cargoLayout;
    @BindView(R2.id.cargo_text)
    ShopirollerTextView cargoText;

    private ShoppingCartListener shoppingCartListener;
    private ShoppingCartItem shoppingCartItem;

    public ShoppingCartViewHolder(View itemView, ShoppingCartListener shoppingCartListener) {
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

        if (shoppingCartItem.product.campaignPrice != 0) {
            price.setText(ECommerceUtil.getFormattedPrice(shoppingCartItem.product.campaignPrice * shoppingCartItem.quantity, shoppingCartItem.product.currency));

            campaignPrice.setText(ECommerceUtil.getFormattedPrice(shoppingCartItem.product.price * shoppingCartItem.quantity, shoppingCartItem.product.currency));
            campaignPrice.setPaintFlags(campaignPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            campaignPrice.setVisibility(View.VISIBLE);
        } else {
            campaignPrice.setVisibility(View.GONE);
            price.setText(ECommerceUtil.getFormattedPrice(shoppingCartItem.product.price * shoppingCartItem.quantity, shoppingCartItem.product.currency));
        }

        countTextView.setText(String.valueOf(shoppingCartItem.quantity));

        if (shoppingCartItem.quantity == 1) {
            minusLayout.setEnabled(false);
        } else {
            minusLayout.setEnabled(true);
        }

        if (!shoppingCartItem.product.useFixPrice && shoppingCartItem.product.shippingPrice != 0) {
            cargoLayout.setVisibility(View.VISIBLE);
            cargoText.setText(
                    Html.fromHtml(cargoText.getContext().getString(R.string.e_commerce_shopping_cart_cargo_warning,
                            ECommerceUtil.getFormattedPrice(shoppingCartItem.product.shippingPrice, shoppingCartItem.product.currency))));
        } else {
            cargoLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.remove_image_view)
    public void onClickRemoveItem() {
        shoppingCartListener.onClickRemoveItem(shoppingCartItem.id);
    }

    @OnClick(R2.id.minus_text_view)
    public void onClickMinus() {
        if (shoppingCartItem.quantity != 1)
            shoppingCartListener.onClickUpdateQuantity(shoppingCartItem.id, shoppingCartItem.quantity - 1);
    }

    @OnClick(R2.id.plus_text_view)
    public void onClickPlusItem() {
        if (shoppingCartItem.quantity == shoppingCartItem.product.maxQuantityPerOrder || shoppingCartItem.quantity == shoppingCartItem.product.stock) {
            Toast.makeText(SharedApplication.context, itemView.getContext().getString(R.string.e_commerce_product_detail_maximum_product_message, String.valueOf(shoppingCartItem.quantity)), Toast.LENGTH_SHORT).show();
        } else
            shoppingCartListener.onClickUpdateQuantity(shoppingCartItem.id, shoppingCartItem.quantity + 1);
    }

}
