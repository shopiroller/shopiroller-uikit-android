package com.shopiroller.viewholders;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.activities.CategoryListActivity;
import com.shopiroller.constants.Constants;
import com.shopiroller.models.CategoriesMobileSettings;
import com.shopiroller.models.CategoryResponseModel;
import com.shopiroller.models.enums.CategoryDisplayTypeEnum;
import com.shopiroller.views.legacy.ShopirollerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.category_card_view)
    CardView categoryCardView;
    @BindView(R2.id.category_image_view)
    ImageView categoryImageView;
    @BindView(R2.id.category_name_text_view)
    ShopirollerTextView categoryNameTextView;

    private CategoryResponseModel category;
    private CategoriesMobileSettings mobileSettings;

    public CategoryListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(CategoryResponseModel category, CategoriesMobileSettings mobileSettings) {
        this.category = category;
        this.mobileSettings = mobileSettings;
        CategoryDisplayTypeEnum categoryDisplayTypeEnum = null;
        if (mobileSettings != null && mobileSettings.getCategoryDisplayType() != null) {
            categoryDisplayTypeEnum = mobileSettings.getCategoryDisplayType();
        }

        if (categoryDisplayTypeEnum != null) {
            if(categoryDisplayTypeEnum == CategoryDisplayTypeEnum.IMAGE_AND_TEXT) {
                categoryCardView.setVisibility(View.VISIBLE);
                categoryNameTextView.setVisibility(View.VISIBLE);
                setCategoryImageView(category.getIcon());
            } else if (categoryDisplayTypeEnum == CategoryDisplayTypeEnum.IMAGE_ONLY) {
                categoryNameTextView.setVisibility(View.GONE);
                setCategoryImageView(category.getIcon());
            } else if (categoryDisplayTypeEnum == CategoryDisplayTypeEnum.TEXT_ONLY) {
                categoryNameTextView.setVisibility(View.VISIBLE);
                categoryCardView.setVisibility(View.GONE);
                categoryNameTextView.setBackground(ContextCompat.getDrawable(categoryNameTextView.getContext(), R.drawable.gray_light_rounded_border_rectangle_background));
            }
        } else {
            categoryNameTextView.setVisibility(View.VISIBLE);
            categoryCardView.setVisibility(View.VISIBLE);
            setCategoryImageView(category.getIcon());
        }

        categoryNameTextView.setText(category.getName());
    }

    private void setCategoryImageView(String icon) {
        if (icon == null)  {
            categoryCardView.setVisibility(View.GONE);
            return;
        }
        Glide.with(categoryImageView.getContext()).load(icon).into(categoryImageView);
    }

    @OnClick(R2.id.category_item_container_view)
    public void onCategoryContainerViewClicked() {
        Intent intent = new Intent(categoryCardView.getContext(), CategoryListActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_IS_MAIN_CATEGORY_LIST, true);
        intent.putExtra(Constants.INTENT_EXTRA_CATEGORY_MODEL, (Parcelable) category);
        intent.putExtra(Constants.INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS, mobileSettings);
        categoryCardView.getContext().startActivity(intent);
    }

}
