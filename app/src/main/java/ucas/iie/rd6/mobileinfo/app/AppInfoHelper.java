package ucas.iie.rd6.mobileinfo.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

public class AppInfoHelper {
    private static final String TAG = AppInfoHelper.class.getSimpleName();

    static List<AppInfo> getAppList(Context context, boolean sys) {
        // ret applist
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        // get package
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            AppInfo tmpInfo = new AppInfo();

            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setAppVersionName(packageInfo.versionName);
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
            tmpInfo.setDescription(packageInfo.applicationInfo.loadDescription(packageManager));
            tmpInfo.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));
            tmpInfo.setTargetSdkVersion(packageInfo.applicationInfo.targetSdkVersion);
            tmpInfo.setLastUpdateTime(packageInfo.lastUpdateTime);
            tmpInfo.setFirstInstallTime(packageInfo.firstInstallTime);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tmpInfo.setMinSdkVersion(packageInfo.applicationInfo.minSdkVersion);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                tmpInfo.setAppVersionCode(packageInfo.getLongVersionCode());
            } else {
                tmpInfo.setAppVersionCode(packageInfo.versionCode);
            }
            tmpInfo.setPackageSign(PackageSignUtil.getSign(context, tmpInfo.getPackageName()));
            tmpInfo.setSystem(!((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) == 0));
            // add
            if (sys)
            {
                if (tmpInfo.isSystem()) {
                    appList.add(tmpInfo);
                }
            }
            else
            {
                if (!tmpInfo.isSystem()) {
                    appList.add(tmpInfo);
                }
            }

        }
        return  appList;
    }
}
