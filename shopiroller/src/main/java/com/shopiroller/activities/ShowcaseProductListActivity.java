package com.shopiroller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.constants.Constants;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.ToolbarHelper;
import com.shopiroller.models.ShowcaseResponseModel;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.views.ProductListRecyclerView;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowcaseProductListActivity extends ECommerceBaseActivity {

    @BindView(R2.id.toolbar)
    ShopirollerToolbar toolbar;
    @BindView(R2.id.product_list)
    ProductListRecyclerView productListComponent;

    ShowcaseResponseModel model;

    ECommerceRequestHelper ecommerceRequestHelper;
    LegacyProgressViewHelper legacyProgressViewHelper;


    public static void startActivity(@Nullable Context context, @Nullable ShowcaseResponseModel showcaseModel) {
        Intent intent = new Intent(context, ShowcaseProductListActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_SHOWCASE_MODEL, showcaseModel);
        if (context != null) {
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase_product_list);
        ButterKnife.bind(this);

        ecommerceRequestHelper = new ECommerceRequestHelper();
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);

        new ToolbarHelper().setStatusBar(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);

        toolbar.setTitleTypeface();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (getIntent().hasExtra(Constants.INTENT_EXTRA_SHOWCASE_MODEL)) {
            model = (ShowcaseResponseModel) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_SHOWCASE_MODEL);
            if(model.getShowcase().getId() != null) {
                productListComponent.setShowCaseId(model.getShowcase().getId());
                productListComponent.setProgressView(legacyProgressViewHelper);
                productListComponent.setEmptyViewText(getString(R.string.e_commerce_product_search_no_result_title, model.getShowcase().getName()));
                productListComponent.setDefaultList(model.getProducts());
                productListComponent.initialize(this,null);
                productListComponent.loadData();
            }
            setTitle(model.getShowcase().getName());
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        productListComponent.onFilterResult(requestCode,resultCode,data);
    }

}
