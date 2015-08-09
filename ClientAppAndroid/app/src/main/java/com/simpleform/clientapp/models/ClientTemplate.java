package com.simpleform.clientapp.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParseClassName("ClientsTemplates")
public class ClientTemplate extends ParseObject {

//    String name;
//    String owner;
//    String type;
//    String user;
//    ArrayList<FormField> fields;

    public String getName() {
        return getString("name");
    }
    public void setName(String value) {
        put("name", value);
    }

    public String getOwner() {
        return getString("owner");
    }
    public void setOwner(String value) {
        put("owner", value);
    }

    public String getType() {
        return getString("type");
    }
    public void setType(String value) {
        put("type", value);
    }

    public String getUser() {
        return getString("user");
    }
    public void setUser(String value) {
        put("user", value);
    }

    public ArrayList<FormField> getFields() {
        ArrayList<FormField> formFields = new ArrayList<FormField>();
        Type listType = new TypeToken<ArrayList<FormField>>() {}.getType();
        formFields = new Gson().fromJson(getJSONArray("fields").toString(), listType);
        return formFields;
    }
    public void setFields(ArrayList<FormField> formFields) {
        put("fields", formFields);
    }
}
