package com.ligen.wellwatcher.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by ligen on 2016/5/23.
 */
public class User {

    String name;
    String password;
    String type;

    public User(String name, String password, String type) {
        this.name = name;
        this.password = password;
        this.type = type;
    }



    public String getName() {
        return name;
    }

    public User(JSONObject jsonObject) {
        this.name = jsonObject.getString("name");
        this.password = jsonObject.getString("password");
        this.type = jsonObject.getString("type");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("password", password);
        json.put("type", type);
        return json;
    }

}
