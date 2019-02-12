package com.prestigecode.mobilebank.DB;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class Query extends AsyncTask<String, Void, String> {


    Context context;
    HashMap<String, String> params;
    String superResult;

    String url = "http://usf.prestigecode.com/srpj/query.php";
    URL urlObj;
    HttpURLConnection conn;

    public Query(Context context,HashMap<String, String> params, String object) {
        this.context = context; //make sure we pass context so we can not crash
        this.params = params; //pass through
        this.superResult = object;
        try {
            urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
        } catch (Exception e) {
            Log.e("Query", e.toString());
        }
    }

    //One without object reference
    public Query(Context context,HashMap<String, String> params) {
        this.context = context; //make sure we pass context so we can not crash
        this.params = params; //pass through

        try {
            urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
        } catch (Exception e) {
            Log.e("Query", e.toString());
        }
    }


    @Override
    protected String doInBackground(String... strings) {
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        try{
            //CONN info was located here for scope concerns IF any error happens

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            String paramsString = sbParams.toString();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.e("Query", "result: " + result.toString());
            //superResult = result.toString();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

}
