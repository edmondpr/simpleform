package com.simpleform.clientapp.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseClassName;
import com.parse.ParseObject;

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
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        ArrayList<OwnerField> ownerFields = new ArrayList<OwnerField>();
        ownerFields = (ArrayList<OwnerField>) Arrays.asList(gson.fromJson(getJSONArray("fields").toString(), OwnerField[].class));
        return ownerFields;
    }
    public void setFields(ArrayList<OwnerField> ownerFields) {
        put("fields", ownerFields);
    }
}
