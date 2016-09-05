package com.ligen.wellwatcher.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.dao.CheckInfoDao;
import com.ligen.wellwatcher.dao.UserDao;

import java.util.List;

public class AddDeviceActivity extends AppCompatActivity {

    LinearLayout mLlCheckpoint;
    EditText mEtDevice;
    Spinner mSpinType;
    Button mBtnAddCheckpoint;
    int checkpointCount = 0;
    CheckInfoDao dao;

    List<String> typeList;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        mLlCheckpoint = (LinearLayout) findViewById(R.id.ll_checkpoint);
        mEtDevice = (EditText) findViewById(R.id.et_device);
        mSpinType = (Spinner) findViewById(R.id.spinner_type);

        typeList = CheckInfoDao.getDao(this).getAllTypes();
        type = typeList.get(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinType.setAdapter(adapter);
        mSpinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void addCheckpoint(View view) {

        TextInputLayout til = new TextInputLayout(this);
        til.setHint("巡检点" + ++checkpointCount);
        EditText et = new EditText(this);
        til.addView(et, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlCheckpoint.addView(til, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void finish(View view) {

        dao = CheckInfoDao.getDao(this);
        String devicename = mEtDevice.getText().toString();
        if(devicename.length() == 0) {
            Toast.makeText(AddDeviceActivity.this, "设备名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mLlCheckpoint.getChildCount() == 0) {
            Toast.makeText(AddDeviceActivity.this, "巡检点至少有一个", Toast.LENGTH_SHORT).show();
            return;
        }

        dao.addDevice(devicename, type);
        for(int i=0; i< mLlCheckpoint.getChildCount(); i++) {
            TextInputLayout til = (TextInputLayout) mLlCheckpoint.getChildAt(i);
            EditText et = til.getEditText();
            String checkpoint = et.getText().toString();
            dao.addCheckpoint(checkpoint, devicename);
        }
        Toast.makeText(AddDeviceActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

        Intent data = new Intent(this, AdminActivity.class);
        data.putExtra("devicename", devicename);
        startActivity(data);
        finish();

    }

    public void cancel(View view) {
        Intent data = new Intent(this, AdminActivity.class);
        startActivity(data);
        finish();
    }
}
