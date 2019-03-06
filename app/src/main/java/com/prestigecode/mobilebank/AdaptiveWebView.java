package com.prestigecode.mobilebank;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import com.prestigecode.mobilebank.User.User;

import java.net.URL;
import java.util.HashMap;

public class AdaptiveWebView extends AppCompatActivity {

    User superUser;
    WebView webView;
    String currentURL;
    String intentAction;
    TextView textViewTitle;
    HashMap<String, String> usableURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaptive_web_view);
        textViewTitle = findViewById(R.id.textViewAdaptiveTextTitle);

        superUser = getIntent().getParcelableExtra("User"); //Set this superUser from previous activity via Intent
        webView = findViewById(R.id.WebView_AdaptiveView); //set the webview


        intentAction = getIntent().getExtras().getString("ACTION"); //set extra from AccountHub
        Log.e("AdaptiveWebView", "intentURL: " + intentAction + " - setURL result: " + setURL(intentAction));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(setURL(intentAction)); //Build URL from intent ACTION data
    }

    public String setURL(String pageDestinations) {
        StringBuilder stringBuilder = new StringBuilder();
        String baseURL = "https://www.prestigecode.com/projects/senior/WebView/AdaptiveView.php";

        stringBuilder.append(baseURL); //add baseURL first

        Log.e("AdaptiveView", "pageDestinations: " + pageDestinations);
        //Build the rest of our URL
        switch(pageDestinations) {
            case "savings":
                changeTitle("Savings Account");
                stringBuilder.append("?page=savings");
                break;
            case "checking":
                textViewTitle.setText("Checking Account");
                stringBuilder.append("?page=checking");
                break;
            case "credit":
                textViewTitle.setText("Credit Account");
                stringBuilder.append("?page=credit");
                break;

             default:
                 stringBuilder.append("?page=null");
                 break;
        }


        //for now, GET value will be defined, will probably move to a HTTPS POST
        //so I can identify the user on the website more securely



        //Patch note override
        if(pageDestinations.matches("patch_notes")) {
            //stringBuilder.delete(0, stringBuilder.length()); //clear stringbuilder and replace
            changeTitle("Patch Notes");
            return "https://www.prestigecode.com/projects/senior/WebView/patch_notes.php";
        } else {
            stringBuilder.append("&?id=" + superUser.getID());
            stringBuilder.append("&?token=" + superUser.getToken());
        }
        return ""+stringBuilder.toString();
    }

    public void changeTitle(String string) {
        textViewTitle.setText(""+string);
    }

    public void finishActivity(int flag) {
        //Build intent to pass user back
        Intent intent = new Intent(AdaptiveWebView.this, AccountHub.class);

        //Add ParcelableExtra so we can return superUser to Main Activity
        intent.putExtra("User", superUser); //set inUser class as intent information
        setResult(Activity.RESULT_OK, intent); //send
    }
}
