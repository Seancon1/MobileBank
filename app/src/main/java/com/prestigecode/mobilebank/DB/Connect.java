package com.prestigecode.mobilebank.DB;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class Connect extends AsyncTask<String, Void, String> {

    Context context;
    int flag;


    public Connect(Context context, int flag) {
        this.context = context;
        this.flag = flag;
    }

    @Override
    protected String doInBackground(String... strings) {

        if(flag == 0) {
            //GET METHOD
            try {
                String username = strings[0];
                //String password = strings[1];
                String link = "http://usf.prestigecode.com/srpj/index.php?WOW=" + username;

                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer();
                String line;

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    // break;
                    //include break to stop after one word, I need to read all information returned
                }

                in.close();
                Log.e("OUTPUT", sb.toString());
                return sb.toString();


            } catch (Exception e) {
                Log.e("Connect", "Exception: " + e.getMessage());
                return new String("error");
            }
        } else {
            //## POST METHOD

        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Connect", "onPostExecute: " + s);
        super.onPostExecute(s);
    }
}
