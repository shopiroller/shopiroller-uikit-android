package com.shopiroller.viewholders;

import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.helpers.UtilManager;
import com.shopiroller.models.OrderProduct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 8/27/17.
 */

public class OrderProductViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.product_image)
    public ImageView image;
    @BindView(R2.id.product_title)
    TextView title;
    @BindView(R2.id.price)
    TextView price;
    @BindView(R2.id.description)
    TextView description;

    public OrderProductViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(OrderProduct product) {
        if (product.featuredImage != null && product.featuredImage.n != null)
            Glide.with(itemView)
                    .load(product.featuredImage.n + "?width=" + image.getWidth())
                    .error(Glide.with(itemView)
                            .load(R.drawable.no_image_e_commerce))
                    .placeholder(R.drawable.no_image_e_commerce)
                    .into(image);
        else
            Glide.with(itemView).load(R.drawable.no_image_e_commerce).into(image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.setTransitionName("featuredImage");
        }

        title.setText(UtilManager.localizationHelper().getLocalizedTitle(product.title));

        price.setText(ECommerceUtil.getPriceString(product.price * product.quantity)
                + " " +
                ECommerceUtil.getCurrencySymbol(product.currency));

        description.setText(Html.fromHtml(itemView.getContext().getString(R.string.e_commerce_order_details_quantity, String.valueOf(product.quantity))));
    }

}
