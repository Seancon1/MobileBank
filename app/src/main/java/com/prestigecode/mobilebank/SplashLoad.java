package com.prestigecode.mobilebank;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashLoad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_load);

        //create thread to run, just to load a little bit, 500 millis
        //then return a OK and then pass back through intent
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("Log", "Thread start: running on SplashLoad ");
                    //wait a littel bit
                    sleep(500);

                    //getApplicationContext() grabs the instance we are in, so we can move to another interface
                    //or we can use Activity.this or my case it's Splash.this
                    //Intent intent = new Intent(Splash.this, MainActivity.class);
                    //startActivity(intent);
                    //then we package it and send it back

                    //THIS SECTION is used for when the program starts with Main Activity, we then finish this loading screen, then go back
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent); // value to return to base


                    finish(); //closes the Activity
                    super.run(); //this is so the splash doesnt load everytime you open the app UNLESS you close it and restart it
                } catch (Exception e) {

                }
            }
        };
        thread.start();
    }
}
