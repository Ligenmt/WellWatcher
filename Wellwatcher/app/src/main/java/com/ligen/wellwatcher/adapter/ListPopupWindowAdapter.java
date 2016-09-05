package com.ligen.wellwatcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ligen.wellwatcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ligen on 2016/5/23.
 */
public class ListPopupWindowAdapter extends BaseAdapter {


    private List<String> mList;
    private Context mContext;

    public ListPopupWindowAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_popup, null, false);
            holder.itemTextView = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTextView.setText(mList.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView itemTextView;
    }
}
