package ucas.iie.rd6.mobileinfo.hardware.util;

import ucas.iie.rd6.mobileinfo.hardware.base.BaseBean;
import ucas.iie.rd6.mobileinfo.hardware.base.BaseData;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

class CpuBean extends BaseBean {
    private static final String TAG = CpuBean.class.getSimpleName();

    /**
     * CPU名字
     */
    private String cpuName;
    private String cpuPart;
    private String bogomips;
    private String features;
    private String cpuImplementer;
    private String cpuArchitecture;
    private String cpuVariant;

    public String getBogomips() {
        return bogomips;
    }

    public void setBogomips(String bogomips) {
        this.bogomips = bogomips;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getCpuImplementer() {
        return cpuImplementer;
    }

    public void setCpuImplementer(String cpuImplementer) {
        this.cpuImplementer = cpuImplementer;
    }

    public String getCpuArchitecture() {
        return cpuArchitecture;
    }

    public void setCpuArchitecture(String cpuArchitecture) {
        this.cpuArchitecture = cpuArchitecture;
    }

    public String getCpuVariant() {
        return cpuVariant;
    }

    public void setCpuVariant(String cpuVariant) {
        this.cpuVariant = cpuVariant;
    }

    /**
     * CPU频率
     */
    private String cpuFreq;

    /**
     * CPU最大频率
     */
    private String cpuMaxFreq;

    /**
     * CPU最小频率
     */
    private String cpuMinFreq;

    /**
     * CPU硬件名
     */
    private String cpuHardware;

    /**
     * CPU核数
     */
    private int cpuCores;

    /**
     * CPU温度
     */
    private String cpuTemp;

    /**
     * CPU架构
     */
    private String cpuAbi;

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public String getCpuFreq() {
        return cpuFreq;
    }

    public void setCpuFreq(String cpuFreq) {
        this.cpuFreq = cpuFreq;
    }

    public String getCpuMaxFreq() {
        return cpuMaxFreq;
    }

    public void setCpuMaxFreq(String cpuMaxFreq) {
        this.cpuMaxFreq = cpuMaxFreq;
    }

    public String getCpuMinFreq() {
        return cpuMinFreq;
    }

    public void setCpuMinFreq(String cpuMinFreq) {
        this.cpuMinFreq = cpuMinFreq;
    }

    public String getCpuHardware() {
        return cpuHardware;
    }

    public void setCpuHardware(String cpuHardware) {
        this.cpuHardware = cpuHardware;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public String getCpuTemp() {
        return cpuTemp;
    }

    public void setCpuTemp(String cpuTemp) {
        this.cpuTemp = cpuTemp;
    }

    public String getCpuAbi() {
        return cpuAbi;
    }

    public void setCpuAbi(String cpuAbi) {
        this.cpuAbi = cpuAbi;
    }

    public String getCpuPart() {
        return cpuPart;
    }

    public void setCpuPart(String cpuPart) {
        this.cpuPart = cpuPart;
    }

    @Override
    protected JSONObject toJSONObject() {
        try {
            jsonObject.put(BaseData.Cpu.CPU_NAME, isEmpty(cpuName));
            jsonObject.put(BaseData.Cpu.CPU_PART, isEmpty(cpuPart));
            jsonObject.put(BaseData.Cpu.BOGO_MIPS, isEmpty(bogomips));
            jsonObject.put(BaseData.Cpu.FEATURES, isEmpty(features));
            jsonObject.put(BaseData.Cpu.CPU_IMPLEMENTER, isEmpty(cpuImplementer));
            jsonObject.put(BaseData.Cpu.CPU_ARCHITECTURE, isEmpty(cpuArchitecture));
            jsonObject.put(BaseData.Cpu.CPU_VARIANT, isEmpty(cpuVariant));
            jsonObject.put(BaseData.Cpu.CPU_FREQ, isEmpty(cpuFreq));
            jsonObject.put(BaseData.Cpu.CPU_MAX_FREQ, isEmpty(cpuMaxFreq));
            jsonObject.put(BaseData.Cpu.CPU_MIN_FREQ, isEmpty(cpuMinFreq));
            jsonObject.put(BaseData.Cpu.CPU_HARDWARE, isEmpty(cpuHardware));
            jsonObject.put(BaseData.Cpu.CPU_CORES, cpuCores);
            jsonObject.put(BaseData.Cpu.CPU_TEMP, isEmpty(cpuTemp));
            jsonObject.put(BaseData.Cpu.CPU_ABI, isEmpty(cpuAbi));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return super.toJSONObject();
    }
}


public class CpuInfo {
    /**
     * 获取CPU的名字
     *
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    private static void getCpuName(CpuBean cpuBean) {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    String result = line.toLowerCase();
                    String[] array = result.split(":\\s+", 2);
                    //cpu名字
                    if (array[0].startsWith("model name")) {
                        cpuBean.setCpuName(array[1]);
                    }
                    //cpu架构
                    else if (array[0].startsWith("cpu part")) {
                        cpuBean.setCpuPart(array[1]);
                    }
                    //cpu品牌
                    else if (array[0].startsWith("hardware")) {
                        cpuBean.setCpuHardware(array[1]);
                    }
                    //cpu速度
                    else if (array[0].startsWith("bogomips")) {
                        cpuBean.setBogomips(array[1]);
                    }
                    //cpu细节描述
                    else if (array[0].startsWith("features")) {
                        cpuBean.setFeatures(array[1]);
                    }
                    //cpu ARM架构
                    else if (array[0].startsWith("cpu implementer")) {
                        cpuBean.setCpuImplementer(array[1]);
                    }
                    //cpu 指令集架构
                    else if (array[0].startsWith("cpu architecture")) {
                        cpuBean.setCpuArchitecture(array[1]);
                    }
                    //cpu 变化
                    else if (array[0].startsWith("cpu variant")) {
                        cpuBean.setCpuVariant(array[1]);
                    }
                }
            } catch (IOException e) {
                Log.i(TAG, e.toString());
            }
        } catch (IOException e) {
            Log.i(TAG, e.toString());
        }
    }

    private static final String TAG = CpuInfo.class.getSimpleName();

    static JSONObject getCpuJSON(Context context) {
        CpuBean cpuBean = new CpuBean();
        try {
            getCpuName(cpuBean);
            cpuBean.setCpuFreq(getCurCpuFreq() + "KHZ");
            cpuBean.setCpuMaxFreq(getMaxCpuFreq() + "KHZ");
            cpuBean.setCpuMinFreq(getMinCpuFreq() + "KHZ");
            cpuBean.setCpuCores(getHeart());
            cpuBean.setCpuTemp(getCpuTemp() + "℃");
            cpuBean.setCpuAbi(putCpuAbi());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        return cpuBean.toJSONObject();
    }

    private static String putCpuAbi() {
        String[] abis;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String abi : abis) {
            stringBuilder.append(abi);
            stringBuilder.append(",");
        }

        try {
            return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        return null;

    }

    private static String getCpuTemp() {
        String temp = null;
        try {
            FileReader fr = new FileReader("/sys/class/thermal/thermal_zone9/subsystem/thermal_zone9/temp");
            BufferedReader br = new BufferedReader(fr);
            temp = br.readLine();
            br.close();
        } catch (IOException e) {
            Log.i(TAG, e.toString());
        }
        return TextUtils.isEmpty(temp) ? null : temp.length() >= 5 ? (Integer.valueOf(temp) / 1000) + "" : temp;
    }

    private static int getHeart() {

        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = 0;
        }
        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]", pathname.getName());
        }
    };

    private static String getCurCpuFreq() {

        String result = "N/A";

        try {

            FileReader fr = new FileReader(

                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");

            BufferedReader br = new BufferedReader(fr);

            String text = br.readLine();

            result = text.trim();

        } catch (FileNotFoundException e) {

            Log.i(TAG, e.toString());

        } catch (IOException e) {
            Log.i(TAG, e.toString());

        }

        return result;

    }


    private static String getMaxCpuFreq() {

        String result = "";

        ProcessBuilder cmd;

        try {

            String[] args = {"/system/bin/cat",

                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};

            cmd = new ProcessBuilder(args);

            Process process = cmd.start();

            InputStream in = process.getInputStream();

            byte[] re = new byte[24];

            while (in.read(re) != -1) {

                result = result + new String(re);

            }

            in.close();

        } catch (IOException ex) {

            Log.i(TAG, ex.toString());

            result = "N/A";

        }

        return result.trim();

    }


    private static String getMinCpuFreq() {

        String result = "";

        ProcessBuilder cmd;

        try {

            String[] args = {"/system/bin/cat",

                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};

            cmd = new ProcessBuilder(args);

            Process process = cmd.start();

            InputStream in = process.getInputStream();

            byte[] re = new byte[24];

            while (in.read(re) != -1) {

                result = result + new String(re);

            }

            in.close();

        } catch (IOException ex) {

            Log.i(TAG, ex.toString());

            result = "N/A";

        }

        return result.trim();

    }

    public static String getCpuInfo(Context context) {
        JSONObject info =  getCpuJSON(context);
        String res = "";
        try{
            res += "CPU Name : " +  info.getString(BaseData.Cpu.CPU_NAME) + "\n";
            res += "CPU Part : " +  info.getString(BaseData.Cpu.CPU_PART) + "\n";
            res += "Bogo Mips : " +  info.getString(BaseData.Cpu.BOGO_MIPS) + "\n";
            res += "Features : " +  info.getString(BaseData.Cpu.FEATURES) + "\n";
            res += "CPU Implementer : " +  info.getString(BaseData.Cpu.CPU_IMPLEMENTER) + "\n";
            res += "CPU Architecture : " +  info.getString(BaseData.Cpu.CPU_ARCHITECTURE) + "\n";
            res += "CPU Variant : " +  info.getString(BaseData.Cpu.CPU_VARIANT) + "\n";
            res += "CPU Freq : " +  info.getString(BaseData.Cpu.CPU_FREQ) + "\n";
            res += "CPU Max Freq : " +  info.getString(BaseData.Cpu.CPU_MAX_FREQ) + "\n";
            res += "CPU Min Freq : " +  info.getString(BaseData.Cpu.CPU_MIN_FREQ) + "\n";
            res += "CPU Hardware : " +  info.getString(BaseData.Cpu.CPU_HARDWARE) + "\n";
            res += "CPU Cores : " +  info.getString(BaseData.Cpu.CPU_CORES) + "\n";
            res += "CPU Temp : " +  info.getString(BaseData.Cpu.CPU_TEMP) + "\n";
            res += "CPU ABI : " +  info.getString(BaseData.Cpu.CPU_ABI) + "\n";
        } catch (JSONException e){}
        return res;


    }
}
