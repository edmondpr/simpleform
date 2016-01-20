package com.simpleform.clientapp.models;

import com.parse.ParseObject;

public class ClientField extends ParseObject{
   // String type;
   // String defaultValue;
   // String connect;
   // String label;
   // String value;
   // Integer position;
   // String objectId;
   // String formId;


    public String getType() {
        return getString("type");
    }
    public void setType(String value) {
        put("type", value);
    }

    public String getDefaultValue() {
        return getString("defaultValue");
    }
    public void setDefaultValue(String value) {
        put("defaultValue", value);
    }

    public String getConnect() {
        return getString("connect");
    }
    public void setConnect(String value) {
        put("connect", value);
    }

    public String getLabel() {
        return getString("label");
    }
    public void setLabel(String value) {
        put("label", value);
    }

    public String getValue() {
        return getString("value");
    }
    public void setValue(String value) {
        put("value", value);
    }

    public Integer getPosition() {
        return getInt("position");
    }
    public void setPosition(Integer value) {
        put("position", value);
    }

    public String getObjectId() {
        return getString("objectId");
    }
    public void setObjectId(String value) {
        put("objectId", value);
    }

    public String getFormIdl() {
        return getString("formId");
    }
    public void setFormId(String value) {
        put("formId", value);
    }
}
