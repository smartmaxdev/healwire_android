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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddressDetailsActivity extends AppCompatActivity {

    EditText editTextFirstName,editTextLastName,editTextAddress,editTextPincode;
    Button buttonUpdate;

    String firstName,lastName,address,pincode,phone;

    RelativeLayout relativeLayoutMain;


    String updateProfileStatus,responseMessage;

    ProgressDialog progressDialog;




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

        setContentView(R.layout.activity_address_details);

        progressDialog = new ProgressDialog(this);
        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);


        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPincode = (EditText) findViewById(R.id.editTextPincode);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        firstName = getIntent().getStringExtra("first_name");
        lastName = getIntent().getStringExtra("last_name");
        address = getIntent().getStringExtra("address");
        pincode = getIntent().getStringExtra("pincode");
        phone = getIntent().getStringExtra("phone");


        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextAddress.setText(address);
        editTextPincode.setText(pincode);



//        Toast.makeText(getApplicationContext(),""+AppController.userEmail,Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),""+SharedPreferenceHandler.PHONE,Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),""+AppController.INT_USER_TYPE,Toast.LENGTH_LONG).show();


        // Listener for button Update Profile

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = editTextFirstName.getText().toString();
                lastName = editTextLastName.getText().toString();
                address = editTextAddress.getText().toString();
                pincode = editTextPincode.getText().toString();
                phone = SharedPreferenceHandler.PHONE;


                updateUserProfile(firstName,lastName,address,pincode,phone);


            }
        });



        

    }



    // To update the user profile

    public void updateUserProfile(final String firstName,final String lastName,final String address,final String pincode,final String phone){



        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StringRequest updateUserProfileRequest = new StringRequest(Request.Method.POST,AppController.UPDATE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        clearFields();



                        try{
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);
                            updateProfileStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            //  Toast.makeText(getApplicationContext(),"" + registrationStatus,Toast.LENGTH_LONG).show();


                            if(updateProfileStatus.equals("SUCCESS")){

                                // Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_INDEFINITE)
                                        .setAction("DONE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent intentLogin = new Intent(getApplicationContext(),HomeActivity.class);
                                                startActivity(intentLogin);

//                                                Intent intentProfile = new Intent(getApplicationContext(),ProfileActivity.class);
//                                                startActivity(intentProfile);



                                            }
                                        })
                                        .show();


                                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                                sharedPreferenceHandler.setUserProfile(getApplicationContext(),firstName,lastName,address,AppController.userEmail,phone,pincode);




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
                        }catch(Exception e){
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                progressDialog.dismiss();
//
//                Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
//                startActivity(intentHome);




            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                Log.d("first_name",firstName);
                Log.d("last_name",lastName);
                Log.d("address",address);
                Log.d("pincode",pincode);
                Log.d("phone",phone);
               // Log.d("user_type",AppController.userType);
                Log.d("email", AppController.userEmail);


                params.put("first_name",firstName);
                params.put("last_name",lastName);
                params.put("address",address);
                params.put("pincode",pincode);
                params.put("phone",phone);
                params.put("user_type",String.valueOf(2));
                params.put("email", AppController.userEmail);

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
        AppController.getInstance().addToRequestQueue(updateUserProfileRequest);

        updateUserProfileRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Using Volley

    public void clearFields(){

        editTextFirstName.getText().clear();
        editTextLastName.getText().clear();
        editTextAddress.getText().clear();
        editTextPincode.getText().clear();



    }








}
