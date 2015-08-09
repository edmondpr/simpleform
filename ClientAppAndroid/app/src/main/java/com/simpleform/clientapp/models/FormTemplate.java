package com.simpleform.clientapp.models;

import java.util.ArrayList;

public class FormTemplate {
    private String objectId;
    private String name;
    private String owner;
    private String type;
    private String user;
    private ArrayList<FormField> fields;

    public FormTemplate() {
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<FormField> getFields() {
        return fields;
    }

    public void setFields(ArrayList<FormField> fields) {
        this.fields = fields;
    }
}
