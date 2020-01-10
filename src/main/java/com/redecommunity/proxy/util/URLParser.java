package com.redecommunity.proxy.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by @SrGutyerrez
 */
public class URLParser {
    public static JSONObject parse(String url) {
        try {
            return URLParser.parse(new URL(url));
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static JSONObject parse(URL url) {
        try {
            URLConnection urlConnection = url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder data = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }

            return (JSONObject) JSONValue.parse(data.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
