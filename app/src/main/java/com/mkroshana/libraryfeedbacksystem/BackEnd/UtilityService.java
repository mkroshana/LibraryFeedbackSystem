package com.mkroshana.libraryfeedbacksystem.BackEnd;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UtilityService
{
    /**
     * Hide the on-screen keyboard
     * @param view
     * @param activity
     */
    public void hideKeyboard(View view, Activity activity)
    {
        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Starts the application in fullscreen mode
     * @param currentActivity
     */
    public void ImmersiveFSMode (Activity currentActivity)
    {
        int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        currentActivity.getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
    }
}
