package ucas.iie.rd6.mobileinfo.hardware.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.lang.reflect.Method;
import static android.content.Context.WIFI_SERVICE;

@SuppressLint("MissingPermission")
public class NetWorkInfo {
    /**
     * @param context
     * @return a boolean ,true or false
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager mgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mgr == null) {
                return false;
            }
            NetworkInfo[] info = mgr.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            //ignore

        }
        return false;
    }

    /**
     * @param context
     * @return a string of network type: WIFI、2G、3G、4G、Other
     */
    public static String networkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return "WIFI";
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "Other";
        }
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Other";
        }
    }

    /**
     * @param context
     * @return boolean type, true means has card
     */
    public static boolean hasSimCard(Context context) {
        boolean result = true;
        try {
            TelephonyManager telMgr = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    result = false;
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    result = false;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            //ignore
        }
        return result;
    }

    /**
     * @param context
     * @return a string of mobile network type,such as 2G_GPRS and so on
     */
    public static String networkTypeMobile(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        if (telephonyManager == null || !hasSimCard(context)) {
            return "Other";
        }
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "2G_GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "2G_EDGE";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "2G_CDMA";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "2G_1xRTT";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G_IDEN";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "3G_UMTS";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "3G_EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "3G_EVDO_A";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "3G_HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "3G_HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "3G_HSPA";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "3G_EVDO_B";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "3G_EHRPD";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G_SHPAP";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G_LTE";
            default:
                return "Other";
        }
    }

    /**
     * @param context
     * @return int, wifi rssi
     */
    public static int getWifiRssi(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager != null) {
                WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
                return mWifiInfo.getRssi();
            }
        } catch (Exception e) {
            //ignore
        }
        return 0;
    }

    private static final int MIN_RSSI = -100;
    private static final int MAX_RSSI = -55;

    /**
     * @param rssi
     * @return int, rssi number
     */
    public static int calculateSignalLevel(int rssi) {
        if (rssi <= MIN_RSSI) {
            return 0;
        } else if (rssi >= MAX_RSSI) {
            return 4;
        } else {
            float inputRange = (MAX_RSSI - MIN_RSSI);
            float outputRange = (4);
            return (int) ((float) (rssi - MIN_RSSI) * outputRange / inputRange);
        }
    }

    /**
     * @param level
     * @return String, rssi level
     */
    public static String checkSignalRssi(int level) {
        String levelStr = "无信号";
        if (level > 4) {
            level = 4;
        }
        switch (level) {
            case 0:
                levelStr = "无信号";
                break;
            case 1:
                levelStr = "信号差";
                break;
            case 2:
                levelStr = "信号中";
                break;
            case 3:
                levelStr = "信号良";
                break;
            case 4:
                levelStr = "信号优";
                break;
            default:
                break;
        }
        return levelStr;
    }

    /**
     *
     * @param ipInt
     * @return String
     */
    public static String intIP2StringIP(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     *
     * @param context
     * @return String
     */
    public static String getWifiIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
            return ipAddress;
        }
        return "";
    }

    /**
     *
     * @return String
     */
    public static String getMobileIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            return "";
        }
        return "";
    }

    /**
     *
     * @return String
     */
    public static String getOutNetIP() {
        String mNetIpAddress="";
        InputStream inStream;
        try {
            URL infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    strber.append(line).append("\n");
                }
                inStream.close();
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                JSONObject jsonObject = new JSONObject(strber.substring(start, end + 1));
                mNetIpAddress = jsonObject.optString("cip", "");
            }
        } catch (Throwable e) {

        }
        return mNetIpAddress;
    }

    /**
     * @param context
     * @return String of IP
     */
    public static String getLocalIp(Context context) {
        if (context == null) {
            return "";
        }
        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo info = conManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 3/4g网络
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return getMobileIp();
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    return getWifiIP(context); // 局域网地址
                    //return getOutNetIP(); // 外网地址
                } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    // 以太网有限网络
                    return getMobileIp();
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static Map<String,Integer> getMobileDbm(Context context) {
        int dbm = 0;
        int level = 0;
        int asu = 0;
        Map dict = new HashMap<String, Integer>();
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            List<CellInfo> cellInfoList;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (tm == null) {
                    dict.put("dbm",dbm);
                    dict.put("level",level);
                    dict.put("asu",asu);
                    return dict;
                }
                cellInfoList = tm.getAllCellInfo();
                if (null != cellInfoList) {
                    for (CellInfo cellInfo : cellInfoList) {
                        if (cellInfo instanceof CellInfoGsm) {
                            CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthGsm.getDbm();
                            level = cellSignalStrengthGsm.getLevel();
                            asu = cellSignalStrengthGsm.getAsuLevel();
                        } else if (cellInfo instanceof CellInfoCdma) {
                            CellSignalStrengthCdma cellSignalStrengthCdma =
                                    ((CellInfoCdma) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthCdma.getDbm();
                            level = cellSignalStrengthCdma.getLevel();
                            asu = cellSignalStrengthCdma.getAsuLevel();
                        } else if (cellInfo instanceof CellInfoLte) {
                            CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthLte.getDbm();
                            level = cellSignalStrengthLte.getLevel();
                            asu = cellSignalStrengthLte.getAsuLevel();
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            if (cellInfo instanceof CellInfoWcdma) {
                                CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                        ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                                dbm = cellSignalStrengthWcdma.getDbm();
                                level = cellSignalStrengthWcdma.getLevel();
                                asu = cellSignalStrengthWcdma.getAsuLevel();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
        dict.put("dbm",dbm);
        dict.put("level",level);
        dict.put("asu",asu);
        return dict;
    }

    /**
     *
     * @param context
     * @return boolean
     */
    public static boolean checkIsRoaming(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isRoaming();
            }
        }
        return false;
    }

    public static String[] readDnsServersFromConnectionManager(Context context) {
        LinkedList<String> dnsServers = new LinkedList<>();
        if (Build.VERSION.SDK_INT >= 21 && context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                for (Network network : connectivityManager.getAllNetworks()) {
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                    if (networkInfo.getType() == activeNetworkInfo.getType()) {
                        LinkProperties lp = connectivityManager.getLinkProperties(network);
                        for (InetAddress addr : lp.getDnsServers()) {
                            dnsServers.add(addr.getHostAddress());
                        }
                    }
                }
            }
        }
        return dnsServers.isEmpty() ? new String[0] : dnsServers.toArray(new String[dnsServers.size()]);
    }

    public static String[] readDnsServersFromWifiManager(Context context) {
        LinkedList<String> dnsServers = new LinkedList<>();
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null || !wifiManager.isWifiEnabled()) {
                return new String[0];
            }
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            if (dhcpInfo != null) {
                if (dhcpInfo.dns1 != 0) {
                    dnsServers.add(intToIp(dhcpInfo.dns1));
                }
                if (dhcpInfo.dns2 != 0) {
                    dnsServers.add(intToIp(dhcpInfo.dns2));
                }
            }
        } catch (Exception e) {
        }
        return dnsServers.isEmpty() ? new String[0] : dnsServers.toArray(new String[dnsServers.size()]);
    }

    public static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    public static String[] readDnsServersFromCommand() {
        LinkedList<String> dnsServers = new LinkedList<>();
        try {
            Process process = Runtime.getRuntime().exec("getprop");
            InputStream inputStream = process.getInputStream();
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = lnr.readLine()) != null) {
                int split = line.indexOf("]: [");
                if (split == -1) {
                    continue;
                }
                String property = line.substring(1, split);
                String value = line.substring(split + 4, line.length() - 1);
                if (property.endsWith(".dns")
                        || property.endsWith(".dns1")
                        || property.endsWith(".dns2")
                        || property.endsWith(".dns3")
                        || property.endsWith(".dns4")) {
                    InetAddress ip = InetAddress.getByName(value);
                    if (ip == null) {
                        continue;
                    }
                    value = ip.getHostAddress();
                    if (value == null) {
                        continue;
                    }
                    if (value.length() == 0) {
                        continue;
                    }
                    dnsServers.add(value);
                }
            }
        } catch (IOException e) {
        }
        return dnsServers.isEmpty() ? new String[0] : dnsServers.toArray(new String[dnsServers.size()]);
    }

    public static String[] DNS_SERVER_PROPERTIES = new String[]{"net.dns1",
            "net.dns2", "net.dns3", "net.dns4"};

    public static String[] readDnsServersFromSystemProperties() {
        SystemProperties.init();
        LinkedList<String> dnsServers = new LinkedList<>();
        for (String property : DNS_SERVER_PROPERTIES) {
            String server = SystemProperties.get(property, "");
            if (server != null && !server.isEmpty()) {
                try {
                    InetAddress ip = InetAddress.getByName(server);
                    if (ip == null) {
                        continue;
                    }
                    server = ip.getHostAddress();
                    if (server == null || server.isEmpty()) {
                        continue;
                    }
                } catch (Throwable throwable) {
                    continue;
                }
                dnsServers.add(server);
            }
        }
        return dnsServers.toArray(new String[dnsServers.size()]);
    }

    private static class SystemProperties {
        private static boolean isReflectInited = false;

        public static void init() {
            if (!isReflectInited) {
                isReflectInited = true;
                try {
                    Class<?> cls = Class.forName("android.os.SystemProperties");
                    setPropertyMethod = cls.getDeclaredMethod("set", new Class<?>[]{String.class, String.class});
                    setPropertyMethod.setAccessible(true);
                    getPropertyMethod = cls.getDeclaredMethod("get", new Class<?>[]{String.class, String.class});
                    getPropertyMethod.setAccessible(true);
                } catch (Throwable throwable) {
                }
            }
        }

        private static Method getPropertyMethod = null;

        public static String get(String property, String defaultValue) {
            String propertyValue = defaultValue;
            if (getPropertyMethod != null) {
                try {
                    propertyValue = (String) getPropertyMethod.invoke(null, property, defaultValue);
                } catch (Throwable throwable) {
                }
            }
            return propertyValue;
        }

        private static Method setPropertyMethod = null;

        public static void set(String property, String value) {
            if (setPropertyMethod != null) {
                try {
                    setPropertyMethod.invoke(null, property, value);
                } catch (Throwable throwable) {
                }
            }
        }
    }

    public static String[] readDnsServers(Context context) {
        String[] dnsServers = readDnsServersFromConnectionManager(context);
        if (dnsServers.length == 0) {
            dnsServers = readDnsServersFromSystemProperties();
            if (dnsServers.length == 0) {
                dnsServers = readDnsServersFromCommand();
            }
        }
        return dnsServers;
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * @param context
     * @return
     */
    public static String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     * @return
     */
    public static String getMacFromFile() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     * @return
     */
    public static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }
    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac = "02:00:00:00:00:00";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacFromFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;
    }

    public static String getUserAgent(Context context){
        String useragent="";
        useragent = new WebView(context).getSettings().getUserAgentString();
        return useragent;
    }
    /**
     * @return description: A string list with WiFi name;
     */
    public static List<String> getWifiListInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        List<ScanResult> wifiList = new ArrayList<>();
        List<String> mWifiResult = new ArrayList<>();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            HashMap<String, Integer> signalStrength = new HashMap<String, Integer>();
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                if (!scanResult.SSID.isEmpty()) {
                    String key = scanResult.SSID + " " + scanResult.capabilities;
                    if (!signalStrength.containsKey(key)) {
                        mWifiResult.add(scanResult.SSID);
                        signalStrength.put(key, i);
                        wifiList.add(scanResult);
                    }
                }
            }
        }
        return mWifiResult;
    }
}
