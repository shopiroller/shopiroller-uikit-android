package com.shopiroller.viewholders;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.shopiroller.R2;
import com.shopiroller.activities.CategoryListActivity;
import com.shopiroller.adapter.CategoryListAdapter;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.HeaderModel;
import com.shopiroller.models.ShowcaseResponseModel;
import com.shopiroller.models.ShowcaseStatus;
import com.shopiroller.models.SliderDataModel;
import com.shopiroller.models.SliderSlidesModel;
import com.shopiroller.views.ShopirollerShowcaseView;
import com.shopiroller.views.ShopirollerSliderView;
import com.shopiroller.views.legacy.ShopirollerTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.mobiroller_slider_view)
    ShopirollerSliderView shopirollerSliderView;
    @BindView(R2.id.category_recycler_view)
    RecyclerView categoryRecyclerView;
    @BindView(R2.id.category_see_all_linear_layout)
    LinearLayout categorySeeAllLinearLayout;
    @BindView(R2.id.explore_header_text_view)
    ShopirollerTextView exploreHeaderTextView;
    @BindView(R2.id.category_header_text_view)
    ShopirollerTextView categoryHeaderTextView;
    @BindView(R2.id.showcase_linear_layout)
    LinearLayout showcaseLinearLayout;

    private HeaderModel headerModel;

    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(HeaderModel headerModel) {
        this.headerModel = headerModel;
        setShowcase(headerModel.showcaseList);
        setSlider(headerModel.sliderList);
        setCategories(headerModel.categoryList);
    }

    public void setShowcase(List<ShowcaseResponseModel> list) {
        headerModel.showcaseList = list;
        if (showcaseLinearLayout.getChildCount() > 0)
            showcaseLinearLayout.removeAllViews();

        if (headerModel.showcaseList != null)
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getShowcase().getStatus() == ShowcaseStatus.Active) {
                    ShopirollerShowcaseView showcaseView = new ShopirollerShowcaseView(showcaseLinearLayout.getContext());
                    showcaseLinearLayout.addView(showcaseView);

                    showcaseView.setSectionTitle(list.get(i).getShowcase().getName());
                    showcaseView.start(showcaseLinearLayout.getContext());

                    showcaseView.addToAdapter(list.get(i));
                }
            }
    }

    public void setCategories(List<CategoryResponseModel> list) {
        headerModel.categoryList = list;

        categoryRecyclerView.setHasFixedSize(true);
        CategoryListAdapter adapter = null;

        if (list != null && list.size() > 0) {
            LinearLayoutManager manager = new LinearLayoutManager(showcaseLinearLayout.getContext(), RecyclerView.HORIZONTAL, false);
            List<CategoryResponseModel> dataList = new ArrayList<>();

            for (CategoryResponseModel category : list) {
                if (category.isActive()) {
                    dataList.add(category);
                }
            }

            if (dataList.size() > 0) {
                adapter = new CategoryListAdapter(showcaseLinearLayout.getContext(), dataList, headerModel.categoriesMobileSettings);
                categoryRecyclerView.setLayoutManager(manager);
                categoryRecyclerView.setAdapter(adapter);

                setCategoryVisibility(View.VISIBLE);
            } else {
                setCategoryVisibility(View.GONE);
            }
        } else {
            setCategoryVisibility(View.GONE);
        }

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void setCategoryVisibility(int visibility) {
        categoryRecyclerView.setVisibility(visibility);
        categoryHeaderTextView.setVisibility(visibility);
        categorySeeAllLinearLayout.setVisibility(visibility);
        exploreHeaderTextView.setVisibility(visibility);
    }

    public void setSlider(List<SliderDataModel> list) {
        headerModel.sliderList = list;
        if (shopirollerSliderView.isStarted)
            return;
        if (list != null
                && list.size() != 0
                && list.get(0).isActive()
                && list.size() > 0) {
            List<SliderDataModel> dataModelList = list; // Duplicate data to manipulate

            List<SliderSlidesModel> slidesModelList = new ArrayList<>();

            // Filter the slides which is inactive.
            for (SliderSlidesModel model : list.get(0).getSlides()) {
                if (model.isActive()) {
                    slidesModelList.add(model);
                }
            }

            dataModelList.get(0).setSlides(slidesModelList);

            if (slidesModelList.size() > 0) { // Activate the slider if it contains at least one item.
                shopirollerSliderView.start(showcaseLinearLayout.getContext(), dataModelList, 0);

                if (slidesModelList.size() > 1) {
                    shopirollerSliderView.getSliderViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
//                        enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
                        }
                    });
                    shopirollerSliderView.setSliderDotsVisibility(View.VISIBLE);
                } else {
                    shopirollerSliderView.setSliderDotsVisibility(View.GONE);
                }

                shopirollerSliderView.setVisibility(View.VISIBLE);
            } else {
                shopirollerSliderView.setVisibility(View.GONE);
            }
        } else {
            shopirollerSliderView.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.category_see_all_linear_layout)
    public void onSeeAllCategoryLinearLayoutClicked() {
        itemView.getContext()
                .startActivity(new Intent(itemView.getContext(), CategoryListActivity.class));
    }
}
