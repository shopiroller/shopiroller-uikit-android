package com.shopiroller.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.constants.Constants;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.models.ProductImage;
import com.shopiroller.views.HackyViewPager;
import com.shopiroller.views.legacy.ShopirollerTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProductGalleryBottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R2.id.view_pager)
    HackyViewPager viewPager;
    @BindView(R2.id.count_text_view)
    ShopirollerTextView countTextView;
    @BindView(R2.id.close_button)
    ImageView closeButton;
    Unbinder unbinder;

    private List<ProductImage> images;
    private LegacyProgressViewHelper legacyProgressViewHelper;
    private int position;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog dialoga = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = dialoga.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });
        return bottomSheetDialog;
    }

    @Override
    public void onViewCreated(View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.layout_e_commerce_product_image_gallery, null);
        unbinder = ButterKnife.bind(this, contentView);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        legacyProgressViewHelper = new LegacyProgressViewHelper(getActivity());
        if (getArguments() != null && getArguments().containsKey(Constants.PRODUCT_IMAGE_LIST)) {
            images = (List<ProductImage>) getArguments().getSerializable(Constants.PRODUCT_IMAGE_LIST);
            if (images.size() == 0)
                images.add(new ProductImage());
            position = getArguments().getInt(Constants.PRODUCT_IMAGE_LIST_POSITION, 0);
        } else
            dismiss();
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter();
        viewPager.setAdapter(imageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String text = String.format("<font color=#969fa2>%s/</font><font color=#000000>%s</font>", position + 1, imageAdapter.getCount());
                countTextView.setText(Html.fromHtml(text));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(position);
        String text = String.format("<font color=#969fa2>%s/</font><font color=#000000>%s</font>", position + 1, imageAdapter.getCount());
        countTextView.setText(Html.fromHtml(text));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class ImagePagerAdapter extends PagerAdapter {

        LayoutInflater inflater;

        ImagePagerAdapter() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.layout_e_commerce_gallery_item, view, false);
            PhotoView imageView = imageLayout.findViewById(R.id.image_view);
            legacyProgressViewHelper.show();
            if (images.get(position).n != null) {
                Glide.with(getActivity())
                        .load(images.get(position).n)
                        .placeholder(R.drawable.no_image_e_commerce)
                        .thumbnail(
                                Glide.with(getActivity())
                                        .load(images.get(position).n + "?width=" + 100))
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                legacyProgressViewHelper.dismiss();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                legacyProgressViewHelper.dismiss();
                                return false;
                            }
                        }).into(imageView);
            } else
                Glide.with(getActivity()).load(R.drawable.no_image_e_commerce).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        legacyProgressViewHelper.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        legacyProgressViewHelper.dismiss();
                        return false;
                    }
                }).into(imageView);

            view.addView(imageLayout, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return imageLayout;
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

}
