package com.prestigecode.mobilebank;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.prestigecode.mobilebank.DB.Query;
import com.prestigecode.mobilebank.DB.QueryThread;
import com.prestigecode.mobilebank.User.User;

import java.util.HashMap;

public class UserLogin extends AppCompatActivity {

    //UI interaction
    Button buttonLogin;
    EditText editTextUN;
    EditText editTextUP;
    TextView errorText;

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

        editTextUN.setText(""); //remove text
        editTextUN.requestFocus(); //get focus

    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);
        //super.onBackPressed();
    }

    //String userName = "";
    //String userLoginToken = "";
    //Constructor
    public UserLogin () { }

    private void finishLogin(String userName, String userLoginToken) {
        //Build intent to pass user back
        Intent intent = new Intent(UserLogin.this, MainActivity.class);
        //lobby.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        //lobby.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);


        //Add ParcelableExtra so we can return superUser to Main Activity
        intent.putExtra("User", new User(0, userName, userLoginToken)); //set info
        //Return OK and pass intent so we can send putExtra (User) back
        setResult(Activity.RESULT_OK, intent);
    }

    public void doLogin(View view) {

        Button button = findViewById(R.id.buttonLogIn);

        try {
            //try login stuff
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Log.e("INSIGHT", "" + editTextUN.getText() + "" + editTextUP.getText() );
                            if(TextUtils.isEmpty(editTextUN.getText()) || TextUtils.isEmpty(editTextUP.getText())){
                                    addErrorText(errorText, "Username and password are required.");
                            } else {
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
                                    sleep(250);
                                    Log.e("Thread Loop", "waiting for thread result to populate");
                                }
                                if(thread.getResult().contains("incorrect")) {
                                    addErrorText(errorText, "No account found, please try again.");
                                } else {
                                    //does not contain incorrect
                                    finishLogin(editTextUN.getText().toString(), thread.getResult()); //set username and auth token for use the rest of the time
                                    finish();
                                }

                            }

                        } catch (Exception e) {
                            Log.e("LoginError", e.toString());
                        }

                    }
                };
                thread.start();
        } catch (Exception e) {
            //do catch
            Log.e("Login", "Error: " + e.toString());
        }
    }

    public void addErrorText(TextView textView, String string) {
        textView.setTextColor(Color.RED);
        //textView.append(string + "\n");
        textView.setText(string + "\n");
    }
}
