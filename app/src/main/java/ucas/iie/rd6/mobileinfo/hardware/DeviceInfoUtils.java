package ucas.iie.rd6.mobileinfo.hardware;

/**
 * Created by yanyan on 20200404
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.READ_PHONE_STATE;


public class DeviceInfoUtils {

    /**
     * 获取设备宽度（px）
     *
     */
    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     */
    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取设备的唯一标识， 需要 “android.permission.READ_Phone_STATE”权限
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return "UnKnown";
        } else {
            return deviceId;
        }
    }


    /**
     * 获取厂商名
     * **/
    public static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取产品名
     * **/
    public static String getDeviceProduct() {
        return android.os.Build.PRODUCT;
    }

    /**
     * 获取手机品牌
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机主板名
     */
    public static String getDeviceBoard() {
        return android.os.Build.BOARD;
    }

    /**
     * 设备名
     * **/
    public static String getDeviceDevice() {
        return android.os.Build.DEVICE;
    }

    /**
     *
     *
     * fingerprit 信息
     * **/
    public static String getDeviceFubgerprint() {
        return android.os.Build.FINGERPRINT;
    }

    /**
     * 硬件名
     *
     * **/
    public static String getDeviceHardware() {
        return android.os.Build.HARDWARE;
    }

    /**
     * 主机
     *
     * **/
    public static String getDeviceHost() {
        return android.os.Build.HOST;
    }

    /**
     *
     * 显示ID
     * **/
    public static String getDeviceDisplay() {
        return android.os.Build.DISPLAY;
    }

    /**
     * ID
     *
     * **/
    public static String getDeviceId() {
        return android.os.Build.ID;
    }

    /**
     * 获取手机用户名
     *
     * **/
    public static String getDeviceUser() {
        return android.os.Build.USER;
    }

    /**
     * 获取手机 硬件序列号
     * **/
    public static String getDeviceSerial() {
        return android.os.Build.SERIAL;
    }

    /**
     * 获取手机Android 系统SDK
     *
     * @return
     */
    public static int getDeviceSDK() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本
     *
     * @return
     */
    public static String getDeviceAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取当前手机系统语言。
     */
    public static String getDeviceDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     */
    public static String getDeviceSupportLanguage() {
        return Locale.getAvailableLocales().toString();
    }

    //调这个函数
    public static String getDeviceAllInfo(Context context) {

        return "\n1. IMEI:\n\t\t" + getIMEI(context)

                + "\n\n2. 设备宽度:\n\t\t" + getDeviceWidth(context)

                + "\n\n3. 设备高度:\n\t\t" + getDeviceHeight(context)

                + "\n\n4. 是否有内置SD卡:\n\t\t" + SDCardUtils.isSDCardMount()

                + "\n\n5. RAM 信息:\n\t\t" + SDCardUtils.getRAMInfo(context)

                + "\n\n6. 内部存储信息\n\t\t" + SDCardUtils.getStorageInfo(context, 0)

                + "\n\n7. SD卡 信息:\n\t\t" + SDCardUtils.getStorageInfo(context, 1)

                + "\n\n8. 是否联网:\n\t\t" + NetWorkUtils.isNetworkConnected(context)

                + "\n\n9. 网络类型:\n\t\t" + NetWorkUtils.getConnectedType(context)

                + "\n\n10. 系统默认语言:\n\t\t" + getDeviceDefaultLanguage()

                + "\n\n11. 硬件序列号(设备名):\n\t\t" + android.os.Build.SERIAL

                + "\n\n12. 手机型号:\n\t\t" + android.os.Build.MODEL

                + "\n\n13. 生产厂商:\n\t\t" + android.os.Build.MANUFACTURER

                + "\n\n14. 手机Fingerprint标识:\n\t\t" + android.os.Build.FINGERPRINT

                + "\n\n15. Android 版本:\n\t\t" + android.os.Build.VERSION.RELEASE

                + "\n\n16. Android SDK版本:\n\t\t" + android.os.Build.VERSION.SDK_INT

                + "\n\n17. 安全patch 时间:\n\t\t" + android.os.Build.VERSION.SECURITY_PATCH

                + "\n\n18. 版本类型:\n\t\t" + android.os.Build.TYPE

                + "\n\n19. 用户名:\n\t\t" + android.os.Build.USER

                + "\n\n20. 产品名:\n\t\t" + android.os.Build.PRODUCT

                + "\n\n21. ID:\n\t\t" + android.os.Build.ID

                + "\n\n22. 显示ID:\n\t\t" + android.os.Build.DISPLAY

                + "\n\n23. 硬件名:\n\t\t" + android.os.Build.HARDWARE

                + "\n\n24. 产品名:\n\t\t" + android.os.Build.DEVICE

                + "\n\n25. Bootloader:\n\t\t" + android.os.Build.BOOTLOADER

                + "\n\n26. 主板名:\n\t\t" + android.os.Build.BOARD

                + "\n\n27. CodeName:\n\t\t" + android.os.Build.VERSION.CODENAME;
                //+ "\n\n29. 语言支持:\n\t\t" + getDeviceSupportLanguage();

    }
}


/*
 * SDCardUtils.java
 */
class SDCardUtils {

    private static final int INTERNAL_STORAGE = 0;
    private static final int EXTERNAL_STORAGE = 1;

    /**
     * 获取 手机 RAM 信息
     * */
    public static String getRAMInfo(Context context) {
        long totalSize = 0;
        long availableSize = 0;

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        totalSize = memoryInfo.totalMem;
        availableSize = memoryInfo.availMem;

        return "可用/总共：" + Formatter.formatFileSize(context, availableSize)
                + "/" + Formatter.formatFileSize(context, totalSize);
    }

    /**
     * 判断SD是否挂载
     */
    public static boolean isSDCardMount() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机存储 ROM 信息
     *
     * type： 用于区分内置存储于外置存储的方法
     *
     * 内置SD卡 ：INTERNAL_STORAGE = 0;
     *
     * 外置SD卡： EXTERNAL_STORAGE = 1;
     * **/
    public static String getStorageInfo(Context context, int type) {

        String path = getStoragePath(context, type);
        /**
         * 无外置SD 卡判断
         * **/
        if (isSDCardMount() == false || TextUtils.isEmpty(path) || path == null) {
            return "无外置SD卡";
        }

        File file = new File(path);
        StatFs statFs = new StatFs(file.getPath());
        String stotageInfo;

        long blockCount = statFs.getBlockCountLong();
        long bloackSize = statFs.getBlockSizeLong();
        long totalSpace = bloackSize * blockCount;

        long availableBlocks = statFs.getAvailableBlocksLong();
        long availableSpace = availableBlocks * bloackSize;

        stotageInfo = "可用/总共："
                + Formatter.formatFileSize(context, availableSpace) + "/"
                + Formatter.formatFileSize(context, totalSpace);

        return stotageInfo;

    }

    /**
     * 使用反射方法 获取手机存储路径
     *
     * **/
    public static String getStoragePath(Context context, int type) {

        StorageManager sm = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        try {
            Method getPathsMethod = sm.getClass().getMethod("getVolumePaths");
            String[] path = (String[]) getPathsMethod.invoke(sm);

            switch (type) {
                case INTERNAL_STORAGE:
                    return path[type];
                case EXTERNAL_STORAGE:
                    if (path.length > 1) {
                        return path[type];
                    } else {
                        return null;
                    }

                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 手机 RAM 信息 方法 一
     * */
    public static String getTotalRAM(Context context) {
        long size = 0;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        size = outInfo.totalMem;

        return Formatter.formatFileSize(context, size);
    }

    /**
     * 手机 RAM 信息 方法 二
     * */
    public static String getTotalRAMOther(Context context) {
        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLine != null) {

            totalRam = (int) Math.ceil((new Float(Float.valueOf(firstLine)
                    / (1024 * 1024)).doubleValue()));

            long totalBytes = 0;

        }

        return Formatter.formatFileSize(context, totalRam);
    }

    /**
     * 获取 手机 可用 RAM
     * */
    public static String getAvailableRAM(Context context) {
        long size = 0;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        size = outInfo.availMem;

        return Formatter.formatFileSize(context, size);
    }

    /**
     * 获取手机内部存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getTotalInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 获取手机内部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getAvailableInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return Formatter.formatFileSize(context, availableBlocksLong
                * blockSizeLong);
    }

    /**
     * 获取手机外部存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getTotalExternalMemorySize(Context context) {
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return Formatter
                .formatFileSize(context, blockCountLong * blockSizeLong);
    }

    /**
     * 获取手机外部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getAvailableExternalMemorySize(Context context) {
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return Formatter.formatFileSize(context, availableBlocksLong
                * blockSizeLong);
    }

    /**
     *
     * SD 卡信息
     * */

    public static String getSDCardInfo() {

        SDCardInfo sd = new SDCardInfo();
        if (!isSDCardMount())
            return "SD card 未挂载!";

        sd.isExist = true;
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());

        sd.totalBlocks = sf.getBlockCountLong();
        sd.blockByteSize = sf.getBlockSizeLong();
        sd.availableBlocks = sf.getAvailableBlocksLong();
        sd.availableBytes = sf.getAvailableBytes();
        sd.freeBlocks = sf.getFreeBlocksLong();
        sd.freeBytes = sf.getFreeBytes();
        sd.totalBytes = sf.getTotalBytes();
        return sd.toString();
    }

    public static class SDCardInfo {
        boolean isExist;
        long totalBlocks;
        long freeBlocks;
        long availableBlocks;
        long blockByteSize;
        long totalBytes;
        long freeBytes;
        long availableBytes;

        @Override
        public String toString() {
            return "isExist=" + isExist + "\ntotalBlocks=" + totalBlocks
                    + "\nfreeBlocks=" + freeBlocks + "\navailableBlocks="
                    + availableBlocks + "\nblockByteSize=" + blockByteSize
                    + "\ntotalBytes=" + totalBytes + "\nfreeBytes=" + freeBytes
                    + "\navailableBytes=" + availableBytes;
        }
    }

    // add start by wangjie for SDCard TotalStorage
    public static String getSDCardTotalStorage(long totalByte) {

        double byte2GB = totalByte / 1024.00 / 1024.00 / 1024.00;
        double totalStorage;
        if (byte2GB > 1) {
            totalStorage = Math.ceil(byte2GB);
            if (totalStorage > 1 && totalStorage < 3) {
                return 2.0 + "GB";
            } else if (totalStorage > 2 && totalStorage < 5) {
                return 4.0 + "GB";
            } else if (totalStorage >= 5 && totalStorage < 10) {
                return 8.0 + "GB";
            } else if (totalStorage >= 10 && totalStorage < 18) {
                return 16.0 + "GB";
            } else if (totalStorage >= 18 && totalStorage < 34) {
                return 32.0 + "GB";
            } else if (totalStorage >= 34 && totalStorage < 50) {
                return 48.0 + "GB";
            } else if (totalStorage >= 50 && totalStorage < 66) {
                return 64.0 + "GB";
            } else if (totalStorage >= 66 && totalStorage < 130) {
                return 128.0 + "GB";
            }
        } else {
            // below 1G return get values
            totalStorage = totalByte / 1024.00 / 1024.00;

            if (totalStorage >= 515 && totalStorage < 1024) {
                return 1 + "GB";
            } else if (totalStorage >= 260 && totalStorage < 515) {
                return 512 + "MB";
            } else if (totalStorage >= 130 && totalStorage < 260) {
                return 256 + "MB";
            } else if (totalStorage > 70 && totalStorage < 130) {
                return 128 + "MB";
            } else if (totalStorage > 50 && totalStorage < 70) {
                return 64 + "MB";
            }
        }

        return totalStorage + "GB";
    }


}


class NetWorkUtils {
    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return networkInfo.isAvailable();
            }
            return false;
        }
        return false;
    }

        /**
         * 获取WIFI信息
         **/
        public static WifiInfo fetchSSIDInfo(Context context) {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return manager.getConnectionInfo();
        }

        /**
         * 获取WIFI名字
         **/
        public String getWifiSSID(Context context) {
            return fetchSSIDInfo(context).getSSID().replace("\"", "");
        }

        /**
         * 获取WIFi的MAC地址
         **/
        public String getWifiMacAddress(Context context) {
            return fetchSSIDInfo(context).getMacAddress();
        }

        /**
         * 获取当前网络连接的类型信息
         * @return
         */
        public static String getConnectedType(Context context) {
            if (context != null) {
                //获取手机所有连接管理对象
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                //获取NetworkInfo对象
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    //返回NetworkInfo的类型
                    String type = networkInfo.getTypeName();
                    return type+"网络";
                }
                return "没有网络";
            }
            return "未知";
        }

        /**
         * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
         */
        public static int getAPNType(Context context) {
            //结果返回值
            int netType = 0;
            //获取手机所有连接管理对象
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //NetworkInfo对象为空 则代表没有网络
            if (networkInfo == null) {
                return netType;
            }
            //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_WIFI) {
                //WIFI
                netType = 1;
            } else if (nType == ConnectivityManager.TYPE_MOBILE) {
                int nSubType = networkInfo.getSubtype();
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
                if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                        && !telephonyManager.isNetworkRoaming()) {
                    netType = 4;
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                        || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                        || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        && !telephonyManager.isNetworkRoaming()) {
                    netType = 3;
                    //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                        || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                        || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                        && !telephonyManager.isNetworkRoaming()) {
                    netType = 2;
                } else {
                    netType = 2;
                }
            }
            return netType;
        }


        /**
         * 判断GPS是否打开，需要ACCESS_FINE_LOCATION权限
         */
        public static boolean isGPSEnabled(Context context) {
            //获取手机所有连接LOCATION_SERVICE对象
            LocationManager locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    }


class BatteryUtils{
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Map<String, String> getBatteryInfo(Context context) {
        BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        //充电状态
        boolean batteryChargeStatus = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            batteryChargeStatus = mBatteryManager.isCharging();
        }
        String batteryChargeStatusString = "";
        if (!batteryChargeStatus) {
            batteryChargeStatusString = "未在充电";
        } else {
            batteryChargeStatusString = "充电中";
        }

        //是否在交流电充电
        int batteryPluggedAc = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PLUGGED_AC);
        String batteryPluggedAcString = "";
        if (batteryPluggedAc == 1) {
            batteryPluggedAcString = "交流电";
        } else {
            batteryPluggedAcString = "~";
        }

        //是否在USB充电
        int batterPluggedUSB = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PLUGGED_USB);
        String batterPluggedUSBString = "";
        if (batterPluggedUSB == 2) {
            batterPluggedUSBString = "USB充电";
        } else {
            batterPluggedUSBString = "~";
        }

        //是否在无线充电
        int batteryPluggedWireless = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PLUGGED_WIRELESS);
        String batteryPliggedWirelessString = "";
        if (batteryPluggedWireless == 4) {
            batteryPliggedWirelessString = "无线充电";
        } else {
            batteryPliggedWirelessString = "~";
        }

        //剩余电量
        int batteryRemain = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        //电池温度
        Intent intent = new Intent();
        int batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);


        Log.d("电池", batteryChargeStatusString + '\\' + batterPluggedUSBString + '\\' +
                batteryPliggedWirelessString + '\\' + batteryPluggedAcString + '\\' + batteryRemain + '\\' + batteryTemperature);
        Map<String, String> BatteryInfo = new HashMap<String, String>();
        BatteryInfo.put("充电状态:", batteryChargeStatusString);
        BatteryInfo.put("充电模式:", batterPluggedUSBString + '\\' + batteryPliggedWirelessString + '\\' + batteryPluggedAcString);
        BatteryInfo.put("剩余电量:", batteryRemain + "%");
        BatteryInfo.put("电池温度:", String.valueOf(batteryTemperature));
        return BatteryInfo;
    }
}


class BandUtils{
    private static Method sysPropGet;
    private static Method sysPropSet;
    public static Map<String, String> getVersionInfo(Context context){
        Map<String, String> bandVersionInfo = new HashMap<String,String>();
        String bandVersion = "";

        try {
            Class<?> S = Class.forName("android.os.SystemProperties");
            Method M[] = S.getMethods();
            for (Method m : M) {
                String n = m.getName();
                if (n.equals("get")) {
                    sysPropGet = m;
                } else if (n.equals("set")) {
                    sysPropSet = m;
                }
            }
            Object bandVersionResult = sysPropGet.invoke(null,"gsm.version.baseband", "no message");
            bandVersion = (String)bandVersionResult;
            bandVersionInfo.put("基带版本信息: ",bandVersion);
            Log.d("基带版本信息:", bandVersion);
            String innnerVerion = "" ;

            if(android.os.Build.DISPLAY .contains(android.os.Build.VERSION.INCREMENTAL)){
                innnerVerion = android.os.Build.DISPLAY;
            }else{
                innnerVerion = android.os.Build.VERSION.INCREMENTAL;
            }
            bandVersionInfo.put("内部版本信息:",innnerVerion);
            Log.d("内部版本信息:", innnerVerion);
            String osKernalVersion = System.getProperty("os.version");
            bandVersionInfo.put("Kernel信息:",osKernalVersion);
            Log.i("Kernel信息:","----Linux Kernal is :"+osKernalVersion);

        }
        catch (Exception e)
        {
            Log.d("getVersionInfo Error", e.toString());

        }
        return bandVersionInfo;
    }
}
