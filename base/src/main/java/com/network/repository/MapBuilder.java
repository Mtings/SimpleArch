package com.network.repository;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    private Map<K, V> map = new HashMap<>();

    public MapBuilder<K, V> put(K k, V v) {
        if (v == null || "".equals(v.toString())) return this;
        map.put(k, v);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
