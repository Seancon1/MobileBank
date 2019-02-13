package com.prestigecode.mobilebank.DB;

import android.content.Context;
import android.util.JsonToken;
import android.util.Log;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.prestigecode.mobilebank.MainActivity;
import com.prestigecode.mobilebank.User.BankAccount;
import com.prestigecode.mobilebank.User.JsonHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import com.fasterxml.jackson.*;

public class QueryThread extends Thread {

    Context context;
    HashMap<String, String> params;
    String superResult = "";

    String url = "http://usf.prestigecode.com/srpj/query.php";
    URL urlObj;
    HttpURLConnection conn;

    //One without object reference
    public QueryThread(Context context,HashMap<String, String> params) {
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
    public void run() {

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

            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);

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
            superResult = null;
            superResult = result.toString();
            //return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public String getResult() {

        return this.superResult;
    }

    public BankAccount getGsonJsonResult() {

        //Test to see if we can parse the json string into a usable object in the application
        String jsonString = this.getResult();
        BankAccount map = new Gson().fromJson(jsonString, BankAccount.class);
        return map;
    }

    public ArrayList<BankAccount> getArrayListResult() {
        return null;
    }

    /*
    public JSONObject getJSONArrayResult() throws Exception {

        List list = new JsonHelper.toList(this.superResult);

        JSONArray jsonArray = new JSONArray(superResult);
        //JSONObject jsonObject = new JSONObject(superResult);

    }
    */

    /*
    Thank you user Riser
    https://stackoverflow.com/questions/17037340/converting-jsonarray-to-arraylist

    I had a very hard time understanding and using the Gson library used here
    Shows how much I know of Java, syntax is hard to understand here for defining the Type type
     */
    public List<BankAccount> getBankAccounts() {
        //Parse card details from json format into an object, in this case it's BankAccount object.
        Gson gson = new Gson();
        Type type = new TypeToken<List<BankAccount>>(){}.getType();
        List<BankAccount> bankAccounts = gson.fromJson(this.getResult(), type);
        /*
        for (BankAccount account : bankAccounts){
            Log.i("Contact Details", account.toString());
        }
        */

        return bankAccounts;
    }


}


