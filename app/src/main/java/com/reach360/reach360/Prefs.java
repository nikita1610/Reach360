package com.reach360.reach360;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    SharedPreferences prefs;
    Context context;

    String prefsKey = "prefs";
    public static String appPackage = "appPackage";
    public static String sensitivity = "sensitivity";

    public Prefs(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE);
    }

    public String getStringPrefs(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public void setStringPrefs(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

}