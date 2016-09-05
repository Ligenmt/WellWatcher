package com.ligen.wellwatcher.model;

/**
 * Created by ligen on 2016/6/29.
 */
public class DeviceRecord {

    String devicename;
    String username;
    String updatetime;
    String type;
    int state;

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DeviceRecord(String devicename, String username, String updatetime, int state) {
        this.devicename = devicename;
        this.username = username;
        this.updatetime = updatetime;
        this.state = state;
    }
}
