package com.shopiroller.views.legacy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.shopiroller.R;

public class ShopirollerBadgeView extends ConstraintLayout {

    private ShopirollerTextView textView;
    private RelativeLayout mainLayout;

    private int textColor;
    private int backgroundColor;
    private String text = "";

    public ShopirollerBadgeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShopirollerBadgeView, 0, 0);
        try {
            textColor = a.getColor(R.styleable.ShopirollerBadgeView_sTextColor,0);
            backgroundColor = a.getColor(R.styleable.ShopirollerBadgeView_sBackgroundColor,0);
            text = a.getString(R.styleable.ShopirollerBadgeView_sText);
        } finally {

            inflate(getContext(), R.layout.mobiroller_badge_view, this);
            textView = findViewById(R.id.badge_text);
            mainLayout = findViewById(R.id.main_layout);

            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        GradientDrawable d = new GradientDrawable();
        d.setCornerRadius(16.0f);
        d.setColor(backgroundColor);
        mainLayout.setBackground(d);

        setText(text);
        textView.setTextColor(textColor);
    }

    public void setText(String text) {
        textView.setText(text);
        textView.setAllCaps(true);
    }
}
