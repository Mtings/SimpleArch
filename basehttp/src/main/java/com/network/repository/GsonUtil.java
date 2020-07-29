package com.network.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

final class GsonUtil {
    static Gson gson = new GsonBuilder().create();

    public static <T> T fromJson(String json, Type classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static String toRAWJson(Object object) {
        return gson.toJson(object);
    }


}
