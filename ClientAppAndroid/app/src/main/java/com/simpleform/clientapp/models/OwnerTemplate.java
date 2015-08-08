package com.simpleform.clientapp.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

@ParseClassName("OwnersTemplates")
public class OwnerTemplate extends ParseObject {

//    String owner;
//    String type;
//    ArrayList<OwnerField> fields;

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

    public ArrayList<OwnerField> getFields() {
        ArrayList<OwnerField> ownerFields = new ArrayList<OwnerField>();
        Type listType = new TypeToken<ArrayList<OwnerField>>() {}.getType();
        ownerFields = new Gson().fromJson(getJSONArray("fields").toString(), listType);
        return ownerFields;
    }
    public void setFields(ArrayList<OwnerField> ownerFields) {
        put("fields", ownerFields);
    }
}
