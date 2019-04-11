package com.example.life;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.life.util.PermissionManager;

public class WeatherActivity extends Fragment {
    private Button record;
    private Button sd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //不同的Activity对应不同的布局
        View view = inflater.inflate(R.layout.weather_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initPermission();
        initView();
        initListener();
        /**
         * TODO 实现底部菜单对应布局控件事件
         * */

    }
    private void initView() {
        record = (Button) getActivity().findViewById(R.id.record);
        sd = (Button) getActivity().findViewById(R.id.sd);
    }
   /* private void initPermission() {
        //同时申请多个权限
//        PermissionManager.getInstance(getApplicationContext()).execute(this, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);



        PermissionManager.getInstance(getContext()).executeDialog(getActivity(), Manifest.permission.RECORD_AUDIO,
                PermissionManager.getInstance(getContext()).new Builder(getActivity())
                        .setMessage("应用需要获取您的录音权限，是否授权？")
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher)
                        .setOk("OK")
                        .setCancel("CANCEL"));
        //请求单个，显示对话框的方式
//        PermissionManager.getInstance(this.getContext()).executeDialog(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION,
//                PermissionManager.getInstance(this.getContext()).new Builder(this.getActivity())
//                        .setMessage("应用需要获取您的GPS权限，是否授权？")
//                        .setTitle(getString(R.string.app_name))
//                        .setIcon(R.mipmap.ic_launcher)
//                        .setOk("OK")
//                        .setCancel("CANCEL"));

//        PermissionManager.getInstance(this.getContext()).executeDialog(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION,
//                PermissionManager.getInstance(this.getContext()).new Builder(this.getActivity())
//                        .setMessage("应用需要获取您的地址权限，是否授权？")
//                        .setTitle(getString(R.string.app_name))
//                        .setIcon(R.mipmap.ic_launcher)
//                        .setOk("OK")
//                        .setCancel("CANCEL"));

//        PermissionManager.getInstance(this.getContext()).executeDialog(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION,
//                PermissionManager.getInstance(this.getContext()).new Builder(this.getActivity())
//                        .setMessage("应用需要获取您的GPS权限，是否授权？")
//                        .setTitle(getString(R.string.app_name))
//                        .setIcon(R.mipmap.ic_launcher)
//                        .setOk("OK")
//                        .setCancel("CANCEL"));
    }
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance(getContext()).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initListener() {
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionManager.getInstance(getContext()).getGrantedInfo(Manifest.permission.ACCESS_FINE_LOCATION) ) {
                    Toast.makeText(getContext(), "gps权限已经获取", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "你还没有获取gps权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionManager.getInstance(getContext()).getGrantedInfo(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(getContext(), "地址权限已经获取", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "你还没有获取地址权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
