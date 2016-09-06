package com.ligen.wellwatcher.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.dao.CheckInfoDao;
import com.ligen.wellwatcher.dao.CheckRecordDao;
import com.ligen.wellwatcher.model.CheckRecord;
import com.ligen.wellwatcher.model.Checkpoint;
import com.ligen.wellwatcher.model.DeviceRecord;
import com.ligen.wellwatcher.util.ImageResizer;
import com.ligen.wellwatcher.util.NfcUtil;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NfcReadActivity extends AppCompatActivity implements View.OnClickListener {

    private String mCurrentUser;
    private String mCurrentType;
    String mDevicename;
    LinearLayout mLlCheckpoint;
    TextView mTvDevice;
    TextView mTvCheckpoint;
    Button mBtnNext;
    Button mBtnCamera;
    ImageView mIvCamera;

    List<Checkpoint> mCheckpointList;
    List<CheckRecord> mCheckpointRecordList;

    int checkpointCount;
    int currentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_read_v2);
        mCurrentUser = SharePrerenceUtil.getCurrentUser(this);
        mCurrentType = SharePrerenceUtil.getCurrentType(this);

        initViews();
        initAnimations();

        if (mCurrentUser == null || mCurrentType == null) {
            Toast.makeText(NfcReadActivity.this, "您还未登录!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        mDevicename = NfcUtil.readNfcTag(getIntent());
        //判断巡检地是否存在
        boolean deviceExists = false;
        List<String> deviceList = CheckInfoDao.getDao(this).getDevicesByType(mCurrentType);
        for (String device : deviceList) {
            if (device.equals(mDevicename)) {
                deviceExists = true;
                break;
            }
        }
        if (!deviceExists) {
            Toast.makeText(NfcReadActivity.this, "未查询到巡检地", Toast.LENGTH_SHORT).show();
//            Button btn = (Button) findViewById(R.id.btn_submit);
//            btn.setText("退出");
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
            return;
        }

        //如果已经巡检过，直接跳到checkpoint activity
        DeviceRecord device = CheckRecordDao.getDao(this).findRecordByUsername(mCurrentUser, mDevicename);
        if (device != null) {
            Intent intent = new Intent(this, CheckpointActivity.class);
            intent.putExtra("devicename", device.getDevicename());
            startActivity(intent);
            finish();
        }

        mTvDevice.setText(mDevicename);

        //显示第一个巡检点
        mCheckpointRecordList = new ArrayList<>();
        mCheckpointList = CheckInfoDao.getDao(this).getCheckpoint(mDevicename);
        if(mCheckpointList.size() == 0) {
            Toast.makeText(NfcReadActivity.this, "未找到巡检点信息", Toast.LENGTH_SHORT).show();
            return;
        }
        checkpointCount = mCheckpointList.size();
        mTvCheckpoint.setText(mCheckpointList.get(0).getCheckpoint());


    }

    AnimatorSet animatorSet;
    ObjectAnimator transX1;
    ObjectAnimator transX2;

    private void initAnimations() {

        transX1 = ObjectAnimator.ofFloat(mLlCheckpoint, "translationX", 0, 1000f);
        transX1.setDuration(500);
        transX2 = ObjectAnimator.ofFloat(mLlCheckpoint, "translationX", 1000f, 0f);
        transX2.setDuration(500);
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playSequentially(transX1, transX2);
    }


    private void initViews() {

        mTvDevice = (TextView) findViewById(R.id.tv_device_name);
        mTvCheckpoint = (TextView) findViewById(R.id.tv_checkpoint);
        mLlCheckpoint = (LinearLayout) findViewById(R.id.ll_checkpoint);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnNext.setOnClickListener(this);
        mBtnCamera = (Button) findViewById(R.id.btn_camera);
        mBtnCamera.setOnClickListener(this);
        mIvCamera = (ImageView) findViewById(R.id.iv_carema);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
        }


        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_next:

                if(mBtnNext.getText().toString().equals("无异常，下一项")) {
                    //保存巡检点记录，无异常
                    CheckRecord record = new CheckRecord(mDevicename, mCheckpointList.get(currentCount).getCheckpoint(), mCurrentUser, 1);
                    mCheckpointRecordList.add(record);

                } else {
                    CheckRecord record = new CheckRecord(mDevicename, mCheckpointList.get(currentCount).getCheckpoint(), mCurrentUser, 2);
                    mCheckpointRecordList.add(record);

                }
                transX1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ++currentCount;
                        mIvCamera.setImageBitmap(null);
                        if (currentCount < checkpointCount) {
                            mCheckpointList.get(currentCount).getCheckpoint();
                            mTvCheckpoint.setText(mCheckpointList.get(currentCount).getCheckpoint());

                        } else {
                            //最后一项巡检完，保存记录
                            int hasError = 1;  //1 正常 2 异常
                            for(CheckRecord cr : mCheckpointRecordList) {
                                if(cr.getState() != 1) {
                                    hasError = 2;
                                }
                                CheckRecordDao.getDao(NfcReadActivity.this).saveCheckRecord(cr.getUsername(), cr.getDeivce(), cr.getCheckpoint(), cr.getState());
                            }
                            CheckRecordDao.getDao(NfcReadActivity.this).saveCheckDevice(mCurrentUser, mDevicename, hasError, new Date());

                            mTvCheckpoint.setText("巡检完成");
                            Toast.makeText(NfcReadActivity.this, "巡检完成", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NfcReadActivity.this, WorkerActivity.class));
                            finish();
                        }
                    }
                });
                animatorSet.start();
                mBtnNext.setText("无异常，下一项");

                break;

            case R.id.btn_camera:

                //拍照
                openCamera(mDevicename);

                break;
        }

    }

    private static int REQUEST_CAMERA = 3;
    private Uri imageUri;

    private void openCamera(String devicename) {
        String user = SharePrerenceUtil.getCurrentUser(this);
        String imgFileName = user + "_" + devicename + "_巡检点" + currentCount;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        //图片保存位置
        String filePath = "/mnt/sdcard/wellwatcher";
        File rootFile = new File(filePath);
        if(!rootFile.exists()) {
            rootFile.mkdir();
        }
        File file = new File("/mnt/sdcard/wellwatcher/", imgFileName + ".jpg");
        imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CAMERA) {
            Bitmap bitmap = ImageResizer.decodeSampledBitmap(imageUri.getPath());
            mIvCamera.setVisibility(View.VISIBLE);
            mIvCamera.setImageBitmap(bitmap);
//            if(data.getData() != null || data.getExtras() != null) {
//
//                //保存巡检点记录，有异常
//                CheckRecord record = new CheckRecord(mDevicename, mCheckpointList.get(currentCount).getCheckpoint(), mCurrentUser, 2);
//                mCheckpointRecordList.add(record);
//
//                Uri uri = data.getData();
////                Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
//                //裁剪图片
//
//            }
            mBtnCamera.setText("保存，下一项");
        }

    }
}
