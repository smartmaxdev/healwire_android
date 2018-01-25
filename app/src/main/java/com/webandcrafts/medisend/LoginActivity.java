package com.webandcrafts.medisend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout relativeLayoutMain;
    EditText editTextEmail,editTextPassword;
    Button buttonLogin;
    TextView textViewClickHere,textViewForgotPassword;
    ProgressDialog progressDialog;
    String loginStatus,responseMessage;
    String email,password;
    String userAccountStatus;
    Snackbar snackbar;

    String firstName,lastName,address,pincode,phone,userDetailsStatus;







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

        setContentView(R.layout.activity_login);

        CookieHandler.setDefault(new CookieManager());



        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        textViewClickHere= (TextView) findViewById(R.id.textViewClickHere);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);

//        Snackbar.make(relativeLayoutMain,"Hello World",Snackbar.LENGTH_LONG).show();



        progressDialog = new ProgressDialog(this);





        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             email = editTextEmail.getText().toString();
             password = editTextPassword.getText().toString();


                AppController.userEmail = email;

                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
//                sharedPreferenceHandler.setUserEmail(getApplicationContext(),AppController.userEmail);


//                email = "vineeth@webandcrafts.com";
//                password = "vineethweb";


                loginUser(email,password);



            }
        });


        textViewClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentRegisterActivity =  new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intentRegisterActivity);
                LoginActivity.this.finish();


            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentForgotActivity =  new Intent(getApplicationContext(),ForgotActivity.class);
                startActivity(intentForgotActivity);
                LoginActivity.this.finish();

            }
        });


    }

    public void clearFields(){

        editTextEmail.getText().clear();
        editTextPassword.getText().clear();


    }



    // To login the user

    public void loginUser(final String email,final String password){



        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest loginUserRequest = new StringRequest(Request.Method.POST,AppController.LOGIN_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try{


                            JSONObject mainResponse = new JSONObject(response);
                            loginStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                            userAccountStatus = jsonObjectData.getString("status");

                            JSONObject jsonObjectPrescriptionStatus = jsonObjectData.getJSONObject("pres_status");
                            JSONObject jsonObjectInvoiceStatus = jsonObjectData.getJSONObject("invoice_status");
                            JSONObject jsonObjectPaymentStatus = jsonObjectData.getJSONObject("payment_status");
                            JSONObject jsonObjectShippingStatus = jsonObjectData.getJSONObject("shipping_status");

                            Log.d("RES_PRES",jsonObjectPrescriptionStatus.toString());
                            Log.d("RES_PRES",jsonObjectInvoiceStatus.toString());
                            Log.d("RES_PRES",jsonObjectPaymentStatus.toString());
                            Log.d("RES_PRES",jsonObjectShippingStatus.toString());


                            SharedPreferenceHandler spHandlerStatus = new SharedPreferenceHandler();
                            spHandlerStatus.setPrescriptionStatus(getApplicationContext(),jsonObjectPrescriptionStatus);
                            spHandlerStatus.setInvoiceStatus(getApplicationContext(),jsonObjectInvoiceStatus);
                            spHandlerStatus.setPaymentStatus(getApplicationContext(),jsonObjectPaymentStatus);
                            spHandlerStatus.setShippingStatus(getApplicationContext(),jsonObjectShippingStatus);

                            Log.d("RES_PRES","STATUSES SET");




                            if(loginStatus.equals("SUCCESS")){

                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_SHORT).show();



                                 Snackbar.make(relativeLayoutMain,responseMessage,Snackbar.LENGTH_LONG).show();
                                //nackbar.show();



                                if(userAccountStatus.equals("PENDING")){

                                    // User account is not active ..pending for approval..Goto Activate account screen or dialog

                                    Toast.makeText(getApplicationContext(),"Activation is pending",Toast.LENGTH_SHORT).show();

                                    Intent intentActivateAccount = new Intent(getApplicationContext(),ActivateAccountActivity.class);
                                    startActivity(intentActivateAccount);
                                    LoginActivity.this.finish();





                                }
                                else if(userAccountStatus.equals("ACTIVE")){

                                    // User account is active ..goto Home

                                    AppController.userStatus = "login";
                                    SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                                    sharedPreferenceHandler.setUserStatus(getApplicationContext(),AppController.userStatus);

                                    sharedPreferenceHandler.setUserEmail(getApplicationContext(),AppController.userEmail);

                                    sharedPreferenceHandler.getUserProfile(getApplicationContext());

                                    AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;

                                   // sharedPreferenceHandler.setUserEmail(getApplicationContext(), AppController.userEmail);


                                      getUserDetails(email);


                                    // Load user details to textViews










                                    Toast.makeText(getApplicationContext(),"Account is active",Toast.LENGTH_SHORT).show();

                                     Intent intentUserProfile =  new Intent(getApplicationContext(),HomeActivity.class);
                                     startActivity(intentUserProfile);
                                     LoginActivity.this.finish();



                                }
                                else{

                                }


                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


                            }
                            else if (loginStatus.equals("FAILURE")){//existing user
                                //Toast.makeText(getActivity(),registrationStatus, Toast.LENGTH_SHORT).show();
//                                new SnackBar(getActivity(),
//                                        "Email already registered").show();

                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();


                            }
                            else{

                            }

                            // messageHandler.sendEmptyMessage(1);
                        }catch(Exception e){
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse != null) {


                    if(networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED){
                        // Unauthorised 401

                        responseMessage = "Invalid login details";

                    }
                    else if(networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){

                        // Unauthorised 500

                        responseMessage = "Some techincal failure.";

                    }


                    Snackbar.make(relativeLayoutMain,responseMessage, Snackbar.LENGTH_INDEFINITE)
                            .setAction("DONE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    clearFields();
                                    editTextEmail.setFocusableInTouchMode(true);
                                    editTextEmail.requestFocus();

                                }
                            })
                            .show();

                }




            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email",email);
                params.put("password",password);


                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                //Response header

                Object setCookie;
                Map headers = response.headers;
                setCookie = headers.get("Set-Cookie");
                AppController.cookies = setCookie.toString();


                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                sharedPreferenceHandler.saveCookie(AppController.cookies,getApplicationContext());

//                Toast.makeText(getApplicationContext(),"Saved cookies",Toast.LENGTH_SHORT).show();

              //  Log.d("VINEETH-SESSION",String.valueOf(response));


                return super.parseNetworkResponse(response);

            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(loginUserRequest);

        loginUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Usinf Volley


    //get the user details


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
