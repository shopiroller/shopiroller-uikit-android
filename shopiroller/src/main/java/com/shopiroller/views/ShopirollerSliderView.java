package com.shopiroller.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.shopiroller.R;
import com.shopiroller.adapter.SliderAdapter;
import com.shopiroller.models.SliderDataModel;

import java.util.List;


public class ShopirollerSliderView extends ConstraintLayout {

    ViewPager sliderViewPager;
    LinearLayout sliderDotsLinearLayout;

    private SliderAdapter adapter;
    public boolean isStarted = false;

    public ShopirollerSliderView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = inflate(context, R.layout.layout_mobiroller_slider_view, this);

        sliderViewPager = view.findViewById(R.id.slider_view_pager);
        sliderDotsLinearLayout = view.findViewById(R.id.slider_dots_linear_layout);
    }

    public void start(Context context, List<SliderDataModel> dataList, int sliderIndex) {
        if (isStarted)
            return;
        isStarted = true;
        int marginInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        adapter = new SliderAdapter(sliderViewPager.getContext(), dataList, sliderIndex);
        sliderViewPager.setAdapter(adapter);
        sliderViewPager.setOffscreenPageLimit(10);
        sliderViewPager.setPageMargin(marginInDp);

        ImageView[] dots = new ImageView[adapter.getCount()];

        Drawable selectedDot = ContextCompat.getDrawable(context, R.drawable.e_commerce_selecteditem_dot);
        Drawable unSelectedDot = ContextCompat.getDrawable(context, R.drawable.e_commerce_nonselecteditem_dot);

        sliderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    dots[i].setImageDrawable(unSelectedDot);
                }
                dots[position].setImageDrawable(selectedDot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < adapter.getCount(); i++) {
            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(unSelectedDot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 0, 0, 0);

            sliderDotsLinearLayout.addView(dots[i], params);
        }

        if (dataList != null && dataList.size() > 0) {
            dots[0].setImageDrawable(selectedDot);
        }

        adapter.notifyDataSetChanged();
    }

    public ViewPager getSliderViewPager() {
        return sliderViewPager;
    }

    public void setSliderDotsVisibility(int visibility) {
        sliderDotsLinearLayout.setVisibility(visibility);
    }

}
