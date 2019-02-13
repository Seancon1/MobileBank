package com.prestigecode.mobilebank.User;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.prestigecode.mobilebank.MainActivity;
import com.prestigecode.mobilebank.R;
import com.prestigecode.mobilebank.UserLogin;

public class MyAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
    }


    public void logUserOut(View view) {
        //Send intent back with RESULT OK so that MainActivity can run logout function
        Intent intent = new Intent(MyAccount.this, MainActivity.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
