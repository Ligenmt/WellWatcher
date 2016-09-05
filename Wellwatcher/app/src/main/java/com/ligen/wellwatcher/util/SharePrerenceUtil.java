package com.ligen.wellwatcher.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ligen on 2016/5/29.
 */
public class SharePrerenceUtil {

    public static SharedPreferences sp;

    public static SharedPreferences getSP(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static String getCurrentUser(Context context) {
        return getSP(context).getString("user", null);
    }

    public static String getCurrentType(Context context) {
        return getSP(context).getString("type", null);
    }

    public static void clearUser(Context context) {
        SharedPreferences sp = getSP(context);
        sp.edit().putString("user", null).commit();
        sp.edit().putString("type", null).commit();
    }

    public static void setUrl(Context context, String url) {
        SharedPreferences sp = getSP(context);
        sp.edit().putString("uploadurl", url).commit();
    }

    public static String getUrl(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getString("uploadurl", "");
    }
}
