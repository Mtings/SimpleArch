package com.ui.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;

public class ActivityStackManager {

    private static ArrayList<Activity> sActivityList = new ArrayList<Activity>();

    public static void remove(Activity activity) {
        synchronized (sActivityList) {
            sActivityList.remove(activity);
        }
    }

    public static void add(Activity activity) {
        synchronized (sActivityList) {
            sActivityList.add(activity);
        }
    }

    public static int getActivityCount() {
        return sActivityList.size() ;
    }

    public static Activity getTheLastActivity() {
        int index = sActivityList.size()-1;
        if(index > -1){

            return sActivityList.get(index);
        }

        return null;
    }

    public static void finish() {

        // clear activity
        synchronized (sActivityList) {
            for(Iterator<Activity> it = sActivityList.iterator(); it.hasNext(); ) {
                Activity item = it.next();
                item.finish();
            }
        }
        sActivityList.clear();
    }


    public static boolean isContain(String activityName){
        boolean result = false;
        for (Activity activity : sActivityList) {
            if(activity.getClass().getName().equals(activityName)){
                result = true;
                break;
            }
        }

        return result;
    }

}