package com.shopiroller.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopiroller.adapter.ProductImageAdapter;
import com.shopiroller.models.VariantDataModel;
import com.shopiroller.models.VariationsModel;
import com.shopiroller.util.ECommerceUtil;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.Shopiroller;
import com.shopiroller.adapter.DialogFilterAdapter;
import com.shopiroller.constants.Constants;
import com.shopiroller.enums.MobirollerDialogType;
import com.shopiroller.fragments.ProductGalleryBottomSheetFragment;
import com.shopiroller.helpers.ColorHelper;
import com.shopiroller.helpers.ItemClickSupport;
import com.shopiroller.helpers.LocalizationHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.helpers.ProgressViewHelper;
import com.shopiroller.helpers.SizeHelper;
import com.shopiroller.helpers.ToolbarHelper;
import com.shopiroller.helpers.UtilManager;
import com.shopiroller.models.AddProductModel;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.PaymentSettings;
import com.shopiroller.models.ProductDetailModel;
import com.shopiroller.models.ProductImage;
import com.shopiroller.models.ProductListModel;
import com.shopiroller.models.ShoppingCartCountEvent;
import com.shopiroller.models.ShoppingCartItemsResponse;
import com.shopiroller.models.Variation;
import com.shopiroller.models.VariationGroupsModel;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.util.ErrorUtils;
import com.shopiroller.views.MaterialListFilterDialog;
import com.shopiroller.views.VideoEnabledWebView;
import com.shopiroller.views.legacy.ShopirollerBadgeView;
import com.shopiroller.views.legacy.ShopirollerButton;
import com.shopiroller.views.legacy.ShopirollerDialog;
import com.shopiroller.views.legacy.ShopirollerTextView;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import org.checkerframework.checker.units.qual.A;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends ECommerceBaseActivity implements VideoPlayerActivity.Listener {

    @BindView(R2.id.image_view_pager)
    ViewPager imageViewPager;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.price)
    TextView price;
    @BindView(R2.id.discounted_price)
    TextView discountedPrice;
    @BindView(R2.id.quantity_up)
    ImageView quantityUp;
    @BindView(R2.id.quantity_down)
    ImageView quantityDown;
    @BindView(R2.id.quantity_amount_text)
    TextView quantity;
    @BindView(R2.id.return_terms_layout)
    ConstraintLayout returnTermsLayout;
    @BindView(R2.id.shipping_terms_layout)
    ConstraintLayout shippingTermsLayout;
    @BindView(R2.id.quantity_layout)
    ConstraintLayout quantityLayout;
    @BindView(R2.id.description_layout)
    ConstraintLayout descriptionLayout;
    @BindView(R2.id.info_layout)
    ConstraintLayout badgeLayout;
    @BindView(R2.id.buy_button)
    ShopirollerButton buyButton;
    @BindView(R2.id.view_pager_count_dots)
    LinearLayout viewPagerCountDots;
    @BindView(R2.id.toolbar)
    ShopirollerToolbar toolbar;
    @BindView(R2.id.gradient_image_view)
    ImageView gradientImageView;
    @BindView(R2.id.sold_out_badge)
    ShopirollerBadgeView soldOutBadge;
    @BindView(R2.id.free_shipping_badge)
    ShopirollerBadgeView freeShippingBadge;
    @BindView(R2.id.sale_badge)
    CardView saleCardView;
    @BindView(R2.id.sale_rate_text_view)
    ShopirollerTextView saleRateTextView;
    @BindView(R2.id.cargo_layout)
    CardView cargoCardView;
    @BindView(R2.id.cargo_text)
    ShopirollerTextView cargoTextView;
    @BindView(R2.id.brand_image_view)
    ImageView brandImageView;
    @BindView(R2.id.constraintLayout)
    LinearLayout webViewLayout;
    @BindView(R2.id.image_view)
    ImageView imageView;
    @BindView(R2.id.toolbar_webview)
    ShopirollerToolbar toolbarWebView;
    @BindView(R2.id.web_view)
    VideoEnabledWebView webView;
    @BindView(R2.id.shipping_title)
    TextView shippingTitleTextView;
    @BindView(R2.id.return_title)
    TextView returnTitleTextView;
    @BindView(R2.id.variant_field_layout)
    LinearLayout variantLayout;
    @BindView(R2.id.video_play_image_view)
    ImageView videoPlayImageView;
    @BindView(R2.id.web_view_for_url)
    WebView webViewForUrl;

    Map<String, String> variantMapData = new HashMap<>();
    Map<Integer, Integer> selectedVariantIndexMapData = new HashMap<>();
    List<VariationGroupsModel> variationGroupsModels = new ArrayList<>();
    List<ProductDetailModel> variants = new ArrayList<>();
    List<ProductImage> mediaImages = new ArrayList<>();
    List<ProductImage> variantImages = new ArrayList<>();
    List<View> variantFields = new ArrayList<>();
    private Integer currentVariantPosition = 1;
    private Integer selectedVariantGroupIndex = 0;
    String videoUrl;

    private int amount;
    int selectedPosition = 0;
    ECommerceRequestHelper eCommerceRequestHelper;
    String productId;
    String featuredImageUrl;
    ProductDetailModel productModel;
    LocalizationHelper localizationHelper;
    ProgressViewHelper progressViewHelper;
    View mCurrentView;
    int mCurrentPosition;
    MaterialListFilterDialog materialListFilterDialog;
    private ProductListModel productListModel;
    ProductImageAdapter imageAdapter;

    public static void startActivity(Context context, String productId, String title, String featuredImageUrl) {
        if (productId == null) {
            Toast.makeText(context, R.string.common_error, Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(new Intent(context, ProductDetailActivity.class)
                .putExtra(Constants.PRODUCT_ID, productId)
                .putExtra(Constants.PRODUCT_TITLE, title)
                .putExtra(Constants.PRODUCT_FEATURED_IMAGE, featuredImageUrl)
        );
    }

    public static void startActivity(Context context, String productId, String title, String featuredImageUrl, ActivityOptionsCompat options) {
        if (productId == null) {
            Toast.makeText(context, R.string.common_error, Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(new Intent(context, ProductDetailActivity.class)
                        .putExtra(Constants.PRODUCT_ID, productId)
                        .putExtra(Constants.PRODUCT_TITLE, title)
                        .putExtra(Constants.PRODUCT_FEATURED_IMAGE, featuredImageUrl)
//                , options.toBundle()
        );
    }

    public static void startActivity(Context context, ProductDetailModel productDetailModel, String title, String featuredImageUrl, ActivityOptionsCompat options) {
        if (productDetailModel == null) {
            Toast.makeText(context, R.string.common_error, Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(new Intent(context, ProductDetailActivity.class)
                        .putExtra(Constants.PRODUCT_DETAIL_MODEL, productDetailModel)
                        .putExtra(Constants.PRODUCT_TITLE, title)
                        .putExtra(Constants.PRODUCT_FEATURED_IMAGE, featuredImageUrl)
//                , options.toBundle()
        );
    }

    public static void startActivity(Context context, ProductListModel productListModel, String title, String featuredImageUrl, ActivityOptionsCompat options) {
        if (productListModel == null) {
            Toast.makeText(context, R.string.common_error, Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(new Intent(context, ProductDetailActivity.class)
                        .putExtra(Constants.PRODUCT_LIST_MODEL, productListModel)
                        .putExtra(Constants.PRODUCT_TITLE, title)
                        .putExtra(Constants.PRODUCT_FEATURED_IMAGE, featuredImageUrl)
//                , options.toBundle()
        );
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_details);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        ButterKnife.bind(this);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (ECommerceUtil.getPaymentSettings().getDeliveryConditionsTitle() != null && !ECommerceUtil.getPaymentSettings().getDeliveryConditionsTitle().isEmpty()) {
            shippingTitleTextView.setText(ECommerceUtil.getPaymentSettings().getDeliveryConditionsTitle());
        } else {
            shippingTitleTextView.setText(getString(R.string.e_commerce_product_detail_delivery_conditions));
        }

        if (ECommerceUtil.getPaymentSettings().getCancellationProcedureTitle() != null && !ECommerceUtil.getPaymentSettings().getCancellationProcedureTitle().isEmpty()) {
            returnTitleTextView.setText(ECommerceUtil.getPaymentSettings().getCancellationProcedureTitle());
        } else {
            returnTitleTextView.setText(getString(R.string.e_commerce_product_detail_return_terms));
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            supportPostponeEnterTransition();
//        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params.topMargin = SizeHelper.getStatusBarHeight(this);
        toolbar.setLayoutParams(params);
        webViewLayout.setBackgroundColor(Shopiroller.getAdapter().getActionBarColor());

        ConstraintLayout.LayoutParams descriptionParams = (ConstraintLayout.LayoutParams) webViewLayout.getLayoutParams();
        descriptionParams.topMargin = SizeHelper.getStatusBarHeight(this);
        webViewLayout.setLayoutParams(descriptionParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if ((ColorHelper.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK) == Color.WHITE) {
                int flags = decor.getSystemUiVisibility();
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decor.setSystemUiVisibility(flags);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        localizationHelper = UtilManager.localizationHelper();
        progressViewHelper = new ProgressViewHelper(this);
        eCommerceRequestHelper = new ECommerceRequestHelper();

        if (getIntent().hasExtra(Constants.PRODUCT_ID))
            productId = getIntent().getStringExtra(Constants.PRODUCT_ID);
        else if (getIntent().hasExtra(Constants.PRODUCT_LIST_MODEL)) {
            productListModel = (ProductListModel) getIntent().getSerializableExtra(Constants.PRODUCT_LIST_MODEL);
            productId = productListModel.id;
            if (productListModel.featuredImage != null)
                featuredImageUrl = productListModel.featuredImage.n;
        } else {
            productModel = (ProductDetailModel) getIntent().getSerializableExtra(Constants.PRODUCT_DETAIL_MODEL);
            productId = productModel.id;
        }

        displayBrandIcon();

        featuredImageUrl = getIntent().getStringExtra(Constants.PRODUCT_FEATURED_IMAGE);
        loadPreUI();

        if (productModel == null)
            getProduct();
        else {
            loadUi();
            int selectedPosition = 0;
            if (productModel.images.size() == 0) {
                selectedPosition = 0;
            } else {
                if (featuredImageUrl != null)
                    for (int i = 0; i < productModel.images.size(); i++) {
                        if (productModel.featuredImage != null && productModel.images.get(i).n.equalsIgnoreCase(productModel.featuredImage.n)) {
                            selectedPosition = i;
                            break;
                        }
                    }
                else
                    selectedPosition = 0;
            }
            imageViewPager.setCurrentItem(selectedPosition);
        }
    }

    private void displayBrandIcon() {
        if (productModel != null && productModel.brand != null && productModel.brand.getIcon() != null && productModel.brand.isActive()) {
            Glide.with(this).load(productModel.brand.getIcon().n).into(brandImageView);
            brandImageView.setVisibility(View.VISIBLE);
        } else {
            brandImageView.setVisibility(View.GONE);
        }
    }

    private void loadPreUI() {
        if (productListModel != null) {
            List<ProductImage> list = new ArrayList<>();
            list.add(productListModel.featuredImage);
            imageAdapter = new ProductImageAdapter(this, list);
            imageViewPager.setAdapter(imageAdapter);
            imageViewPager.setCurrentItem(0);
            title.setText(localizationHelper.getLocalizedTitle(productListModel.title));
            if (productListModel.campaignPrice == 0) {
                price.setText(ECommerceUtil.getPriceString(productListModel.price) + " " + ECommerceUtil.getCurrencySymbol(productListModel.currency));
                discountedPrice.setVisibility(View.GONE);
                saleCardView.setVisibility(View.GONE);
            } else {
                price.setText(ECommerceUtil.getPriceString(productListModel.campaignPrice) + " " + ECommerceUtil.getCurrencySymbol(productListModel.currency));
                discountedPrice.setVisibility(View.VISIBLE);
                saleCardView.setVisibility(View.VISIBLE);
                saleRateTextView.setText(String.format("%%%s", 100 * (productListModel.price - productListModel.campaignPrice) / productListModel.price));
                saleRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                discountedPrice.setText(Html.fromHtml("<strike>" + ECommerceUtil.getPriceString(productListModel.price) + " " + ECommerceUtil.getCurrencySymbol(productListModel.currency) + "</strike>"));
            }
            if (productListModel.stock <= 0) {
                quantityLayout.setVisibility(View.GONE);
                setContinueButton(false);
            }
            amount = 1;
        }
        setSupportActionBar(toolbar);
        toolbar.setTitleTypeface();
        setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onClickShareButton() {
        //TODO
        String shareBody = localizationHelper.getLocalizedTitle(productModel.title) + " " + productModel.getPriceString() + " " + "App Name" + " https://play.google.com/store/apps/details?id=" + "BuildConfig.APPLICATION_ID";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.action_share)));
    }

    private void getProduct() {
        if (NetworkHelper.isConnected(this)) {
            progressViewHelper.show();
            Call<ECommerceResponse<ProductDetailModel>> responseCall = eCommerceRequestHelper.getApiService().getProduct(productId);
            eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<ProductDetailModel>() {
                @Override
                public void done() {
                    if (!isFinishing() && progressViewHelper.isShowing())
                        progressViewHelper.dismiss();
                }

                @Override
                public void onSuccess(ProductDetailModel result) {
                    productModel = result;
                    loadUi();
                }

                @Override
                public void onFailure(ECommerceErrorResponse result) {
                    showWarning(getString(R.string.e_commerce_product_detail_not_found_product_title), getString(R.string.e_commerce_product_detail_not_found_product_description), getString(R.string.e_commerce_product_detail_not_found_product_button), new ShopirollerDialog.DialogButtonCallback() {
                        @Override
                        public void onClickButton() {
                            finish();
                        }
                    });
                }

                @Override
                public void onNetworkError(String result) {
                    ErrorUtils.showErrorToast(ProductDetailActivity.this);
                    finish();
                }
            });
        } else
            DialogUtil.showNoConnectionError(this);
    }

    private void setCurrentVariantIndex() {
        currentVariantPosition = 0;
        for (int i = 0; i < variants.size(); i++) {
            List<String> variantValue = new ArrayList<>();
            for (int j = 0; j < variants.get(i).variantData.size(); j++) {
                variantValue.add(variants.get(i).variantData.get(j).getValue());
            }
            if (variantValue.containsAll(variantMapData.values())) {
                productModel = variants.get(i);
                productId = productModel.id;
                currentVariantPosition = i;
            }
        }
    }

    private void getSelectedVariant() {
        loadVariantModel(currentVariantPosition);
    }

    private void loadVariantModel(int variantIndex) {
        if (!variants.get(variantIndex).images.isEmpty()) {
            int emptyVariantImageCount = 0;
            for (int k = 0; k < variantIndex; k++) {
                if (variants.get(k).images.isEmpty()) {
                    emptyVariantImageCount++;
                }
            }
            imageViewPager.setCurrentItem(variantIndex - emptyVariantImageCount);
        }

        title.setText(localizationHelper.getLocalizedTitle(productModel.title));
        if (productModel.campaignPrice == 0) {
            price.setText(ECommerceUtil.getPriceString(productModel.price) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency));
            discountedPrice.setVisibility(View.GONE);
            saleCardView.setVisibility(View.GONE);
        } else {
            price.setText(ECommerceUtil.getPriceString(productModel.campaignPrice) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency));
            discountedPrice.setVisibility(View.VISIBLE);
            saleCardView.setVisibility(View.VISIBLE);
            saleRateTextView.setText(String.format("%%%s", 100 * (productModel.price - productModel.campaignPrice) / productModel.price));
            saleRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            discountedPrice.setText(Html.fromHtml("<strike>" + ECommerceUtil.getPriceString(productModel.price) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency) + "</strike>"));
        }
        if (productModel.stock <= 0) {
            soldOutBadge.setVisibility(View.VISIBLE);
            quantityLayout.setVisibility(View.GONE);
            setContinueButton(false);
        } else {
            soldOutBadge.setVisibility(View.GONE);
            quantityLayout.setVisibility(View.VISIBLE);
            setContinueButton(true);
        }
        amount = 1;
        quantity.setText(String.valueOf(amount));

    }

    @SuppressLint("RestrictedApi")
    private void loadUi() {
        toolbarWebView.setTitleTypeface();
        toolbarWebView.setTitle(getString(R.string.e_commerce_product_detail_description));
        toolbarWebView.setNavigationOnClickListener(view -> {
            webViewLayout.setVisibility(View.GONE);
            new ToolbarHelper().setStatusBarTransparent(ProductDetailActivity.this);
        });

        displayBrandIcon();

        if (!productModel.videos.isEmpty() && productModel.videos.size() > 0) {
            videoUrl = productModel.videos.get(0);
            videoPlayImageView.setVisibility(View.VISIBLE);
        } else videoPlayImageView.setVisibility(View.GONE);

        if (productModel.variationGroups.size() > 0 && !productModel.variants.isEmpty()) {

            variationGroupsModels = productModel.variationGroups;
            variants = productModel.variants;

            variantLayout.setVisibility(View.VISIBLE);
            variantLayout.setWeightSum(variationGroupsModels.size());

            mediaImages.addAll(productModel.images);
            for (int l = 0; l < variants.size(); l++) {
                variantImages.addAll(productModel.variants.get(l).images);
            }

            productModel = productModel.variants.get(0);
            productId = productModel.id;

            productModel.images.clear();
            productModel.images.addAll(variantImages);
            productModel.images.addAll(mediaImages);

            for (int i = 0; i < variationGroupsModels.size(); i++) {
                LayoutInflater vi = getLayoutInflater();
                View v = vi.inflate(R.layout.item_variant_field, null);

                v.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
                if (variationGroupsModels.size() > 1) {
                    if (i + 1 != variationGroupsModels.size())
                        v.setPadding(0, 0, 30, 0);
                    else
                        v.setPadding(10, 0, 0, 0);
                }

                ShopirollerTextView textView = v.findViewById(R.id.variant_text_view);
                textView.setText(variationGroupsModels.get(i).getVariations().get(0).getValue());
                variantMapData.put(variationGroupsModels.get(i).getName(), variationGroupsModels.get(i).getVariations().get(0).getValue());
                textView.setTag(i);

                int finalI = i;
                getAvailableVariants();
                textView.setOnClickListener(x -> {
                    Integer selectedTextViewPosition = (Integer) textView.getTag();
                    selectedVariantGroupIndex = selectedTextViewPosition;
                    List<String> availableVariantList = getAvailableVariants();
                    if (availableVariantList.size() == 1) {
                        selectedVariantIndexMapData.put(selectedVariantGroupIndex,0);
                    }
                    if (selectedVariantIndexMapData.get(selectedVariantGroupIndex) != null) {
                        materialListFilterDialog = new MaterialListFilterDialog(this, variationGroupsModels.get(finalI).getName(), getAvailableVariants(), selectedVariantIndexMapData.get(selectedVariantGroupIndex));
                    } else {
                        materialListFilterDialog = new MaterialListFilterDialog(this, variationGroupsModels.get(finalI).getName(), getAvailableVariants(), 0);
                    }
                    createVariantSelectionListDialog(materialListFilterDialog, textView);
                });
                variantFields.add(v);
            }
            setVariantFields();
        }

        if (!productModel.images.isEmpty())
            imageView.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadDataWithBaseURL("", "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + UtilManager.localizationHelper().getLocalizedTitle(productModel.description), "text/html", "UTF-8", "");

        if (imageAdapter != null) {
            imageAdapter.addAll(productModel.images);
            imageAdapter.notifyDataSetChanged();
        } else {
            imageAdapter = new ProductImageAdapter(this, productModel.images);
            imageViewPager.setAdapter(imageAdapter);
        }
        title.setText(localizationHelper.getLocalizedTitle(productModel.title));
        if (!productModel.useFixPrice && productModel.shippingPrice != 0) {
            cargoCardView.setVisibility(View.VISIBLE);
            cargoTextView.setText(
                    Html.fromHtml(
                            cargoTextView.getContext().getString(R.string.e_commerce_product_detail_cargo_warning,
                                    String.format("%s %s", ECommerceUtil.getPriceString(productModel.shippingPrice), ECommerceUtil.getCurrencySymbol(productModel.currency)))
                    )
            );
        } else {
            cargoCardView.setVisibility(View.GONE);
        }
        if (productModel.campaignPrice == 0)
            price.setText(ECommerceUtil.getPriceString(productModel.price) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency));
        else
            price.setText(ECommerceUtil.getPriceString(productModel.campaignPrice) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency));

        if (productModel.campaignPrice == 0) {
            price.setText(ECommerceUtil.getPriceString(productModel.price) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency));
            discountedPrice.setVisibility(View.GONE);
            saleCardView.setVisibility(View.GONE);

        } else {
            price.setText(ECommerceUtil.getPriceString(productModel.campaignPrice) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency));
            discountedPrice.setVisibility(View.VISIBLE);
            saleCardView.setVisibility(View.VISIBLE);
            saleRateTextView.setText(String.format("%%%s", Math.round(100 * (productModel.price - productModel.campaignPrice) / productModel.price)));
            saleRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            discountedPrice.setText(Html.fromHtml("<strike>" + ECommerceUtil.getPriceString(productModel.price) + " " + ECommerceUtil.getCurrencySymbol(productModel.currency) + "</strike>"));
        }

        if (productModel.stock <= 0) {
            badgeLayout.setVisibility(View.VISIBLE);
            soldOutBadge.setVisibility(View.VISIBLE);
            quantityLayout.setVisibility(View.GONE);
            setContinueButton(false);
        } else {
            badgeLayout.setVisibility(View.GONE);
            soldOutBadge.setVisibility(View.GONE);
        }

        if (productModel.shippingPrice == 0 && !productModel.useFixPrice) {
            badgeLayout.setVisibility(View.VISIBLE);
            freeShippingBadge.setVisibility(View.VISIBLE);
            if (soldOutBadge.getVisibility() == View.GONE) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) freeShippingBadge.getLayoutParams();
                params.setMarginStart(0);
                freeShippingBadge.setLayoutParams(params);

            }
        } else {
            freeShippingBadge.setVisibility(View.GONE);
        }

        amount = 1;
        quantity.setText(String.valueOf(amount));

        if (imageAdapter != null && imageAdapter.getCount() > 1) {
            ImageView[] dots = new ImageView[imageAdapter.getCount()];

            Drawable selectedDot = ContextCompat.getDrawable(this, R.drawable.e_commerce_selecteditem_dot);
            Drawable unSelectedDot = ContextCompat.getDrawable(this, R.drawable.e_commerce_nonselecteditem_dot);

            imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < imageAdapter.getCount(); i++) {
                        dots[i].setImageDrawable(unSelectedDot);
                    }
                    dots[position].setImageDrawable(selectedDot);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            for (int i = 0; i < imageAdapter.getCount(); i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(unSelectedDot);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(0, 0, 0, 0);

                viewPagerCountDots.addView(dots[i], params);
            }

            dots[0].setImageDrawable(selectedDot);

            imageAdapter.notifyDataSetChanged();
        } else {
            viewPagerCountDots.setVisibility(View.GONE);
        }
    }

    private List<String> getAvailableVariants() {
        setCurrentVariantIndex();
        ProductDetailModel currentVariant = variants.get(currentVariantPosition);
        String selectedVariationGroupId = variationGroupsModels.get(selectedVariantGroupIndex).getId();
        List<ProductDetailModel> otherVariantsList = new ArrayList<>(variants);
        List<ProductDetailModel> newOtherVariantsList = new ArrayList<>();
        List<String> availableVariantsList = new ArrayList<>();
        for (int i = 0; i < currentVariant.variantData.size(); i++) {
            VariantDataModel currentVariantDataModel = currentVariant.variantData.get(i);
            if (!currentVariantDataModel.getVariationGroupId().equals(selectedVariationGroupId)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    otherVariantsList = otherVariantsList.stream().filter(variants -> variants.variantData.contains(currentVariantDataModel)).collect(Collectors.toList());
                } else {
                    for (int j = 0; j < otherVariantsList.size(); j++) {
                        if (otherVariantsList.get(i).variantData.contains(currentVariantDataModel)) {
                            newOtherVariantsList.add(otherVariantsList.get(i));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < otherVariantsList.size(); i++) {
            for (int j = 0; j < otherVariantsList.get(i).variantData.size(); j++) {
                String currentValue = otherVariantsList.get(i).variantData.get(j).getValue();
                if (otherVariantsList.get(i).variantData.get(j).getVariationGroupId().equals(selectedVariationGroupId) && !availableVariantsList.contains(currentValue)) {
                    availableVariantsList.add(currentValue);
                }
            }
        }

        return availableVariantsList;
    }

    private void setVariantFields() {

        boolean isOdd = false;

        if (variantFields.size() %2 != 0) {
            isOdd = true;
        }

        List<View> linearLayouts = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.e_commerce_variant_layout, null);
        ViewGroup linearLayout = rootView.findViewById(R.id.variant_main_view);
        for(int i = 0; i < variantFields.size(); i++) {
            if (i % 2 == 0) {
                rootView = inflater.inflate(R.layout.e_commerce_variant_layout, null);
                linearLayout = rootView.findViewById(R.id.variant_main_view);
                linearLayouts.add(rootView);
            }
                linearLayout.addView(variantFields.get(i));
        }

        if (isOdd) {
            LayoutInflater emptyTextViewInflater = getLayoutInflater();
            View emptyView = emptyTextViewInflater.inflate(R.layout.item_variant_field, null);
            ShopirollerTextView emptyViewTextView = emptyView.findViewById(R.id.variant_text_view);
            emptyViewTextView.setVisibility(View.INVISIBLE);
            View lastRootView = linearLayouts.get(linearLayouts.size() - 1);
            linearLayouts.remove(lastRootView);
            ViewGroup oddLinearLayout = lastRootView.findViewById(R.id.variant_main_view);
            emptyView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
            oddLinearLayout.addView(emptyView);
            linearLayouts.add(lastRootView);
        }

        for (int i = 0; i < linearLayouts.size(); i++) {
            variantLayout.addView(linearLayouts.get(i));
        }

    }

    private void createVariantSelectionListDialog(MaterialListFilterDialog materialListFilterDialog, TextView textView) {
        RecyclerView recyclerView = materialListFilterDialog.show();
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String selectedVariant = (String) ((DialogFilterAdapter) recyclerView.getAdapter()).getItemAtPosition(position);
                textView.setText(selectedVariant);
                variantMapData.put(variationGroupsModels.get((Integer) textView.getTag()).getName(), selectedVariant);
                selectedVariantIndexMapData.put(((Integer) textView.getTag()), position);
                materialListFilterDialog.dismiss();
                setCurrentVariantIndex();
                getSelectedVariant();
            }
        });
    }

    public void setContinueButton(boolean isEnabled) {
        buyButton.setEnabled(isEnabled);
        if (isEnabled) {
            buyButton.setText(getString(R.string.e_commerce_product_detail_buy_now));
        } else {
            buyButton.setText(getString(R.string.e_commerce_product_detail_sold_out_button));
            buyButton.removeIcon();
        }
        buyButton.setColor();
    }

    @OnClick(R2.id.quantity_down)
    public void onClickQuantityDown() {
        if (amount == 1)
            return;
        else {
            amount--;
            quantity.setText(String.valueOf(amount));
        }
    }

    @OnClick(R2.id.quantity_up)
    public void onClickQuantityUp() {
        if (amount == productModel.maxQuantityPerOrder || amount == productModel.stock) {
            Toast.makeText(this, getString(R.string.e_commerce_product_detail_maximum_product_message, String.valueOf(amount)), Toast.LENGTH_SHORT).show();
        } else {
            amount++;
            quantity.setText(String.valueOf(amount));
        }
    }

    private boolean isVariantCanBeAdd() {
        List<ProductDetailModel> variantList = variants;
        List<String> selectedVariantIds = new ArrayList<>();
        for (Map.Entry<String, String> hashMap : variantMapData.entrySet()) {
            String value = hashMap.getValue();
            String selectedVariantId = getVariantId(hashMap.getKey(), value);
            if (!selectedVariantIds.contains(selectedVariantId)) {
                selectedVariantIds.add(selectedVariantId);
            }
        }
        for (int i = 0; i < variantList.size(); i++) {
            List<String> variationIdList = new ArrayList<>();
            for (int j = 0; j < variantList.get(i).variantData.size(); j++) {
                variationIdList.add(variantList.get(i).variantData.get(j).getVariationId());
            }
            if (selectedVariantIds.containsAll(variationIdList)) {
                return true;
            }
        }
        return false;
    }

    private String getVariantId(String variantName, String variantValue) {
        ArrayList<Variation> variationsList = new ArrayList<Variation>();
        String variantId = "";
        for (int i = 0; i < variationGroupsModels.size(); i++) {
            if (variationGroupsModels.get(i).getName().equalsIgnoreCase(variantName)) {
                for (VariationGroupsModel variationGroupsModel : variationGroupsModels) {
                    for (Variation variation : variationGroupsModel.getVariations()) {
                        if (variation.getValue().equalsIgnoreCase(variantValue)) {
                            variationsList.add(variation);
                        }
                    }
                }
            }
        }
        for (Variation variation : variationsList) {
            variantId = variation.getId();
        }
        return variantId;
    }

    @OnClick(R2.id.buy_button)
    public void onClickBuyButton() {
        if (NetworkHelper.isConnected(this)) {
            if (Shopiroller.getUserLoginStatus() && productModel != null) {
                if (!variants.isEmpty()) {
                    if (isVariantCanBeAdd()) {
                        addProductToCart();
                    } else {
                        showWarning(getString(R.string.e_commerce_product_detail_does_not_exist_variant_error),
                                getString(R.string.e_commerce_product_detail_does_not_exist_variant_description),
                                getString(R.string.e_commerce_product_detail_maximum_product_limit_button),
                                null);
                    }
                } else {
                    addProductToCart();
                }
            } else if (!Shopiroller.getUserLoginStatus()) {
                if(Shopiroller.getListener() != null)
                    Shopiroller.getListener().loginNeeded();
            } else {
                Toast.makeText(this, R.string.common_error, Toast.LENGTH_SHORT).show();
            }
        } else
            DialogUtil.showNoConnectionInfo(this);
    }

    private void addProductToCart() {
        progressViewHelper.show();
        Call<ECommerceResponse<ShoppingCartItemsResponse>> responseCall = eCommerceRequestHelper.getApiService().addProductToShoppingCart(Shopiroller.getUserId(), new AddProductModel(productId, amount, localizationHelper.getLocalizedTitle(productModel.title)));
        responseCall.enqueue(new Callback<ECommerceResponse<ShoppingCartItemsResponse>>() {
            @Override
            public void onResponse(Call<ECommerceResponse<ShoppingCartItemsResponse>> call, Response<ECommerceResponse<ShoppingCartItemsResponse>> response) {
                if (!isFinishing() && progressViewHelper != null && progressViewHelper.isShowing())
                    progressViewHelper.dismiss();
                if (response.isSuccessful() || response.code() == 204) {
                    buyButton.setTextAnimated(getString(R.string.e_commerce_product_detail_added_to_shopping_cart_button));
                    if (response.body() != null && response.body().data != null) {
                        ECommerceUtil.setBadgeCount(response.body().data.getShoppingCardCount());
                    }
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    ECommerceResponse eCommerceResponse;
                    try {
                        eCommerceResponse = gson.fromJson(response.errorBody().string(), ECommerceResponse.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (eCommerceResponse == null)
                        return;
                    if (eCommerceResponse.key.equalsIgnoreCase(Constants.PRODUCT_MAX_QUANTITY_PER_ORDER_EXCEEDED)) {
                        showWarning(getString(R.string.e_commerce_product_detail_maximum_product_limit_title), getString(R.string.e_commerce_product_detail_maximum_product_limit_description, String.valueOf(productModel.maxQuantityPerOrder)), getString(R.string.e_commerce_product_detail_maximum_product_limit_button), null);
                    } else if (eCommerceResponse.key.equalsIgnoreCase(Constants.PRODUCT_MAX_STOCK_EXCEEDED)) {
                        showWarning(getString(R.string.e_commerce_product_detail_out_of_stock_title), getString(R.string.e_commerce_product_detail_out_of_stock_description), getString(R.string.e_commerce_product_detail_out_of_stock_button), new ShopirollerDialog.DialogButtonCallback() {
                            @Override
                            public void onClickButton() {
                                finish();
                            }
                        });
                    } else if (eCommerceResponse.isUserFriendlyMessage) {
                        Toast.makeText(ProductDetailActivity.this, eCommerceResponse.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ECommerceResponse<ShoppingCartItemsResponse>> call, Throwable t) {
                if (!isFinishing() && progressViewHelper != null && progressViewHelper.isShowing())
                    progressViewHelper.dismiss();
                ErrorUtils.showErrorToast(ProductDetailActivity.this);
            }
        });
    }

    private void showWarning(String title, String description, String buttonText, ShopirollerDialog.DialogButtonCallback listener) {

        new ShopirollerDialog.Builder()
                .setContext(ProductDetailActivity.this)
                .setTitle(title)
                .setListener(listener)
                .setDescription(description)
                .setType(MobirollerDialogType.BASIC)
                .setColor(Color.parseColor("#F8E7D8"))
                .setIconResource(R.drawable.ic_order_warning_icon)
                .setButtonText(buttonText)
                .show();
    }

    @Override
    public void isNotVideoUrl(String videoUrl) {
        webViewForUrl.setVisibility(View.VISIBLE);
        webViewForUrl.setWebViewClient(new WebViewClient());
        webViewForUrl.getSettings().setJavaScriptEnabled(true);
        webViewForUrl.loadUrl(videoUrl);
    }

    public class ProductImageAdapter extends PagerAdapter {

        private ArrayList<ProductImage> images;
        LayoutInflater inflater;
        AppCompatActivity context;

        ProductImageAdapter(AppCompatActivity context, List<ProductImage> images) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.images = new ArrayList<>(images);
            this.context = context;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        public void add(ProductImage productImage) {
            images.add(productImage);
        }

        public void addAll(List<ProductImage> productImage) {
            for (ProductImage pr :
                    productImage) {
                boolean isFound = false;
                for (int i = 0; i < images.size(); i++) {
                    if (images.get(i).n.equalsIgnoreCase(pr.n)) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound)
                    images.add(pr);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.layout_e_commerce_image_view_pager_item, view, false);
            ImageView imageView = imageLayout.findViewById(R.id.image);
            if (selectedPosition == position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setTransitionName("featuredImage");
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductGalleryBottomSheetFragment productGalleryBottomSheetFragment = new ProductGalleryBottomSheetFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.PRODUCT_IMAGE_LIST, images);
                    bundle.putInt(Constants.PRODUCT_IMAGE_LIST_POSITION, position);
                    productGalleryBottomSheetFragment.setArguments(bundle);
                    productGalleryBottomSheetFragment.setSharedElementEnterTransition(imageView);
                    productGalleryBottomSheetFragment.setSharedElementReturnTransition(imageView);
                    productGalleryBottomSheetFragment.show(context.getSupportFragmentManager(), productGalleryBottomSheetFragment.getTag());
                }
            });
            if (images != null && images.get(position) != null && images.get(position).n != null) {
                Glide.with(context).load(images.get(position).n + "?width=" + imageView.getWidth())
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(imageView);
            } else {
                Glide.with(context).load(R.drawable.no_image_e_commerce).into(imageView);
            }
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

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            invalidateOptionsMenu();
            mCurrentPosition = position;
            mCurrentView = (View) object;
        }
    }

    @Override
    public void onBackPressed() {
        if (webViewLayout.getVisibility() == View.VISIBLE) {
            webViewLayout.setVisibility(View.GONE);
            new ToolbarHelper().setStatusBarTransparent(ProductDetailActivity.this);
            return;
        } else if (webViewForUrl.getVisibility() == View.VISIBLE && webViewForUrl.canGoBack()) {
            webViewForUrl.goBack();
        } else finish();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @OnClick(R2.id.video_play_image_view)
    public void videoPlayButtonTapped() {
        startActivity(VideoPlayerActivity.Companion.getVideoPlayerIntent(videoUrl, this));
    }

    @OnClick(R2.id.return_terms_layout)
    public void onClickTermsLayout() {
        if (ECommerceUtil.getPaymentSettings() == null)
            getTerms(false);
        else
            showTerms(ECommerceUtil.getPaymentSettings().getCancellationProcedureTitle(), localizationHelper.getLocalizedTitle(ECommerceUtil.getPaymentSettings().getCancellationProcedure()));
    }

    @OnClick(R2.id.shipping_terms_layout)
    public void onClickShippingTermsLayout() {
        if (ECommerceUtil.getPaymentSettings() == null)
            getTerms(true);
        else
            showTerms(ECommerceUtil.getPaymentSettings().getDeliveryConditionsTitle(), localizationHelper.getLocalizedTitle(ECommerceUtil.getPaymentSettings().getDeliveryConditions()));
    }

    @OnClick(R2.id.description_text)
    public void onClickDescriptionLayout() {
        webViewLayout.setVisibility(View.VISIBLE);
        new ToolbarHelper().setStatusBarColor(ProductDetailActivity.this);
    }

    private void getTerms(boolean isShipping) {
        if (NetworkHelper.isConnected(this)) {
            progressViewHelper.show();
            new ECommerceUtil().getPaymentSettings(new ECommerceRequestHelper.ECommerceCallBack<PaymentSettings>() {
                @Override
                public void done() {
                    if (!isFinishing() && progressViewHelper.isShowing())
                        progressViewHelper.dismiss();
                }

                @Override
                public void onSuccess(PaymentSettings result) {
                    if (isShipping)
                        showTerms(ECommerceUtil.getPaymentSettings().getDeliveryConditionsTitle(), localizationHelper.getLocalizedTitle(ECommerceUtil.getPaymentSettings().getDeliveryConditions()));
                    else
                        showTerms(ECommerceUtil.getPaymentSettings().getCancellationProcedureTitle(), localizationHelper.getLocalizedTitle(ECommerceUtil.getPaymentSettings().getCancellationProcedure()));
                }

                @Override
                public void onFailure(ECommerceErrorResponse result) {
                    ErrorUtils.showErrorToast(ProductDetailActivity.this);
                }

                @Override
                public void onNetworkError(String result) {
                    ErrorUtils.showErrorToast(ProductDetailActivity.this);
                }
            });
        } else
            DialogUtil.showNoConnectionInfo(this);
    }

    void showTerms(String title, String description) {

        if (!isFinishing()) {
            Spannable descriptionHtml;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                descriptionHtml = (Spannable) Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY, null, null);
            } else {
                descriptionHtml = (Spannable) Html.fromHtml(description, null, null);
            }

            new ShopirollerDialog.Builder()
                    .setContext(this)
                    .setTitle(title)
                    .setIconResource(R.drawable.ic_document_icon)
                    .setDescription(descriptionHtml)
                    .setButtonText(getString(R.string.e_commerce_product_detail_terms_delivery_conditions_popup_button))
                    .setType(MobirollerDialogType.BASIC)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.ecommerce_detail_menu);
        View actionView = toolbar.getMenu().findItem(R.id.action_shopping_cart).getActionView();
        ImageView shoppingBagIcon = (ImageView) actionView.findViewById(R.id.shopping_bag_icon);
        ImageViewCompat.setImageTintList(shoppingBagIcon, ColorStateList.valueOf(ColorHelper.isColorDark(Shopiroller.getTheme().primaryColor) ? Color.WHITE : Color.BLACK));

        setBadgeCount(toolbar, ECommerceUtil.getBadgeCount());
        actionView.setOnClickListener(view -> startShoppingCartActivity());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_share) {
            onClickShareButton();
            return true;
        } else if (itemId == R.id.action_search) {
            startActivity(new Intent(this, ProductSearchActivity.class));
            return true;
        } else if (itemId == R.id.action_shopping_cart) {
            startShoppingCartActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onPostShoppingCartCountEvent(ShoppingCartCountEvent event) {
        setBadgeCount(toolbar, event.shoppingCartItemCount);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}