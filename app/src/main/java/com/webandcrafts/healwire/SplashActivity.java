package com.webandcrafts.healwire;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT<16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            //Hide the status bar
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            //Remember that you should never show the action bar if the status bar is hidden, so hide that too if necessary..
//            ActionBar ab= getSupportActionBar();
//            ab.hide();

           //  getSupportActionBar().hide();


        }
        setContentView(R.layout.activity_splash);




        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                Intent intentHome = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intentHome);

                // close this activity
                finish();

            }
        }, 4000); // wait for 5 seconds






    }


}
