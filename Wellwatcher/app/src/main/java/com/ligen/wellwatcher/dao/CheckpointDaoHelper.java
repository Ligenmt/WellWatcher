package com.ligen.wellwatcher.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ligen on 2016/5/27.
 */
public class CheckpointDaoHelper extends SQLiteOpenHelper {

    private static final String TAG = "CheckpointDaoHelper";
    private static final String DB_NAME = "wellwatcher.db";
    private static final String TABLE_TYPE = "type";
    private static final String TABLE_DEVICE = "device";
    private static final String TABLE_CHECKPOINT = "checkpoint";
    private static final String TABLE_USER = "user";
    private static final String TABLE_RECORD = "checkrecord";
    private static final String TABLE_CHECKDEVICE = "checkdevice";

    public CheckpointDaoHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

@Override
public void onCreate(SQLiteDatabase db) {
    //工种表
    String sql_job = "CREATE TABLE " + TABLE_TYPE + " (" +
            "checkorder integer primary key autoincrement, " +
            "type text " +
            ");";
    db.execSQL(sql_job);
    Log.i(TAG, "table job created");
    //设备表
    String sql_device = "CREATE TABLE " + TABLE_DEVICE + " (" +
            "checkorder integer primary key autoincrement, " +
            "devicename text, " +
            "type text" +
            ");";
    db.execSQL(sql_device);
    Log.i(TAG, "table device created");
    //巡检项表
    String sql_checkpoint = "CREATE TABLE " + TABLE_CHECKPOINT + " (" +
            "checkorder integer primary key autoincrement, " +
            "checkpoint text, " +
            "devicename text not null" +
            ");";
    db.execSQL(sql_checkpoint);
    Log.i(TAG, "table checkpoint created");
    //用户表
    String sql_user = "CREATE TABLE " + TABLE_USER + " (" +
            "id integer primary key autoincrement, " +
            "username text, " +
            "password text not null, " +
            "type text" +
            ");";
    db.execSQL(sql_user);
    Log.i(TAG, "table user created");
    //巡检记录表
    String sql_checkrecord = "CREATE TABLE " + TABLE_RECORD + " (" +
            "id integer primary key autoincrement, " +
            "username text, " +
            "devicename text not null, " +
            "checkpoint text, " + //状态 1正常 2异常
            "state text" +
            ");";
    db.execSQL(sql_checkrecord);
    Log.i(TAG, "table checkrecord created");
    //巡检设备表
    String sql_checkdevice = "CREATE TABLE " + TABLE_CHECKDEVICE + " (" +
            "id integer primary key autoincrement, " +
            "username text, " +
            "type text, " +
            "devicename text not null, " +
            "state text," +   //状态 1正常 2异常
            "updatetime text" +
            ");";
    db.execSQL(sql_checkdevice);
    Log.i(TAG, "table checkdevice created");
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_TYPE);
        db.execSQL("drop table if exists " + TABLE_DEVICE);
        db.execSQL("drop table if exists " + TABLE_CHECKPOINT);
        db.execSQL("drop table if exists " + TABLE_USER);
        db.execSQL("drop table if exists " + TABLE_RECORD);
        db.execSQL("drop table if exists " + TABLE_CHECKDEVICE);
        onCreate(db);
    }
}
