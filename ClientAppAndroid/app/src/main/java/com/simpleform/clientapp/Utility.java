package com.simpleform.clientapp;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class Utility {

    public static String getLoggedInUser() {
        String loggedInUser = "edmondpr@gmail.com";
        return loggedInUser;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
