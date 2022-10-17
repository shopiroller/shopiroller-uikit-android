package com.shopiroller.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.shopiroller.R;
import com.shopiroller.constants.Constants;
import com.shopiroller.fragments.ProductGalleryBottomSheetFragment;
import com.shopiroller.models.ProductImage;

import java.util.ArrayList;
import java.util.List;

public class ProductImageAdapter extends PagerAdapter {

    private ArrayList<ProductImage> images;
    LayoutInflater inflater;
    AppCompatActivity context;

    public ProductImageAdapter(AppCompatActivity context, List<ProductImage> images) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images =  new ArrayList<>(images);
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.layout_e_commerce_image_view_pager_item, view, false);
        ImageView imageView = imageLayout.findViewById(R.id.image);
        imageView.setOnClickListener(v -> {

            ProductGalleryBottomSheetFragment productGalleryBottomSheetFragment = new ProductGalleryBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.PRODUCT_IMAGE_LIST, images);
            productGalleryBottomSheetFragment.setArguments(bundle);
            productGalleryBottomSheetFragment.show(context.getSupportFragmentManager(), productGalleryBottomSheetFragment.getTag());
        });
        Glide.with(context).load(images.get(position).n).into(imageView);
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