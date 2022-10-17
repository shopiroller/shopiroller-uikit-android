package com.shopiroller.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.helpers.ToolbarHelper;
import com.shopiroller.views.VideoEnabledWebView;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDescriptionActivity extends ECommerceBaseActivity {

    @BindView(R2.id.toolbar)
    ShopirollerToolbar toolbar;
    @BindView(R2.id.web_view)
    VideoEnabledWebView webView;

    public static String INTENT_PRODUCT_DESCRIPTION  = "ProductDescription";
    public String description  = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_commerce_product_description);
        ButterKnife.bind(this);

        new ToolbarHelper().setStatusBar(this);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

        setSupportActionBar(toolbar);

        toolbar.setTitleTypeface();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setTitle(getString(R.string.e_commerce_product_detail_description));
        if(getIntent().hasExtra(INTENT_PRODUCT_DESCRIPTION))
            description = getIntent().getStringExtra(INTENT_PRODUCT_DESCRIPTION);

        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadDataWithBaseURL("", "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + description, "text/html", "UTF-8", "");

    }

}
