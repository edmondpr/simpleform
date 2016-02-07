package com.simpleform.clientapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.simpleform.clientapp.models.ClientField;
import com.simpleform.clientapp.models.ClientTemplate;
import com.simpleform.clientapp.models.FormField;
import com.simpleform.clientapp.models.OwnerTemplate;

import java.util.ArrayList;

public class SimpleFormApplication extends Application {

    public static ArrayList<FormField> myProfileFormFields;

    @Override
    public void onCreate() {
        super.onCreate();
        //Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ClientTemplate.class);
        ParseObject.registerSubclass(OwnerTemplate.class);
        ParseObject.registerSubclass(ClientField.class);
        Parse.initialize(this, "HxlZ3d7O3BuGM6oION0qPLrtrh5TcqnGR1eRecmA", "NP9FyiUzHqbR9LEZXeJ4cgjkfHTTnieMAYJCZkhX");
    }
}