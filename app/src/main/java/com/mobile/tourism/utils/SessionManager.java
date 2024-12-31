package com.mobile.tourism.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "TourismAppSession";
    private static final String KEY_TOKEN = "jwt_token";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void clearToken() {
        editor.remove(KEY_TOKEN);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public String getAuthorizationHeader() {
        String token = getToken();
        if (token != null) {
            return "Bearer " + token;
        }
        return null;
    }
} 