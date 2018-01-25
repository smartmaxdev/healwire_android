package com.webandcrafts.healwire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentResponseActivity extends AppCompatActivity {

    Button buttonComplete;
    TextView textViewMessage;
    ImageView imageViewPrescription,imageViewPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_response);

        buttonComplete = (Button) findViewById(R.id.buttonComplete);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
        imageViewPrescription = (ImageView) findViewById(R.id.imageViewPrescription);
        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);


        if(AppController.PAYMENT_STATUS.equals("success")){


            buttonComplete.setText("Done");
            textViewMessage.setText("Congratulations ! Your payment was successful.");
            imageViewPicture.setImageResource(R.drawable.success_payment);

        }
        else{

             buttonComplete.setText("Try again");
             textViewMessage.setText("Your payment was failed.Please try again later.");
            imageViewPicture.setImageResource(R.drawable.failed_payment);

        }


        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentHome =  new Intent(PaymentResponseActivity.this,HomeActivity.class);
                startActivity(intentHome);
                PaymentResponseActivity.this.finish();

            }
        });

        imageViewPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentHome =  new Intent(PaymentResponseActivity.this,HomeActivity.class);
                startActivity(intentHome);
                PaymentResponseActivity.this.finish();


            }
        });

    }



}
