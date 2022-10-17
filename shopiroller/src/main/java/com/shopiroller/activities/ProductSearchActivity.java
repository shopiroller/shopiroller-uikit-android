package com.shopiroller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.LegacyToolbarHelper;
import com.shopiroller.util.ScreenUtil;
import com.shopiroller.views.MaterialSearchView;
import com.shopiroller.views.ProductListRecyclerView;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import butterknife.ButterKnife;

public class ProductSearchActivity extends ECommerceBaseActivity {

    private ShopirollerToolbar toolbar;
    private ProductListRecyclerView productListComponent;
    private MaterialSearchView searchView;

    LegacyToolbarHelper toolbarHelper;
    LegacyProgressViewHelper legacyProgressViewHelper;

    private MenuItem mSearchItem;
    private String mKeyword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ecommerce_search_list_view);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        productListComponent  = findViewById(R.id.product_list);
        searchView = findViewById(R.id.search_view);

        toolbarHelper = new LegacyToolbarHelper();
        setToolbar();
        new LegacyToolbarHelper().setStatusBar(this);
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);

        productListComponent.setProgressView(legacyProgressViewHelper);
        productListComponent.initialize(this,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        productListComponent.onFilterResult(requestCode,resultCode,data);
    }

    private void setToolbar() {

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);

        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(view -> {
            finish();
            try {
                View viewa = getCurrentFocus();
                if (viewa != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(viewa.getWindowToken(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbarHelper.setStatusBar(this);
        getSupportActionBar().setTitle(mKeyword);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (toolbar != null && toolbar.getMenu() != null)
            toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.e_commerce_search_option_menu);
        mSearchItem = menu.findItem(R.id.m_search);
        searchView.setMenuItem(mSearchItem);
        searchView.setHint(getString(R.string.action_search));
        searchView.setSubmitOnClick(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null) {
                    return false;
                } else if (query.trim().equalsIgnoreCase("")) {
                    return true;
                }

                ECommerceUtil.addSearchSuggestion(query);
                searchView.setSuggestions(ECommerceUtil.getSearchSuggestions());
                productListComponent.setKeyword(query);
                mKeyword = query;
                toolbar.setTitle(query);
                productListComponent.reloadData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.showSuggestions();
                productListComponent.setEmptyViewVisibility(View.GONE);
                ScreenUtil.showKeyboard(ProductSearchActivity.this);
            }

            @Override
            public void onSearchViewClosed() {
                if (mKeyword == null) {
                    finish();
                }
            }
        });
        searchView.showSearch();
        searchView.setSuggestions(ECommerceUtil.getSearchSuggestions());
        searchView.showSuggestions();

        return true;
    }

}
