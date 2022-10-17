package com.shopiroller.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.Shopiroller;
import com.shopiroller.activities.CategoryListActivity;
import com.shopiroller.activities.ProductDetailActivity;
import com.shopiroller.constants.Constants;
import com.shopiroller.models.SliderSlidesModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SliderFragment extends Fragment implements View.OnClickListener {

    @BindView(R2.id.rounded_image_view)
    ImageView roundedImageView;
    @BindView(R2.id.container_card_view)
    CardView containerCardView;

    Unbinder unbinder;

    SliderSlidesModel model;

    public SliderFragment() {
        // Required empty public constructor
    }

    public static SliderFragment newInstance(SliderSlidesModel sliderSlidesModel) {
        SliderFragment sliderFragment = new SliderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.INTENT_EXTRA_SLIDER_MODEL, sliderSlidesModel);
        sliderFragment.setArguments(bundle);
        return sliderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            SliderSlidesModel sliderSlidesModel = getArguments().getParcelable(Constants.INTENT_EXTRA_SLIDER_MODEL);
            model = sliderSlidesModel;

            Glide.with(getContext())
                    .load(sliderSlidesModel.getImageUrl().n)
                    .into(roundedImageView);
        }

        roundedImageView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (model != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    ((Activity) getContext()),
                    containerCardView,
                    "featuredImage");

            switch (model.getNavigationType()) {
                case Constants.SLIDER_TYPE_WEB:
                    openWebView();
                    break;
                case Constants.SLIDER_TYPE_PRODUCT:
                    ProductDetailActivity.startActivity(getContext(), model.getNavigationLink(), null, model.getImageUrl().n, options);
                    break;
                case Constants.SLIDER_TYPE_CATEGORY:
                    Intent intent = new Intent(getActivity(), CategoryListActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_IS_SLIDER_INTENT, model.getNavigationLink());
                    startActivity(intent);
                    break;
                case Constants.SLIDER_TYPE_NONE:
                    break;
            }

        }
    }

    private void openWebView() {
        if (URLUtil.isValidUrl(model.getNavigationLink())) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(Shopiroller.getTheme().primaryColor);
            builder.setShowTitle(true);
            builder.addDefaultShareMenuItem();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(model.getNavigationLink()));
        }
    }

}
