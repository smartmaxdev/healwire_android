package com.webandcrafts.healwire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {

    RelativeLayout relativeLayoutMain;

    TextView textViewName, textViewEmail, textViewPhone, textViewAddress;
    Button buttonEditProfile,buttonLogout;

    String userDetailsStatus,responseMessage;

    String email;
    String firstName,lastName,address,phone,pincode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT<16){
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        else{
//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            View decorView = getWindow().getDecorView();
//            //Hide the status bar
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//            //Remember that you should never show the action bar if the status bar is hidden, so hide that too if necessary..
////            ActionBar ab= getSupportActionBar();
////            ab.hide();
//
//            // getSupportActionBar().hide();
//
//
//        }


        setContentView(R.layout.activity_profile);


        CookieHandler.setDefault(new CookieManager());


        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewPhone = (TextView) findViewById(R.id.textViewPhone);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);

        buttonEditProfile = (Button) findViewById(R.id.buttonEditProfile);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);


        SharedPreferenceHandler spHandler = new SharedPreferenceHandler();
        spHandler.getUserProfile(getApplicationContext());



        //email = AppController.userEmail;

        email = spHandler.getUserEmail(getApplicationContext());


       
        Log.d("RESPONSE-LOG",spHandler.getCookie(getApplicationContext()));


         getUserDetails(email);


        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAddressDetails = new Intent(getApplicationContext(),AddressDetailsActivity.class);
                intentAddressDetails.putExtra("first_name",firstName);
                intentAddressDetails.putExtra("last_name",lastName);
                intentAddressDetails.putExtra("address",address);
                intentAddressDetails.putExtra("pincode",pincode);
                intentAddressDetails.putExtra("phone",phone);
                startActivity(intentAddressDetails);



            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppController.userStatus = "logout";
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                sharedPreferenceHandler.setUserStatus(getApplicationContext(),AppController.userStatus);


              // Toast.makeText(getApplicationContext(),"mail"+SharedPreferenceHandler.EMAIL,Toast.LENGTH_LONG).show();




                Intent intentLogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intentLogin);
                ProfileActivity.this.finish();




            }
        });



    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
//        startActivity(intentHome);
//        ProfileActivity.this.finish();
//
//    }

    // To get the user details

    public void getUserDetails(final String email){


        final StringRequest getUserDetailsRequest = new StringRequest(Request.Method.POST,AppController.GET_USER_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                      //  Toast.makeText(getApplicationContext(),"" + response,Toast.LENGTH_LONG).show();


                        try{
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);

                            userDetailsStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            JSONObject jsonObjectData = mainResponse.getJSONObject("data");


//                            Toast.makeText(getApplicationContext(),"" + userTypeDataStatus,Toast.LENGTH_LONG).show();


                            if(userDetailsStatus.equals("SUCCESS")){

//                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                firstName = jsonObjectData.getString("first_name");
                                lastName = jsonObjectData.getString("last_name");
                                address = jsonObjectData.getString("address");
                                phone = jsonObjectData.getString("phone");
                                pincode = jsonObjectData.getString("pincode");






                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


                            }
                            else{//existing user
                                //Toast.makeText(getActivity(),registrationStatus, Toast.LENGTH_SHORT).show();
//                                new SnackBar(getActivity(),
//                                        "Email already registered").show();

                                //  Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();



                            }

                            // messageHandler.sendEmptyMessage(1);

                            //Set the value from server

                            SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                            sharedPreferenceHandler.setUserProfile(getApplicationContext(),firstName,lastName,address,email,phone,pincode);

                            // Load user details to textViews

                            sharedPreferenceHandler.getUserProfile(getApplicationContext());



                            AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;



                            textViewName.setText(AppController.userFullName);
                            String addressStr = SharedPreferenceHandler.ADDRESS.toString();
                            textViewAddress.setText(addressStr);
                            textViewPhone.setText(SharedPreferenceHandler.PHONE);
                            textViewEmail.setText(SharedPreferenceHandler.EMAIL);





                        }catch(Exception e){
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {





            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();


//                params.put("phone",phone);
                  params.put("email",email);
//                params.put("password",password);
//                params.put("confirm_password",confirmpassword);
//                params.put("user_type",userType);

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map headers = new HashMap();
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                if(!sharedPreferenceHandler.getCookie(getApplicationContext()).equals(""))
                    headers.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));

                Log.d("HEADER_LOG",headers.toString());

               // return super.getHeaders();
                return headers;

            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(getUserDetailsRequest);

        getUserDetailsRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Using Volley




}
