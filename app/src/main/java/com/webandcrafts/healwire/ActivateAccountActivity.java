package com.webandcrafts.healwire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ActivateAccountActivity extends AppCompatActivity {

    EditText editTextSecurityCode;
    Button buttonSubmit;
    String securitycode,email;
    ProgressDialog progressDialog;
    String activationStatus,responseMessage;






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

            // getSupportActionBar().hide();


        }
        setContentView(R.layout.activity_activate_account);

        editTextSecurityCode = (EditText) findViewById(R.id.editTextSecurityCode);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        progressDialog = new ProgressDialog(this);



        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                securitycode = editTextSecurityCode.getText().toString();

                activateUser(AppController.userEmail,securitycode);



            }
        });

    }

    // To activate the user

    public void activateUser(final String email,final String password){



        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest loginUserRequest = new StringRequest(Request.Method.POST,AppController.ACTIVATE_ACCOUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try{


                            JSONObject mainResponse = new JSONObject(response);
                            activationStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");




                            if(activationStatus.equals("SUCCESS")){

                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_SHORT).show();


                                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                                sharedPreferenceHandler.setUserEmail(getApplicationContext(),AppController.userEmail);


                                Intent intentAddressDetials = new Intent(getApplicationContext(),AddressDetailsActivity.class);
                                startActivity(intentAddressDetials);
                                ActivateAccountActivity.this.finish();


                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


                            }
                            else{//existing user
                                //Toast.makeText(getActivity(),registrationStatus, Toast.LENGTH_SHORT).show();
//                                new SnackBar(getActivity(),
//                                        "Email already registered").show();

                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();


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

                Toast.makeText(getApplicationContext(),"Failed. Please try again.",Toast.LENGTH_LONG).show();

                Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intentHome);
                ActivateAccountActivity.this.finish();


            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email",email);
                params.put("security_code",securitycode);


                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(loginUserRequest);

        loginUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Usinf Volley




}
