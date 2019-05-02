package com.example.life;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.life.net.UserId;
import com.example.life.net.service.LocationService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment searchFragment;
    private Fragment warmClockFragment;
    private Fragment weatherFragment;
    private Fragment[] fragments;
    private int lastFragment;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    LocationManager locationManager;
    Location location;
    LoginDialog loginDialog;
    RegisterDialog registerDialog;
    String uid;
    UserId app= (UserId) getApplication();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (UserId) getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own warn clock", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initFragment();
        initPermission();
        initLocation();
        initTextView(navigationView);
    }
    private void initTextView(NavigationView navigationView){
        loginDialog = new LoginDialog(this, app);
        registerDialog = new RegisterDialog(this);
        loginDialog.setRegisterDialog(registerDialog);

        View navHeaderView = navigationView.getHeaderView(0);
        loginDialog.setNavHeaderView(navHeaderView);
        uid = loginDialog.uid;
        TextView warmLoginTx = (TextView)navHeaderView.findViewById(R.id.WarmLoginTx);
        warmLoginTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                loginDialog.show();
            }
        });

    }
    private void initLocation(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 获取location对象
        location = getBestLocation(locationManager);
        send2Backend(location);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                300, 8, new LocationListener() {

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        send2Backend(locationManager
                                .getLastKnownLocation(provider));
                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                        send2Backend(null);
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        location = getBestLocation(locationManager);// 每次都去获取GPS_PROVIDER优先的location对象
                        send2Backend(location);
                    }
                });
    }
    private Location getBestLocation(LocationManager locationManager) {
        Location result = null;
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            result = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (result != null) {
                return result;
            } else {
                result = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return result;
            }
        }
        return result;
    }
    private void send2Backend(Location location) {
        if (location != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("位置信息：\n");
            sb.append("经度：" + location.getLongitude() + ", 纬度："
                    + location.getLatitude());
            Toast.makeText(MainActivity.this, "location:"+
                    app.getUserId() +
                    sb, Toast.LENGTH_SHORT).show();
            new LocationService().addLocation(app.getUserId(), location.getLongitude(),  location.getLatitude());
        } else {
            Toast.makeText(MainActivity.this, "location:空" , Toast.LENGTH_SHORT).show();
        }
    }
    private void initPermission() {
        boolean isPermission = checkSelfPermissionAll(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        if (isPermission) {
            Toast.makeText(MainActivity.this, "正在查看!", Toast.LENGTH_SHORT).show();
            return;
        }
//        请求权限
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
    }

    private boolean checkSelfPermissionAll(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isPermission = true;
            for (int grant : grantResults) {
                // 判断是否所有的权限都已经授予了
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isPermission = false;
                    break;
                }
            }
            if (isPermission) {
                Toast.makeText(MainActivity.this, "我看看", Toast.LENGTH_SHORT).show();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("天气功能需要访问")
//                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent();
//                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                                intent.setData(Uri.parse("package:" + getPackageName()));
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("退出", null);
//                builder.show();
            }
        }
    }


    private void initFragment() {
        searchFragment = new SearchActivity();
        warmClockFragment = new WarmClockActivity();
        ((WarmClockActivity) warmClockFragment).app = app;
        weatherFragment = new WeatherActivity();

        fragments = new Fragment[]{searchFragment, warmClockFragment, weatherFragment};
        lastFragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainView, searchFragment).show(searchFragment).commitAllowingStateLoss ();
    }

    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastFragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.mainView, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
            if (lastFragment != 0) {
                initLocation();
                switchFragment(lastFragment, 0);
                lastFragment = 0;
            }
//            return true;
        } else if (id == R.id.nav_gallery) {
            if (lastFragment != 1) {
                switchFragment(lastFragment, 1);
                lastFragment = 1;

            }

//            return true;
        } else if (id == R.id.nav_slideshow) {
            if (lastFragment != 2) {
                switchFragment(lastFragment, 2);
                lastFragment = 2;
            }
//            return true;
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
//        drawer.closeDrawers();
        return true;
    }
}
