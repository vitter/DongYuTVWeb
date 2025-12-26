package xyz.jdynb.tv.utils;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.jdynb.tv.model.UpgradeInfoModel;

public class WebUpgradeUtil {

  private static final String TAG = WebUpgradeUtil.class.getSimpleName();

  private static final Map<String, List<UpgradeInfoModel>> UPGRADE_PACKAGE_MAP = new HashMap<>();


  static {
    UPGRADE_PACKAGE_MAP.put("arm", Arrays.asList(
            new UpgradeInfoModel(
                    "com.google.android.webview",
                    "122.0.6261.64",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.google.android.webview_122.0.6261.64_armeabi-v7a.zip",
                    "网络"),
            new UpgradeInfoModel(
                    "com.android.webview",
                    "113.0.5672.136",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.android.webview_113.0.5672.13_armeabi-v7a.zip",
                    "网络"),
            new UpgradeInfoModel(
                    "com.huawei.webview",
                    "14.0.0.331",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.huawei.webview_14.0.0.331_arm64-v8a_armeabi-v7a.zip",
                    "网络"),
            new UpgradeInfoModel(
                    "com.android.chrome",
                    "122.0.6261.43",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.android.chrome_122.0.6261.64_armeabi-v7a.zip",
                    "网络"),

            new UpgradeInfoModel("com.amazon.webview.chromium",
                    "118-5993-tv.5993.155.51",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.amazon.webview.chromium_118-5993-tv.5993.155.51_armeabi-v7a.zip",
                    "网络"),

            new UpgradeInfoModel("com.amazon.webview.chromium",
                    "118-5993-tv.5993.155.51",
                    "com.webview.chromium_118-5993-tv.5993.155.51_armeabi-v7a.apk",
                    "内置"),
            new UpgradeInfoModel(
                    "com.android.chrome",
                    "122.0.6261.43",
                    "",
                    "安装包")


    ));

    UPGRADE_PACKAGE_MAP.put("arm64", Arrays.asList(
            new UpgradeInfoModel(
                    "com.google.android.webview",
                    "122.0.6261.64",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.google.android.webview_122.0.6261.64_arm64-v8a.zip",
                    "网络"),
            new UpgradeInfoModel(
                    "com.huawei.webview",
                    "14.0.0.331",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.huawei.webview_14.0.0.331_arm64-v8a_armeabi-v7a.zip",
                    "网络")
    ));

    UPGRADE_PACKAGE_MAP.put("x86", Arrays.asList(
            new UpgradeInfoModel(
                    "com.google.android.webview",
                    "122.0.6261.64",
                    "https://raw.githubusercontent.com/JonaNorman/ShareFile/main/com.google.android.webview_122.0.6261.64_x86.zip",
                    "网络"),
            new UpgradeInfoModel("com.google.android.webview",
                    "131.0.6778.105",
                    "com.google.android.webview_131.0.6778.105-677810506_minAPI26_maxAPI28(x86)(nodpi)_apkmirror.com.apk",
                    "内置")
    ));
    UPGRADE_PACKAGE_MAP.put("x86_64", Arrays.asList(
            new UpgradeInfoModel(
                    "com.google.android.webview",
                    "131.0.6778.135",
                    "https://github.com/VoryWork/AndroidWebviewNew/releases/download/131.0.6778.135/x64.apk",
                    "网络")
    ));
  }

  public static UpgradeInfoModel getMatchUpGradeInfo(Context context) {
    try {
      String arch = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).nativeLibraryDir;
      Log.i(TAG, "getMatchUpGradeInfo: arch = " + arch);
      if (arch.contains("arm64")) {
        return UPGRADE_PACKAGE_MAP.get("arm64").get(0);
      } else if (arch.contains("arm")) {
        return UPGRADE_PACKAGE_MAP.get("arm").get(0);
      } else if (arch.contains("x86_64")) {
        return UPGRADE_PACKAGE_MAP.get("x86_64").get(0);
      } else if (arch.contains("x86")) {
        return UPGRADE_PACKAGE_MAP.get("x86").get(0);
      }
      return null;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}