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

//    String owner;
//    String type;
//    String user;
//    ArrayList<ClientField> fields;

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

    public ArrayList<ClientField> getFields() {
        /*GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();*/
        ArrayList<ClientField> clientFields = new ArrayList<ClientField>();
        Type listType = new TypeToken<ArrayList<ClientField>>() {}.getType();
        clientFields = new Gson().fromJson(getJSONArray("fields").toString(), listType);
        //clientFields = Arrays.asList(gson.fromJson(getJSONArray("fields").toString(), ClientField[].class));
        return clientFields;
    }
    public void setFields(ArrayList<ClientField> clientFields) {
        put("fields", clientFields);
    }
}
