package com.prestigecode.mobilebank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.prestigecode.mobilebank.User.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EncodingUtils;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import com.prestigecode.mobilebank.User.Util;


public class AccountHub extends AppCompatActivity {

    User superUser = null;
    WebView webView;
    String currentURL;
    HashMap<String, String> usableURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_hub);


        //Very important
        superUser = getIntent().getParcelableExtra("User"); //Set this superUser from previous activity via Intent

        webView = findViewById(R.id.WebView_AccountHub);

        //Thanks https://stackoverflow.com/questions/7586564/how-to-send-post-data-with-code-in-an-android-webview
        String postData = "ID=" + superUser.getID() + "&token=" + superUser.getToken();

        webView.postUrl("https://www.prestigecode.com/projects/senior/WebView/Lobby_AccountBalancePreview.php", EncodingUtils.getBytes(postData, "BASE64"));


        /*
        Thanks
        https://stackoverflow.com/questions/3149216/how-to-listen-for-a-webview-finishing-loading-a-url
         */
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                currentURL = webView.getUrl(); //update url for each load
               // showCurrentURL();

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
                            case "1":
                                Toast.makeText(getApplicationContext(),"Action 1 Detected" , Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                //do nothing?
                                Toast.makeText(getApplicationContext(),"URL: ?do= " + actionWord, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                } catch (Exception e) {
                    Log.e("AccountHub", "could not perform action from parsed URL - " + e.toString());
                }

            }
        });
    }

    public void showCurrentURL() {
        Toast.makeText(getApplicationContext(),"URL: " + currentURL, Toast.LENGTH_SHORT).show();
    }


}
