package com.ligen.wellwatcher.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ligen.wellwatcher.model.Checkpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡检信息DAO，包括工种type，巡检地device，巡检点checkpoint
 * Created by ligen on 2016/5/28.
 */
public class CheckInfoDao {

    CheckpointDaoHelper helper;

    public static CheckInfoDao instance;

    public static CheckInfoDao getDao(Context context) {
        if (instance == null) {
            instance = new CheckInfoDao(context);
        }
        return instance;
    }

    private CheckInfoDao(Context context) {
        helper = new CheckpointDaoHelper(context);
    }

    public void addCheckpoint(String checkpoint, String deviceName, String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into checkpoint(checkpoint, devicename, type) values(?, ?, ?)", new Object[]{checkpoint, deviceName, type});
        db.close();
    }

    public List<Checkpoint> getCheckpoint(String devicename, String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select checkpoint, checkorder, devicename from checkpoint where devicename=? and type=? order by checkorder", new String[]{devicename, type});
        List<Checkpoint> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String checkpoint = cursor.getString(0);
            int checkorder = cursor.getInt(1);
            devicename = cursor.getString(2);
            list.add(new Checkpoint(checkpoint, checkorder, devicename));
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 删除某device下所有checkpoint
     * @param devicename
     */
    public void removeCheckpoint(String devicename) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from checkpoint where devicename = ?", new Object[]{devicename});
        db.close();
    }

    public void addDevice(String devicename, String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into device(devicename, type) values(?, ?)", new Object[]{devicename, type});
        db.close();
    }

    public List<String> getDevicesByType(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select devicename, checkorder from device where type=? order by checkorder", new String[]{type});
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String devicename = cursor.getString(0);
            list.add(devicename);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 移除device,user，先移除相关的checkpoint
     * @param devicename
     */
    public void removeDevice(String devicename) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from checkpoint where devicename = ?", new Object[]{devicename});
        db.execSQL("delete from device where devicename = ?", new Object[]{devicename});
        db.close();

    }

    public void addType(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into type(type) values(?)", new Object[]{type});
        db.close();
    }

    public List<String> getAllTypes() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select type from type", null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String type = cursor.getString(0);
            list.add(type);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 移除一个工种
     * @param type
     */
    public void removeType(String type) {
        //删除所有相关device
        List<String> devicesByType = getDevicesByType(type);
        for(String devicename : devicesByType) {
            removeDevice(devicename);
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        //删除type
        db.execSQL("delete from type where type = ?", new Object[]{type});
        db.close();
    }
}
