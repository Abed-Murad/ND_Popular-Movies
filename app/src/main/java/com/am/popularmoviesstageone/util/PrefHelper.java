package com.am.popularmoviesstageone.util;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefHelper {

    private final static String PREF_NAME_KEY = "my_pref";

    private static PrefHelper sInstance = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;


    public static PrefHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PrefHelper();
            preferences = context.getSharedPreferences(PREF_NAME_KEY, Context.MODE_PRIVATE);
            editor = preferences.edit();
            editor.apply();
        }
        return sInstance;
    }

    public void saveValueWithIntegerKey(int key, String value) {
        editor.putString(String.valueOf(key), value).apply();
    }

    public void saveStringValue(String key, String value) {
        editor.putString(key, value).apply();
    }

    public void addBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public boolean isBooleanValueExists(String key) {
        return preferences.getBoolean(key, false);
    }


    public String getValueWithIntegerKey(int id) {
        return preferences.getString(String.valueOf(id), "No Value Found !");
    }

    public String getToken() {
        return preferences.getString("token", "No Token Found !");
    }

    public void saveToken(String token) {
        editor.putString("token", token).apply();
    }

    public void saveUserId(String userId) {
        editor.putString("user_id", userId).apply();
    }

    public String getUserId() {
        return preferences.getString("user_id", "No User Id Found !");
    }

    public void saveUserName(String userName) {
        editor.putString("user_name", userName).apply();
    }

    public String getUserName() {
        return preferences.getString("user_name", "No User Name Found !");
    }

    public void saveMenuSelection(String selection) {
        editor.putString("selection", selection).apply();
    }

    public String getMenuSelection() {
        return preferences.getString("selection", "No Selection Found !");
    }

}
