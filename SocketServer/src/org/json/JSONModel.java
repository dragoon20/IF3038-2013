package org.json;

public abstract class JSONModel {
    
    public abstract JSONObject toJsonObject();
    public abstract void fromJsonObject(JSONObject jObject);
}