package com.prestigecode.mobilebank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.prestigecode.mobilebank.DB.Query;
import com.prestigecode.mobilebank.DB.QueryThread;
import com.prestigecode.mobilebank.User.AreYouSure;
import com.prestigecode.mobilebank.User.User;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import com.prestigecode.mobilebank.User.*;

public class UserLogin extends AppCompatActivity {

    //UI interaction
    Button buttonLogin;
    EditText editTextUN;
    EditText editTextUP;
    TextView errorText;
    ProgressBar progressBar;
    final String logInResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_login);

        buttonLogin = findViewById(R.id.buttonLogIn);
        editTextUN = findViewById(R.id.editTextUN);
        editTextUP = findViewById(R.id.editTextPass);
        errorText = findViewById(R.id.textViewLoginError);
        progressBar = findViewById(R.id.progressBar_UserLogin);

        progressBar.setVisibility(View.INVISIBLE);
        editTextUN.setText(""); //remove text
        editTextUN.requestFocus(); //get focus

    }

    @Override
    public void onBackPressed() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AreYouSure areYouSure = new AreYouSure();
        areYouSure.show(ft,"1");
        setResult(RESULT_CANCELED);
        //super.onBackPressed();
    }


    //String userName = "";
    //String userLoginToken = "";
    //Constructor
    public UserLogin () { }

    private void finishLogin(int inID, String inUserName, String inUserLoginToken) {
        //Build intent to pass user back
        Intent intent = new Intent(UserLogin.this, MainActivity.class);
        //lobby.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        //lobby.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);


        //Add ParcelableExtra so we can return superUser to Main Activity
        intent.putExtra("User", new User(inID, inUserName, inUserLoginToken)); //set info
        //Return OK and pass intent so we can send putExtra (User) back
        setResult(Activity.RESULT_OK, intent);
    }

    private void finishLogin(User inUser) {
        //Build intent to pass user back
        Intent intent = new Intent(UserLogin.this, MainActivity.class);

        //Add ParcelableExtra so we can return superUser to Main Activity
        intent.putExtra("User", inUser); //set inUser class as intent information
        setResult(Activity.RESULT_OK, intent); //send
    }

    public void openPatchNotes(View view) {
        Intent intent = new Intent(UserLogin.this, AdaptiveWebView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("User", new User()); //pass blank user so that
        intent.putExtra("ACTION", "patch_notes");
        startActivity(intent);
    }

    public void doLogin(View view) {

        Button button = findViewById(R.id.buttonLogIn);
       // progressBar.setVisibility(View.VISIBLE);

        try {
            //try login stuff
                Thread thread = new Thread() {

                    @Override
                    public void run() {

                        try {
                            Log.e("INSIGHT", "" + editTextUN.getText() + "" + editTextUP.getText() );
                            if(TextUtils.isEmpty(editTextUN.getText()) || TextUtils.isEmpty(editTextUP.getText())){
                                    addResultText(errorText, "Username and password are required.", 0);
                            } else {
                                addResultText(errorText,"One moment...", 1);
                                //progressBar.animate();
                                //Call login query
                                String result = "";
                                HashMap<String, String> hashMap = new HashMap<>(); //gen map to populate values
                                hashMap.put("action", "login");
                                hashMap.put("username", editTextUN.getText().toString());
                                hashMap.put("password", editTextUP.getText().toString());
                                String msg = "";
                                //new Query(getApplicationContext(), hashMap, msg).execute(); //pass map to login task
                                QueryThread thread = new QueryThread(getApplicationContext(), hashMap); //pass map to login task
                                thread.start();

                                Log.e("Result Found", thread.getResult());
                                while(thread.getResult().length() < 1) {
                                    addResultText(errorText, "Loading...", 1);

                                    sleep(250);
                                }
                                if(thread.getResult().contains("incorrect")) {
                                    addResultText(errorText, "No account found, please try again.", 0);
                                } else {
                                    finishLogin(thread.getUserAccount()); //set username and auth token for use the rest of the time
                                    finish();
                                }

                            }

                        } catch (Exception e) {
                            Log.e("Login", "Error " + e.toString());
                        }
                    }
                };
                thread.start();
        } catch (Exception e) {
            //do catch
            Log.e("Login", "Error: " + e.toString());
        }
       //1 progressBar.setVisibility(View.INVISIBLE);
    }

    public void doLoginAsync(View view) {

        Button button = findViewById(R.id.buttonLogIn);
        String itemResult = "item";
        int paramVal = 1;
        int progressItem = 0;
        // progressBar.setVisibility(View.VISIBLE);
        Log.e("Login", "Using asynctask!");
        AsyncTask asyncTask = new AsyncTask<Integer, Integer, String>() {

            String asyncResult = "yay";

            @Override
            protected void onProgressUpdate(Integer... values) {
                //increment one for the first integer passed
                values[0]++;
            }

            @Override
            protected String doInBackground(Integer... integers) {
                //Log.e("INSIGHT", "" + editTextUN.getText() + "" + editTextUP.getText() );
                for(int num : integers) {
                    Log.e("ASYNC TASK", "Integer passed: " + num);
                }

                if(TextUtils.isEmpty(editTextUN.getText()) || TextUtils.isEmpty(editTextUP.getText())){
                    //textView.setText("Username and password are required.");
                } else {
                    //textView.setText("One moment...");

                    HashMap<String, String> hashMap = new HashMap<>(); //gen map to populate values

                    hashMap.put("action", "login");
                    hashMap.put("username", editTextUN.getText().toString());
                    hashMap.put("password", editTextUP.getText().toString());
                    String msg = "";
                    //new Query(getApplicationContext(), hashMap, msg).execute(); //pass map to login task
                    QueryThread thread = new QueryThread(getApplicationContext(), hashMap); //pass map to login task
                    //Call login query
                    Log.e("Result Found", thread.getResult());

                    while(thread.getResult().length() < 1) {
                        //textView.setText("Loading...");
                        //Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                        //do nothing
                    }
                        asyncResult = thread.getResult();

                        //finishLogin(thread.getUserAccount()); //set username and auth token for use the rest of the time
                        //finish();
                    }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if(asyncResult.contains("incorrect")) {
                    //addResultText(errorText, "No account found, please try again.", 0);
                    s="invalid";
                }
                s = asyncResult;

            }

            @Override
            protected void onPreExecute() {
                //textView = textViewIn;
                super.onPreExecute();
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }
        };

        asyncTask.execute(paramVal, progressItem, itemResult);




        //1 progressBar.setVisibility(View.INVISIBLE);
    }



    public void addResultText(TextView textView, String string, int tag) {
        switch(tag) {
            case 0:
                textView.setTextColor(Color.RED);
                break;
            case 1:
                textView.setTextColor(Color.BLUE);
            break;
        }

        textView.setText(string + "\n");
    }

    public void addResultText(TextView textView, String string) {
        //Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
        //textView.setText(string + "\n");
    }

    public void openRegistration(View view) {
        Intent intent = new Intent(UserLogin.this, Web_RegisterAccount.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
