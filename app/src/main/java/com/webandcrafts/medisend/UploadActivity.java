package com.webandcrafts.medisend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UploadActivity extends AppCompatActivity {

    RelativeLayout relativeLayoutCancel, relativeLayoutUpload;
    ProgressDialog progressDialog;
    ImageView imageViewPrescription;
    String prescriptionStatus,responseMessage;
    String email,presstatus,cart_item_name[],cart_item_qty[],cart_item_code[];
    Bitmap pres_image;
    RelativeLayout relativeLayoutMain;

    int cart_length;

    private static int RESULT_LOAD_IMG = 1;
    private int PICK_IMAGE_REQUEST = 1;

    String imgDecodableString;

    static String image_to_MyCart,image_thumb_to_MyCart;



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






        setContentView(R.layout.activity_upload);

        progressDialog = new ProgressDialog(this);

        relativeLayoutCancel = (RelativeLayout) findViewById(R.id.relativeLayoutCancel);
        relativeLayoutUpload = (RelativeLayout) findViewById(R.id.relativeLayoutAboutTitle);
        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);


        imageViewPrescription = (ImageView) findViewById(R.id.imageViewPrescription);

       // relativeLayoutUpload.setEnabled(false);


        email = AppController.userEmail;

//        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 2);
//        onResume();

        if(AppController.DEVICE_CAMERA == 1 ){

           // relativeLayoutUpload.setEnabled(true);

            imageViewPrescription.setImageBitmap(HomeActivity.cameraBitmap);
            pres_image = HomeActivity.cameraBitmap;
            AppController.DEVICE_CAMERA = 0;


        }
        else{

            Intent intent = new Intent();
// Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        }







        // To cancel upload


        relativeLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadActivity.this.finish();



            }
        });

        // To upload prescription


        relativeLayoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            // To upload that image file

                uploadFile(getResizedBitmap(pres_image,1080),getResizedBitmap(pres_image,720));

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            relativeLayoutUpload.setEnabled(true);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                pres_image = bitmap;


                ImageView imageView = (ImageView) findViewById(R.id.imageViewPrescription);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 2) {
//
//                // Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//
//                Uri selectedImage = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getBaseContext().getContentResolver().query(selectedImage,filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//
//                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
//                //thumbnail = Bitmap.createScaledBitmap(thumbnail,1280, 720, true);
//                thumbnail = Bitmap.createBitmap(thumbnail);
//
//                String selectedImagePath;
//                selectedImagePath = getPath(selectedImage);
//
//                pres_image = thumbnail;
//                //imageViewPrescription.setImageBitmap(pres_image);
//                imageViewPrescription.setImageURI(selectedImage);
//
//
//            }
//
//        }else{
//            finish();
//        }
//
//
//    }


    private String getPath(Uri uri) {
        // TODO Auto-generated method stub

        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();

    }


    public void uploadFile(Bitmap bitmap,Bitmap thumb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        ByteArrayOutputStream baos_thumb = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 60, baos_thumb);
        byte[] b_thumb = baos_thumb.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);
        String image_thumb = Base64.encodeToString(b_thumb, Base64.DEFAULT);
        Toast.makeText(getApplicationContext(),"File added with prescription.",Toast.LENGTH_SHORT).show();

        if(AppController.MY_CART_TO_CHOOSE_FILE_PATH.equals("true")){

            // Finish upload activity and proceed with place order.

            UploadActivity.image_to_MyCart = image;
            UploadActivity.image_thumb_to_MyCart = image_thumb;
            UploadActivity.this.finish();



        }
        else{

            uploadPrescription(image,image_thumb);
        }


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }




    public  void uploadPrescription(final String prescription,final String prescription_thumb){
        Context mContext = this;
//        dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.custom_dialog_pres);
//        dialog.show();

        progressDialog.setMessage("Uploading Prescription...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest uploadPrescriptionRequest = new StringRequest(Request.Method.POST,AppController.STORE_PRESCRIPTION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();


                        try{
                            JSONObject mainResponse = new JSONObject(response);
                            prescriptionStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");




                            if(prescriptionStatus.equals("SUCCESS")){

                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intentHome);
                                UploadActivity.this.finish();



                            }
                            else{

                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();


                            }







                           // messageHandler.sendEmptyMessage(1);
                        }catch(Exception e){
                            //messageHandler.sendEmptyMessage(99);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // dialog.dismiss();
                //messageHandler.sendEmptyMessage(98);


                NetworkResponse networkResponse = error.networkResponse;

                if(networkResponse != null){


                    if(networkResponse.statusCode == HttpStatus.SC_PRECONDITION_FAILED){
                        //412 code Precondition failed

                        responseMessage = " Prescription is required";

                    }
                    else if(networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST){
                        //400 : Bad Request

                        responseMessage = "Email or prescription empty.";


                    }
                    else{

                    }

                    Snackbar.make(relativeLayoutMain,responseMessage, Snackbar.LENGTH_INDEFINITE)
                            .setAction("DONE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {



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
                params.put("prescription",prescription);
                params.put("prescription_thumb",prescription_thumb);
//                params.put("cart_length","1");     /// "1"   1""
//                params.put("id0","5");
//                params.put("quantity0","2");


//                if(cart_length>0 && prefs.getInt("from_my_cart_buy", 0)==1){
//                    params.put("cart_length",cart_length+"");
//
//                    for(int i =0 ; i<cart_length; i++){
//                        params.put("item_name"+i, prefs.getString("med_name"+i, null));
//                        params.put("quantity"+i, prefs.getString("quantity"+i, "1"));
//                        params.put("item_code"+i, prefs.getString("item_code"+i, null));
//                        params.put("price"+i, prefs.getString("price"+i, null));
//                    }
//
//                }



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
        AppController.getInstance().addToRequestQueue(uploadPrescriptionRequest);
        uploadPrescriptionRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }//volley




}
