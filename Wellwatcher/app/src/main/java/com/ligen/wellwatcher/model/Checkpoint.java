package com.ligen.wellwatcher.model;

/**
 * Created by ligen on 2016/5/28.
 */
public class Checkpoint {

    private String checkpoint;
    private String devicename;
    private int checkOrder;

    public Checkpoint(String checkpoint, int checkOrder, String devicename) {
        this.checkpoint = checkpoint;
        this.devicename = devicename;
        this.checkOrder = checkOrder;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public int getCheckOrder() {
        return checkOrder;
    }

    public void setCheckOrder(int checkOrder) {
        this.checkOrder = checkOrder;
    }
}
