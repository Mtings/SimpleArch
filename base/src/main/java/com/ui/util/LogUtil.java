package com.ui.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import android.text.TextUtils;
import android.util.Log;

public class LogUtil {
    public static boolean DEBUG = false;
    public static void print(Object error) {
        if (DEBUG) {
            jsonFormatterLog("" + error);
        }
    }

    private static void jsonFormatterLog(String s) {
        if (TextUtils.isEmpty(s)) {
            Log.e("LOG:", "" + s);
            return;
        }
        String json = s;
        String tag = null;
        if (s.indexOf("{") > 0) {
            tag = s.substring(0, s.indexOf("{"));
            json = s.substring(s.indexOf("{"));
        }
        if (s.indexOf("{") > -1) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(json);
                json = gson.toJson(jsonElement);
                if (!TextUtils.isEmpty(tag)) {
                    json = tag + "\n" + json;
                }
                Log.e("LOG:", "" + json);
                return;
            } catch (Exception e) {
            }
        }
        Log.e("LOG:", "" + s);
    }
}
