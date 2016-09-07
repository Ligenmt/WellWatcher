package com.ligen.wellwatcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        sb.append("巡检项：\n");
        for(Checkpoint cp : checkpointList) {
            sb.append("    ").append(cp.getCheckpoint()).append("\n");
        }
        this.setText(sb.toString());
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
        this.setLineSpacing(5, 1.2f);

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
