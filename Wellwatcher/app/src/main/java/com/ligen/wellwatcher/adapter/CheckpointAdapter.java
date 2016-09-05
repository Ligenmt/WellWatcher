package com.ligen.wellwatcher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.model.CheckRecord;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import java.io.File;
import java.util.List;

/**
 * 显示已巡检过的巡检点
 * Created by ligen on 2016/7/2.
 */
public class CheckpointAdapter extends BaseAdapter{

    List<CheckRecord> mCheckRecords;
    Context mContext;
    String mDevicename;
    String mCurrentUser;

    public CheckpointAdapter(String devicename, List<CheckRecord> mCheckRecords, Context context) {
        this.mCheckRecords = mCheckRecords;
        this.mContext = context;
        this.mDevicename = devicename;
        this.mCurrentUser = SharePrerenceUtil.getCurrentUser(context);
    }

    @Override
    public int getCount() {
        return mCheckRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return mCheckRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_listview_checkedrecord, null);
            holder = new ViewHolder();
            holder.tvCheckpoint = (TextView) convertView.findViewById(R.id.tv_checkpoint_name);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_checkpoint_state);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvCheckpoint.setText(mCheckRecords.get(position).getCheckpoint());
        int state = mCheckRecords.get(position).getState();
        if (state == 1) {
            holder.tvState.setText("正常");
        } else {
            holder.tvState.setText("异常");
        }
        String imgFileName = mCurrentUser + "_" + mDevicename + "_" + mCheckRecords.get(position).getCheckpoint();
        String path = "/mnt/sdcard/" + imgFileName + ".jpg";
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            holder.iv.setImageBitmap(bitmap);
            holder.iv.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    class ViewHolder {
        TextView tvCheckpoint;
        TextView tvState;
        ImageView iv;
    }
}


