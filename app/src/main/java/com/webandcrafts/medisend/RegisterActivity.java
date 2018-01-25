package com.webandcrafts.medisend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

     RelativeLayout relativeLayoutMain;
     EditText editTextEmail,editTextPhone,editTextPassword;
     Spinner spinnerUserType;
     Button buttonRegister;
     ProgressDialog progressDialog;
     String registrationStatus,responseMessage,userTypeDataStatus,validateEmailStatus;
     String email,password,confirmPassword,phone,userType;

      List listUserType = new ArrayList();
      List listUserTypeId = new ArrayList();

      ArrayAdapter arrayAdapterUserType;




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

        setContentView(R.layout.activity_register);


        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);

        progressDialog = new ProgressDialog(this);

//        listUserType.add("CUSTOMER");
//        listUserType.add("MEDICAL PRESCRIPTIONER");

        getUserType();


       // arrayAdapterUserType = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listUserType);
        arrayAdapterUserType = new ArrayAdapter(this,R.layout.custom_spinner,listUserType);
        //arrayAdapterUserType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterUserType.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
//        spinnerUserType.setAdapter(arrayAdapterUserType);






         buttonRegister.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 email = editTextEmail.getText().toString();
                 password = editTextPassword.getText().toString();
                 confirmPassword = password;
                 phone = editTextPhone.getText().toString();
                // userType = spinnerUserType.getSelectedItem().toString();
                 //userType = "2";

                   // inputValidData();

                // validateUserEmail( );




//                 email = "cde@gmail.com";
//                 password = "ram123456";
//                 confirmPassword = "ram123456";
//                 phone = "9635256544";
//                 userType = "2";

                // registerUser(email,phone,password,confirmPassword,userType);

                 registerUser(phone,email,password,confirmPassword,userType);

                 buttonRegister.setEnabled(false);




//                 if(editTextEmail.length()<1 & editTextPhone.length()<1  ){
////                     new SnackBar(getActivity(),
////                             " Please fill the mandatory").show();
//
//                     Toast.makeText(getApplicationContext(),"Please fill the mandatory",Toast.LENGTH_LONG).show();
//
//                 }
//                 else if(validateEmail(editTextEmail.getText().toString())){
//
//                     if(editTextPassword.length()<1 ){
//                         //Toast.makeText(getActivity(), "Password length must be greater than 8", Toast.LENGTH_SHORT).show();
//
////                         new SnackBar(getActivity(),
////                                 " Password length must be greater than 4").show();
//
//                         Toast.makeText(getApplicationContext(),"Password length must be greater than 4",Toast.LENGTH_LONG).show();
//
//
//                     }
//                     else if(editTextPhone.length()<10 || editTextPhone.length()>12)
////                         new SnackBar(getActivity(),
////                                 "Provide valid Mobile Number").show();
//                     Toast.makeText(getApplicationContext(),"Provide valid Mobile Number",Toast.LENGTH_LONG).show();
//
//                     else{
//
//
//                         email = editTextEmail.getText().toString();
//                         password = editTextPassword.getText().toString();
//                         phone = editTextPhone.getText().toString();
//                         userType = spinnerUserType.getSelectedItem().toString();
//
//                         registerUser(phone,email,password,password,userType);
//
////                         doVolley(phone.getText().toString().trim(),username.getText().toString().trim(),password.getText().toString().trim(),password.getText().toString().trim(),
////                                 spinner.getSelectedItem().toString());
//
//
//                     }
//
//                 }else
////                     new SnackBar(getActivity(),
////                             "Provide valid email").show();
//                     Toast.makeText(getApplicationContext(),"Provide valid email",Toast.LENGTH_LONG).show();
//
//
//
             }


         });


          editTextPassword.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                  //Validate the email field






              }

              @Override
              public void afterTextChanged(Editable editable) {

              }
          });

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                userType = listUserTypeId.get(i).toString();

                if(userType.equals("MEDICAL_PROFESSIONAL")){

                  AppController.INT_USER_TYPE = 2;

                }
                else if(userType.equals("CUSTOMER")){

                    AppController.INT_USER_TYPE = 3;
                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }



    public void inputValidData(){

        if(editTextEmail.length()<1){

          Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();



        }


    }

    // To Register the user

    public void registerUser(final String phone,final String email,final String password,final String confirmpassword,final String userType){



        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StringRequest createUserRequest = new StringRequest(Request.Method.POST,AppController.CREATE_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        clearFields();



                        try{
                           // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);
                            registrationStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                          //  Toast.makeText(getApplicationContext(),"" + registrationStatus,Toast.LENGTH_LONG).show();


                            if(registrationStatus.equals("SUCCESS")){

                               // Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();


                                AppController.userEmail = email;
                                AppController.userPassword = password;
                                AppController.userPhone = phone;
                                AppController.userType = userType;

                                buttonRegister.setEnabled(true);

                                Intent intentActivateAccount = new Intent(getApplicationContext(),ActivateAccountActivity.class);
                                startActivity(intentActivateAccount);




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

                progressDialog.dismiss();

                NetworkResponse networkResponse = error.networkResponse;




                if(networkResponse.statusCode == HttpStatus.SC_CONFLICT){
                    responseMessage = "Account already exists";

                }
                else if(networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST){
                    responseMessage = "Empty Parameters or Invalid Email Format";

                }
                else if(networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    responseMessage = "Some techincal failure.";

                }

                Snackbar.make(relativeLayoutMain,responseMessage, Snackbar.LENGTH_INDEFINITE)
                        .setAction("DONE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intentHome= new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intentHome);
                                RegisterActivity.this.finish();

                            }
                        })
                        .show();







            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("phone",phone);
                params.put("email",email);
                params.put("password",password);
                params.put("confirm_password",confirmpassword);
                params.put("user_type",userType);

                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(createUserRequest);

        createUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Using Volley



    // To Validate the email field

    public void validateUserEmail(){



        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StringRequest validateEmailRequest = new StringRequest(Request.Method.GET,"http://192.168.1.216/p/product/Medisend/user/check-user-name?u_name=vineeth@webandcrafts.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();




                        try{
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);
                            validateEmailStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            //  Toast.makeText(getApplicationContext(),"" + registrationStatus,Toast.LENGTH_LONG).show();






                            if(validateEmailStatus.equals("SUCCESS")){

                               Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                // Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();




                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


                            }
                            else{//existing user
                                //Toast.makeText(getActivity(),registrationStatus, Toast.LENGTH_SHORT).show();
//                                new SnackBar(getActivity(),
//                                        "Email already registered").show();

                                     Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                //Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();



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

                params.put("u_name",email);

                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(validateEmailRequest);

        validateEmailRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Using Volley




    // To Register the user

    public void getUserType(){





        final StringRequest getUserTypeRequest = new StringRequest(Request.Method.GET,AppController.GET_USER_TYPE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try{
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);

                            userTypeDataStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            JSONArray jsonArrayData = mainResponse.getJSONArray("data");

//                            Toast.makeText(getApplicationContext(),"" + userTypeDataStatus,Toast.LENGTH_LONG).show();


                            if(userTypeDataStatus.equals("SUCCESS")){

//                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();


                                for(int i = 0; i< jsonArrayData.length(); i++){

                                    JSONObject jsonObjectInner =  jsonArrayData.getJSONObject(i);

                                    String userType = jsonObjectInner.getString("type");
                                    int userTypeId = jsonObjectInner.getInt("id");




                                    listUserType.add(userType);
                                    listUserTypeId.add(userTypeId);



                                }



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


                            // Load user type to spinner

                            spinnerUserType.setAdapter(arrayAdapterUserType);



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

//                params.put("phone",phone);
//                params.put("email",email);
//                params.put("password",password);
//                params.put("confirm_password",confirmpassword);
//                params.put("user_type",userType);

                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(getUserTypeRequest);

        getUserTypeRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    } //Using Volley





    //Validate email


    public boolean validateEmail(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())

            return true;
        else

            return false;
    }

    public void clearFields(){

        editTextEmail.getText().clear();
        editTextPhone.getText().clear();
        editTextPassword.getText().clear();


    }






}
