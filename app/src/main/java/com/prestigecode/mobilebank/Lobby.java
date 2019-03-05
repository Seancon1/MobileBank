package com.prestigecode.mobilebank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import com.prestigecode.mobilebank.User.User;
import com.prestigecode.mobilebank.ui.lobby.LobbyFragment;

import java.util.Map;

public class Lobby extends AppCompatActivity {

    User superUser = null;
    int tickCount = 0;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LobbyFragment.newInstance())
                    .commitNow();
        }


        //Very important
        superUser = getIntent().getParcelableExtra("User"); //Set this superUser from previous activity via Intent

        //Set Component to be interacted with
        final TextView textViewTopText = findViewById(R.id.textViewTopInfo);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                //Start a loop to run with a tick for every second
                while(superUser != null) {
                    sleep(1000);

                    //perform check for balance?
                    //some changing effects
                    textViewTopText.setText("Welcome " + superUser.getName() + " | " + ++tickCount);

                }

                } catch (Exception e) {

                }
            }
        };
        thread.start();


    }
}
