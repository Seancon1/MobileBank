package com.prestigecode.mobilebank;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prestigecode.mobilebank.DB.QueryThread;
import com.prestigecode.mobilebank.User.BankAccount;
import com.prestigecode.mobilebank.User.MyAccount;
import com.prestigecode.mobilebank.User.User;
import com.prestigecode.mobilebank.UserBankHandler.dummy.DummyContent;

import com.prestigecode.mobilebank.UserBankHandler.*;
import com.prestigecode.mobilebank.DB.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    //Super variables
    User superUser = null;
    boolean hasLoggedIn = false;
    int tick = 0;

    //Assign result with global scope, for now
    //HashMap<String,String> jsonResult;
    //JSONArray jsonResult;
    //BankAccount jsonResult;
    List<BankAccount> jsonBankAccountResult;

    //UI items
    TextView txtAccountWelcome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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



        //Main Thread tick: 1 second per tick
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("Log", "Thread start: running on MainActivity ");

                    while(true) {

                        if(superUser == null) {
                            // .1 delay shortens the time that the main activity is shown upon startup
                            sleep(100); //
                        } else {
                            //when user is populated, then a normal 1 second tick is established
                            sleep(250);
                        }

                        tick++; //Tick increment

                        //Makes sure that the superUser is still authenticated
                        //the application will stop or redirect if user is no longer active (5 minute)
                        if (superUser == null && !hasLoggedIn) {
                            hasLoggedIn = true;
                            //superUser = new User(); //Populate just so tick doesn't continuously open new intents
                            Intent intent = new Intent(MainActivity.this, UserLogin.class);
                            //intent.putExtra("User", superUser);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //intent.addFlags(Intent.makeMainActivity(UserLogin));

                            //This request code is what is needed when the result is returned
                            startActivityForResult(intent, 0);
                        }
                    }

                } catch (Exception e) {}
            }
        };
        thread.start();

    }


    //OnActivityResult is the default class that will handle the return codes for each intent that returns information here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Check which requestCode was used
        switch(requestCode) {
            //Case go to the correct logic for that resultCode

            case 0: //login
                if(resultCode == Activity.RESULT_OK) {
                    hasLoggedIn = true;
                    superUser = data.getParcelableExtra("User"); //returns User defined in login, and put it for access here
                    //Update display items
                    txtAccountWelcome = findViewById(R.id.textViewAccountWelcome);
                    txtAccountWelcome.setText("Welcome " + superUser.getName());

                    //Update Quick display of user bank accounts
                    System.out.println("Updating User UI Items");
                    fetchAccountInfo();


                }
                if(resultCode == Activity.RESULT_CANCELED) {
                    superUser = null; //to reset superUser to null
                    hasLoggedIn = false;
                    //finish(); //experimental
                }
                break;

            case 1: //logout
                if(resultCode == Activity.RESULT_OK) {
                    logUserOut();
                }
        }

        //Maybe here I can issue an update on items?


    }

    public void amILogged(View view) {
        doCheckIfLogged();

        Intent intent = new Intent(MainActivity.this, MyAccount.class);
        intent.putExtra("User", superUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 1);
    }

    private void doCheckIfLogged() {
        if(superUser == null) {
            Toast.makeText(getApplicationContext(), "User is not logged in.", Toast.LENGTH_SHORT).show();
        } else {
            /*
            Toast.makeText(getApplicationContext(), "Welcome "+ superUser.getName(), Toast.LENGTH_SHORT).show();
            Intent toLobby = new Intent(MainActivity.this, Lobby.class);
            //toLobby.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //make this the new root activity
            startActivity(toLobby);
            */
            Toast.makeText(getApplicationContext(), "Welcome "+ superUser.getName() + " [" + superUser.getToken() + "]", Toast.LENGTH_SHORT).show();
        }

    }

    //TestList
    public void openList(View view) {
        //Intent intent = new Intent(MainActivity.this, MyItemRecyclerViewAdapter.class);
        //loginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //startActivity(intent);
        //Fragment fragment = new Fragment();
        Button button = findViewById(R.id.button2);
        String buttonResponse;
        try {
            /*
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "login");
            hashMap.put("username", superUser.getName());
            hashMap.put("password", "wow");
            new Query(this.getApplicationContext(), hashMap).execute();
            */
            Log.e("UserInfo", "token is" + superUser.getToken());

            //button.setText();

        } catch (Exception e) {
            Log.e("mysql", "openList method failed" + e.toString());
        }


    }


    public void fetchAccountInfo() {
        /*
        For now I am running threads for each new operation since I need a result
        I need to use AsyncTask for this but still learning how to get results easier without null point exception somewhere
         */
        Thread singleThread = new Thread() {
            @Override
            public void run() {

                try {
                    HashMap<String, String> hashMap = new HashMap<>(); //gen map to populate values
                    hashMap.put("action", "getBankAccounts");
                    hashMap.put("UID", ""+superUser.getID());
                    hashMap.put("TOKEN", superUser.getToken());
                    QueryThread thread = new QueryThread(getApplicationContext(), hashMap);
                    thread.start();

                    while(thread.getResult().length() < 1) {
                        sleep(250);
                        Log.e("displayAccountInfo", "waiting for thread result to populate");
                    }
                    jsonBankAccountResult = thread.getBankAccounts();

                } catch (Exception e) {
                    Log.e("fetch Thread", e.toString());
                }

            }
        };
        singleThread.start();
    }


    public void displayAccountInfo(View view) {

        fetchAccountInfo();

        TextView textView = findViewById(R.id.textViewSimpleBankAccountView);

        try {
            System.out.println("TRYING TO GET EACH ITEM TO APPEND");
                /*
                for(Map.Entry<String, String> item : jsonResult.entrySet()) {
                    System.out.println("Json item: " + item.getKey());
                    textView.append("" + item.getKey() + " - :" + item.getValue());
                }
                */
                textView.setTextSize(25);
                textView.setText("");
                //loop through each BankAccount that was created and display relevant information
                for(BankAccount item : jsonBankAccountResult) {
                    textView.append("" + item.toString() + "\n");
                }




            /*
            //iterate through each object in the HashMap located inside jsonResult
            for(Map.Entry<String, Object> item: jsonResult.entrySet()) {
                textView.append("Account Type " + item.getKey() + " - Balance: " + item.getValue());
            }
            */
            /*
            Iterator it = jsonResult.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry item = (Map.Entry)it.next();
                textView.setText("");
                textView.setText("");
            }
            */


        } catch (Exception e) {
            Log.e("error",e.toString());
            textView.setText("Unable to fetch account information.");
        }

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        //must implement this in order for the list to not break the application
    }


    /**
     * User Logout Function
     */
    public boolean logUserOut() {
        superUser = null; //set user to null, removing reference to object in memory
        hasLoggedIn = false; //to switch that a user is not logged in, triggering other login checks
        jsonBankAccountResult = null;
        return true;
    }
}
