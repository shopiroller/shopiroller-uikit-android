package com.shopiroller.network

import android.os.Build
import android.util.Log
import android.widget.Toast
import com.shopiroller.Shopiroller.adapter
import com.shopiroller.models.ECommerceResponse
import com.google.gson.Gson
import com.shopiroller.SharedApplication
import com.shopiroller.models.ECommerceErrorResponse
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import com.google.gson.GsonBuilder
import com.shopiroller.R
import com.shopiroller.constants.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by OKN on 22.02.2017.
 */
class ECommerceRequestHelper {
    val apiService: ECommerceServiceInterface
    fun <T> enqueue(call: Call<ECommerceResponse<T>?>, callBack: ECommerceCallBack<T>) {
        call.enqueue(object : Callback<ECommerceResponse<T>?> {
            override fun onResponse(
                call: Call<ECommerceResponse<T>?>,
                response: Response<ECommerceResponse<T>?>
            ) {
                if (response.isSuccessful) {
                    callBack.done()
                    Log.e("onResponse", "onSuccess")
                    callBack.onSuccess(response.body()!!.data)
                } else {
                    Log.e("onResponse", "failed")
                    callBack.done()
                    if (response.code() != 502 && response.errorBody() != null) {
                        val errorBody = Gson().fromJson(
                            response.errorBody()!!.charStream(), ECommerceResponse::class.java
                        )
                        if (errorBody != null) callBack.onFailure(errorBody) else Toast.makeText(
                            SharedApplication.context, SharedApplication.context.getString(
                                R.string.common_error
                            ), Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            SharedApplication.context, SharedApplication.context.getString(
                                R.string.common_error
                            ), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ECommerceResponse<T>?>, t: Throwable) {
                Log.e("onResponse", "onFailure " + t.localizedMessage)
                callBack.done()
                Toast.makeText(
                    SharedApplication.context,
                    SharedApplication.context.getString(R.string.please_check_your_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
                callBack.onNetworkError(SharedApplication.context.getString(R.string.please_check_your_internet_connection))
            }
        })
    }

    interface ECommerceCallBack<T> {
        fun done()
        fun onSuccess(result: T)
        fun onFailure(result: ECommerceErrorResponse?)
        fun onNetworkError(result: String?)
    }

    companion object {
        /**
         * Request meta tags
         */
        private const val REQUEST_META_APP_VERSION = "app_version"
        private const val REQUEST_META_OS_TYPE = "os_type"
        private const val REQUEST_META_OS_VERSION = "os_version"
        private const val REQUEST_META_LANGUAGE = "language"
        private const val REQUEST_FALLBACK_LANGUAGE = "X-Fallback-Language"
        private const val REQUEST_HEADER_LANGUAGE = "Accept-Language"
        private const val REQUEST_CLIENT_ECOMMERCE_VERSION = "ECommerce-Client-Version"

        /**
         * Client request timeout values
         */
        private const val REQ_CONNECTION_TIMEOUT = 15
        private const val REQ_WRITE_TIMEOUT = 15
        private const val REQ_READ_TIMEOUT = 30

        /**
         * Meta request values
         */
        private const val META_VAL_OS_TYPE = "1"
        private const val CLIENT_ECOMMERCE_VERSION = "2"
    }

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val clientBuilder = OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .connectTimeout(REQ_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQ_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQ_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        val aliasKey = adapter?.getAliasKey()
        val finalHeaderLang = adapter?.getLocale()?.toUpperCase(Locale.ENGLISH) + "-" + adapter?.getLocale()?.toLowerCase(Locale.ENGLISH)
        clientBuilder.addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "Content-Type",
                    "application/json"
                ) //todo sr check appVersionName is fine
                .addHeader(REQUEST_META_APP_VERSION, "1.0")
                .addHeader(REQUEST_META_OS_TYPE, META_VAL_OS_TYPE)
                .addHeader(REQUEST_META_OS_VERSION, Build.VERSION.RELEASE)
                .addHeader(
                    REQUEST_FALLBACK_LANGUAGE, adapter?.getDefaultLang()?.toLowerCase(Locale.ENGLISH)
                )
                .addHeader(
                    REQUEST_META_LANGUAGE, adapter?.getLocale()?.toUpperCase(Locale.ENGLISH)
                )
                .addHeader(REQUEST_HEADER_LANGUAGE, finalHeaderLang)
                .addHeader(REQUEST_CLIENT_ECOMMERCE_VERSION, CLIENT_ECOMMERCE_VERSION)
                .addHeader("api-Key", adapter?.getApiKey())
            if (aliasKey != null) {
                request.addHeader("alias-key", aliasKey)
            }
            chain.proceed(request.build())
        }
        val client = clientBuilder.build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        var retrofit: Retrofit? = null
        retrofit = if (aliasKey != null && !aliasKey.equals("", ignoreCase = true)) {
            Retrofit.Builder()
                .baseUrl(Constants.Applyze_ECommerce_BaseURL_Shopiroller)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        } else {
            Retrofit.Builder()
                .baseUrl(Constants.Applyze_ECommerce_BaseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        apiService = retrofit.create(
            ECommerceServiceInterface::class.java
        )
    }
}