package com.just.stone.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.just.stone.ApplicationEx;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by luowp on 2016/7/12.
 */
public class DeviceUtil {
    public enum eScreenOrientation {
        PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
        LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
        PORTRAIT_REVERSE(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT),
        LANDSCAPE_REVERSE(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE),
        UNSPECIFIED_ORIENTATION(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        public final int activityInfoValue;

        eScreenOrientation(int orientation) {
            activityInfoValue = orientation;
        }
    }

    private final static String LOG_TAG = "LionToolsAboutDeviceActivity";
    private Context context;

    public DeviceUtil(Context c) {
        context = c;
    }
    //dp 转化成px
    public static int dp2Px(int dp) {
        final float scale = ApplicationEx.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2Dp(float px) {
        final float scale = ApplicationEx.getInstance().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        float fontScale = ApplicationEx.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        float fontScale = ApplicationEx.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) ApplicationEx.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return Math.min(metrics.widthPixels, metrics.heightPixels);
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) ApplicationEx.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    public static String getDeviceModel() {
        String model = Build.MODEL;
        String manufacture = Build.MANUFACTURER;
        if (model.toLowerCase().contains(manufacture.toLowerCase())) {
            model = model.substring(0, 1).toUpperCase() + model.substring(1);
            return model;
        } else {
            manufacture = manufacture.substring(0, 1).toUpperCase() + manufacture.substring(1);
            return manufacture.toUpperCase() + " " + model;
        }
    }

    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            return imei == null ? "" : imei;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean IsMobile(Context context) {
        boolean NetWorkOpened = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            NetWorkOpened = (networkInfo != null) && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
        } catch (Exception e) {
        }
        return NetWorkOpened;
    }

    public static boolean IsNetworkWIFI(Context context) {
        boolean NetWorkOpened = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            NetWorkOpened = (networkInfo != null) && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
        }
        return NetWorkOpened;
    }

    public static boolean isScreenLandscap() {
        return DeviceUtil.currentScreenOrientation() == DeviceUtil.eScreenOrientation.LANDSCAPE_REVERSE ||
                DeviceUtil.currentScreenOrientation() == DeviceUtil.eScreenOrientation.LANDSCAPE;
    }

    public static eScreenOrientation currentScreenOrientation() {
        final int rotation = ((WindowManager) ApplicationEx.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        final int orientation = ApplicationEx.getInstance().getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                    return eScreenOrientation.PORTRAIT;
                else
                    return eScreenOrientation.PORTRAIT_REVERSE;
            case Configuration.ORIENTATION_LANDSCAPE:
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                    return eScreenOrientation.LANDSCAPE;
                else
                    return eScreenOrientation.LANDSCAPE_REVERSE;
            default:
                return eScreenOrientation.UNSPECIFIED_ORIENTATION;
        }
    }

    // 停止自动亮度调节
    public static void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    // 开启亮度自动调节
    public static void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * 设置手机飞行模式
     *
     * @param context
     * @param enabling true:设置为飞行模式 false:取消飞行模式
     */
    public static void setAirplaneModeOn(Context context, boolean enabling) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, enabling ? 1 : 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", enabling);
        context.sendBroadcast(intent);
    }

    /**
     * 判断手机是否是飞行模式
     *
     * @param context
     * @return
     */
    public static boolean getAirplaneMode(Context context) {
        int isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        return (isAirplaneMode == 1) ? true : false;
    }

    public static boolean isAutoBrightness(Activity act) {
        boolean automicBrightness = false;
        ContentResolver aContentResolver = act.getContentResolver();
        try {
            automicBrightness = Settings.System.getInt(aContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Exception e) {
        }
        return automicBrightness;
    }

    // 改变亮度
    public static void setLightness(Activity act, int value) {
        try {
            Settings.System.putInt(act.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
            WindowManager.LayoutParams lp = act.getWindow().getAttributes();
            lp.screenBrightness = (value <= 0 ? 1 : value) / 255f;
            act.getWindow().setAttributes(lp);
        } catch (Exception e) {
        }
    }

    // 获取亮度
    public static int getLightness(Activity act) {
        return Settings.System.getInt(act.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, -1);
    }

    public static int getScreenOffTimeout(Activity act) {
        return Settings.System.getInt(act.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, -1);
    }

    public static void setScreenOffTimeout(Activity act, int value) {
        try {
            Settings.System.putInt(act.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, value);
        } catch (Exception e) {
        }
    }

    /**
     * 返回手机移动数据的状态
     *
     * @param pContext
     * @param arg      默认填null
     * @return true 连接 false 未连接
     */
    public boolean getMobileDataState(Context pContext, Object[] arg) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }

            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);

            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

            return isOpen;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置手机的移动数据
     */
    public void setMobileData(Context pContext, boolean pBoolean) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;

            Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);

            method.invoke(mConnectivityManager, pBoolean);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setHapticFeedback(Context context) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getHapticFeedback(Context context) {
        int result = 0;
        try {
            result = Settings.System.getInt(context.getContentResolver(), Settings.System.HAPTIC_FEEDBACK_ENABLED, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean getVibrate(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT < 16) {
            int type = audioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
            return type == AudioManager.VIBRATE_SETTING_ON;
        } else {
            int type = audioManager.getRingerMode();
            return type != AudioManager.RINGER_MODE_SILENT;
        }
    }

    private void setVibrate(Context context, boolean enable) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT < 16) {
            if (enable) {
                audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
            } else {
                audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
            }
        } else {
            if (enable) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            } else {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
        }
    }

    //Internal Storage exist
    public static boolean existInternalStorage() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public static long getInternalStorageTotal() {
        String internalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return getTotalSize(internalStoragePath);
    }

    public static long getInternalStorageFree() {
        String internalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return getAvailableSize(internalStoragePath);
    }

//    public static long getExternalSDCardTotalSize() {
//        return StorageVolumeUtil.getExternalStorageTotalSize(ApplicationEx.getInstance().getApplicationContext());
//    }

//    public static long getExternalSDCardFressSize() {
//        List<String> sdcardPaths = getExtSDCardPaths();
//        long freeSize = 0;
//        for (String path : sdcardPaths) {
//            freeSize += getAvailableSize(path);
//        }
//        return freeSize;
//    }

//    public static List<String> getExtSDCardPaths() {
//        return StorageVolumeUtil.getExternalStoragePaths(ApplicationEx.getInstance().getApplicationContext());
//    }

    public static long getAvailableSize(String path) {
        long availableMemory = 0;
        try {
            StatFs statFs = new StatFs(path);
            // 获取可用的内存块
            long blocks;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blocks = statFs.getAvailableBlocksLong();
            } else {
                blocks = statFs.getAvailableBlocks();
            }

            int size = statFs.getBlockSize();
            // 计算可用的内存空间
            availableMemory = blocks * size;
        } catch (Exception e) {

        }

        return availableMemory;
    }

    public static long getTotalSize(String path) {
        long totalSize = 0;
        try {
            StatFs statFs = new StatFs(path);
            // 获取可用的内存块
            long blocks;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blocks = statFs.getBlockCountLong();
            } else {
                blocks = statFs.getBlockCount();
            }

            long size = statFs.getBlockSize();
            // 计算可用的内存空间
            totalSize = blocks * size;
        } catch (Exception e) {

        }
        return totalSize;
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableRomSize() {
        File path = Environment.getDataDirectory();
        long blockSize = 0, availableBlocks = 0;

        try {
            StatFs stat = new StatFs(path.getPath());
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        } catch (Exception e) {
        }

        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static long getTotalRomSize() {
        File path = Environment.getDataDirectory();
        long blockSize = 0, totalBlocks = 0;

        try {
            StatFs stat = new StatFs(path.getPath());
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        } catch (Exception e) {

        }

        return totalBlocks * blockSize;
    }

    public static long storageUse(){
        if (DeviceUtil.existInternalStorage()){
            return DeviceUtil.getInternalStorageTotal() - DeviceUtil.getInternalStorageFree();
        } else {
            return DeviceUtil.getTotalRomSize() - DeviceUtil.getAvailableRomSize();
        }
    }

    public static long storageTotal(){
        if (DeviceUtil.existInternalStorage()){
            return DeviceUtil.getInternalStorageTotal();
        } else {
            return DeviceUtil.getTotalRomSize();
        }
    }


//    public static double storagePercent(){
//        long totalSize = storageTotal();
//        return totalSize > 0 ? storageUse() * 100 / totalSize : 0;
//    }
//
//    public static double romPercent(){
//        long romTotal = DeviceUtil.getTotalRomSize();
//        long romUsage = romTotal - DeviceUtil.getAvailableRomSize();
//        return romTotal > 0 ? romUsage * 100 / romTotal : 0;
//    }
//
//    public static double sdcardPercent(){
//        long sdcardTotal = DeviceUtil.getExternalSDCardTotalSize();
//        long sdcardUsage = sdcardTotal - DeviceUtil.getExternalSDCardFressSize();
//        return  sdcardTotal > 0 ? sdcardUsage * 100 / sdcardTotal : 0;
//    }

    public static boolean isScreenOn() {
        PowerManager pm = (PowerManager) ApplicationEx.getInstance().getSystemService(Context.POWER_SERVICE);

        boolean screenOn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            screenOn = pm.isInteractive();
        } else {
            screenOn = pm.isScreenOn();
        }

        return screenOn;
    }

    //px
    public static int getStatusBarHeight() {
        Resources resources = ApplicationEx.getInstance().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
