package com.shopiroller.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.Shopiroller;
import com.shopiroller.constants.Constants;
import com.shopiroller.fragments.CategoryListFragment;
import com.shopiroller.fragments.CategoryProductListFragment;
import com.shopiroller.helpers.ColorHelper;
import com.shopiroller.helpers.ToolbarHelper;
import com.shopiroller.models.CategoriesMobileSettings;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.ShoppingCartCountEvent;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

public class CategoryListActivity extends ECommerceBaseActivity implements FragmentManager.OnBackStackChangedListener {

    FragmentTransaction fragmentTransaction;
    ShopirollerToolbar toolbar;
    private boolean isOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        toolbar = findViewById(R.id.toolbar_top);
        new ToolbarHelper().setStatusBar(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setTitleTypeface();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (getIntent().hasExtra(Constants.INTENT_EXTRA_IS_MAIN_CATEGORY_LIST) && getIntent().hasExtra(Constants.INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS)) {
            startSubCategoryFragment(Objects.requireNonNull(getIntent().getParcelableExtra(Constants.INTENT_EXTRA_CATEGORY_MODEL)),
                    (CategoriesMobileSettings) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS));
        } else if(getIntent().hasExtra(Constants.INTENT_EXTRA_IS_SLIDER_INTENT)) {
            CategoryResponseModel model = new CategoryResponseModel();
            model.setCategoryId(getIntent().getStringExtra(Constants.INTENT_EXTRA_IS_SLIDER_INTENT));
            startCategoryProductListFragment(model);
        } else {
            startCategoryFragment();
        }
    }

    public void startCategoryFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.category_list_frame_layout, CategoryListFragment.newInstance());
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        isOpened = true;
    }

    public void startSubCategoryFragment(CategoryResponseModel model, CategoriesMobileSettings mobileSettings) {
        if (model.getSubCategories() != null && model.getSubCategories().size() > 0) {
            CategoryListFragment fragment = CategoryListFragment.newInstance(model, mobileSettings);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.category_list_frame_layout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            isOpened = true;
        } else {
            startCategoryProductListFragment(model);
        }
    }

    public void startCategoryProductListFragment(CategoryResponseModel model) {
        CategoryProductListFragment fragment = CategoryProductListFragment.newInstance(model);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.category_list_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        isOpened = true;
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().findFragmentById(R.id.category_list_frame_layout) == null && isOpened) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.ecommerce_main_menu);
        View actionView = toolbar.getMenu().findItem(R.id.action_shopping_cart).getActionView();
        ImageView shoppingBagIcon = actionView.findViewById(R.id.shopping_bag_icon);
        ImageViewCompat.setImageTintList(shoppingBagIcon, ColorStateList.valueOf(ColorHelper.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK));

        setBadgeCount(toolbar, ECommerceUtil.getBadgeCount());
        actionView.setOnClickListener(view -> startShoppingCartActivity());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {
            startActivity(new Intent(this, ProductSearchActivity.class));
            return true;
        } else if (itemId == R.id.action_shopping_cart) {
            startShoppingCartActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onPostShoppingCartCountEvent(ShoppingCartCountEvent event) {
        setBadgeCount(toolbar, event.shoppingCartItemCount);
    }

}
