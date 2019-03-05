package com.prestigecode.mobilebank.User;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

public class Util {

    /*
    This class will hold useful tools
     */


    /*
        Dissect url from each page load into parameters that can be inspected easier
    */
    public HashMap<String, String> dissectWebViewURL(String inURL) {

        HashMap<String, String> map = new HashMap<>();
        try {

            /*
            Thank you
            https://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection/13592324
             */

            //Using legacy library for this
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(inURL), "UTF-8");


            for (NameValuePair param : params) {
                System.out.println(param.getName() + " : " + param.getValue());
                map.put(param.getName(), param.getValue());
            }

            return map;
        } catch (Exception e) {
            Log.e("AccountHub", ""+e.toString());
            return null;
        }

    }

}
