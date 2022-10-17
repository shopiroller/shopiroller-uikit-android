package com.shopiroller.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.shopiroller.R;
import com.shopiroller.Shopiroller;
import com.shopiroller.activities.CategoryListActivity;
import com.shopiroller.activities.ProductDetailActivity;
import com.shopiroller.constants.Constants;
import com.shopiroller.models.SliderDataModel;
import com.shopiroller.models.SliderSlidesModel;
import com.shopiroller.util.ScreenUtil;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private List<SliderDataModel> dataList;
    private int sliderIndex;
    LayoutInflater inflater;

    public SliderAdapter(@NonNull Context context, List<SliderDataModel> dataList, int sliderIndex) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = dataList;
        this.sliderIndex = sliderIndex;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.fragment_slider, view, false);
        ImageView imageView = imageLayout.findViewById(R.id.rounded_image_view);

        SliderSlidesModel model = dataList.get(0).getSlides().get(position);
        if (model != null && model.getImageUrl() != null && model.getImageUrl().n != null) {
            Glide.with(imageView.getContext())
                    .load(model.getImageUrl().n + "?width=" + ScreenUtil.getScreenWidth())
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext()).load(R.drawable.no_image_e_commerce).into(imageView);
        }

        imageView.setOnClickListener(view1 -> {
            if (model != null) {
                switch (model.getNavigationType()) {
                    case Constants.SLIDER_TYPE_WEB:
                        openWebView(imageView.getContext(), model.getNavigationLink());
                        break;
                    case Constants.SLIDER_TYPE_PRODUCT:
                        ProductDetailActivity.startActivity(imageView.getContext(), model.getNavigationLink(), null, model.getImageUrl().n);
                        break;
                    case Constants.SLIDER_TYPE_CATEGORY:
                        Intent intent = new Intent(imageView.getContext(), CategoryListActivity.class);
                        intent.putExtra(Constants.INTENT_EXTRA_IS_SLIDER_INTENT, model.getNavigationLink());
                        imageView.getContext().startActivity(intent);
                        break;
                    case Constants.SLIDER_TYPE_NONE:
                        break;
                }

            }
        });
        view.addView(imageLayout, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        return imageLayout;
    }

    private void openWebView(Context context, String url) {
        if (URLUtil.isValidUrl(url)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(Shopiroller.getTheme().primaryColor);
            builder.setShowTitle(true);
            builder.addDefaultShareMenuItem();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        }
    }

    @Override
    public int getCount() {
        if (dataList != null && dataList.get(sliderIndex).getSlides().size() > 0)
            return dataList.get(sliderIndex).getSlides().size();

        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
