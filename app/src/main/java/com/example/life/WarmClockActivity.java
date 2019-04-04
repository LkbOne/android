package com.example.life;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.life.adapter.WarmClockAdapt;
import com.example.life.broadcast.AlarmBroadcast;
import com.example.life.entity.ClockEntity;
import com.example.life.net.service.WarnClockService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class WarmClockActivity extends Fragment {
    private ListView warmList;
    private WarmClockAdapt adapter;
    private List<ClockEntity> clockList = new ArrayList<>();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //不同的Activity对应不同的布局
        View view = inflater.inflate(R.layout.warn_clock_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * TODO 实现底部菜单对应布局控件事件
         * */

        initClock();
        adapter = new WarmClockAdapt(getActivity(), R.layout.clock_item, clockList);
        warmList = (ListView) getActivity().findViewById(R.id.warm_list);
        warmList.setAdapter(adapter);
        initAlarm();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimeClock();
                Snackbar.make(view, "test", Snackbar.LENGTH_LONG)
                        .setAction("right", null).show();
            }
        });
    }

    private void initTimeClock(){
        Calendar cal = Calendar.getInstance();

        new TimePickerDialog(getContext(),2, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                ClockEntity clockEntity = new ClockEntity();
                clockEntity.setClock(cal.getTime());
                clockEntity.setDesc("no desc");
                clockList.add(clockEntity);
                adapter.notifyDataSetChanged();
                setAlarm(cal);
                new WarnClockService().addWarmClock();
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
    }


    private void setAlarm(Calendar calendar) {
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
    }
    private void initAlarm() {
        // 实例化AlarmManager
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        // 设置闹钟触发动作
        Intent intent = new Intent(getActivity(), AlarmBroadcast.class);
        intent.setAction("startAlarm");
        pendingIntent = PendingIntent.getBroadcast(getContext(), 110, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 设置闹钟
        /**
         * AndroidL开始，设置的alarm的触发时间必须大于当前时间 5秒
         *
         * 设置一次闹钟-(5s后启动闹钟)
         *
         * @AlarmType int type：闹钟类型
         * ELAPSED_REALTIME：在指定的延时过后，发送广播，但不唤醒设备（闹钟在睡眠状态下不可用）。如果在系统休眠时闹钟触发，它将不会被传递，直到下一次设备唤醒。
         * ELAPSED_REALTIME_WAKEUP：在指定的延时过后，发送广播，并唤醒设备（即使关机也会执行operation所对应的组件）。延时是要把系统启动的时间SystemClock.elapsedRealtime()算进去的。
         * RTC：指定当系统调用System.currentTimeMillis()方法返回的值与triggerAtTime相等时启动operation所对应的设备（在指定的时刻，发送广播，但不唤醒设备）。如果在系统休眠时闹钟触发，它将不会被传递，直到下一次设备唤醒（闹钟在睡眠状态下不可用）。
         * RTC_WAKEUP：指定当系统调用System.currentTimeMillis()方法返回的值与triggerAtTime相等时启动operation所对应的设备（在指定的时刻，发送广播，并唤醒设备）。即使系统关机也会执行 operation所对应的组件。
         *
         * long triggerAtMillis：触发闹钟的时间。
         * PendingIntent operation：闹钟响应动作（发广播，启动服务等）
         */
//        alarmManager.set(@AlarmType int type, long triggerAtMillis, PendingIntent operation);

        /**
         * AndroidL开始repeat的周期必须大于60秒
         *
         * 设置精准周期闹钟-该方法提供了设置周期闹钟的入口，闹钟执行时间严格按照startTime来处理，使用该方法需要的资源更多，不建议使用。
         *
         * @AlarmType int type：闹钟类型
         * long triggerAtMillis：触发闹钟的时间。
         * long intervalMillis：闹钟两次执行的间隔时间
         * PendingIntent operation：闹钟响应动作（发广播，启动服务等）
         */
//        alarmManager.setRepeating(@AlarmType int type, long triggerAtMillis, long intervalMillis, PendingIntent operation);

        /**
         * AndroidL开始repeat的周期必须大于60秒
         *
         * 设置不精准周期闹钟- 该方法也用于设置重复闹钟，与第二个方法相似，不过其两个闹钟执行的间隔时间不是固定的而已。它相对而言更省电（power-efficient）一些，因为系统可能会将几个差不多的闹钟合并为一个来执行，减少设备的唤醒次数。
         *
         * @AlarmType int type：闹钟类型
         * long triggerAtMillis：触发闹钟的时间。
         * long intervalMillis：闹钟两次执行的间隔时间
         * 内置变量
         * INTERVAL_DAY： 设置闹钟，间隔一天
         * INTERVAL_HALF_DAY： 设置闹钟，间隔半天
         * INTERVAL_FIFTEEN_MINUTES：设置闹钟，间隔15分钟
         * INTERVAL_HALF_HOUR： 设置闹钟，间隔半个小时
         * INTERVAL_HOUR： 设置闹钟，间隔一个小时
         *
         * PendingIntent operation：闹钟响应动作（发广播，启动服务等）
         */
//        alarmManager.setInexactRepeating(@AlarmType int type, long triggerAtMillis, long intervalMillis, PendingIntent operation);


        // 设置时区（东八区）-需要加权限SET_TIME_ZONE
//        alarmManager.setTimeZone("GMT+08:00");

        // 取消闹钟
//        alarmManager.cancel(pendingIntent);
    }

    private void initClock(){
        ClockEntity clockEntity = new ClockEntity();
        clockEntity.setClock(new Date());
        clockEntity.setDesc("success");
        ClockEntity clockEntity2 = new ClockEntity();
        clockEntity2.setClock(new Date());
        clockEntity2.setDesc("success_*_2");
        ClockEntity clockEntity3 = new ClockEntity();
        clockEntity3.setClock(new Date());
        clockEntity3.setDesc("success_*_3");
        ClockEntity clockEntity4 = new ClockEntity();
        clockEntity4.setClock(new Date());
        clockEntity4.setDesc("success_*_4");
        clockList.add(clockEntity);
        clockList.add(clockEntity2);
        clockList.add(clockEntity3);
        clockList.add(clockEntity4);

    }

}
