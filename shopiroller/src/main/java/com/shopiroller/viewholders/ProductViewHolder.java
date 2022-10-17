package com.shopiroller.viewholders;

import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.helpers.UtilManager;
import com.shopiroller.models.ProductDetailModel;
import com.shopiroller.util.ScreenUtil;
import com.shopiroller.views.legacy.ShopirollerBadgeView;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 8/27/17.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {


    @BindView(R2.id.product_image)
    public ImageView image;
    @BindView(R2.id.product_title)
    TextView title;
    @BindView(R2.id.product_price)
    TextView price;
    @BindView(R2.id.product_price_campaign)
    TextView priceCampaign;
    @BindView(R2.id.sold_out_badge)
    ShopirollerBadgeView soldOutBadge;
    @BindView(R2.id.sale_badge)
    CardView saleBadge;
    @BindView(R2.id.free_shipping_badge)
    ShopirollerBadgeView freeShippingBadge;
    @BindView(R2.id.sale_rate_text_view)
    ShopirollerTextView saleRateTextView;

    public ProductViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ProductDetailModel product) {
        if (product.featuredImage != null && product.featuredImage.n != null) {
            Glide.with(itemView)
                    .load(product.featuredImage.n + "?width=" +ScreenUtil.getScreenWidth() / 2)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .error(Glide.with(itemView)
                            .load(R.drawable.no_image_e_commerce))
                    .placeholder(R.drawable.no_image_e_commerce)
                    .into(image);
        } else
            Glide.with(itemView).load(R.drawable.no_image_e_commerce).into(image);

        title.setText(UtilManager.localizationHelper().getLocalizedTitle(product.title));

        if (product.campaignPrice != 0) {
            price.setText(String.format("%s %s", ECommerceUtil.getPriceString(product.campaignPrice), ECommerceUtil.getCurrencySymbol(product.currency)));
            priceCampaign.setText(String.format("%s %s", ECommerceUtil.getPriceString(product.price), ECommerceUtil.getCurrencySymbol(product.currency)));
            priceCampaign.setPaintFlags(priceCampaign.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceCampaign.setVisibility(View.VISIBLE);
            saleBadge.setVisibility(View.VISIBLE);
            saleRateTextView.setText(String.format("%%%s", Math.round(100 * (product.price - product.campaignPrice) / product.price)));
            saleRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        } else {
            saleBadge.setVisibility(View.GONE);
            priceCampaign.setVisibility(View.GONE);
            price.setText(String.format("%s %s", ECommerceUtil.getPriceString(product.price), ECommerceUtil.getCurrencySymbol(product.currency)));
        }

        if(product.shippingPrice == 0 && !product.useFixPrice) {
            freeShippingBadge.setVisibility(View.VISIBLE);
        } else {
            freeShippingBadge.setVisibility(View.GONE);
        }

        if (product.stock == 0) {
            soldOutBadge.setVisibility(View.VISIBLE);
            soldOutBadge.requestLayout();
        } else {
            soldOutBadge.setVisibility(View.GONE);
        }


    }

}
