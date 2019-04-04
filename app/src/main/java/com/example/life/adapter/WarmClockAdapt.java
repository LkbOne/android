package com.example.life.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.life.R;
import com.example.life.entity.ClockEntity;

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
        timeText.setText(String.valueOf(clockFormat(clock.getClock())));
        descText.setText(clock.getDesc());
        return view;
    }
}
