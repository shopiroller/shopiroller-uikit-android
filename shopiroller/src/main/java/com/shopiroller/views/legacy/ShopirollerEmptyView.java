package com.shopiroller.views.legacy;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.shopiroller.R;

public class ShopirollerEmptyView extends ConstraintLayout {

    private ImageView backgroundImageView;
    private ImageView imageView;
    private ShopirollerTextView titleTextView;
    private ShopirollerTextView descriptionTextView;
    private ImageView noContentImageView;

    private int backgroundImageTintColor;
    private Drawable icon;
    private String title;
    private String description;
    private boolean isOval;
    private boolean showNoContent;

    public ShopirollerEmptyView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShopirollerEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShopirollerEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShopirollerEmptyView, 0, 0);
        try {
            backgroundImageTintColor = a.getInteger(R.styleable.ShopirollerEmptyView_sBackgroundImageTintColor, 0);
            title = a.getString(R.styleable.ShopirollerEmptyView_sTitle);
            description = a.getString(R.styleable.ShopirollerEmptyView_sDescription);
            int iconId = a.getResourceId(R.styleable.ShopirollerEmptyView_sEmptyIcon, -1);
            icon = AppCompatResources.getDrawable(getContext(), iconId);
            isOval = a.getBoolean(R.styleable.ShopirollerEmptyView_sBackgroundImageIsOval,false);
            showNoContent = a.getBoolean(R.styleable.ShopirollerEmptyView_sShowNoContent,false);
        } finally {
            inflate(getContext(), R.layout.shopiroller_empty_view, this);

            backgroundImageView = findViewById(R.id.empty_background_image_view);
            imageView = findViewById(R.id.empty_image_view);
            titleTextView = findViewById(R.id.empty_title_text_view);
            descriptionTextView = findViewById(R.id.empty_description_text_view);
            noContentImageView = findViewById(R.id.no_content_image);

            setTheme();
            a.recycle();
        }
    }

    public void setTheme() {
        setTitle(title);
        setDescription(description);

        if(icon != null)
            imageView.setImageDrawable(icon);

        if(isOval) {
            backgroundImageView.setImageResource(R.drawable.circle_gray_background);
        }

        if(showNoContent) {
            noContentImageView.setVisibility(VISIBLE);
        } else
            noContentImageView.setVisibility(GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backgroundImageView.setImageTintList(ColorStateList.valueOf(backgroundImageTintColor));
        }
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setDescription(String description) {
        if(description != null) {
            descriptionTextView.setText(description);
            descriptionTextView.setVisibility(VISIBLE);
        } else {
            descriptionTextView.setVisibility(GONE);
        }
    }

    public String getDescription() {
        return description;
    }
}
