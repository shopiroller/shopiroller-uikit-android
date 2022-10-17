package com.shopiroller.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.activities.ShowcaseProductListActivity;
import com.shopiroller.adapter.ShowcaseAdapter;
import com.shopiroller.models.ProductDetailModel;
import com.shopiroller.models.ShowcaseResponseModel;
import com.shopiroller.views.legacy.ShopirollerTextView;

import java.util.ArrayList;
import java.util.List;

public class ShopirollerShowcaseView extends ConstraintLayout {

    RecyclerView recyclerView;
    ShopirollerTextView titleTextView;
    LinearLayout categorySeeAllLayout;

    private ShowcaseAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ShowcaseResponseModel model;

    public ShopirollerShowcaseView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerShowcaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.layout_mobiroller_showcase_view, this);
        recyclerView = findViewById(R.id.showcase_recycler_view);
        titleTextView = findViewById(R.id.showcase_title_text_view);
        categorySeeAllLayout = findViewById(R.id.category_see_all_linear_layout);
        categorySeeAllLayout.setOnClickListener(view -> onSeeAllViewClicked());
    }

    public void start(Context context) {
        adapter = new ShowcaseAdapter((Activity) context, new ArrayList<>());

        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void addToAdapter(ShowcaseResponseModel newItems) {
        this.model = newItems;

        List<ProductDetailModel> holderList = newItems.getProducts();

        holderList = checkProductListIsValid(holderList);

        if (adapter.getItemCount() == 0 && (holderList == null || holderList.size() == 0)) {
            recyclerView.setVisibility(View.GONE);
        } else {
            if (adapter.getItemCount() == 0) {
                recyclerView.setVisibility(View.VISIBLE);
            }
            adapter.addItems(holderList);
        }
    }

    private List<ProductDetailModel> checkProductListIsValid(List<ProductDetailModel> newItems) {
        if (newItems == null)
            return null;
        for (int i = 0; i < newItems.size(); i++) {
            if (newItems.get(i) == null || !newItems.get(i).isValid()) {
                newItems.remove(i);
                i--;
            }
        }
        return newItems;
    }

    public void setSectionTitle(String title) {
        titleTextView.setText(title);
    }

    public void onSeeAllViewClicked() {
        ShowcaseProductListActivity.startActivity(getContext(), model);
    }

}
