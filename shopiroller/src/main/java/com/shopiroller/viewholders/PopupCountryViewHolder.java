package com.shopiroller.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 8/27/17.
 */

public class PopupCountryViewHolder extends RecyclerView.ViewHolder {


    @BindView(R2.id.text)
    TextView text;

    @BindView(R2.id.checked_image_view)
    ImageView checkedImageView;

    public PopupCountryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindText(String value) {
        text.setText(value);
    }

    public void bindTextWithCheckedImage(String value) {
        text.setText(value);
        checkedImageView.setVisibility(View.VISIBLE);
    }

}
