package com.ui.util;

import java.util.List;

public class ListUtil {
    public static boolean isExistsListValue(List list, Object object) {
        if (list == null || list.size() == 0 || object == null) return false;
        for (Object obj : list) {
            if (object.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public static void removeListValue(List list, Object object) {
        if (list == null || list.size() == 0 || object == null) return;
        for (int i = list.size() - 1; i >= 0; i--) {
            Object obj = list.get(i);
            if (object.equals(obj)) {
                list.remove(i);
            }
        }
    }
}
