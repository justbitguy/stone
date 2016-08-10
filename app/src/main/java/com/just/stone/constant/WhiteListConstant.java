package com.just.stone.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zac on 2016/8/1.
 */
public class WhiteListConstant {

    private static final String[] SYSTEM_LIST= new String []{
        "com.android.backupconfirm",
        "com.cyanogenmod.filemanager",
        "com.android.providers.userdictionary",
        "com.dsi.ant.server",
        "com.android.providers.downloads.ui",
        "com.android.calllogbackup",
        "com.android.apps.tag",
        "com.cyngn.stats",
        "com.cyanogen.ambient.updater",
        "com.google.android.apps.cloudprint",
        "com.android.dreams.phototable",
        "com.android.galaxy4",
        "org.cyanogenmod.providers.datausage",
        "org.cyanogenmod.wallpapers.photophase",
        "org.cyanogenmod.cmsettings",
        "com.android.providers.partnerbookmarks",
        "org.cyanogenmod.resolver",
        "com.android.frameworks.telresources",
        "com.android.shell",
        "com.android.calculator2",
        "com.cyngn.cameranext",
        "org.cyanogenmod.bugreport",
        "com.android.managedprovisioning",
        "com.android.proxyhandler",
        "com.google.android.play.games",
        "com.google.android.feedback",
        "com.google.android.marvin.talkback",
        "com.android.wallpaper.livepicker",
        "com.google.android.onetimeinitializer",
        "cyanogenmod.platform",
        "com.cyngn.theme.chooser",
        "com.android.inputdevices",
        "com.qualcomm.timeservice",
        "com.android.bluetooth",
        "com.google.android.apps.photos",
        "com.android.printspooler",
        "com.android.smspush",
        "com.android.inputmethod.latin",
        "com.android.dreams.basic",
        "com.cyanogenmod.voicewakeup",
        "com.android.contacts",
        "com.android.noisefield",
        "com.android.vpndialogs",
        "com.android.server.telecom",
        "com.android.phasebeam",
        "com.cyngn.modlocksettings",
        "com.android.carrierconfig",
        "com.android.systemui",
        "com.android.phone",
        "com.android.nfc",
        "org.cyanogenmod.profiles",
        "com.google.android.apps.plus",
        "android",
        "com.android.certinstaller",
        "com.android.providers.telephony",
        "com.google.android.webview",
        "com.android.providers.downloads",
        "com.android.development",
        "com.google.android.gsf",
        "com.cyngn.themestore",
        "com.cyngn.lockscreen.live",
        "com.cyanogenmod.wallpapers",
        "com.android.dialer",
        "com.android.htmlviewer",
        "com.android.location.fused",
        "org.codeaurora.bluetooth",
        "org.cyanogenmod.screencast",
        "com.android.providers.contacts",
        "com.android.bluetoothmidiservice",
        "com.cyanogen.app.suggest",
        "com.cyngn.gallerynext",
        "com.cyanogenmod.setupwizard",
        "com.google.android.apps.walletnfcrel",
        "com.google.android.talk",
        "com.cyngn.devicemanager",
        "com.cyngn.chromecustomizations",
        "com.cyngn.audiofx",
        "com.android.sharedstoragebackup",
        "com.cyngn.livelockscreen.service",
        "com.google.android.apps.magazines",
        "com.google.android.setupwizard",
        "com.android.wallpapercropper",
        "com.cyngn.hexo",
        "com.cyngn.logger",
        "org.cyanogenmod.weather.provider",
        "com.google.android.calendar",
        "com.android.wallpaper",
        "com.qualcomm.shutdownlistner",
        "com.cyanogenmod.settings.device",
        "com.google.android.videos",
        "com.google.android.syncadapters.contacts",
        "com.android.providers.settings",
        "com.android.captiveportallogin",
        "com.google.android.backuptransport",
        "com.android.statementservice",
        "com.google.android.configupdater",
        "com.cyngn.bookmarkprovider",
        "com.android.pacprocessor",
        "com.android.terminal",
        "com.cyngn.discovery",
        "com.android.settings"
    };

    public static final String[] INPUT_METHOD_LIST = new String[]{
        "com.baidu.input"
    };

    public static final List<String> systemList(){
        return Arrays.asList(SYSTEM_LIST);
    }

    public static final List<String> inputMethodList(){
        return Arrays.asList(INPUT_METHOD_LIST);
    }
}
