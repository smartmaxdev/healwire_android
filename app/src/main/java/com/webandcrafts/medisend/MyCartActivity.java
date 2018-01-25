package com.webandcrafts.medisend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyCartActivity extends AppCompatActivity {

    ListView listViewMyCart;
    MyCartItems items;

    ImageView imageViewDelete;

    FloatingActionButton floatingActionButtonUpload,floatingActionButtonChooseFile,floatingActionButtonCamera;
    RelativeLayout relativeLayoutMain,relativeLayoutButtons;

//    static Bitmap cameraBitmap;


    ProgressDialog progressDialog;
    Bitmap pres_image;
    String prescriptionStatus,responseMessage;
    String email;
    int is_pres_required ;

    String image,image_thumb;

    private int PICK_IMAGE_REQUEST = 1;

    static final int REQUEST_IMAGE_CAPTURE = 1;



   // ArrayList<String> arrayListMedicineName;

    ArrayList<HashMap<String,String>> arrayListMedicineName;

    static List<MyCartItems> cartItemsList;

    MyCartListAdapter adapter;

    String flag;

    int delete_position;

    Button buttonPlaceOrder;


    TextView textViewEmpty;



    int cart_length;


    public static final String[] item_Name = new String[] { "Strawberry",
            "Banana", "Orange", "Mixed" };

    public static final String[] item_Quantity = new String[] {
            "It is an aggregate accessory fruit",
            "It is the largest herbaceous flowering plant", "Citrus Fruit",
            "Mixed Fruits" };






    ArrayAdapter<String> arrayAdapterMedicine;
    String medicineName,quantity;

    private SimpleAdapter listAdapter;






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

        setContentView(R.layout.activity_my_cart);

        //Load the data from intents

//         medicineName = getIntent().getStringExtra("item_name");
//         quantity = getIntent().getStringExtra("quantity");

        progressDialog = new ProgressDialog(this);

        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);
        relativeLayoutButtons = (RelativeLayout) findViewById(R.id.relativeLayoutButtons);




        textViewEmpty = (TextView) findViewById(android.R.id.empty);

        buttonPlaceOrder = (Button) findViewById(R.id.buttonPlaceOrder);

        floatingActionButtonUpload = (FloatingActionButton) findViewById(R.id.floatingActionButtonUpload);
        floatingActionButtonCamera = (FloatingActionButton) findViewById(R.id.floatingActionButtonCamera);
        floatingActionButtonChooseFile = (FloatingActionButton) findViewById(R.id.floatingActionButtonChooseFile);





        cartItemsList = new ArrayList<MyCartItems>();

        imageViewDelete = (ImageView) findViewById(R.id.imageViewDelete);



        listViewMyCart = (ListView) findViewById(R.id.listViewMyCart);

        SharedPreferenceHandler spHandler = new SharedPreferenceHandler();

        email = AppController.userEmail;




//        Toast.makeText(getApplicationContext(),spHandler.getCartSize(getApplicationContext())+"",Toast.LENGTH_SHORT).show();



        cart_length = spHandler.getCartSize(getApplicationContext()); // Should be loaded from sharedPreference


        if(cart_length>0){

            textViewEmpty.setVisibility(View.GONE);

           // String med_name,quantity;
//            for(int i = 0 ; i<cart_length;i++){
//                med_name = prefs.getString("med_name"+i, null);
//                quantity = prefs.getString("quantity"+i, "1");

                medicineName = getIntent().getStringExtra("item_name");
                quantity = getIntent().getStringExtra("quantity");
                flag ="true";


           // Toast.makeText(getApplicationContext(),quantity,Toast.LENGTH_SHORT).show();


                items = new MyCartItems(medicineName, quantity);
                //cartItemsList.add(items);
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

               cartItemsList = sharedPreferenceHandler.loadCartItems(getApplicationContext());

            buttonPlaceOrder.setVisibility(View.VISIBLE);


             // Check any one prescription required....
            //-------------------------------------------------------

            for(int i = 0;i<cart_length;i++){

                MyCartItems myCartItems = new MyCartItems();
                myCartItems = cartItemsList.get(i);

                if(myCartItems.getIs_pres_required() == 1){

                    // Prescription is required

                    Snackbar.make(relativeLayoutMain, "You need to upload a Prescription.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("DONE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // floatingActionButtonUpload.setVisibility(View.VISIBLE);


//                                    Intent intentHome= new Intent(getApplicationContext(),HomeActivity.class);
//                                    startActivity(intentHome);

                                }
                            })
                            .show();



                       // floatingActionButtonUpload.setVisibility(View.VISIBLE);

                         break;

                }
                else{

                    // Prescription not required
                   // floatingActionButtonUpload.setVisibility(View.GONE);

                   continue;

                }

            }


            //------------------------------------------------------------------




      // }


            adapter = new MyCartListAdapter(this,cartItemsList);
            listViewMyCart.setAdapter(adapter);


        }else{


            textViewEmpty.setVisibility(View.VISIBLE);

            listViewMyCart.setEmptyView(textViewEmpty);


            buttonPlaceOrder.setVisibility(View.INVISIBLE);



//            buy.setVisibility(View.GONE);
//            menu.setVisibility(View.GONE);
        }


        listViewMyCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




//                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
//
//                cartItemsList.remove(i);
//
//                sharedPreferenceHandler.removeCartItems(getApplicationContext(),items,i);
//               // cartItemsList = sharedPreferenceHandler.loadCartItems(getApplicationContext());
//
//               // listViewMyCart.invalidateViews();
//
//                adapter.notifyDataSetChanged();

                 EditText viewLayout ;
                 TextView textView;

                 viewLayout  = (EditText) view.findViewById(R.id.editTextQuantity);

                 textView = (TextView) findViewById(R.id.textViewGroupMedicineName);

                String quan = viewLayout.getText().toString();
                String medNAme = textView.getText().toString();



//                Toast.makeText(getApplicationContext(),"Quantity = "+quan,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),"Name = "+medNAme,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),"Postion = "+i,Toast.LENGTH_SHORT).show();








            }
        });


        listViewMyCart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        listViewMyCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                delete_position = i;

                if(imageViewDelete.getVisibility() == View.GONE){

                    imageViewDelete.setVisibility(View.VISIBLE);

                }

                return false;
            }
        });


        floatingActionButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Upload the prescription if either one of the item :  is_prescription_required = 1



                if(relativeLayoutButtons.getVisibility() == View.GONE){

                    relativeLayoutButtons.setVisibility(View.VISIBLE);

                }
                else{
                    relativeLayoutButtons.setVisibility(View.GONE);
                }


//                Intent intent = new Intent();
//// Show only images, no videos or anything else
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//// Always show the chooser (if there are multiple options available)
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);



//                uploadFile(getResizedBitmap(pres_image,600),getResizedBitmap(pres_image,100));



            }
        });


        // Camera button

        floatingActionButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppController.userStatus.equals("login")){

                    dispatchTakePictureIntent();

                }
                else{

                    Intent intentLogin =  new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intentLogin);
                }

            }
        });

        // Choose file button

        floatingActionButtonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(AppController.userStatus.equals("login")){


                    if(SharedPreferenceHandler.CART_LENGTH > 0){

//                        Intent intentUpload = new Intent(getApplicationContext(),MyCartActivity.class);
//                        startActivity(intentUpload);

                        AppController.MY_CART_TO_CHOOSE_FILE_PATH = "true";


                        Intent intentUpload = new Intent(getApplicationContext(),UploadActivity.class);
                        startActivity(intentUpload);




                    }
                    else{

                        //Cart length is 0

                        // Just upload prescrption only.

                        Intent intentUpload = new Intent(getApplicationContext(),UploadActivity.class);
                        startActivity(intentUpload);



                    }



                }
                else{

                    Intent intentLogin =  new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intentLogin);

                }




            }
        });


        // place order button

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // uploadPrescription(image,image_thumb);

            if(AppController.MY_CART_TO_CHOOSE_FILE_PATH.equals("true")){

                image = UploadActivity.image_to_MyCart;
                image_thumb = UploadActivity.image_thumb_to_MyCart;

                AppController.MY_CART_TO_CHOOSE_FILE_PATH = "false";

            }


                if(image != null  && image_thumb != null){

                    uploadPrescription(image,image_thumb);
                    buttonPlaceOrder.setEnabled(false);



                }
                else{


                    Snackbar.make(relativeLayoutMain, "You need to upload a Prescription.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("DONE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // floatingActionButtonUpload.setVisibility(View.VISIBLE);


//                                    Intent intentHome= new Intent(getApplicationContext(),HomeActivity.class);
//                                    startActivity(intentHome);

                                }
                            })
                            .show();

                }




//                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
//
//                cartItemsList.clear();
//
//                sharedPreferenceHandler.removeAllCart(getApplicationContext());
//
//                adapter.notifyDataSetChanged();
//
//                cart_length = sharedPreferenceHandler.getCartSize(getApplicationContext()); // Should be loaded from sharedPreference
//
//
//                if(cart_length == 0){
//
//                    textViewEmpty.setVisibility(View.VISIBLE);
//                    listViewMyCart.setEmptyView(textViewEmpty);
//                    buttonPlaceOrder.setVisibility(View.INVISIBLE);
//
//                }



            }
        });




        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                cartItemsList.remove(delete_position);






                sharedPreferenceHandler.removeCartItems(getApplicationContext(),items,delete_position);
                 //cartItemsList = sharedPreferenceHandler.loadCartItems(getApplicationContext());
                 //sharedPreferenceHandler.storeCartItems(getApplicationContext(),cartItemsList);
                sharedPreferenceHandler.getCartArrayList(getApplicationContext());


                SharedPreferenceHandler.arrayListMedicineName.clear();

                for(int j = 0;j<SharedPreferenceHandler.CART_LENGTH;j++) {

                    MyCartItems myCarts = (MyCartItems) SharedPreferenceHandler.cartList.get(j);
                    SharedPreferenceHandler.arrayListMedicineName.add(myCarts.getMedName());

                    // Toast.makeText(getApplicationContext(), myCarts.getMedName() + "", Toast.LENGTH_SHORT).show();

                }


                // Toast.makeText(getApplicationContext(),"Array="+cartItemsList.toArray(),Toast.LENGTH_SHORT).show();



                // listViewMyCart.invalidateViews();

                adapter.notifyDataSetChanged();

                imageViewDelete.setVisibility(View.GONE);

                cart_length = sharedPreferenceHandler.getCartSize(getApplicationContext()); // Should be loaded from sharedPreference


                if(cart_length == 0){

                    textViewEmpty.setVisibility(View.VISIBLE);
                    listViewMyCart.setEmptyView(textViewEmpty);
                    buttonPlaceOrder.setVisibility(View.INVISIBLE);

                }

                Log.d("LENGTH",String.valueOf(cart_length));



            }
        });







    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                pres_image = bitmap;


//                ImageView imageView = (ImageView) findViewById(R.id.imageViewPrescription);
//                imageView.setImageBitmap(bitmap);

                uploadFile(getResizedBitmap(pres_image,600),getResizedBitmap(pres_image,100));



            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            HomeActivity.cameraBitmap = (Bitmap) extras.get("data");
            Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(intent);
            AppController.DEVICE_CAMERA = 1;



        }



    }





    // upload the file

    public void uploadFile(Bitmap bitmap, Bitmap thumb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        ByteArrayOutputStream baos_thumb = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 60, baos_thumb);
        byte[] b_thumb = baos_thumb.toByteArray();
        image = Base64.encodeToString(b, Base64.DEFAULT);
        image_thumb = Base64.encodeToString(b_thumb, Base64.DEFAULT);
        Toast.makeText(getApplicationContext(),"file upload success",Toast.LENGTH_SHORT).show();


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

        progressDialog.setMessage("Please wait...");
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

                                buttonPlaceOrder.setEnabled(true);

                                //Clear the cart items.


                                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                                cartItemsList.clear();

                                sharedPreferenceHandler.removeAllCart(getApplicationContext());

                                adapter.notifyDataSetChanged();

                                cart_length = sharedPreferenceHandler.getCartSize(getApplicationContext()); // Should be loaded from sharedPreference


                                if(cart_length == 0){

                                    textViewEmpty.setVisibility(View.VISIBLE);
                                    listViewMyCart.setEmptyView(textViewEmpty);
                                    buttonPlaceOrder.setVisibility(View.INVISIBLE);

                                }






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

            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email",email);

//                params.put("prescription",prescription);
//                params.put("prescription_thumb",prescription_thumb);
//                params.put("cart_length","1");     /// "1"   1""
//                params.put("id0","5");
//                params.put("quantity0","2");


                if(cart_length>0){

                    params.put("cart_length",String.valueOf(cart_length));
                    for(int i = 0;i<cart_length;i++){

                        MyCartItems myCartItems = new MyCartItems();
                        myCartItems = cartItemsList.get(i);

                        params.put("id"+i,String.valueOf(myCartItems.getId()));
                        params.put("quantity"+i,myCartItems.getQty());

                        if(myCartItems.getIs_pres_required() == 0){

                            is_pres_required = 0;
                        }
                        else{

                            is_pres_required = 1;
                        }

                    }






                }

                params.put("is_pres_required",String.valueOf(is_pres_required));

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
