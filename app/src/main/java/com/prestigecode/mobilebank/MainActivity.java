package com.prestigecode.mobilebank;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.prestigecode.mobilebank.User.User;
import com.prestigecode.mobilebank.UserBankHandler.dummy.DummyContent;

import com.prestigecode.mobilebank.UserBankHandler.*;
import com.prestigecode.mobilebank.DB.*;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    //Super variables
    User superUser = null;
    boolean hasLoggedIn = false;
    int tick = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //Layout for bank information
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        //need a layout manager to implement and set for the recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        */

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ItemFragment itemFragment = ItemFragment.newInstance(1);
        //itemFragment.onAttach(this.getApplicationContext());
        fragmentTransaction.replace(R.id.viewBankAccounts, itemFragment);
        fragmentTransaction.commit();

        //we want to create a loadscreen really quick so hide all what we are loading
        Intent splash = new Intent(MainActivity.this, SplashLoad.class);
        startActivityForResult(splash, 0001);
        //wait for the intent to come back with data
        //then continue on until

        //Assign to a variable so that it can be referenced later on and passed to new Intents
        //user = new User();

        /*
        //Create a service to run and control all User information
        Intent intent = new Intent(this, UserService.class);
        intent.putExtra("User", superUser);
        //intent.putExtra("textview", textView);
        startService(intent);
        */

        TextView textView = findViewById(R.id.textView2);

        //Main Thread tick: 1 second per tick
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("Log", "Thread start: running on MainActivity ");

                    while(true) {
                        sleep(1000); // 1 second
                        tick++; //Tick increment

                        //Makes sure that the superUser is still authenticated
                        //the application will stop or redirect if user is no longer active (1 minute)
                        if (superUser == null && !hasLoggedIn) {
                            hasLoggedIn = true;
                            //superUser = new User(); //Populate just so tick doesn't continuously open new intents
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            //intent.putExtra("User", superUser);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            //This request code is what is needed when the result is returned
                            startActivityForResult(intent, 0);
                        }
                    }

                } catch (Exception e) {}
            }
        };
        thread.start();

        //doCheckIfLogged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Check which requestCode was used
        switch(requestCode) {
            //Case go to the correct logic for that result

            case 0: //login
                if(resultCode == Activity.RESULT_OK) {
                    hasLoggedIn = true;
                    superUser = data.getParcelableExtra("User"); //returns User defined in login, and put it for access here
                }
                if(resultCode == Activity.RESULT_CANCELED) {
                    superUser = null; //to reset superUser to null
                    hasLoggedIn = false;
                }
        }

    }

    public void amILogged(View view) {
        doCheckIfLogged();
    }

    private void doCheckIfLogged() {
        if(superUser == null) {
            Toast.makeText(getApplicationContext(), "User is not logged in.", Toast.LENGTH_SHORT).show();
            Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
            //loginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //loginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(loginActivity);
        } else {
            /*
            Toast.makeText(getApplicationContext(), "Welcome "+ superUser.getName(), Toast.LENGTH_SHORT).show();
            Intent toLobby = new Intent(MainActivity.this, Lobby.class);
            //toLobby.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //make this the new root activity
            startActivity(toLobby);
            */
            Toast.makeText(getApplicationContext(), "Welcome "+ superUser.getName(), Toast.LENGTH_SHORT).show();
        }

    }

    //TestList
    public void openList(View view) {
        //Intent intent = new Intent(MainActivity.this, MyItemRecyclerViewAdapter.class);
        //loginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //startActivity(intent);
        //Fragment fragment = new Fragment();
        try {
            Connect conn = new Connect();
            conn.doConnect();
        } catch (Exception e) {
            Log.e("mysql", "openList method failed");
        }


    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        //must implement this in order for the list to not break the application
    }
}
