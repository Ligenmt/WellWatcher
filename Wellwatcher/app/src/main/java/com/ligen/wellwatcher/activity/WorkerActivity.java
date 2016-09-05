package com.ligen.wellwatcher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.adapter.WorkerDeviceAdapter;
import com.ligen.wellwatcher.dao.CheckInfoDao;
import com.ligen.wellwatcher.dao.CheckRecordDao;
import com.ligen.wellwatcher.model.CheckRecord;
import com.ligen.wellwatcher.model.DeviceRecord;
import com.ligen.wellwatcher.util.HttpUtil;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

public class WorkerActivity extends AppCompatActivity {

    private static final String TAG = "WorkerActivity";
    public static String currentUser;
    private CheckInfoDao dao;

    private ListView mLvDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        currentUser = SharePrerenceUtil.getCurrentUser(this);
        String type = SharePrerenceUtil.getCurrentType(this);

        getSupportActionBar().setTitle("巡检人:" + currentUser);
        if (currentUser == null) {
            //跳回登陆界面
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        dao = CheckInfoDao.getDao(this);
        mLvDevice = (ListView) findViewById(R.id.lv_device);
        List<String> devicesList = dao.getDevicesByType(type);
        List<DeviceRecord> recordDevices = CheckRecordDao.getDao(this).getAllRecordDevicesByUsername(currentUser);
        mLvDevice.setAdapter(new WorkerDeviceAdapter(this, devicesList, recordDevices));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_worker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //登出
            case R.id.action_logout:
                SharePrerenceUtil.clearUser(this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                Toast.makeText(WorkerActivity.this, "登出", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_reset:
                resetData();
                break;
            case R.id.action_upload:
                uploadData();
                break;
        }
        return true;
    }

    /**
     * 重置巡检记录
     */
    private void resetData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("重置巡检记录")
            .setMessage("你确定要重置吗")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentUser = SharePrerenceUtil.getCurrentUser(WorkerActivity.this);
                    removeUserRecord();
                    Toast.makeText(WorkerActivity.this, "重置成功!", Toast.LENGTH_SHORT).show();
                    List<String> devicesList = dao.getDevicesByType(SharePrerenceUtil.getCurrentType(WorkerActivity.this));
                    List<DeviceRecord> recordDevices = CheckRecordDao.getDao(WorkerActivity.this).getAllRecordDevicesByUsername(currentUser);
                    mLvDevice.setAdapter(new WorkerDeviceAdapter(WorkerActivity.this, devicesList, recordDevices));

                    String filePath = "/mnt/sdcard/wellwatcher";
                    File rootFile = new File(filePath);
                    if (rootFile.isDirectory()) {
                        String[] images = rootFile.list();
                        for (String imagename : images) {
                            if (imagename.startsWith(currentUser + "_")) {
                                File file = new File(rootFile + "/" + imagename);
                                file.delete();
                                Log.i(TAG, "delete file:" + imagename);
                            }
                        }
                    }
                    dialog.dismiss();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
    }

    /**
     * 移除该用户所有巡检记录
     */
    private void removeUserRecord() {
        CheckRecordDao.getDao(WorkerActivity.this).removeCheckRecordByUsername(currentUser);
        CheckRecordDao.getDao(WorkerActivity.this).removeCheckDevicesByUsername(currentUser);
    }

    /**
     * 上传数据
     */
    private void uploadData() {

        String username = SharePrerenceUtil.getCurrentUser(WorkerActivity.this);
//        List<DeviceRecord> records = CheckRecordDao.getDao(this).getAllRecordDevicesByUsername(username);

        if(!HttpUtil.isConnect(this)) {
            Toast.makeText(WorkerActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("数据上传")
                .setMessage("确定要上传数据吗？这会重置当前巡检记录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        JSONObject data = new JSONObject();
                        data.put("user", currentUser);
                        List<DeviceRecord> deviceRecords = CheckRecordDao.getDao(WorkerActivity.this).getAllRecordDevicesByUsername(currentUser);
                        for (DeviceRecord deviceRecord : deviceRecords) {
                            JSONObject device = new JSONObject();
                            List<CheckRecord> recordList = CheckRecordDao.getDao(WorkerActivity.this).getRecordByUsernameAndDevice(currentUser, deviceRecord.getDevicename());
                            for (CheckRecord record : recordList) {
                                device.put(record.getCheckpoint(), record.getState());
                            }
                            data.put(deviceRecord.getDevicename(), device);
                        }
                        data.put("updatetime", new Date());
                        String url = SharePrerenceUtil.getUrl(WorkerActivity.this);
                        try {
                            String returnStr = HttpUtil.doPost(url, data.toJSONString());
                            if (returnStr.equals("500")) {
                                Toast.makeText(WorkerActivity.this, "上传失败，服务器异常", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (Exception e) {
                            Toast.makeText(WorkerActivity.this, "传输失败，请检查网络", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        }
                        Toast.makeText(WorkerActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        //重置当前记录
                        resetData();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

}
