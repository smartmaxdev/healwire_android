package com.webandcrafts.medisend;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public class WebViewPaymentActivity extends AppCompatActivity {

    boolean loadingFinished = true;
    boolean redirect = false;





    SharedPreferenceHandler sharedPreferenceHandler;

    WebView webViewPayment;
    ProgressDialog progressDialog;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_payment);

        sharedPreferenceHandler = new SharedPreferenceHandler();



        webViewPayment = (WebView) findViewById(R.id.webViewPayment);

       // Toast.makeText(getApplicationContext(),""+AppController.INVOICE_ID,Toast.LENGTH_SHORT).show();



        webViewPayment.setWebViewClient(new MyWebViewClient());

        webViewPayment.getSettings().setJavaScriptEnabled(true);
        webViewPayment.getSettings().setSupportZoom(true);
        webViewPayment.getSettings().setBuiltInZoomControls(true);
        webViewPayment.getSettings().setDomStorageEnabled(true);

        Map<String,String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));

       // Toast.makeText(getApplicationContext(),sharedPreferenceHandler.getCookie(getApplicationContext()),Toast.LENGTH_LONG).show();


       // webViewPayment.setWebViewClient(webViewClient);
        webViewPayment.loadUrl(AppController.BASE_PAYMENT_URL + "/"+String.valueOf(AppController.INVOICE_ID)+"/1",extraHeaders);




    }

    public class  MyWebViewClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            Log.d("RES_STARTED","SITE STARTED = " + url );

//            if(AppController.PAYMENT_SUCCESS_URL.equals(url)){
//                // Payment was success
//
//                Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
//                startActivity(intentHome);
//                WebViewPaymentActivity.this.finish();
//
//
//            }
//            else{
//
//
//            }

          //  Toast.makeText(getApplicationContext(), ""+url, Toast.LENGTH_SHORT).show();



        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.d("RES_LOADED","SITE FINISHED = " + url);

            if(AppController.PAYMENT_SUCCESS_URL.equals(url)){
                // Payment was success

//                PaymentResponseActivity.buttonComplete.setText("Go Home");
//                PaymentResponseActivity.textViewMessage.setText("Congratulations ! Your payment was successful.");

                 AppController.PAYMENT_STATUS = "success";


                Intent intentHome = new Intent(getApplicationContext(),PaymentResponseActivity.class);
                startActivity(intentHome);
                WebViewPaymentActivity.this.finish();


            }
            else if(AppController.PAYMENT_FAILED_URL.equals(url)){

                // Payment was failure

//                PaymentResponseActivity.buttonComplete.setText("Try again");
//                PaymentResponseActivity.textViewMessage.setText("Your payment was failed.Please try again later.");

                AppController.PAYMENT_STATUS = "failure";


                Intent intentHome = new Intent(getApplicationContext(),PaymentResponseActivity.class);
                startActivity(intentHome);
                WebViewPaymentActivity.this.finish();

            }



           // Toast.makeText(getApplicationContext(), ""+url, Toast.LENGTH_SHORT).show();




        }
    }









}
