package com.jhy.org.yueqiu.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Weather;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.RongUtils;
import com.jhy.org.yueqiu.utils.Preferences;
import com.jhy.org.yueqiu.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.common.FileUtils;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class App extends Application implements BDLocationListener {

    public static final class user {
        public static final String id = "user.id";
        public static final String name = "user.name";
        public static final String portrait_uri = "user.portrait_uri";
        public static final String token = "user.token";
        public static final String city_code = "user.city_code";
        public static final String loc_latitude = "user.location.latitude";
        public static final String loc_longitude = "user.location.longitude";
    }

    public static final class version {
        public static final int first_launch = 7;
        public static final int major = 3;
        public static final int minor = 7;
        public static final String ver = "v" + major + "." + minor;
    }

    public static final String PACKAGE_NAME = "com.jhy.org.yueqiu";
    public static final String FIRST_LAUNCH = "app.first_launch_" + version.first_launch;
    private static App app = null;
    private static boolean initialized = false;
    private static BDLocation userLocation = null;
    private static LocationClient locationClient = null;

    private static Person currentUser = null;
    private static List<OnReceiveUserLocationListener> locationListeners = null;

    private static Logx logx = new Logx(App.class);

    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex.install(this);
        app = this;
    }

    public synchronized static void initialize () {
        if (!initialized) {
            SDKInitializer.initialize(app);
            Bmob.initialize(app, Key.bmob.application_id);
            RongUtils.initialize(app);
            app.initLocation();
            initConfig();
            initialized = true;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // Enabling multidex support.
        // 1. 65535 limited
        // 2. run -- error ：Java.exe finished with non-zero exit value 2
        MultiDex.install(this);
    }

    public static void registerReceiveUserLocation (OnReceiveUserLocationListener listener) {
        if (listener != null) {
            if (userLocation == null) {
                locationListeners.add(listener);
            } else {
                listener.onReceiveUserLocation(userLocation);
            }
        }
    }

    public static BDLocation getUserLocation () {
        if (userLocation == null) {
            Preferences preferences = Preferences.getInstance();
            userLocation = new BDLocation();

            float latitude = preferences.get(user.loc_latitude, (float) userLocation.getLatitude());
            float longitude = preferences.get(user.loc_longitude, (float) userLocation.getLongitude());

            userLocation.setLatitude(latitude);
            userLocation.setLongitude(longitude);
        }
        return userLocation;
    }

    public static App getInstance () { return app; }

    // 获得当前进程的名字
    public static String getCurProcessName (Context context) {
        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void initLocation () {
        locationListeners = new ArrayList<OnReceiveUserLocationListener>();

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(this);
        locationClient.setLocOption(option);
        locationClient.start();
    }

    private static void initConfig () {
        Preferences preferences = Preferences.getInstance();
        if (preferences.get(FIRST_LAUNCH, true)) {
            Bitmap avatar = BitmapFactory.decodeResource(app.getResources(), R.drawable.icon_sidebar_head);
            Bitmap logo = BitmapFactory.decodeResource(app.getResources(), R.drawable.icon_sidebar_head);
            File avatarFile = FileUtils.convertBitmap2File(avatar, "/sdcard/yueqiu/user", "avatar.png");
            File logoFile = FileUtils.convertBitmap2File(logo, "/sdcard/yueqiu/user", "logo.png");

            preferences.set(user.portrait_uri, Uri.fromFile(avatarFile).toString())
                    .set(user.city_code, Weather.CITY_CODE_DEFAULT)
                    .set(FIRST_LAUNCH, false)
                    .commit();
            logx.e("file uri: " + Uri.fromFile(avatarFile).toString());
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        App.userLocation = bdLocation;
        locationClient.stop();
        logx.e("百度定位 成功: (" + bdLocation.getLatitude() + ", " + bdLocation.getLongitude() + ")");

        float latitude = (float) bdLocation.getLatitude();
        float longitude = (float) bdLocation.getLongitude();

        for (OnReceiveUserLocationListener listener : locationListeners) {
            listener.onReceiveUserLocation(bdLocation);
        }
        uploadUserLocation(latitude, longitude);

        String cityCode = Weather.convertCityToCode(bdLocation.getCity());
        Preferences.getInstance()
                .set(user.city_code, cityCode)
                .set(user.loc_latitude, latitude)
                .set(user.loc_longitude, longitude)
                .commit();
//        logx.e("保存cityCode: " + cityCode);
    }

    private void uploadUserLocation (double latitude, double longitude) {
        String userId = (String) BmobUser.getObjectByKey(app, "objectId");
        if (!Utils.isEmpty(userId)) {
            Person person = new Person();
            person.setLocation(new BmobGeoPoint(longitude, latitude));
            person.update(app, userId, new UpdateListener() {
                @Override
                public void onSuccess() {
//                    logx.e("上传用户位置信息 成功!");
                }

                @Override
                public void onFailure(int i, String s) {
                    logx.e("上传用户位置信息 失败: " + s);
                }
            });
        }
    }
}
