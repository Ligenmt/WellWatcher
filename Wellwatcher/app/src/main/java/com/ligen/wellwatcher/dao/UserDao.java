package com.ligen.wellwatcher.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ligen.wellwatcher.model.Checkpoint;
import com.ligen.wellwatcher.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ligen on 2016/5/28.
 */
public class UserDao {

    CheckpointDaoHelper helper;

    public static UserDao instance;

    public static UserDao getDao(Context context) {
        if (instance == null) {
            instance = new UserDao(context);
        }
        return instance;
    }

    private UserDao(Context context) {
        helper = new CheckpointDaoHelper(context);
    }

    public void addUser(String username, String password, String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into user(username, password, type) values(?,?,?)", new Object[]{username, password, type});
        db.close();
    }

    public User getUser(String username) {

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select username, password, type from user where username=?", new String[]{username});
        User admin = null;
        if(cursor.moveToNext()) {
            username = cursor.getString(0);
            String password = cursor.getString(1);
            String type = cursor.getString(2);
            admin = new User(username, password, type);
        }
        cursor.close();
        db.close();
        return admin;

    }

    public void changePassword(String username, String password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update user set password=? where username=?", new Object[]{password, username});
        db.close();
    }


    public List<User> getAllUsers() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select username, password, type from user", null);
        List<User> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String username = cursor.getString(0);
            String password = cursor.getString(1);
            String type = cursor.getString(2);
            list.add(new User(username, password, type));
        }
        cursor.close();
        db.close();
        return list;
    }

    public void removeUser(String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //移除用户的同时移除其巡检记录
        db.execSQL("delete from checkrecord where username=?", new String[]{username});
        db.execSQL("delete from checkdevice where username=?", new String[]{username});

        db.execSQL("delete from user where username = ?", new Object[]{username});
        db.close();
    }

}
