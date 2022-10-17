package com.shopiroller.views.legacy;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.shopiroller.R;
import com.shopiroller.enums.ColorEnum;
import com.shopiroller.enums.FontTypeEnum;

public class ShopirollerTextView extends androidx.appcompat.widget.AppCompatTextView {

    private int color;
    private int size;

    public ShopirollerTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShopirollerTextView, 0, 0);
        try {
            color = a.getInteger(R.styleable.ShopirollerTextView_sColorType, -1);
            size = a.getInteger(R.styleable.ShopirollerTextView_sSizeType, 0);
        } finally {
            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        if(color != -1)
        setTextColor(ColorEnum.getResIdByResOrder(color));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, FontTypeEnum.getFontSizeByResOrder(size));;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(FontTypeEnum.getResIdByResOrder(size, getContext()));
    }

}
