package com.prestigecode.mobilebank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

public class Web_RegisterAccount extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__register_account);

        webView = findViewById(R.id.WebView_RegisterAccount);
        //URL url = new URL("https://www.prestigecode.com/projects/senior/WebView/");
        webView.loadUrl("https://www.prestigecode.com/projects/senior/WebView/RegisterNewAccount.php");
        //WebViewClient webViewClient = new WebViewClient().;
        //webView.setWebViewClient();
        //webView.po
    }






}
