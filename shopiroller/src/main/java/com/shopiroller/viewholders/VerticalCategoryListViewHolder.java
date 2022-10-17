package com.shopiroller.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.activities.CategoryListActivity;
import com.shopiroller.models.CategoriesMobileSettings;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.enums.CategoryDisplayTypeEnum;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerticalCategoryListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.category_image_view)
    ImageView categoryImageView;
    @BindView(R2.id.category_name_text_view)
    ShopirollerTextView nameTextView;
    @BindView(R2.id.category_item_count_text_view)
    ShopirollerTextView itemCountTextView;
    @BindView(R2.id.category_card_view)
    CardView categoryCardView;

    private Activity activity;
    private CategoryResponseModel model;
    private CategoriesMobileSettings mobileSettings;

    public VerticalCategoryListViewHolder(@NonNull View itemView, Activity activity) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.activity = activity;
    }

    public void bind(CategoryResponseModel model , CategoriesMobileSettings mobileSettings) {
        this.model = model;
        this.mobileSettings = mobileSettings;
        CategoryDisplayTypeEnum categoryDisplayTypeEnum = null;

        if (mobileSettings != null && mobileSettings.getCategoryDisplayType() != null) {
            categoryDisplayTypeEnum = mobileSettings.getCategoryDisplayType();
        }

        if (categoryDisplayTypeEnum != null) {
            if(categoryDisplayTypeEnum == CategoryDisplayTypeEnum.IMAGE_AND_TEXT) {
                categoryCardView.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                setCategoryImageView(model.getIcon());
            } else if (categoryDisplayTypeEnum == CategoryDisplayTypeEnum.IMAGE_ONLY) {
                nameTextView.setVisibility(View.GONE);
                setCategoryImageView(model.getIcon());
            } else if (categoryDisplayTypeEnum == CategoryDisplayTypeEnum.TEXT_ONLY) {
                nameTextView.setVisibility(View.VISIBLE);
                categoryCardView.setVisibility(View.GONE);
            }

        } else {
            nameTextView.setVisibility(View.VISIBLE);
            categoryCardView.setVisibility(View.VISIBLE);
            setCategoryImageView(model.getIcon());
        }
        if(model.totalProducts>0){
            itemCountTextView.setText(String.format(activity.getResources().getString(R.string.e_commerce_category_list_category_item_count), String.valueOf(model.totalProducts)));
            itemCountTextView.setVisibility(View.VISIBLE);
        }
        nameTextView.setText(model.getName());
    }

    private void setCategoryImageView(String icon) {
        if (icon == null)  {
            categoryCardView.setVisibility(View.GONE);
            return;
        }
        Glide.with(categoryImageView.getContext()).load(icon).into(categoryImageView);
    }

    @OnClick(R2.id.vertica_list_item_root_view)
    public void onRootViewClicked() {
        if (model.getSubCategories() != null && model.getSubCategories().size() > 0 && mobileSettings != null) {
            ((CategoryListActivity) activity).startSubCategoryFragment(model,mobileSettings);
        } else {
            ((CategoryListActivity) activity).startCategoryProductListFragment(model);
        }
    }

}
