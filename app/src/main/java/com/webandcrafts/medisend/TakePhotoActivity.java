package com.webandcrafts.medisend;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TakePhotoActivity extends AppCompatActivity {

    RelativeLayout relativeLayoutCancel,relativeLayoutUpload;
    ImageView imageViewFlash, imageViewRefresh, imageViewCapture;

    FrameLayout frameLayoutPreview;

    Bitmap pres_image = null;


    private static final String TAG = "CameraDemo";

    private Camera mCamera;
    private SurfaceView mPreview;


    Preview preview;
    ImageView buttonClick,flash,refresh;
    boolean lightOn=false;
    Camera.Parameters p;



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

        setContentView(R.layout.activity_take_photo);



        preview = new Preview(this);
        frameLayoutPreview = (FrameLayout) findViewById(R.id.frameLayoutPreview);
        frameLayoutPreview.addView(preview);



        relativeLayoutCancel = (RelativeLayout) findViewById(R.id.relativeLayoutCancel);
        relativeLayoutUpload = (RelativeLayout) findViewById(R.id.relativeLayoutUpload);

        imageViewFlash = (ImageView) findViewById(R.id.imageViewFlash);
        imageViewRefresh = (ImageView) findViewById(R.id.imageViewRefresh);
        imageViewCapture = (ImageView) findViewById(R.id.imageViewCapture);




        // Listener for upload

        relativeLayoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pres_image!=null){
                    //uploadFile(pres_image);
                  //  uploadFile(getResizedBitmap(pres_image,600),getResizedBitmap(pres_image,100));
                }



            }
        });


        // Listener for cancel

        relativeLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    preview.camera.startPreview();
                    resetPhotoTake();
                }catch (Exception e) {
                }

            }
        });


        //  Listener for Flash

        imageViewFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                    if(!lightOn){
                        Camera.Parameters p = preview.camera.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        preview.camera.setParameters(p);
                        lightOn=true;
                    }else{
                        Camera.Parameters p = preview.camera.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        preview.camera.setParameters(p);
                        lightOn =false;
                    }
                }else{
                   // Toast.makeText(this, "No Flash support", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // Listener for Refresh

        imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    preview.camera.startPreview();
                    resetPhotoTake();
                }catch (Exception e) {
                }


            }
        });

        //Listener for Capture

        imageViewCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lightOn){
                    Camera.Parameters p = preview.camera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    preview.camera.setParameters(p);
                }
                preview.camera.takePicture(null, null,
                        jpegCallback);


                setPhotoTake();



            }
        });


    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    public void resetPhotoTake(){

        relativeLayoutCancel.setVisibility(View.GONE);
        relativeLayoutUpload.setVisibility(View.GONE);
        buttonClick.setClickable(true);
        flash.setClickable(true);
        refresh.setClickable(true);
        frameLayoutPreview.setClickable(true);

    }
    public void setPhotoTake(){

        relativeLayoutCancel.setVisibility(View.VISIBLE);
        relativeLayoutUpload.setVisibility(View.VISIBLE);
        buttonClick.setClickable(false);
        flash.setClickable(false);
        ///refresh.setClickable(false);

    }



    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
        }
    };

    /** Handles data for raw picture */
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    /** Handles data for jpeg picture */
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            pres_image = BitmapFactory.decodeByteArray(data , 0, data .length);
            p = camera.getParameters();
        }
    };



}
