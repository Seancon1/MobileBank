package com.prestigecode.mobilebank;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.*;
import android.widget.TextView;
import android.widget.Toast;
import com.prestigecode.mobilebank.User.User;
import com.prestigecode.mobilebank.User.User_MoneyRequest;
import com.prestigecode.mobilebank.User.Util;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textViewTitle = findViewById(R.id.textViewAdaptiveTextTitle);

        superUser = getIntent().getParcelableExtra("User"); //Set this superUser from previous activity via Intent
        webView = findViewById(R.id.WebView_AdaptiveView); //set the webview


        intentAction = getIntent().getExtras().getString("ACTION"); //set extra from AccountHub
        Log.e("AdaptiveWebView", "intentURL: " + intentAction + " - setURL result: " + setURL(intentAction));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(setURL(intentAction)); //Build URL from intent ACTION data
        webView.canGoForward();
        webView.canGoBack();
        /*
        onPageFinished is also included here in order for me
        to load the WebView with a new URL based
        on activity inside the WebView itself.
        Instead of loading another Android Activity.
         */


        webView.setWebViewClient(new WebViewClient() {

            /**
             * onReceivedError and onReceivedHttpError
             * prevent the user from encountering any undesirable webpage request, closes AdaptiveWebView.
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e("AdaptiveWebView", "error received" + request.toString());
                Toast.makeText(getApplicationContext(),"Error detected, returning you to safety" , Toast.LENGTH_SHORT).show();
                finish();
                //super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                Log.e("AdaptiveWebView", "http error received" + request.toString());
                Toast.makeText(getApplicationContext(),"Error detected, returning you to safety" , Toast.LENGTH_SHORT).show();
                finish();
                //super.onReceivedHttpError(view, request, errorResponse);
            }

            public void onPageFinished(WebView view, String url) {

                currentURL = webView.getUrl(); //update url for each load
                Log.e("AdaptiveWebView", "URL : " + currentURL);
                usableURL = new Util().dissectWebViewURL(currentURL); //parse url into something usable
                String actionWord = "";

                try{
                    //Actions to do when parameter is available inside usableURL
                    if(!usableURL.isEmpty()) {
                        //Will switch() to something better later
                        if(usableURL.containsKey("do")) {
                            actionWord = usableURL.get("do");
                        }

                    /*
                    action to do based on actionWord,
                    e.g ?do=Fetch1
                    key: do
                    value: Fetch1
                    actionWord = Fetch1
                     */

                        switch(actionWord) {
                            case "showall":
                                Toast.makeText(getApplicationContext(),"Showing your accounts" , Toast.LENGTH_SHORT).show();

                                break;
                            case "savings":
                                Toast.makeText(getApplicationContext(),"Action 'savings' Detected" , Toast.LENGTH_SHORT).show();
                                break;
                            case "OpenTransfer":
                                webView.clearHistory(); //so user does not go back
                                changeTitle("Money Transfer");
                                Log.e("AdaptiveWebView", "OpenTransfer detected | " +getAdditionalURLParam("outID") + " " + getAdditionalURLParam("recipientName"));
                                Intent intent = new Intent(AdaptiveWebView.this, User_MoneyRequest.class);
                                intent.putExtra("User", superUser); //set inUser class as intent information
                                intent.putExtra("TransferID", getAdditionalURLParam("outID")); //should get &outID=[THIS VALUE]
                                intent.putExtra("recipient", getAdditionalURLParam("recipientName")); //should get &outID=[THIS VALUE]
                                startActivity(intent); //send
                                break;
                            default:
                                //do nothing?
                                //Toast.makeText(getApplicationContext(),"AMBIGUOUS Action - URL: ?do= " + actionWord, Toast.LENGTH_SHORT).show();
                                //adaptiveWebViewIntent.putExtra("ACTION", "none");
                                break;
                        }
                    }
                } catch (Exception e) {
                    Log.e("AdaptiveWebView", "could not perform action from parsed URL - " + e.toString());
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public String setURL(String pageDestination) {
        StringBuilder stringBuilder = new StringBuilder();
        String baseURL = "https://www.prestigecode.com/projects/senior/WebView/AdaptiveView.php?";

        stringBuilder.append(baseURL); //add baseURL first

        Log.e("AdaptiveView", "pageDestinations: " + pageDestination);
        //Build the rest of our URL
        switch(pageDestination) {
            case "showall":
                changeTitle("Accounts");
                stringBuilder.append("&page=showall");
                break;
            case "openNewAccount":
                changeTitle("Open New Bank Account");
                stringBuilder.append("&page=openNewAccount");
                break;
            case "savings":
                changeTitle("Savings Account");
                stringBuilder.append("&page=showsavings");
                break;
            case "checking":
                changeTitle("Checking Account");
                stringBuilder.append("&page=showchecking");
                break;
            case "credit":
                changeTitle("Credit Account");
                stringBuilder.append("&page=showcredit");
                break;
            case "accHistory":
                changeTitle("Credit Account");
                stringBuilder.append("&page=accHistory");
                break;

            case "safety":
                break;

             default:
                 /*
                 Allows for non-hardcoded actions to still redirect to a page. Is this safe?
                  */
                 changeTitle("Ambiguous Action");
                 stringBuilder.append("&page=" + pageDestination);
                 break;
        }


        //for now, GET value will be defined, will probably move to a HTTPS POST
        //so I can identify the user on the website more securely




        //Patch note override
        if(pageDestination.matches("patch_notes")) {
            //stringBuilder.delete(0, stringBuilder.length()); //clear stringbuilder and replace
            changeTitle("Patch Notes");
            return "https://www.prestigecode.com/projects/senior/WebView/patch_notes.php";
        } else {
            stringBuilder.append("&id=" + superUser.getID());
            stringBuilder.append("&token=" + superUser.getToken());
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

    /**
     Provides a way to get another variable inside the current URL
     */
    public String getAdditionalURLParam(String param) {
        Log.e("AdaptiveWebView", ": getAdditionalURLparam - param:" + param);
        usableURL = new Util().dissectWebViewURL(currentURL); //parse url into something usable
        return  usableURL.get(param);
    }
}

/*
        if(pageDestination.matches("OpenTransfer")) {
            Log.e("AdaptiveWebView", "OpenTransfer detected");
            Log.e("AdaptiveWebView", "Additional url param: outID: " + getAdditionalURLParam("outID"));
            Intent intent = new Intent(AdaptiveWebView.this, User_MoneyRequest.class);
            intent.putExtra("User", superUser); //set inUser class as intent information
            intent.putExtra("TransferID", getAdditionalURLParam("outID")); //should get &outID=[THIS VALUE]
            setResult(Activity.RESULT_OK, intent); //send
        }
 */
