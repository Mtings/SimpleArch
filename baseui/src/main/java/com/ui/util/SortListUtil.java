package com.ui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings({"all"})
public class SortListUtil<E> {
    public void Sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    Method m1 = ((E) a).getClass().getMethod(method, new Class<?>[]{});
                    Method m2 = ((E) b).getClass().getMethod(method, new Class<?>[]{});
                    if (sort != null && "desc".equals(sort))//倒序
                        ret = m2.invoke(((E) b), new Object[]{}).toString().compareTo(m1.invoke(((E) a), new Object[]{}).toString());
                    else//正序
                        ret = m1.invoke(((E) a), new Object[]{}).toString().compareTo(m2.invoke(((E) b), new Object[]{}).toString());
                } catch (NoSuchMethodException ne) {
                } catch (IllegalAccessException ie) {
                } catch (InvocationTargetException it) {
                }
                return ret;
            }
        });
    }
}