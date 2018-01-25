package com.webandcrafts.medisend;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG = "CameraDemo";
   // Camera camera;
    Preview preview;
    ImageView imageViewCapture;



    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    Camera.PictureCallback rawCallback;
   // Camera.ShutterCallback shutterCallback;
   // Camera.PictureCallback jpegCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_take_photo);


        preview = new Preview(this);
        ((FrameLayout) findViewById(R.id.frameLayoutPreview)).addView(preview);

        imageViewCapture = (ImageView) findViewById(R.id.imageViewCapture);
        imageViewCapture.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        Log.d(TAG, "onCreate'd");





    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };



    /** Handles data for jpeg picture */
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;
            long time = 0;
            try {
                // write to local sandbox file system
//                outStream = CameraDemo.this.openFileOutput(String.format("%d.jpg", System.currentTimeMillis()), 0);
                // Or write to sdcard
                time =  System.currentTimeMillis();
                outStream = new FileOutputStream(String.format("/sdcard/%d.jpg",time));
                outStream.write(data);
                outStream.close();
                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {



            }
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}

