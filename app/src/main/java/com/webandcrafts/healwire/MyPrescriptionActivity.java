package com.webandcrafts.healwire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPrescriptionActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    ProgressDialog progressDialog;
    String prescriptionStatus,responseMessage,email;

    TextView textViewEmpty;

    SharedPreferenceHandler sharedPreferenceHandlerStatus;

    private List<Prescription> prescriptionList = new ArrayList<Prescription>();
    private ListView listViewPrescription;
    private MyPrescriptionListAdapter listPrescriptionAdapter;


//    public static List<JSONObject> paidPrescription = new ArrayList<JSONObject>();
//    public static List<JSONObject> unpaidPrescription = new ArrayList<JSONObject>();


//    JSONObject jsonObjectPrescription;



//    ArrayList<HashMap<String,String>> prescriptionList;
    //private SimpleAdapter listPrescriptionAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
        email = sharedPreferenceHandler.getUserEmail(getApplicationContext());
//        Toast.makeText(getApplicationContext(),"Current="+email,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"USER="+AppController.userEmail,Toast.LENGTH_SHORT).show();




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

        setContentView(R.layout.activity_my_prescription);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        textViewEmpty = (TextView) findViewById(android.R.id.empty);




        listViewPrescription = (ListView) findViewById(R.id.listViewPrescription);
        listPrescriptionAdapter = new MyPrescriptionListAdapter(this, prescriptionList);
        listViewPrescription.setAdapter(listPrescriptionAdapter);

        progressDialog = new ProgressDialog(this);

       // Toast.makeText(getApplicationContext(),"mail:"+email,Toast.LENGTH_LONG).show();


       // getPrescriptionThumbComplete(email);


        getPrescriptionThumb(email);




        listViewPrescription.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                AppController.invoice_Id_position = i;




                // getting movie data for the row
                Prescription prescription = prescriptionList.get(AppController.invoice_Id_position);


                Intent intentPrescriptionDetails = new Intent(MyPrescriptionActivity.this,PrescriptionDetailsActivity.class);
                startActivity(intentPrescriptionDetails);



//                SharedPreferenceHandler sp = new SharedPreferenceHandler();
//                sp.getPrescriptionStatus(getApplicationContext());
//                sp.getInvoiceStatus(getApplicationContext());
//
//
//                if(SharedPreferenceHandler.VERIFIED == prescription.getStatusId()){
//
//
//
//                    Intent intentPrescriptionDetails = new Intent(MyPrescriptionActivity.this,PrescriptionDetailsActivity.class);
//                    startActivity(intentPrescriptionDetails);
//
//                }
//                else if(SharedPreferenceHandler.UNVERIFIED == prescription.getStatusId()){
//
//                    Intent intentPrescriptionDetails = new Intent(MyPrescriptionActivity.this,PrescriptionDetailsActivity.class);
//                    startActivity(intentPrescriptionDetails);
//
//
//
//
//                }
//                else{
//
//                }

//                Toast.makeText(getApplicationContext(),""+SharedPreferenceHandler.VERIFIED,Toast.LENGTH_LONG).show();
//
//
//
//
//                Toast.makeText(getApplicationContext(),prescription.getStatus(),Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),""+prescription.getStatusId(),Toast.LENGTH_LONG).show();




            }
        });



    }

    @Override
    protected void onRestart() {
        super.onRestart();

       // Toast.makeText(getApplicationContext(),"restart",Toast.LENGTH_SHORT).show();




    }

    @Override
    protected void onResume() {
        super.onResume();
      //  Toast.makeText(getApplicationContext(),"resume",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"pause",Toast.LENGTH_SHORT).show();







    }



    //To load detials of My Prescription of a user


    public synchronized void getPrescriptionThumb(final String email){

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        StringRequest doLogin = new StringRequest(Request.Method.POST,AppController.GET_PRESCRIPTION_THUMB_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();


                        try{




                            JSONObject mainResponse = new JSONObject(response);



                            prescriptionStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            if(prescriptionStatus.equals("SUCCESS")){

                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");


                                if(HomeActivity.unpaidPrescription.size() == 0){

                                    textViewEmpty.setVisibility(View.VISIBLE);
                                }
                                else{

                                    textViewEmpty.setVisibility(View.GONE);

                                }

                                for(int i = 0;i<HomeActivity.unpaidPrescription.size();i++){




                                    //-----------------------------------------------------------------------------------------------


                                    String create_on = HomeActivity.unpaidPrescription.get(i).getString("created_on");

                                    Log.d("Dates",String.valueOf(create_on));

                                    String pres_status = HomeActivity.unpaidPrescription.get(i).getString("pres_status");
                                    String path =  HomeActivity.unpaidPrescription.get(i).getString("path");
                                    int invoice_id =  HomeActivity.unpaidPrescription.get(i).getInt("invoice_id");
                                    int pres_status_id =  HomeActivity.unpaidPrescription.get(i).getInt("pres_status_id");

                                    int invoice_status_id = HomeActivity.unpaidPrescription.get(i).getInt("invoice_status_id");

                                    String invoice_status = HomeActivity.unpaidPrescription.get(i).getString("invoice_status");



                                    //------------------------------------------------------------------------------------------



//                                    String create_on = jsonArrayPrescriptions.getJSONObject(i).getString("created_on");
//                                    String pres_status = jsonArrayPrescriptions.getJSONObject(i).getString("pres_status");
//                                    String path = jsonArrayPrescriptions.getJSONObject(i).getString("path");
//                                    int invoice_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_id");
//                                    int pres_status_id = jsonArrayPrescriptions.getJSONObject(i).getInt("pres_status_id");
//
//                                    int invoice_status_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_status_id");
//
//                                    String invoice_status = jsonArrayPrescriptions.getJSONObject(i).getString("invoice_status");




                                    Prescription prescription = new Prescription();


                                    prescription.setDate(create_on);
                                    prescription.setStatus(pres_status);
                                    prescription.setStatusId(pres_status_id);
                                    prescription.setImageUrl(path);
                                    prescription.setInvoice_Id(invoice_id);

                                    prescriptionList.add(prescription);


                                    AppController.invoiceIdList.add(invoice_id);

                                    Log.d("INVOICE_IDS",String.valueOf(AppController.invoiceIdList.get(i)));

//                                    if(invoice_status_id == 1 || invoice_status_id == 3 || invoice_status_id == 4){
//
//                                        prescriptionList.add(prescription);
//
//
//
//                                    }









                                }



                                listPrescriptionAdapter.notifyDataSetChanged();
                            }
                        }catch(Exception e){
                            // messageHandler.sendEmptyMessage(99);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  messageHandler.sendEmptyMessage(98);

                Log.d("EMPTY","NO DATA FROM SERVER");

                progressDialog.dismiss();
                textViewEmpty.setVisibility(View.VISIBLE);




            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email",email);

//                Toast.makeText(getApplicationContext(),"mail:"+email,Toast.LENGTH_LONG).show();




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
        AppController.getInstance().addToRequestQueue(doLogin);
//        doLogin.setRetryPolicy(new DefaultRetryPolicy(
//                (int) TimeUnit.SECONDS.toMillis(20),
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }//volley






//    //To load detials of My Prescription of a user
//
//
//    public synchronized void getPrescriptionThumbComplete(final String email){
//
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//
//
//        StringRequest doLogin = new StringRequest(Request.Method.POST,AppController.GET_PRESCRIPTION_THUMB_URL,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//
////                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
//
//                        progressDialog.dismiss();
//
//
//                        try{
//
//                            JSONObject mainResponse = new JSONObject(response);
//
//
//
//                            prescriptionStatus = mainResponse.getString("status");
//                            responseMessage = mainResponse.getString("msg");
//
//                            if(prescriptionStatus.equals("SUCCESS")){
//
//                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
//                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");
//
//
//                                //Toast.makeText(getApplicationContext(),"Array : "+jsonArrayPrescriptions.length(),Toast.LENGTH_LONG).show();
//
//
//                                for(int i = 0;i<jsonArrayPrescriptions.length();i++){
//
//
//
//
//                                    int invoice_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_id");
//
//
//                                    int invoice_status_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_status_id");
//
//                                    jsonObjectPrescription = jsonArrayPrescriptions.getJSONObject(i);
//
//
//
//
//                                    if(invoice_status_id == 1 || invoice_status_id == 3 || invoice_status_id == 4){
//
//
//                                       unpaidPrescription.add(jsonObjectPrescription);
//
//
//                                    }
//                                    else{
//
//                                       paidPrescription.add(jsonObjectPrescription);
//
//                                    }
//
//
//
//
//                                }
//
//
//                                Toast.makeText(getApplicationContext(),"UNPAID Array : "+unpaidPrescription.size(),Toast.LENGTH_LONG).show();
//                                Toast.makeText(getApplicationContext(),"PAID Array : "+paidPrescription.size(),Toast.LENGTH_LONG).show();
//
//
//                                Log.d("UNPAID JSONOBJECT",String.valueOf(unpaidPrescription));
//                                Log.d("PAID JSONOBJECT",String.valueOf(paidPrescription));
//
//
//
//
//
//
//
//                                //listPrescriptionAdapter.notifyDataSetChanged();
//                            }
//                        }catch(Exception e){
//                            // messageHandler.sendEmptyMessage(99);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //  messageHandler.sendEmptyMessage(98);
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//
//                params.put("email",email);
//
//
//
//                return params;
//            }
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                Map headers = new HashMap();
//                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
//
//                if(!sharedPreferenceHandler.getCookie(getApplicationContext()).equals(""))
//                    headers.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));
//
//                Log.d("HEADER_LOG",headers.toString());
//
//                // return super.getHeaders();
//                return headers;
//
//            }
//
//
//
//        };
//        AppController.getInstance().addToRequestQueue(doLogin);
////        doLogin.setRetryPolicy(new DefaultRetryPolicy(
////                (int) TimeUnit.SECONDS.toMillis(20),
////                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//    }//volley











}
