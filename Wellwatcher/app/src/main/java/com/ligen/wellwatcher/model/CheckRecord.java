package com.ligen.wellwatcher.model;

/**
 * Created by ligen on 2016/6/28.
 */
public class CheckRecord {

    String deivce; //设备名
    String checkpoint; //巡检点
    String username; //用户
    int state;  //状态 1正常 2异常

    public CheckRecord(String deivce, String checkpoint, String username, int state) {
        this.deivce = deivce;
        this.checkpoint = checkpoint;
        this.username = username;
        this.state = state;
    }

    public String getDeivce() {
        return deivce;
    }

    public void setDeivce(String deivce) {
        this.deivce = deivce;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
