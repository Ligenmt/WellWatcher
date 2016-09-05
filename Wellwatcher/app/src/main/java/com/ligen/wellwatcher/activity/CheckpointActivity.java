package com.ligen.wellwatcher.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.adapter.CheckpointAdapter;
import com.ligen.wellwatcher.dao.CheckRecordDao;
import com.ligen.wellwatcher.model.CheckRecord;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import java.util.List;

/**
 * 显示已经巡检过的设备
 */
public class CheckpointActivity extends AppCompatActivity {

    private TextView mTvDevice;
    private ListView mLvCheckpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);

        mTvDevice = (TextView) findViewById(R.id.tv_device_name);
        mLvCheckpoint = (ListView) findViewById(R.id.lv_checkpoint);

        String devicename = getIntent().getStringExtra("devicename");
        String username = SharePrerenceUtil.getCurrentUser(this);

        mTvDevice.setText(devicename);
        List<CheckRecord> checkRecords = CheckRecordDao.getDao(this).getRecordByUsernameAndDevice(username, devicename);
        CheckpointAdapter adapter = new CheckpointAdapter(devicename, checkRecords, this);
        mLvCheckpoint.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkpoint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                break;
        }
        return true;
    }
}
