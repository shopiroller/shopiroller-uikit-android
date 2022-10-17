package com.shopiroller.activities;

import static com.shopiroller.constants.Constants.PAY_PAL_REQUEST_FAILED_STATUS_CODE;
import static com.shopiroller.constants.Constants.PAY_PAL_REQUEST_SUCCESS;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.shopiroller.SharedApplication;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.network.ECommerceRequestHelper;
import com.shopiroller.workers.PayPalRequestWorker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayPalActivity extends AppCompatActivity implements PaymentMethodNonceCreatedListener,
        BraintreeCancelListener, BraintreeErrorListener {

    private BraintreeFragment mBraintreeFragment;
    public final static String PAYPAL_AUTH_TOKEN_KEY = "PayPalAuthTokenIntentExtra";
    public final static String PAYPAL_ORDER_ID_KEY = "PayPalOrderIdIntentExtra";
    public final static String PAYPAL_NONCE_KEY = "PayPalNonceIntentExtra";
    public final static String PAYPAL_CURRENCY_CODE_KEY = "PayPalCurrencyCodeIntentExtra";
    public final static String PAYPAL_AMOUNT_KEY = "PayPalAmountIntentExtra";
    public final static String PAYPAL_DISPLAY_NAME_KEY = "PayPalDisplayNameIntentExtra";
    private String token;
    private String orderId;
    private String currencyCode;
    private String displayName;
    private double amount;
    private String errorCode = "0";
    LegacyProgressViewHelper legacyProgressViewHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);
        if (getIntent().hasExtra(PAYPAL_AUTH_TOKEN_KEY)
                && getIntent().hasExtra(PAYPAL_ORDER_ID_KEY)
                && getIntent().hasExtra(PAYPAL_CURRENCY_CODE_KEY)
                && getIntent().hasExtra(PAYPAL_AMOUNT_KEY)) {
            token = getIntent().getStringExtra(PAYPAL_AUTH_TOKEN_KEY);
            orderId = getIntent().getStringExtra(PAYPAL_ORDER_ID_KEY);
            currencyCode = getIntent().getStringExtra(PAYPAL_CURRENCY_CODE_KEY);
            displayName = getIntent().getStringExtra(PAYPAL_DISPLAY_NAME_KEY);
            amount = getIntent().getDoubleExtra(PAYPAL_AMOUNT_KEY, 0.0);
        } else {
            failed();
        }
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, token);

            PayPalRequest request = new PayPalRequest(String.valueOf(amount))
                    .displayName(displayName)
                    .currencyCode(currencyCode)
                    .intent(PayPalRequest.INTENT_AUTHORIZE);
            PayPal.requestOneTimePayment(mBraintreeFragment, request);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            failed();
        }
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        queueSuccessRequest(paymentMethodNonce.getNonce());
        Intent data = new Intent();
        data.putExtra(PAY_PAL_REQUEST_SUCCESS, true);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onCancel(int requestCode) {
        failed();
    }

    @Override
    public void onError(Exception error) {
        if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;

            try {
                JSONObject objectJSON = new JSONObject(errorWithResponse.getErrorResponse());
                if (objectJSON.getJSONObject("paymentResource") != null && objectJSON.getJSONObject("paymentResource").getString("errorName") != null) {
                    errorCode = "P100";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        error.printStackTrace();
        failed();
    }

    private void queueSuccessRequest(String nonce) {

        Data data = new Data.Builder()
                .putString(PAYPAL_ORDER_ID_KEY, orderId)
                .putString(PAYPAL_NONCE_KEY, nonce)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest
                .Builder(PayPalRequestWorker.class)
                .setConstraints(new Constraints
                        .Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .setInputData(data)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(SharedApplication.context).enqueue(workRequest);
    }

    private void failed() {

        if (orderId == null)
            orderId = "";

        Call<ECommerceResponse> responseCall = new ECommerceRequestHelper().getApiService().failurePayment(orderId);
        responseCall.enqueue(new Callback<ECommerceResponse>() {
            @Override
            public void onResponse(Call<ECommerceResponse> call, Response<ECommerceResponse> response) {

            }

            @Override
            public void onFailure(Call<ECommerceResponse> call, Throwable t) {

            }
        });

        Intent data = new Intent();
        data.putExtra(PAY_PAL_REQUEST_SUCCESS, false);
        data.putExtra(PAY_PAL_REQUEST_FAILED_STATUS_CODE, errorCode);
        setResult(RESULT_OK, data);
        finish();
    }

}
