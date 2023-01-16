package com.mkroshana.libraryfeedbacksystem.BackEnd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

public class SharedPreferenceClass
{
    private static final String USER_PREF = "user_feedback";
    private SharedPreferences appShared;
    private SharedPreferences.Editor prefsEditor;

    /**
     * Create a path to store data needed for tokens and temporary answers in the device
     * @param context
     */
    public SharedPreferenceClass(Context context)
    {
        appShared = context.getSharedPreferences(USER_PREF, Activity.MODE_PRIVATE);
        this.prefsEditor = appShared.edit();
    }

    /**
     * Retrieve integer values in the device storage
     * @param key
     * @return Integer Value
     */
    public int getValue_int(String key)
    {
        return appShared.getInt(key, 0);
    }

    /**
     * Store integer values in the device storage
     * @param key
     * @param value
     */
    public void setValue_int(String key, int value)
    {
        prefsEditor.putInt(key, value).commit();
    }

    /**
     * Retrieve string values in the device storage
     * @param key
     * @return String Value
     */
    public String getValue_string(String key)
    {
        return appShared.getString(key, "");
    }

    /**
     * Store string values in the device storage
     * @param key
     * @param value
     */
    public void setValue_string(String key, String value)
    {
        prefsEditor.putString(key, value).commit();
    }

    /**
     * Retrieve boolean values in the device storage
     * @param key
     * @return Boolean Value
     */
    public boolean getValue_boolean(String key)
    {
        return appShared.getBoolean(key, false);
    }

    /**
     * Store boolean values in the device storage
     * @param key
     * @param value
     */
    public void setValue_boolean(String key, boolean value)
    {
        prefsEditor.putBoolean(key, value).commit();
    }

    /**
     * Clears all the admin tokens and usernames
     */
    public void adminClear()
    {
        prefsEditor.remove("adminToken").commit();
        prefsEditor.remove("adminUsername").commit();
    }

    /**
     * Clears all the stored answers and the user token only
     */
    public void clear()
    {
        prefsEditor.remove("answer01").commit();
        prefsEditor.remove("answer02").commit();
        prefsEditor.remove("answer03").commit();
        prefsEditor.remove("answer04").commit();
        prefsEditor.remove("answer05").commit();
        prefsEditor.remove("answer06").commit();
        prefsEditor.remove("answer07").commit();
        prefsEditor.remove("token").commit();
    }
}
