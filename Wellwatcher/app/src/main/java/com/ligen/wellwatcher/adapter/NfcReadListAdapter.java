package com.ligen.wellwatcher.adapter;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.model.Checkpoint;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import java.io.File;
import java.util.List;

/**
 * 显示nfc读取后的巡检点
 * Created by ligen on 2016/6/8.
 */
public class NfcReadListAdapter extends BaseAdapter {

    private AppCompatActivity mContext;
    private String mDevicename;
    private List<Checkpoint> mCheckpointList;
//    private List<Boolean> mCheckBoxList;
    private JSONArray mCheckList;

    public NfcReadListAdapter(AppCompatActivity context, List<Checkpoint> checkpointList, String devicename) {
        this.mContext = context;
        this.mCheckpointList = checkpointList;
        this.mDevicename = devicename;

        mCheckList = new JSONArray();
        for (int i=0; i<checkpointList.size(); i++) {
            JSONObject check = new JSONObject();
            check.put("checkpoint", mCheckpointList.get(i).getCheckpoint());
            check.put("checkstate", false); //true为有异常
            mCheckList.add(check);
        }
    }

    @Override
    public int getCount() {
        return mCheckpointList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCheckpointList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_listview_nfccheckpoint, null);
            holder = new ViewHolder();
            holder.tvCheckpoint = (TextView) convertView.findViewById(R.id.tv_checkpoint_name);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            holder.btnCamera = (Button) convertView.findViewById(R.id.btn_camera);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    mCheckBoxList.set(position, isChecked);
                    mCheckList.getJSONObject(position).put("checkstate", isChecked);
                }
            });
            holder.btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera(mDevicename, mCheckpointList.get(position).getCheckpoint());
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvCheckpoint.setText(mCheckpointList.get(position).getCheckpoint());
        holder.cb.setChecked(mCheckList.getJSONObject(position).getBooleanValue("checkstate"));

        return convertView;
    }

    private void openCamera(String devicename, String checkpoint) {

        String user = SharePrerenceUtil.getCurrentUser(mContext);
        String imgFileName = user + "_" + devicename + "_" + checkpoint;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        //图片保存位置
        File file = new File("/mnt/sdcard/", imgFileName  + ".jpg");
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        mContext.startActivityForResult(intent, 1);
    }

    class ViewHolder {
        TextView tvCheckpoint;
        CheckBox cb;
        Button btnCamera;
        ImageView iv;
    }

    public JSONArray getCheckResult() {
        return mCheckList;
    }
}
