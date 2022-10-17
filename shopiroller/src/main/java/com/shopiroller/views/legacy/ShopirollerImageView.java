package com.shopiroller.views.legacy;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.core.widget.ImageViewCompat;

import com.shopiroller.R;
import com.shopiroller.enums.ColorEnum;

public class ShopirollerImageView extends androidx.appcompat.widget.AppCompatImageView {

    private int color = -1;

    public ShopirollerImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShopirollerImageView, 0, 0);
        try {
            if(a.getInteger(R.styleable.ShopirollerImageView_sTintType, -1) != -1)
                color = ColorEnum.getResIdByResOrder(a.getInteger(R.styleable.ShopirollerImageView_sTintType, -1));

        } finally {
            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        setBackgroundResource(outValue.resourceId);
        if(color != -1)
        ImageViewCompat.setImageTintList(this,ColorStateList.valueOf(color));
    }
}
