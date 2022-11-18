package com.hacktiv.ecommerce.Login;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLogin(boolean login) {
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    public boolean getLogin() {
        return  sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    public void setUID(String UID) {
        editor.putString("KEY_UID", UID);
        editor.commit();
    }

    public String getUID() {
        return sharedPreferences.getString("KEY_UID", "");
    }

    public void setLevel(String level) {
        editor.putString("KEY_LEVEL", level);
        editor.commit();
    }

    public String getLevel() {
        return sharedPreferences.getString("KEY_LEVEL", "");
    }
}
