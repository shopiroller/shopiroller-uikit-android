package com.shopiroller.workers;

import static com.shopiroller.activities.PayPalActivity.PAYPAL_NONCE_KEY;
import static com.shopiroller.activities.PayPalActivity.PAYPAL_ORDER_ID_KEY;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.shopiroller.constants.Constants;
import com.shopiroller.models.CompleteOrder;
import com.shopiroller.models.ECommerceResponse;
import com.shopiroller.models.OrderResponseInner;
import com.shopiroller.network.ECommerceRequestHelper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


public class PayPalRequestWorker extends Worker {

    public PayPalRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String orderId = getInputData().getString(PAYPAL_ORDER_ID_KEY);
        String nonce = getInputData().getString(PAYPAL_NONCE_KEY);

        CompleteOrder completeOrder = new CompleteOrder();
        completeOrder.orderId = orderId;
        completeOrder.nonce = nonce;
        completeOrder.paymentType = Constants.PAYPAL;

        Call<ECommerceResponse<OrderResponseInner>> responseCall = new ECommerceRequestHelper().getApiService().tryAgain(completeOrder);
        try {
            Response<ECommerceResponse<OrderResponseInner>> response = responseCall.execute();
            if(response.isSuccessful())
            {
                return Result.success();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.failure();
    }

}
