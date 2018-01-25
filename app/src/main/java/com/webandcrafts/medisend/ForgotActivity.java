package com.webandcrafts.medisend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ForgotActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button buttonSubmit;

    String email;

    String forgotStatus,responseMessage;

    ProgressDialog progressDialog;



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

        setContentView(R.layout.activity_forgot);


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        progressDialog = new ProgressDialog(this);



        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editTextEmail.getText().toString();
                AppController.userEmail = email;

               // email = "vineeth@webandcrafts.com";
                forgotPassword(email);



            }
        });

    }



    // To login the user

    public void forgotPassword(final String email){


        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest forgotPasswordRequest = new StringRequest(Request.Method.POST,AppController.RESET_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();


                        try{


                            JSONObject mainResponse = new JSONObject(response);
                            forgotStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            //JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                           // userAccountStatus = jsonObjectData.getString("status");



                            if(forgotStatus.equals("SUCCESS")){


                                  Intent intentResetPassword = new Intent(getApplicationContext(),ResetPasswordActivity.class);
                                  startActivity(intentResetPassword);
                                  ForgotActivity.this.finish();


                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


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

            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email",email);



                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(forgotPasswordRequest);

        forgotPasswordRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Using Volley





}
