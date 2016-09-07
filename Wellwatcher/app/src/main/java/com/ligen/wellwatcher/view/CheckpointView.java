package com.ligen.wellwatcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ligen.wellwatcher.model.Checkpoint;

import java.util.List;

/**
 * 显示巡检点
 * Created by ligen on 2016/9/7.
 */
public class CheckpointView extends TextView {

    public CheckpointView(Context context, List<Checkpoint> checkpointList) {
        super(context);
        StringBuilder sb = new StringBuilder();
        for(Checkpoint cp : checkpointList) {
            sb.append(cp.getCheckpoint()).append("\n");
        }
        this.setText(sb.toString());
    }

    public CheckpointView(Context context) {
        super(context);
    }

    public CheckpointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckpointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
