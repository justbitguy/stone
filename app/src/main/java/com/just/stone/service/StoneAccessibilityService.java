package com.just.stone.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import com.just.stone.ApplicationEx;
import com.just.stone.R;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Zac on 2016/7/28.
 */
public class StoneAccessibilityService extends AccessibilityService {
    public static final String KEY_INTENT_CLICK_RESULT = "result";

    public static final int CLICK_RESULT_SUCCEED = 1;
    public static final int CLICK_RESULT_FAIL = -1;
    public static final int CLICK_RESULT_DISABLE = -2;
    public static final int CLICK_RESULT_NOT_FOUND = -3;
    public static final int CLICK_RESULT_NOT_SUPPORT = -4;
    public static final int REQUEST_SHOW_ACCESSIBILITY_SETTINGS = 1586;

    public static AtomicBoolean sIsStopping = new AtomicBoolean(true);

    private static final String ACTION_CALLBACK = "PowerAccessibilityService.Callback";
    private static final String ACTION_CHANGE_CANDO = "PowerAccessibilityService.ChangeCando";
    public static final String KEY_INTENT_CANDO = "cando";

    private static volatile boolean sCando = false;

    /**
     * 强制停止的按钮id
     */
    private static final String[] FORCE_STOP_BUTTON_IDS = {
            "com.android.settings:id/force_stop_button",
    };
    /**
     * 确定按钮的id
     */
    private static final String[] CONFIRM_BUTTON_IDS = {

    };
    /**
     * 强制停止按钮的字符串id
     */
    private static final int[] FORCE_STOP_BUTTON_TEXT_IDS = {

    };
    /**
     * 强制停止字符串
     */
    private static final String[] FORCE_STOP_BUTTON_TEXT = new String[FORCE_STOP_BUTTON_TEXT_IDS.length];
    /**
     * 强制停止额外的字符串
     */
    private static final String[] FORCE_STOP_BUTTON_TEXT_EXT = new String[]{
            "FORCE STOP",
            "结束运行",
            "結束運行",
            "結束操作",
            "强行停止",
            "強行停止",
            "强制停止",
            "فرض الإيقاف",
            "Beenden erzwingen",
            "Forzar detención",
            "Forcer l'arrêt",
            "Termina",
            "強制停止",
            "Forçar parada",
            "Durmaya zorla",
    };
    /**
     * 确定按钮的字符串 id
     */
    private static final int[] CONFIRM_BUTTON_TEXT_IDS = {
            R.string.ok_string,
            android.R.string.ok,
            android.R.string.yes
    };
    /**
     * 确定按钮的字符串
     */
    private static final String[] CONFIRM_BUTTON_TEXT = new String[CONFIRM_BUTTON_TEXT_IDS.length];
    /**
     * 确定按钮额外的字符串
     */
    private static final String[] CONFIRM_BUTTON_TEXT_EXT = new String[]{
            "强制停止",
            "強制停止",
            "是",
            "ok",
            "确定",
            "確定",
            "موافق",
            "Aceptar",
            "Oke",
            "Tamam",
            "yes",
            "ОК",//注意这个并不是英文的OK
            "Да",
            "Во ред",
            "ΘĶ",
            "‏نعم",
            "Ja",
            "Sí",
            "Oui",
            "Ya",
            "Si",
            "はい",
            "Sim",
            "Evet",
            "예",
            "យល់ព្រម",
            "လုပ်မည်",
            "ใช่",
            "ඔව්",
            "ಹೌದು",
            "Bəli",
            "Da",
            "Ano",
            "Bai",
            "Já",
            "Ndiyo",
            "‏بەڵێ",
            "Jā",
            "Taip",
            "Igen",
            "Ha",
            "Tak",
            "Áno",
            "Kyllä",
            "Có",
            "Ναι",
            "Иә",
            "Так",
            "დიახ",
            "Այո",
            "‏بله",
            "አዎ",
            "ठीक छ",
            "होय",
            "हो",
            "हाँ",
            "হ্যাঁ",
            "ਹਾਂ",
            "હા",
            "ஆம்",
            "Oldu",
            "Onartu",
            "კარგი",
    };

    private boolean mReadstringable;
    private boolean mCanuseid;


    public void onCreate(){
        mCanuseid = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
        mReadstringable = readForceStopString() != null;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getChangeCandoAction(getApplicationContext()));
        registerReceiver(mChangeCandoReceiver, intentFilter);
    }

    @Override
    protected void onServiceConnected (){
    }

    @Override
    public void onAccessibilityEvent (AccessibilityEvent event){
        try {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                String packageName = "";
                if (event.getPackageName() != null)
                    packageName = event.getPackageName().toString();

                if (TextUtils.isEmpty(packageName)) {
                    return;
                }

//                boolean focusViewIsActivity = false;
//                String focusViewClassName = event.getClassName().toString();
//                for (String activity : ACTIVITY_KEY) {
//                    if (focusViewClassName.toLowerCase().contains(activity) && !focusViewClassName.equals("com.quick.gamebooster.activity.StubRecorderActivity")) {
//                        focusViewIsActivity = true;
//                        break;
//                    }
//                }
//
//                AppManagerUtil appUtil = new AppManagerUtil(this);
//                String currentLauncherPkg = appUtil.getHomeLauncherPkg();
//                String lastBoostGame = ApplicationEx.getInstance().lastBoostGame;
//
//                if (Stringutil.isEmpty(lastBoostGame) && GameManager.getInstance().isGameOrInBoostList(packageName)) {
//                    lastBoostGame = packageName;
//                }
//
//                //5.0以下是可以直接获取top activity
//                boolean visible = packageName.equals(lastBoostGame);
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                    visible = appUtil.getTopProcesses(this, lastBoostGame);
//                }
//                else if (UserPermissionManager.isStatAccessPermissionAllow(this, false)) {
//                    String currentTopActivity = AppManagerUtil.getTopActivity(this);
//                    if (!Stringutil.isEmpty(currentTopActivity)) {
//                        visible = currentTopActivity.equals(lastBoostGame);
//                    }
//                }
//
//                if (focusViewClassName.equals("com.android.systemui.media.MediaProjectionPermissionActivity")) {
//                    visible = true;
//                }
//
//                LogUtil.d(LOG_TAG, "onAccessibilityEvent--" + event.getPackageName().toString() + "--visible " + visible + "--lastFocusWindow:" + mLastFocusWindow + "--focusViewClassName:" + focusViewClassName);
//
//                if (!packageName.equals(mLastFocusWindow)) {
//                    LogUtil.d(LOG_TAG, "onAccessibilityEvent--FIX" + String.format("(%s %s %s %s %s %s)", visible, mLastBoostGameIsVisible, focusViewIsActivity, packageName.equals(currentLauncherPkg), packageName.equals(currentLauncherPkg), lastBoostGame.isEmpty()));
//                    //cover view is not activity always means lastFocusView not hide complete, so we reset visible to true
//                    if (!visible && mLastBoostGameIsVisible &&
//                            (!focusViewIsActivity && !packageName.equals(currentLauncherPkg) && !mLastFocusWindow.isEmpty() && !lastBoostGame.isEmpty())) {
//                        visible = true;
//                    }
//
//                    //boost app visual status changed
//                    Intent gameIntent = new Intent();
//                    gameIntent.setAction("com.quick.gamebooster.intent.action.APP_STARTED");
//                    gameIntent.putExtra("packageName", packageName);
//                    gameIntent.putExtra("lastBoostGame", lastBoostGame);
//                    gameIntent.putExtra("visible", visible);
//                    gameIntent.putExtra("isHome", currentLauncherPkg.equals(packageName));
//                    sendBroadcast(gameIntent);
//                    mLastFocusWindow = packageName;
//                    mLastBoostGameIsVisible = visible;
//                }
            }

            if (!sCando) return;

            AccessibilityNodeInfo source = event.getSource();

            if (source == null) return;

            int etype = event.getEventType();
            if (etype != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return;

            CharSequence classname = event.getClassName();
            if (classname == null) {
                return;
            }


            LogUtil.d("force stop", "start");
            String eclass = classname.toString();
            LogUtil.d("force stop", "click start" + eclass);
            if (eclass.endsWith("InstalledAppDetailsTop") || "com.android.settings.applications.InstalledAppDetailsActivity".equals(eclass)) {// 刚刚弹出设置界面，点击  强制停止 按钮
                int result = clickAnyByViewId(source, FORCE_STOP_BUTTON_IDS);

                if (result != CLICK_RESULT_SUCCEED && mReadstringable) {
                    result = Math.max(result, clickAnyByViewText(source, readForceStopString()));
                    LogUtil.d("force stop", "case1");
                }
                if (result != CLICK_RESULT_SUCCEED) {
                    fullString(FORCE_STOP_BUTTON_TEXT_IDS, FORCE_STOP_BUTTON_TEXT);
                    result = Math.max(result, clickAnyByViewText(source, FORCE_STOP_BUTTON_TEXT));
                    LogUtil.d("force stop", "case2");
                }
                if (result != CLICK_RESULT_SUCCEED) {
                    result = Math.max(result, clickAnyByViewText(source, FORCE_STOP_BUTTON_TEXT_EXT));
                    LogUtil.d("force stop", "case3");
                }

                if (result == CLICK_RESULT_NOT_FOUND) {
                    //记录日志
                    //LogUtil(source, FORCE_STOP_BUTTON_TEXT_EXT);
                    LogUtil.d("force stop", "case4");
                    //				sendResult(result);
                }
                if (result != CLICK_RESULT_SUCCEED) {
                    LogUtil.d("force stop", "case5");
                    sendResult(result);
                }

                LogUtil.d("force stop", "case6 " + result);

            } else if (eclass.endsWith("AlertDialog")) {//弹出确认对话框 点击确认按钮
                int result = clickAnyByViewId(source, CONFIRM_BUTTON_IDS);

                if (result != CLICK_RESULT_SUCCEED) {
                    fullString(CONFIRM_BUTTON_TEXT_IDS, CONFIRM_BUTTON_TEXT);//
                    result = Math.max(result, clickAnyByViewText(source, CONFIRM_BUTTON_TEXT));
                    LogUtil.d("force stop", "btn1");
                }
                if (result != CLICK_RESULT_SUCCEED) {
                    result = Math.max(result, clickAnyByViewText(source, CONFIRM_BUTTON_TEXT_EXT));
                    LogUtil.d("force stop", "btn2");
                }

                if (result == CLICK_RESULT_NOT_FOUND) {
                    //记录日志
                    //log(source, CONFIRM_BUTTON_TEXT_EXT);
                    LogUtil.d("force stop", "btn3");
                }
                sendResult(result);
                LogUtil.d("force stop", "btn4 " + result);
            }
            else {
                LogUtil.d("force stop", "unrecgnized window");
            }

            LogUtil.d("force stop", "click end" + eclass);
        } catch (Exception e) {
            LogUtil.d("error", e.getStackTrace().toString());
            LogUtil.d("poweraccessibillty", e.getMessage());
        }
    }

    @Override
    public void onInterrupt(){
    }

    private void sendResult(int result) {
        Intent intent = new Intent();
        intent.setAction(getCallBackAction(getApplicationContext()));
        intent.putExtra(KEY_INTENT_CLICK_RESULT, result);
        if (!sCando) return;
        sCando = false;
        sendBroadcast(intent);
    }

    /***
     * @param source
     * @param viewIds
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int clickAnyByViewText(AccessibilityNodeInfo source, String... texts) {
        int result = CLICK_RESULT_NOT_FOUND;

        for (int i = 0; i < texts.length; i++) {
            String text = texts[i];
            //API 14
            List<AccessibilityNodeInfo> infos = source.findAccessibilityNodeInfosByText(text);

            int cresult = clickView(infos);
            result = Math.max(result, cresult);
            if (result == CLICK_RESULT_SUCCEED) break;
        }

        return result;
    }

    /***
     * 传入一堆id，只要有一个点击成功就返回true，全部没点击成功返回false
     *
     * @param source
     * @param viewIds
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private int clickAnyByViewId(AccessibilityNodeInfo source, String... viewIds) {
        if (!mCanuseid) {
            return CLICK_RESULT_NOT_SUPPORT;
        }

        int result = CLICK_RESULT_NOT_FOUND;

        for (int i = 0; i < viewIds.length; i++) {
            //API 18
            List<AccessibilityNodeInfo> infos = source.findAccessibilityNodeInfosByViewId(viewIds[i]);

            int cresult = clickView(infos);
            result = Math.max(result, cresult);
            if (result == CLICK_RESULT_SUCCEED) break;
        }

        return result;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int clickView(List<AccessibilityNodeInfo> infos) {
        int result = CLICK_RESULT_NOT_FOUND;

        if (infos == null || infos.isEmpty()) return result;

        for (int i = 0; i < infos.size(); i++) {
            AccessibilityNodeInfo tmpinfo = infos.get(i);
            if (tmpinfo == null || !tmpinfo.getClassName().equals(Button.class.getName())) continue;

            if (!tmpinfo.isEnabled()) {
                result = Math.max(result, CLICK_RESULT_DISABLE);
                continue;
            }
//            if (!sCando) return 0;
            boolean cresult = tmpinfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            result = Math.max(result, cresult ? CLICK_RESULT_SUCCEED : CLICK_RESULT_FAIL);

            tmpinfo.recycle();
        }

        return result;
    }

    private String readForceStopString() {
        try {
            String pname = "com.android.settings";

            Context context = createPackageContext(pname, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            Resources resource = context.getResources();
            int forcestopid = resource.getIdentifier("force_stop", "string", pname);
            String string = context.getString(forcestopid);
            return string;
        } catch (Exception e) {
            return null;
        }
    }

    private void fullString(int[] ids, String[] strings) {
        if (ids.length != strings.length) {
            throw new IllegalArgumentException("ids.length 必须和 strings.length 相同");
        }
        for (int i = 0; i < ids.length; i++) {
            strings[i] = getString(ids[i]);
        }
    }

    public static String getCallBackAction(Context context) {
        return context.getPackageName() + "." + ACTION_CALLBACK;
    }

    public static boolean showAccessibilitySettings(Activity activity) {
        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        try {
            activity.startActivityForResult(intent, REQUEST_SHOW_ACCESSIBILITY_SETTINGS);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static boolean isEnabled(Context context) {
        boolean enable = false;
        try {
            int accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);

            if (accessibilityEnabled == 1) {
                String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                if (settingValue != null && settingValue.contains(StoneAccessibilityService.class.getSimpleName()) && settingValue.contains(context.getPackageName())) {
                    enable = true;
                }
            }
        } catch (Settings.SettingNotFoundException e) {
        }finally {
            //ApplicationEx.getInstance().resetAccessibilityStatusIfNeed(enable);
            return enable;
        }
    }

    public static void setCando(Context context, boolean cando) {
        Intent stopintent = new Intent();
        stopintent.setAction(getChangeCandoAction(context));
        stopintent.putExtra(KEY_INTENT_CANDO, cando);
        context.sendBroadcast(stopintent);
    }

    public static String getChangeCandoAction(Context context) {
        return /*context.getPackageName() + "." + */ACTION_CHANGE_CANDO;
    }

    @Override
    public void onDestroy() {
        //LogUtil.d(LOG_TAG, "PowerAccessibilityService onDestroy");
        unregisterReceiver(mChangeCandoReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mChangeCandoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("force-stop","receive mChangeCandoReceiver!");
            sCando = intent.getBooleanExtra(KEY_INTENT_CANDO, false);
        }
    };
}
