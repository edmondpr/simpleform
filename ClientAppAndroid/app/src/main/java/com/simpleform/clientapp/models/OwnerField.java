package com.simpleform.clientapp.models;

import com.parse.ParseObject;

public class OwnerField extends ParseObject {
    // String connect;
    // String formId
    // String label;
    // Integer left;
    // Integer position;
    // Integer top;
    // String objectId;

    public String getConnect() {
        return getString("connect");
    }
    public void setConnect(String value) {
        put("connect", value);
    }

    public String getFormIdl() {
        return getString("formId");
    }
    public void setFormId(String value) {
        put("formId", value);
    }

    public String getLabel() {
        return getString("label");
    }
    public void setLabel(String value) {
        put("label", value);
    }

    public Integer getLeft() {
        return getInt("left");
    }
    public void setLeft(Integer value) {
        put("left", value);
    }

    public Integer getPosition() {
        return getInt("position");
    }
    public void setPosition(Integer value) {
        put("position", value);
    }

    public Integer getTop() {
        return getInt("top");
    }
    public void setTop(Integer value) {
        put("top", value);
    }

    public String getObjectId() {
        return getString("objectId");
    }
    public void setObjectId(String value) {
        put("objectId", value);
    }
}
