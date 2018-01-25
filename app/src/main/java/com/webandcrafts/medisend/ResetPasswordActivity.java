package com.webandcrafts.medisend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class ResetPasswordActivity extends AppCompatActivity {

    RelativeLayout relativeLayoutMain;


    EditText editTextSecurityCode,editTextPassword,editTextConfirmPassword;
    Button buttonSubmit;
    ProgressDialog progressDialog;

    String resetStatus,responseMessage;

    String email,securityCode,newPassword,confirmPassword;





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


        setContentView(R.layout.activity_reset_password);

        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);


        progressDialog = new ProgressDialog(this);

        editTextSecurityCode = (EditText) findViewById(R.id.editTextSecurityCode);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = AppController.userEmail;
                securityCode = editTextSecurityCode.getText().toString();
                newPassword = editTextPassword.getText().toString();
                confirmPassword = editTextConfirmPassword.getText().toString();

                resetPassword(email,securityCode,newPassword,confirmPassword);

                buttonSubmit.setEnabled(false);







            }
        });



    }

    // To reset or change the paasword


    public void resetPassword(final String email, final String securityCode, final String newPassword, final String confirmPassword){


        progressDialog.setMessage("Changing password...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest resetPasswordRequest = new StringRequest(Request.Method.POST,AppController.RESET_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                        try{


                            JSONObject mainResponse = new JSONObject(response);
                            resetStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                           // JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                            // userAccountStatus = jsonObjectData.getString("status");






                            if(resetStatus.equals("SUCCESS")){


                               // Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_SHORT).show();

                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_INDEFINITE)
                                        .setAction("DONE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                buttonSubmit.setEnabled(true);


                                                Intent intentLogin= new Intent(getApplicationContext(),LoginActivity.class);
                                                startActivity(intentLogin);
                                                ResetPasswordActivity.this.finish();



                                            }
                                        })
                                        .show();



//                                Intent intentLogin= new Intent(getApplicationContext(),LoginActivity.class);
//                                startActivity(intentLogin);


                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


                            }
                            else if (resetStatus.equals("FAILURE")){//existing user
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

            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email",email);
                params.put("security_code",securityCode);
                params.put("new_password",newPassword);
                params.put("confirm_password",confirmPassword);



                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(resetPasswordRequest);

        resetPasswordRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Using Volley




}
