package com.ligen.wellwatcher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;

public class WorkerActivity extends AppCompatActivity {

    private static final String TAG = "WorkerActivity";
    public String mCurrentUser;
    public String mCurrentType;

    private CheckInfoDao dao;

    private ListView mLvDevice;

    List<String> mDevicesList;
    List<DeviceRecord> mRecordDevices;
    WorkerDeviceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        mCurrentUser = SharePrerenceUtil.getCurrentUser(this);
        mCurrentType = SharePrerenceUtil.getCurrentType(this);

        getSupportActionBar().setTitle("巡检人:" + mCurrentUser);
        if (mCurrentUser == null) {
            //跳回登陆界面
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        dao = CheckInfoDao.getDao(this);
        mLvDevice = (ListView) findViewById(R.id.lv_device);
        mDevicesList = dao.getDevicesByType(mCurrentType);
        mRecordDevices = CheckRecordDao.getDao(this).getAllRecordDevicesByUsername(mCurrentUser);
        mAdapter = new WorkerDeviceAdapter(this, mDevicesList, mRecordDevices);
        mLvDevice.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDevicesList = dao.getDevicesByType(mCurrentType);
        mRecordDevices = CheckRecordDao.getDao(this).getAllRecordDevicesByUsername(mCurrentUser);
        mAdapter = new WorkerDeviceAdapter(this, mDevicesList, mRecordDevices);
        mLvDevice.setAdapter(mAdapter);
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
                    mCurrentUser = SharePrerenceUtil.getCurrentUser(WorkerActivity.this);
                    removeUserRecord();
                    Toast.makeText(WorkerActivity.this, "重置成功!", Toast.LENGTH_SHORT).show();
                    List<String> devicesList = dao.getDevicesByType(SharePrerenceUtil.getCurrentType(WorkerActivity.this));
                    List<DeviceRecord> recordDevices = CheckRecordDao.getDao(WorkerActivity.this).getAllRecordDevicesByUsername(mCurrentUser);
                    mLvDevice.setAdapter(new WorkerDeviceAdapter(WorkerActivity.this, devicesList, recordDevices));

                    String filePath = "/mnt/sdcard/wellwatcher";
                    File rootFile = new File(filePath);
                    if (rootFile.isDirectory()) {
                        String[] images = rootFile.list();
                        for (String imagename : images) {
                            if (imagename.startsWith(mCurrentUser + "_")) {
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
        CheckRecordDao.getDao(WorkerActivity.this).removeCheckRecordByUsername(mCurrentUser);
        CheckRecordDao.getDao(WorkerActivity.this).removeCheckDevicesByUsername(mCurrentUser);
    }

    /**
     * 上传数据
     */
    private void uploadData() {

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
                        data.put("user", mCurrentUser);
                        data.put("type", mCurrentType);
                        List<DeviceRecord> deviceRecords = CheckRecordDao.getDao(WorkerActivity.this).getAllRecordDevicesByUsername(mCurrentUser);
                        JSONObject records = new JSONObject();
                        for (DeviceRecord deviceRecord : deviceRecords) {
                            JSONObject device = new JSONObject();
                            List<CheckRecord> recordList = CheckRecordDao.getDao(WorkerActivity.this).getRecordByUsernameAndDevice(mCurrentUser, deviceRecord.getDevicename());
                            for (CheckRecord record : recordList) {
                                device.put(record.getCheckpoint(), record.getState());
                            }
                            records.put(deviceRecord.getDevicename(), device);
                        }
                        data.put("records", records);
                        Date date = new Date();
                        data.put("updatetime", date.toString());
                        data.put("_id", mCurrentUser + "_" + mCurrentType + "_" + date.toString());
                        data.put("photos", getRecordCameras());
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

    private JSONObject getRecordCameras() {

        JSONObject result = new JSONObject();
        String prefix = mCurrentType + "_" + mCurrentUser;
        String filePath = "/mnt/sdcard/wellwatcher";
        File rootFile = new File(filePath);
        if(!rootFile.exists()) {
            rootFile.mkdir();
        }
        String[] list = rootFile.list();
        for (String filename : list) {
            if(filename.startsWith(prefix)) {
                File photo = new File(filePath + "/" + filename);
                Uri uri = Uri.fromFile(photo);
                Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
                byte[] bytes = bStream.toByteArray();
                String base64 = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
                result.put(filename, base64);
            }
        }

        return result;
    }

}
