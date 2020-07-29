package com.ui.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonUtil {
    public static <T> T fromJson(String json, Type classOfT) {
        Gson gson =new GsonBuilder().create();
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static String toRAWJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }


}
