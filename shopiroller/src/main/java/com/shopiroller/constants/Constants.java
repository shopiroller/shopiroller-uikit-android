package com.shopiroller.constants;

import android.graphics.Color;

import com.shopiroller.R;
import com.shopiroller.Shopiroller;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static String Applyze_BaseURL = Shopiroller.getAdapter().getApplyzeBaseURL();
    public static String Shopiroller_BaseURL = Shopiroller.getAdapter().getShopirollerBaseURL();

    public static String Applyze_ECommerce_BaseURL_Shopiroller = Shopiroller_BaseURL + "";
    public static String Applyze_ECommerce_BaseURL = Applyze_BaseURL + "ecommerce/";

    public static String Applyze_Payment_Complete = "http://applyze-ecommerce-service/v1/paymentSuccess";
    public static String Applyze_Payment_Complete_1 = "https://ecommerce.applyze.com/v2.0/paymentSuccess";
    public static String Applyze_Payment_Complete_2 = "/paymentSuccess";
    public static String Applyze_Payment_Failed = "http://applyze-ecommerce-service/v1/paymentFailure";
    public static String Applyze_Payment_Failed_1 = "https://ecommerce.applyze.com/v2.0/paymentFailure";
    public static String Applyze_Payment_Failed_2 = "/paymentFailure";

    public static String E_COMMERCE_SCREEN_ID = ""; //todo sr set
    public static final String MODULE_ECOMMERCE_PRO_VIEW = "aveECommerceProView";
    public static final String MODULE_ECOMMERCE_VIEW = "aveECommerceView";

    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_LIST_MODEL = "productListModel";
    public static final String PRODUCT_DETAIL_MODEL = "productDetailModel";
    public static final String PRODUCT_TITLE = "productTitle";
    public static final String PRODUCT_FEATURED_IMAGE = "productFeaturedImage";
    public static final String PRODUCT_IMAGE_LIST = "productImageList";
    public static final String PRODUCT_IMAGE_LIST_POSITION = "productImageListPosition";
    public static final String MAKE_ORDER_MODEL = "makeOrderModel";
    public static final String AGREEMENT_URL = "agreementUrl";
    public static final String ORDER_RESPONSE_MODEL = "orderResponseModel";
    public static final String ORDER_INNER_RESPONSE_MODEL = "orderInnerResponseModel";
    public static final String ORDER_FAILED_RESPONSE_MODEL = "orderFailedResponseModel";
    public static final String IS_PAYMENT_SUCCESS = "isPaymentSuccess";
    public static final String ORDER_FAILED_STATUS_CODE = "orderFailedStatusCode";
    public static final String ORDER_ID = "orderId";
    public static final String ORDER_DETAIL_MODEL= "orderDetailModel";
    public static final String ONLINE_PAYMENT_3D_HTML = "onlinePayment3dHtml";
    public static final int ONLINE_PAYMENT_3D_HTML_REQUEST_CODE = 1003;
    public static final int PAYPAL_REQUEST_CODE = 1004;
    public static final String ONLINE_PAYMENT_3D_HTML_REQUEST_SUCCESS = "paymentSuccess";
    public static final String ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED = "paymentFailed";
    public static final String ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED_STATUS_CODE = "paymentFailedStatusCode";
    public static final String PAY_PAL_REQUEST_SUCCESS = "payPalPaymentSuccess";
    public static final String PAY_PAL_REQUEST_FAILED_STATUS_CODE = "payPalPaymentFailedStatusCode";
    public static final String SEARCH_SUGGESTIONS = "searchSuggestions";
    public static final String SEARCHED_KEYWORD = "searchedKeyword";
    public static final String SHOPPING_CART = "shoppingCart";
    public static final String SEARCH_TEXT = "searchText";
    public static final String CATEGORY_ID = "categoryId";
    public static final String SHOW_CASE_ID = "showcaseId";

    public static final String SHOPPING_CART_NOT_ENOUGH_STOCK = "NotEnoughStock";
    public static final String SHOPPING_CART_OUT_OF_STOCK = "OutOfStock";
    public static final String SHOPPING_CART_OVER_PUBLISHMENT_DATE = "OverPublishmentDate";
    public static final String SHOPPING_CART_PRODUCT_NOT_FOUND = "ProductNotFound";

    public static final String INTENT_EXTRA_IS_MAIN_CATEGORY_LIST = "isMainCategoryList";
    public static final String INTENT_EXTRA_SUB_CATEGORY_MODEL = "subCategoryModel";
    public static final String INTENT_EXTRA_CATEGORIES_MOBILE_SETTINGS = "categoriesMobileSettings";
    public static final String INTENT_EXTRA_CATEGORY_MODEL = "categoryModel";
    public static final String INTENT_EXTRA_IS_SLIDER_INTENT = "categorySlider";
    public static final String INTENT_EXTRA_SHOWCASE_MODEL = "showcaseModel";
    public static final String INTENT_EXTRA_SLIDER_MODEL = "SliderModel";

    public static final String SLIDER_TYPE_WEB = "Web";
    public static final String SLIDER_TYPE_PRODUCT = "Product";
    public static final String SLIDER_TYPE_CATEGORY = "Category";
    public static final String SLIDER_TYPE_NONE = "None";

    public static final String PRODUCT_MAX_QUANTITY_PER_ORDER_EXCEEDED = "MaxQuantityPerOrderExceeded";
    public static final String PRODUCT_MAX_STOCK_EXCEEDED = "MaxStockExceeded";

    public static final Map<String, Integer> INVALID_CART_ITEM_STATUS = new HashMap<String, Integer>() {{
        put(SHOPPING_CART_NOT_ENOUGH_STOCK, R.string.e_commerce_shopping_cart_invalid_product_left);
        put(SHOPPING_CART_OUT_OF_STOCK, R.string.e_commerce_shopping_cart_invalid_out_of_stock);
        put(SHOPPING_CART_OVER_PUBLISHMENT_DATE, R.string.e_commerce_shopping_cart_invalid_publishment_end);
        put(SHOPPING_CART_PRODUCT_NOT_FOUND, R.string.e_commerce_shopping_cart_invalid_removed_basket);
    }};

    // Payment options
    public static final String PAY_AT_DOOR = "PayAtDoor";
    public static final String ONLINE = "Online";
    public static final String ONLINE3DS = "Online3DS";
    public static final String TRANSFER = "Transfer";
    public static final String PAYPAL = "PayPal";
    public static final String STRIPE = "Stripe";

    public static final String WHATSAPP = "Whatsapp";
    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    // Order status
    public static final String WAITING_PAYMENT = "WaitingPayment";
    public static final String WAITING_APPROVAL = "WaitingApproval";
    public static final String APPROVED = "Approved";
    public static final String WAITING_SUPPLYING = "WaitingForSupplying";
    public static final String PREPARING = "Preparing";
    public static final String SHIPPED = "Shipped";
    public static final String DELIVERED = "Delivered";
    public static final String CANCEL_REQUESTED = "CancelRequested";
    public static final String CANCELED = "Canceled";
    public static final String REFUNDED = "Refunded";
    public static final String PAYMENT_FAILED = "PaymentFailed";

    public static final String NEW_LINE = "\n";

    public static final float MobiRoller_Preferences_StandardHeight = 568;
    public static final float MobiRoller_Preferences_StandardWidth = 320;


    public static final Map<String, Integer> ORDER_STATUS = new HashMap<String, Integer>() {{
        put(WAITING_PAYMENT, R.string.e_commerce_order_status_waiting_payment);
        put(WAITING_APPROVAL, R.string.e_commerce_order_status_waiting_approval);
        put(APPROVED, R.string.e_commerce_order_status_approved);
        put(WAITING_SUPPLYING, R.string.e_commerce_order_status_waiting_supplying);
        put(PREPARING, R.string.e_commerce_order_status_preparing);
        put(SHIPPED, R.string.e_commerce_order_status_shipped);
        put(DELIVERED, R.string.e_commerce_order_status_delivered);
        put(CANCEL_REQUESTED, R.string.e_commerce_order_status_cancel_requested);
        put(CANCELED, R.string.e_commerce_order_status_canceled);
        put(REFUNDED, R.string.e_commerce_order_status_refunded);
        put(PAYMENT_FAILED, R.string.e_commerce_order_status_payment_failed);
    }};

    public static final Map<String, Integer> PAYMENT_TYPES = new HashMap<String, Integer>() {{
        put(PAY_AT_DOOR, R.string.e_commerce_payment_method_selection_pay_at_door);
        put(ONLINE, R.string.e_commerce_payment_method_selection_credit_card);
        put(ONLINE3DS, R.string.e_commerce_payment_method_selection_credit_card);
        put(TRANSFER, R.string.e_commerce_payment_method_selection_transfer);
    }};

    public static final Map<String, Integer> ORDER_STATUS_ICONS = new HashMap<String, Integer>() {{
        put(WAITING_PAYMENT, R.drawable.ic_e_commerce_order_waiting_payment_icon);
        put(WAITING_APPROVAL, R.drawable.ic_e_commerce_order_waiting_approval_icon);
        put(APPROVED, R.drawable.ic_e_commerce_order_approved_icon);
        put(WAITING_SUPPLYING, R.drawable.ic_e_commerce_order_waiting_supplying_icon);
        put(PREPARING, R.drawable.ic_e_ecommerce_order_preparing_icon);
        put(SHIPPED, R.drawable.ic_e_commerce_order_shipped_icon);
        put(DELIVERED, R.drawable.ic_e_commerce_order_delivered_icon);
        put(CANCEL_REQUESTED, R.drawable.ic_e_commerce_order_payment_failed_icon);
        put(CANCELED, R.drawable.ic_e_commerce_order_cancelled_icon);
        put(REFUNDED, R.drawable.ic_e_commerce_order_refunded_icon);
        put(PAYMENT_FAILED, R.drawable.ic_e_commerce_order_payment_failed_icon);
    }};


    public static final Map<String, Integer> ORDER_STATUS_RED = new HashMap<String, Integer>() {{
        put(WAITING_APPROVAL, Color.parseColor("#F8E7D8"));
        put(WAITING_PAYMENT, Color.parseColor("#F8E7D8"));
        put(WAITING_SUPPLYING, Color.parseColor("#F8E7D8"));
        put(PREPARING, Color.parseColor("#F8E7D8"));
        put(SHIPPED, Color.parseColor("#F8E7D8"));
        put(APPROVED, Color.parseColor("#D8F5E5"));
        put(DELIVERED, Color.parseColor("#D8F5E5"));
        put(REFUNDED, Color.parseColor("#FEE6E3"));
        put(CANCEL_REQUESTED, Color.parseColor("#FEE6E3"));
        put(CANCELED, Color.parseColor("#FEE6E3"));
        put(PAYMENT_FAILED, Color.parseColor("#FEE6E3"));
    }};

    public static final Map<String, Integer> PAYMENT_STATUS_CODES = new HashMap<String, Integer>() {{
        put("P100", R.string.e_commerce_result_pay_pal_currency_not_supported);
        put("800", R.string.e_commerce_result_credit_card_not_enough_balance);
        put("801", R.string.e_commerce_result_credit_card_process_did_not_approved);
        put("900", R.string.e_commerce_result_credit_card_invalid_payment_info);
        put("901", R.string.e_commerce_result_credit_card_lost_or_stolen);
        put("902", R.string.e_commerce_result_credit_card_expired_card);
        put("903", R.string.e_commerce_result_credit_card_invalid_cvc);
        put("904", R.string.e_commerce_result_credit_card_requires_three_d);
        put("905", R.string.e_commerce_result_credit_card_installment_not_allowed);
        put("906", R.string.e_commerce_result_credit_card_too_many_invalid);
        put("907", R.string.e_commerce_result_credit_card_not_enough_limit);
        put("1100", R.string.e_commerce_result_credit_card_invalid_enum);
        put("1101", R.string.e_commerce_result_credit_card_already_done_before);
    }};


}
