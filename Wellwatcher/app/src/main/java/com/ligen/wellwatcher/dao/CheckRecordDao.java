package com.ligen.wellwatcher.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ligen.wellwatcher.model.CheckRecord;
import com.ligen.wellwatcher.model.DeviceRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ligen on 2016/6/28.
 */
public class CheckRecordDao {

    CheckpointDaoHelper helper;

    public static CheckRecordDao instance;

    public static CheckRecordDao getDao(Context context) {
        if (instance == null) {
            instance = new CheckRecordDao(context);
        }
        return instance;
    }

    private CheckRecordDao(Context context) {
        helper = new CheckpointDaoHelper(context);
    }

    public void saveCheckRecord(String username, String device, String checkpoint, int state) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into checkrecord(username, devicename, checkpoint, state) values(?,?,?,?)", new Object[]{username, device, checkpoint, state});
        db.close();
    }

    public List<CheckRecord> getRecordByUsernameAndDevice(String username, String device) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select username, devicename, checkpoint, state from checkrecord where username=? and devicename=?", new String[]{username, device});
        List<CheckRecord> checkRecords = new ArrayList<>();
        while (cursor.moveToNext()) {
            device = cursor.getString(1);
            String checkpoint = cursor.getString(2);
            int state = cursor.getInt(3);
            checkRecords.add(new CheckRecord(device, checkpoint, username, state));
        }
        cursor.close();
        db.close();
        return checkRecords;

    }

    public void removeCheckRecordByUsername(String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from checkrecord where username=?", new String[]{username});
        db.close();
    }

    public void saveCheckDevice(String username, String device, int state, Date updatetime) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into checkdevice(username, devicename, state, updatetime) values(?,?,?,?)", new Object[]{username, device, state, updatetime.toString()});
        db.close();
    }

    public List<DeviceRecord> getAllRecordDevicesByUsername(String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select username, devicename, updatetime, state from checkdevice where username=?", new String[]{username});
        List<DeviceRecord> checkDevices = new ArrayList<>();
        while (cursor.moveToNext()) {
            String devicename = cursor.getString(1);
            String updatetime = cursor.getString(2);
            int state = cursor.getInt(3);
            checkDevices.add(new DeviceRecord(devicename, username, updatetime, state));
        }
        cursor.close();
        db.close();
        return checkDevices;

    }

    public DeviceRecord findRecordByUsername(String username, String devicename) {

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select devicename, username, updatetime, state from checkdevice where username=? and devicename=?", new String[]{username, devicename});
        DeviceRecord deviceRecord = null;
        if (cursor.moveToNext()) {
            devicename = cursor.getString(0);
            username = cursor.getString(1);
            String updatetime = cursor.getString(2);
            int state = cursor.getInt(3);
            deviceRecord = new DeviceRecord(devicename, username, updatetime, state);
        }
        return deviceRecord;
    }

    public void removeCheckDevicesByUsername(String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from checkdevice where username=?", new String[]{username});
        db.close();
    }


}
