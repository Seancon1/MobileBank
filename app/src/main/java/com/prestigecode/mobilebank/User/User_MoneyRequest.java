package com.prestigecode.mobilebank.User;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.fasterxml.jackson.core.base.ParserBase;
import com.prestigecode.mobilebank.DB.QueryThread;
import com.prestigecode.mobilebank.MainActivity;
import com.prestigecode.mobilebank.R;
import com.prestigecode.mobilebank.UserLogin;

import java.util.HashMap;

/**
 * User_MoneyRequest
 * Basic interface that enables user to request money or send money.
 */

public class User_MoneyRequest extends AppCompatActivity {

    User superUser = null;
    String getOutID = "0";
    String getRecipientName = null;
    TextView txtRecipientName = null;
    String queryResult = "";
    Button requestButton = null;
    Button sendButton = null;
    EditText editTextAmount = null;


    /*
    *Thread specific values, replaceable with AsyncTask
     */
        String queryThreadStatus = null;
        boolean queryThreadComplete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__money_request);

        requestButton = findViewById(R.id.btnUserMoneyRequest_Request);
        sendButton = findViewById(R.id.btnUserMoneyRequest_Send);
        editTextAmount = findViewById(R.id.editTextAmount);

        superUser = getIntent().getParcelableExtra("User");
        txtRecipientName = findViewById(R.id.txtViewRecipientName);
        getOutID = getIntent().getStringExtra("TransferID");
        getRecipientName = getIntent().getStringExtra("recipient");
        Log.e("User_MoneyRequest", "TransferID: " + getOutID);
        Log.e("User_MoneyRequest", "recipient name: " + getRecipientName);

        txtRecipientName.setText("To: " + getRecipientName);

        sendButton.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSubmit()) {
                    doMoneySend(getOutID, Double.parseDouble(editTextAmount.getText().toString()), 1);
                }
            }
        });

        requestButton.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSubmit()) {
                    doMoneySend(getOutID, Double.parseDouble(editTextAmount.getText().toString()), 0);
                }
            }
        });

        /*
        Thread thread = new Thread() {


            @Override
            public void run() {
                try {
                    Log.e("User_MoneyRequest", "Thread started ");

                    //CANCELED should always remain false UNLESS user wants to close application
                    //Otherwise loop indefinitely


                } catch (Exception e) {
                    Log.e("User_MoneyRequest", "Thread " + e.toString());
                }
            }
        };
        thread.start();
*/

    }

    /**
     * Method for sending money to the specified user.
     *
     */
    private void doMoneySend(String inID, double inAmount, int type) {
        /*
        For now I am running threads for each new operation since I need a result
        I need to use AsyncTask for this but still learning how to get results easier without null point exception somewhere
         */

        queryThreadComplete = false;
            changeButtonEnabledStatus(false);
            Thread singleThread = new Thread() {
                @Override
                public void run() {

                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("action", "TransferRequest");
                        hashMap.put("UID", ""+superUser.getID());
                        hashMap.put("TOKEN", superUser.getToken());
                        hashMap.put("requestID", inID);
                        hashMap.put("transferAmount", "" + inAmount);
                        //0 for request, 1 for send
                        if(type == 0) {
                            hashMap.put("transferType", "0");
                        } else {
                            hashMap.put("transferType", "1");
                        }


                        QueryThread thread = new QueryThread(getApplicationContext(), hashMap);
                        thread.start();

                        while(thread.getResult().length() < 1) {
                            sleep(250);
                            Log.e("User_MoneyRequest", "Waiting for response...");
                        }

                        queryResult = thread.getResult();
                        //finish();
                    } catch (Exception e) {
                        Log.e("User_MoneyRequest", e.toString());
                    }


                }
            };
            singleThread.start();

            /**
                !!!
             Aware of running this on main will cause unresponsiveness
                !!!
             */
                while(queryThreadComplete == false) {
                    //when user is populated, then a normal 1 second tick is established
                    Log.e("User_MoneyRequest", "tick: " + queryResult);
                    if(queryResult.length() > 0) {

                        if(queryResult.contains("success")) {
                            queryThreadComplete = true;
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            queryThreadComplete = true;
                            Toast.makeText(getApplicationContext(), "Failed, try again", Toast.LENGTH_SHORT).show();
                            changeButtonEnabledStatus(true);
                        }

                    }
                }

        //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
    }

    public void setResultDisplay(String text) {
        txtRecipientName.setText("To: " + txtRecipientName + "\n");
        txtRecipientName.append("" + text);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean canSubmit() {
        String inputString = editTextAmount.getText().toString();


        if(!inputString.isEmpty() && (Double.parseDouble(inputString) > 0.0) && inputString.length() > 0 && !inputString.matches(".")) {
            return true;
        }

        Toast.makeText(getApplicationContext(), "You must put a valid value.", Toast.LENGTH_LONG).show();
        return false;
    }

    public void changeButtonEnabledStatus(boolean status) {
        sendButton.setEnabled(status);
        requestButton.setEnabled(status);
    }

}
