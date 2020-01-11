package com.redecommunity.proxy.skin.factory;

import com.redecommunity.common.shared.skin.data.Skin;
import com.redecommunity.proxy.util.URLParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by @SrGutyerrez
 */
public class SkinFactory {
    public static Skin getSkin(String username) {
        try {
            String uuid = SkinFactory.getUUID(username);

            if (uuid == null) return null;

            URL url = new URL("https://api.minetools.eu/profile/" + uuid);

            JSONObject jsonObject = URLParser.parse(url);

            if (jsonObject == null) return null;

            JSONObject raw = (JSONObject) jsonObject.get("raw");
            JSONArray object = (JSONArray) raw.get("properties");
            JSONObject properties = (JSONObject) object.get(0);
            String value = properties.get("value").toString();
            String signature = properties.get("signature").toString();

            return new Skin(
                    0,
                    signature,
                    value,
                    System.currentTimeMillis(),
                    true,
                    username
            );
        } catch (MalformedURLException exception) {
            return null;
        }
    }

    private static String getUUID(String username) {
        try {
            URL url = new URL("https://api.minetools.eu/uuid/" + username);
            JSONObject jsonObject = URLParser.parse(url);

            if (jsonObject == null) return null;

            Boolean error = jsonObject.get("error") != null;

            if (error) return null;

            return (String) jsonObject.get("id");
        } catch (MalformedURLException exception) {
            return null;
        }
    }
}
