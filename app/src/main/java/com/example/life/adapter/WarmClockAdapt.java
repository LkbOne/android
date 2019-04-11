package com.example.life.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.life.R;
import com.example.life.entity.ClockEntity;

import java.util.Date;
import java.util.List;

import static com.example.life.util.DateUtil.clockFormat;

public class WarmClockAdapt extends ArrayAdapter {
    private int resourceId;
    public WarmClockAdapt(Context context, int textViewResourceId, List<ClockEntity> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClockEntity clock = (ClockEntity) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView timeText = (TextView) view.findViewById(R.id.time);//获取该布局内的图片视图
        TextView descText = (TextView) view.findViewById(R.id.desc);//获取该布局内的文本视图
        TextView subTime = (TextView)view.findViewById(R.id.subTime);
        RadioButton openRBtn = (RadioButton) view.findViewById(R.id.openRbt);
        timeText.setText(String.valueOf(clockFormat(new Date(clock.getTime()))));
        descText.setText(clock.getDesc());

        if(clock.getStatus() == 1){
            subTime.setText("未开启");
        }
        if(clock.getStatus() == 0){
            openRBtn.setChecked(true);
            if(clock.getCycle() == 0){
                long now = new Date().getTime();
                long time = clock.getTime();
                long interval = time > now ? (now - time + (60 * 60 * 24*1000)):(now - time);
                int hour = (int) (interval / (60 * 60 * 1000));
                int min = (int) ((interval % (60 * 60 * 1000)) / (60 * 1000));
                String intervalText = "每天 | ";
                if(hour > 0){
                    intervalText += String.valueOf(hour) + " 小时";
                }
                if(min > 0){
                    intervalText+= String.valueOf(min)+" 分钟后响铃";
                }
                if(hour == 0 && min == 0){
                    intervalText += "即将响铃";
                }
                subTime.setText(intervalText);
            }

        }
        return view;
    }
}
