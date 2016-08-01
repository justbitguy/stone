package com.just.stone.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.just.stone.ApplicationEx;
import com.just.stone.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InstalledPackageManager {
    private static InstalledPackageManager sPackageUtil = null;
    private ArrayList<PackageInfo> mPackages = new ArrayList<>();
    private InstalledPackageManager(){}

    public static InstalledPackageManager getInstance(){
        if (sPackageUtil == null) {
            synchronized (InstalledPackageManager.class) {
                if (sPackageUtil == null) {
                    sPackageUtil = new InstalledPackageManager();
                }
            }
        }
        return sPackageUtil;
    }

    public List<PackageInfo> getPackageInfoList(){
        Context ct = ApplicationEx.getInstance();
        PackageManager packageManager =getPackageManager(ct);
        synchronized (sPackageUtil) {
            if (mPackages.size() <= 0) {
                try {
                    mPackages.clear();
                    mPackages.addAll(packageManager.getInstalledPackages(0));
                } catch (Exception e) {
                    LogUtil.error(e);
                    try {
                        mPackages.clear();
                        mPackages.addAll(packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            return (List<PackageInfo>) mPackages.clone();
        }
    }

    public Map<String, String> getInstalledAppMap() {
        Map<String, String> appMap = new HashMap<String, String>();
        try {
            PackageManager packageManager =getPackageManager(ApplicationEx.getInstance());
            List<PackageInfo> packages = getPackageInfoList();
            for (PackageInfo packageInfo : packages) {
                try {
                    String name = String.valueOf(packageInfo.applicationInfo.loadLabel(packageManager));
                    if (!TextUtils.isEmpty(name)) {
                        name = name.toLowerCase();
                    }

                    appMap.put(packageInfo.packageName, name);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appMap;
    }

    public Set<String> getPkgNameOfInstalledApp() {
        Set<String> pkgSet = new HashSet<>();
        try {
            List<PackageInfo> packages = getPackageInfoList();
            for (PackageInfo packageInfo : packages) {
                pkgSet.add(packageInfo.packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pkgSet;
    }

    public boolean removePackageInfo(String packagename){
        synchronized (sPackageUtil) {
            for (PackageInfo pakge : mPackages) {
                if(pakge.packageName.equals(packagename)){
                    mPackages.remove(pakge);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addPackageInfo(Context context,String packagename){
        synchronized (sPackageUtil) {
            try {
                PackageInfo packageInfo = getPackageManager(context).getPackageInfo(packagename, 0);
                if(packageInfo == null) {
                    return false;
                }
                else{
                    mPackages.add(packageInfo);
                    return true;
                }
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
        return false;
    }

    public PackageManager getPackageManager(Context context){
        PackageManager packageManager = context.getPackageManager();
        return packageManager;
    }
}
