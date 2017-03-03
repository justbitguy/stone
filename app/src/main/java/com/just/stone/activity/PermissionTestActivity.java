package com.just.stone.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.just.stone.R;
import com.just.stone.util.LogUtil;

import java.security.Permission;

/**
 * Created by zhangjinwei on 2017/2/22.
 */

public class PermissionTestActivity extends Activity{

    private static final int PERMISSION_REQUEST_CODE = 12345;
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    private boolean isRequireCheck; // 是否需要系统权限检测, 防止和系统提示框重叠

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_test);
        startCheckPermission();
    }

    private void startCheckPermission() {
//        for (int i = 0; i < 3; ++i) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[i])) {
//                LogUtil.d("permission-test", "before: "  + i +  ", no request again.");
//            }
//        }
//        if (lacksPermissions(this, PERMISSIONS)) {
//
//        }

//        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
//        for (int i = 0; i < PERMISSIONS.length; ++i) {
//            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[i]);
//            LogUtil.d("perm-test", "shouldShow: " + shouldShow);
//        }

        long beforeTime = System.currentTimeMillis();
        int result = PermissionChecker.checkSelfPermission(this, PERMISSIONS[0]);
        long afterTime = System.currentTimeMillis();
        if (afterTime - beforeTime < 1000 && result <= -1) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }

    // 判断权限集合
    public boolean lacksPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }


    // 判断是否缺少权限
    private boolean lacksPermission(Context context, String permission) {
        //return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
        int result = PermissionChecker.checkSelfPermission(context, permission);
        boolean showShow = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        //ActivityCompat.requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
        return result <= PackageManager.PERMISSION_DENIED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        for (int i = 0; i < permissions.length; ++i) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
//                LogUtil.d("permission-test", ""  + i +  ", no request again.");
//            }
//        }

        LogUtil.d("perm-test", "requestCode: " + requestCode + " onRequestPermissionsResult");

        for (String perm : permissions) {
            LogUtil.d("perm-test", "perm: " + perm);
        }

        for (int i = 0; i < grantResults.length; ++i) {
            LogUtil.d("perm-test", "result: " + grantResults[i]);
        }
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
            showMissingPermissionDialog();
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
//        setResult(PERMISSIONS_GRANTED);
//        finish();
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.help);
//        builder.setMessage(R.string.string_help_text);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
//                setResult(PERMISSIONS_DENIED);
//                finish();
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}
