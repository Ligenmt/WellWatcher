package com.ligen.wellwatcher.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.model.DeviceRecord;

import java.util.List;

/**
 * 展示工人当前巡检进度
 * Created by ligen on 2016/6/10.
 */
public class WorkerDeviceAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDevicenameList;
    private List<DeviceRecord> mCheckedDevices;

    public WorkerDeviceAdapter(Context context, List<String> mDevicenameList, List<DeviceRecord> checkedDevices) {
        this.mContext = context;
        this.mDevicenameList = mDevicenameList;
        this.mCheckedDevices = checkedDevices;
    }


    @Override
    public int getCount() {
        return mDevicenameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDevicenameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_listview_device, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_device_name);
            holder.cv = (CardView) convertView.findViewById(R.id.cv_device);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String devicename = mDevicenameList.get(position);
        holder.tv.setText(devicename);
        holder.cv.setCardBackgroundColor(mContext.getResources().getColor(R.color.浅红));
        //无记录设备为红色，有记录无异常为绿色，有记录有异常为黄色
        for (DeviceRecord deviceRecord : mCheckedDevices) {
            if (deviceRecord.getDevicename().equals(devicename)) {
                if(deviceRecord.getState() == 1) {
                    holder.cv.setCardBackgroundColor(mContext.getResources().getColor(R.color.green));
                } else {
                    holder.cv.setCardBackgroundColor(mContext.getResources().getColor(R.color.yellow));
                }

            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        CardView cv;
    }
}
