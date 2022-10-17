package com.shopiroller.views.legacy;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.enums.ColorEnum;
import com.shopiroller.util.ColorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopirollerButton extends ConstraintLayout {

    @BindView(R2.id.main_layout)
    ConstraintLayout mainLayout;
    @BindView(R2.id.text_view)
    TextView textView;
    @BindView(R2.id.left_image_view)
    ImageView leftImageView;
    @BindView(R2.id.success_icon)
    ImageView successIcon;
    @BindView(R2.id.layout)
    RelativeLayout layout;

    private int backgroundColor;
    private int textColor;
    private String text;
    private boolean hasRadius;
    private Drawable icon;
    private int themeColor;

    public ShopirollerButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShopirollerButton, 0, 0);
        try {
            backgroundColor = a.getInteger(R.styleable.ShopirollerButton_sButtonBackgroundColor, 0);
            textColor = a.getInteger(R.styleable.ShopirollerButton_sButtonTextColor, 0);
            hasRadius = a.getBoolean(R.styleable.ShopirollerButton_sButtonHasRadius, false);
            text = a.getString(R.styleable.ShopirollerButton_sButtonText);
            int iconId = a.getResourceId(R.styleable.ShopirollerButton_sButtonIcon, -1);
            if (iconId != -1)
                icon = AppCompatResources.getDrawable(getContext(), iconId);
            themeColor = a.getInteger(R.styleable.ShopirollerButton_sButtonColorType, -1);
        } finally {

            inflate(getContext(), R.layout.mobiroller_button_layout, this);
            ButterKnife.bind(this);

            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
            setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));
        }

        if (themeColor != -1) {
            setBackgroundColor(ColorEnum.getResIdByResOrder(themeColor));
            setTextColor(ColorUtil.isColorDark(ColorEnum.getResIdByResOrder(themeColor)) ? Color.WHITE : Color.BLACK);
        } else {
            setBackgroundColor(backgroundColor);
            setTextColor(textColor);
        }

        setText(text);

        if (icon != null) {
            leftImageView.setVisibility(VISIBLE);
            leftImageView.setImageDrawable(icon);
            ImageViewCompat.setImageTintList(leftImageView, ColorStateList.valueOf(ColorUtil.isColorDark(themeColor) ? Color.WHITE : Color.BLACK));
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        GradientDrawable d = new GradientDrawable();
        if (hasRadius)
            d.setCornerRadius(16.0f);
        d.setColor(color);
        mainLayout.setBackground(d);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextAnimated(String text) {

        String currentText = textView.getText().toString();
        setClickable(false);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down_ecommerce);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Animation slideInDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_down_ecommerce);
                slideInDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animation slideInOutDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down_ecommerce);
                                slideInOutDown.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        textView.setText(currentText);
                                        Animation slideInDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_down_ecommerce);
                                        slideInDown.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                if (successIcon != null)
                                                    successIcon.setVisibility(GONE);

                                                setClickable(true);
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                        layout.startAnimation(slideInDown);

                                        successIcon.setVisibility(GONE);
                                        layout.setVisibility(VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                successIcon.startAnimation(slideInOutDown);
                            }
                        }, 500);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layout.setVisibility(GONE);

                successIcon.setVisibility(VISIBLE);
                successIcon.startAnimation(slideInDown);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layout.startAnimation(animation);

//        if (leftImageView != null) {
//            Animation animations = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down_ecommerce);
//            animations.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Animation slideInDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_down_ecommerce);
//                            slideInDown.setAnimationListener(new Animation.AnimationListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animation animation) {
//                                    setClickable(true);
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animation animation) {
//
//                                }
//                            });
//                            leftImageView.startAnimation(slideInDown);
//                            leftImageView.setVisibility(VISIBLE);
//                        }
//                    }, 1000);
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            leftImageView.setVisibility(GONE);
//
//            leftImageView.startAnimation(animation);
//        }
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        setOpacity(isEnabled);
    }

    public void setOpacity(boolean isEnabled) {
        if (!isEnabled)
            setBackgroundColor(ColorUtil.getColorWithAlpha(ColorEnum.getResIdByResOrder(themeColor), 0.8f));
        else
            setBackgroundColor(ColorEnum.getResIdByResOrder(themeColor));
    }

    public void setColor() {
        setBackgroundColor(ColorEnum.getResIdByResOrder(themeColor));
    }

    public void removeIcon() {
        if (leftImageView != null)
            leftImageView.setVisibility(GONE);
    }
}
