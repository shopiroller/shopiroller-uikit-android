package com.shopiroller.util;

import static com.shopiroller.constants.Constants.DELIVERED;
import static com.shopiroller.constants.Constants.ORDER_STATUS;
import static com.shopiroller.constants.Constants.ORDER_STATUS_ICONS;
import static com.shopiroller.constants.Constants.ORDER_STATUS_RED;

import android.content.Context;
import android.graphics.Color;

import com.shopiroller.Shopiroller;
import com.shopiroller.constants.Constants;
import com.shopiroller.models.ECommerceErrorResponse;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.MakeOrderAddress;
import com.shopiroller.models.PaymentSettings;
import com.shopiroller.models.ShoppingCartCountEvent;
import com.shopiroller.models.VariationGroupsItem;
import com.shopiroller.models.user.UserBillingAddressModel;
import com.shopiroller.models.user.UserShippingAddressModel;
import com.shopiroller.network.ECommerceRequestHelper;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import io.paperdb.Paper;
import retrofit2.Call;

public class ECommerceUtil {

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static DateFormat dateFormatSimple = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static DateFormat dateFormatOrderDate = new SimpleDateFormat("dd MMMM yyyy");
    public static DateFormat dateFormatOrderTime = new SimpleDateFormat("EEEE, HH:mm");
    public static DateFormat dateFormatOrderDetail = new SimpleDateFormat("dd MMMM yyyy EEEE, HH:mm");
    private static NumberFormat formatter;
    private static int badgeCount;
    private static PaymentSettings paymentSettings;

    public static String dateFormatOrderDateString = "dd MMMM yyyy";
    public static String dateFormatOrderTimeString = "EEEE, HH:mm";
    public static String dateFormatOrderDetailString = "dd MMMM yyyy EEEE, HH:mm";

    public static String APP_SETTINGS_BOOK = "appSettingsBook";

    public String getFormattedDateTime(String stringDate, String format) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = ECommerceUtil.dateFormat.parse(stringDate);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFormattedPrice(Double price, String currency) {
        if (formatter == null) {
            formatter = NumberFormat.getCurrencyInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
        }
        formatter.setCurrency(Currency.getInstance(currency));
        return formatter.format(price);
    }

    public static String getOrderStatus(String status, Context context) {
        if (ORDER_STATUS.containsKey(status))
            return context.getString(ORDER_STATUS.get(status));
        return status;
    }

    public static int getOrderStatusIcon(String status) {
        if (ORDER_STATUS_ICONS.containsKey(status))
            return ORDER_STATUS_ICONS.get(status);
        return 0;
    }

    public static int getOrderStatusColor(String status, Context context) {
        if (ORDER_STATUS_RED.containsKey(status))
            return ORDER_STATUS_RED.get(status);
        else if (DELIVERED.equalsIgnoreCase(status))
            return Color.parseColor("#4bbf71");
        else
            return Color.parseColor("#464646");
    }

    public static String[] getSearchSuggestions() {
        if (Paper.book(APP_SETTINGS_BOOK).contains(Constants.SEARCH_SUGGESTIONS)) {
            List<String> suggestions = Paper.book(APP_SETTINGS_BOOK).read(Constants.SEARCH_SUGGESTIONS, new ArrayList<>());
            return suggestions.toArray(new String[suggestions.size()]);
        }
        return new String[0];
    }

    public static List<String> getSearchSuggestionList() {
        if (Paper.book(APP_SETTINGS_BOOK).contains(Constants.SEARCH_SUGGESTIONS)) {
            List<String> suggestions = Paper.book(APP_SETTINGS_BOOK).read(Constants.SEARCH_SUGGESTIONS, new ArrayList<>());
            return suggestions;
        }
        return new ArrayList<String>();
    }


    public static void addSearchSuggestion(String keyword) {
        List<String> suggestions = new ArrayList<>();
        if (Paper.book(APP_SETTINGS_BOOK).contains(Constants.SEARCH_SUGGESTIONS))
            suggestions = Paper.book(APP_SETTINGS_BOOK).read(Constants.SEARCH_SUGGESTIONS, new ArrayList<>());
        if (!suggestions.contains(keyword)) {
            suggestions.add(keyword);
            Paper.book(APP_SETTINGS_BOOK).write(Constants.SEARCH_SUGGESTIONS, suggestions);
        }
    }

    public static void removeSearchSuggestion(String keyword) {
        List<String> suggestions = new ArrayList<>();
        if (Paper.book(APP_SETTINGS_BOOK).contains(Constants.SEARCH_SUGGESTIONS))
            suggestions = Paper.book(APP_SETTINGS_BOOK).read(Constants.SEARCH_SUGGESTIONS, new ArrayList<>());
        if (suggestions.contains(keyword)) {
            suggestions.remove(keyword);
            Paper.book(APP_SETTINGS_BOOK).write(Constants.SEARCH_SUGGESTIONS, suggestions);
        }
    }

    public static boolean validateCreditCardNumber(String str) {

        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int anInt : ints) {
            sum += anInt;
        }
        return sum % 10 == 0;
    }

    public static void setBadgeCount(int count) {
        badgeCount = count;
        EventBus.getDefault().post(new ShoppingCartCountEvent(count));
    }

    public static int getBadgeCount() {
        return badgeCount;
    }

    public void getBadge() {
        if (Shopiroller.getUserId() == null)
            return;
        ECommerceRequestHelper eCommerceRequestHelper = new ECommerceRequestHelper();
        Call<ECommerceResponse<Integer>> responseCall = eCommerceRequestHelper.getApiService().getShoppingCartCount(Shopiroller.getUserId());
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<Integer>() {
            @Override
            public void done() {
            }

            @Override
            public void onSuccess(Integer result) {
                if (result == null)
                    result = 0;
                setBadgeCount(result);
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
            }

            @Override
            public void onNetworkError(String result) {
            }
        });
    }

    public void getPaymentSettings(ECommerceRequestHelper.ECommerceCallBack<PaymentSettings> callBack) {
        ECommerceRequestHelper eCommerceRequestHelper = new ECommerceRequestHelper();
        Call<ECommerceResponse<PaymentSettings>> responseCall = eCommerceRequestHelper.getApiService().getPaymentSettings();
        eCommerceRequestHelper.enqueue(responseCall, new ECommerceRequestHelper.ECommerceCallBack<PaymentSettings>() {
            @Override
            public void done() {
                if (callBack != null) {
                    callBack.done();
                }
            }

            @Override
            public void onSuccess(PaymentSettings result) {
                ECommerceUtil.paymentSettings = result;
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
            }

            @Override
            public void onFailure(ECommerceErrorResponse result) {
                if (callBack != null) {
                    callBack.onFailure(result);
                }
            }

            @Override
            public void onNetworkError(String result) {
                if (callBack != null) {
                    callBack.onNetworkError(result);
                }
            }
        });
    }

    public static PaymentSettings getPaymentSettings() {
        return paymentSettings;
    }


    public static MakeOrderAddress getOrderAddress(UserBillingAddressModel address) {
        MakeOrderAddress orderAddress = new MakeOrderAddress();
        orderAddress.city = address.city;
        orderAddress.companyName = address.companyName;
        orderAddress.state = address.state;
        orderAddress.country = address.country;
        orderAddress.description = address.addressLine;
        orderAddress.id = address.id;
        orderAddress.identityNumber = address.identityNumber;
        orderAddress.taxOffice = address.taxOffice;
        orderAddress.taxNumber = address.taxNumber;
        orderAddress.zipCode = address.zipCode;
        orderAddress.phoneNumber = address.contact.phoneNumber;
        orderAddress.nameSurname = address.contact.nameSurname;
        return orderAddress;
    }

    public static MakeOrderAddress getOrderAddress(UserShippingAddressModel address) {
        MakeOrderAddress orderAddress = new MakeOrderAddress();
        orderAddress.city = address.city;
        orderAddress.state = address.state;
        orderAddress.country = address.country;
        orderAddress.description = address.addressLine;
        orderAddress.id = address.id;
        orderAddress.zipCode = address.zipCode;
        orderAddress.phoneNumber = address.contact.phoneNumber;
        orderAddress.nameSurname = address.contact.nameSurname;
        return orderAddress;
    }

    public static String formatVariantForQuery(List<VariationGroupsItem> variantList) {
        StringBuilder query = new StringBuilder();
        if (variantList != null) {
            for (int i = 0; i < variantList.size(); i++) {
                if (variantList.get(i) != null && variantList.get(i).getVariations() != null) {
                    StringBuilder tempQuery = new StringBuilder(";" + variantList.get(i).getId() + ":");
                    boolean isFound = false;
                    for (int j = 0; j < variantList.get(i).getVariations().size(); j++) {
                        if (variantList.get(i).getVariations().get(j).isChecked()) {
                            tempQuery.append(variantList.get(i).getVariations().get(j).getId() + ",");
                            isFound = true;
                        }
                    }
                    if (isFound) {
                        tempQuery = tempQuery.deleteCharAt(tempQuery.length() - 1);
                        query.append(tempQuery);
                    }
                }
            }
        }
        if (query.length() > 0) {
            query.deleteCharAt(0);
        }

        return query.toString();
    }

    public static HashMap<String, List<String>> getSelectedVariantList(String queryFormat) {
        HashMap<String, List<String>> hashMap = new HashMap<>();

        if (queryFormat != null) {
            String[] queryArray = queryFormat.split(";");
            for (int i = 0; i < queryArray.length; i++) {
                hashMap.put(queryArray[i].split(":")[0], Arrays.asList(queryArray[i].split(":")[1].split(",")));
            }
        }

        return hashMap;
    }
}
