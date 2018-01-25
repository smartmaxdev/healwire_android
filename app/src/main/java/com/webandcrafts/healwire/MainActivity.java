package com.webandcrafts.healwire;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


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

            getSupportActionBar().hide();


        }

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);




//            ActionBar actionBar = getSupportActionBar();
//            actionBar.setDisplayHomeAsUpEnabled(true);


        initNavigationDrawer();

    }


    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.profile:

                        Intent intentProfile =  new Intent(getApplicationContext(),ProfileActivity.class);
                        startActivity(intentProfile);

                        break;
                    case R.id.myprescription:

                        Intent intentPrescription=  new Intent(getApplicationContext(),MyPrescriptionActivity.class);
                        startActivity(intentPrescription);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myorders:

                        Intent intentOrders=  new Intent(getApplicationContext(),MyOrdersActivity.class);
                        startActivity(intentOrders);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.itemsincart:

                        Intent intentItemsInCart=  new Intent(getApplicationContext(),MyCartActivity.class);
                        startActivity(intentItemsInCart);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about:

                        Intent intentAbout=  new Intent(getApplicationContext(),AboutusActivity.class);
                        startActivity(intentAbout);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        finish();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
      //  TextView tv_email = (TextView)header.findViewById(R.id.textViewName);
        //tv_email.setText("raj.amalw@learn2crack.com");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }






}
