package com.shopiroller.activities;

import static com.shopiroller.constants.Constants.Applyze_Payment_Complete;
import static com.shopiroller.constants.Constants.Applyze_Payment_Complete_1;
import static com.shopiroller.constants.Constants.Applyze_Payment_Complete_2;
import static com.shopiroller.constants.Constants.Applyze_Payment_Failed;
import static com.shopiroller.constants.Constants.Applyze_Payment_Failed_1;
import static com.shopiroller.constants.Constants.Applyze_Payment_Failed_2;
import static com.shopiroller.constants.Constants.ONLINE_PAYMENT_3D_HTML;
import static com.shopiroller.constants.Constants.ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED;
import static com.shopiroller.constants.Constants.ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED_STATUS_CODE;
import static com.shopiroller.constants.Constants.ONLINE_PAYMENT_3D_HTML_REQUEST_SUCCESS;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shopiroller.R;
import com.shopiroller.R2;
import com.shopiroller.helpers.LegacyProgressViewHelper;
import com.shopiroller.helpers.LegacyToolbarHelper;
import com.shopiroller.helpers.NetworkHelper;
import com.shopiroller.util.DialogUtil;
import com.shopiroller.views.legacy.ShopirollerToolbar;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Online3DSecureGateway extends ECommerceBaseActivity {

    @BindView(R2.id.web_view)
    WebView webView;
    @BindView(R2.id.toolbar_top)
    ShopirollerToolbar toolbar;
    private String paymentHtml;
    LegacyToolbarHelper toolbarHelper;
    private LegacyProgressViewHelper legacyProgressViewHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_3d_secure_web_view);
        ButterKnife.bind(this);
        toolbarHelper = new LegacyToolbarHelper();
        legacyProgressViewHelper = new LegacyProgressViewHelper(this);
        toolbarHelper.setToolbar(this, findViewById(R.id.toolbar_top));

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.getNavigationIcon().setAutoMirrored(true);
        }
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbarHelper.setStatusBar(this);
        getSupportActionBar().setTitle(getString(R.string.e_commerce_payment_secure_payment_title));

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);

        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        paymentHtml = getIntent().getStringExtra(ONLINE_PAYMENT_3D_HTML);
        webView.setWebViewClient(new Online3DWebViewClient());
        settings.setDefaultTextEncodingName("utf-8");
        webView.loadDataWithBaseURL(null, paymentHtml, "text/html", "utf-8", null);
    }


    class Online3DWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            DialogUtil.showNoConnectionInfo(Online3DSecureGateway.this, new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Intent data = new Intent();
                    data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED, false);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!NetworkHelper.isConnected(Online3DSecureGateway.this)) {
                DialogUtil.showNoConnectionInfo(Online3DSecureGateway.this, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent data = new Intent();
                        data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED, false);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
            }
            if (url.equalsIgnoreCase(Applyze_Payment_Complete) || url.equalsIgnoreCase(Applyze_Payment_Complete_1)  || url.equalsIgnoreCase(Applyze_Payment_Complete_2)) {
                Intent data = new Intent();
                data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_SUCCESS, true);
                setResult(RESULT_OK, data);
                finish();
            } else if (url.equalsIgnoreCase(Applyze_Payment_Failed) || url.equalsIgnoreCase(Applyze_Payment_Failed_1)  || url.equalsIgnoreCase(Applyze_Payment_Failed_2)) {
                Intent data = new Intent();
                data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED, false);
                setResult(RESULT_OK, data);
                finish();
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
                legacyProgressViewHelper.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("onPageStarted",url);
            if (!isFinishing() && legacyProgressViewHelper != null && !legacyProgressViewHelper.isShowing())
                legacyProgressViewHelper.show();
            if (url.equalsIgnoreCase(Applyze_Payment_Complete) || url.equalsIgnoreCase(Applyze_Payment_Complete_1) || url.contains(Applyze_Payment_Complete_2)) {
                Intent data = new Intent();
                data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_SUCCESS, true);
                setResult(RESULT_OK, data);
                finish();
            } else if (url.equalsIgnoreCase(Applyze_Payment_Failed)  || url.equalsIgnoreCase(Applyze_Payment_Failed_1) || url.contains(Applyze_Payment_Failed_2)) {

                Map<String, String> query_pairs = new HashMap<>();
                try {
                    query_pairs = splitQuery(new URL(url));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String mResult = "";
                if(query_pairs.containsKey("status"))
                    mResult = query_pairs.get("status");


                Intent data = new Intent();
                data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED_STATUS_CODE, mResult);
                data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED, true);
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
            legacyProgressViewHelper.dismiss();
        Intent data = new Intent();
        data.putExtra(ONLINE_PAYMENT_3D_HTML_REQUEST_FAILED, true);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isFinishing() && legacyProgressViewHelper != null && legacyProgressViewHelper.isShowing())
            legacyProgressViewHelper.dismiss();
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        if(query!=null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }
        return query_pairs;
    }
}
