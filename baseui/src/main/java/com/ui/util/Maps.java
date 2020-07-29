package com.ui.util;

import java.util.HashMap;

public final class Maps {
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> HashMap<K, V> newHashMap(int initialCapacity) {
        return new HashMap<K, V>(initialCapacity);
    }
}
