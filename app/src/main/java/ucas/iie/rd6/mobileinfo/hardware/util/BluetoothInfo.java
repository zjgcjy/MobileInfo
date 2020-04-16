package ucas.iie.rd6.mobileinfo.hardware.util;

import ucas.iie.rd6.mobileinfo.hardware.base.BaseBean;
import ucas.iie.rd6.mobileinfo.hardware.base.BaseData;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class BluetoothBean extends BaseBean {
    private static final String TAG = BluetoothBean.class.getSimpleName();

    /**
     * 蓝牙地址
     */
    private String bluetoothAddress;

    /**
     * 蓝牙是否打开
     */
    private boolean isEnabled;

    /**
     * 连接的手机的信息
     */
    private List<JSONObject> device;

    /**
     * 手机设置的名字
     */
    private String phoneName;

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<JSONObject> getDevice() {
        return device;
    }

    public void setDevice(List<JSONObject> device) {
        this.device = device;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    @Override
    protected JSONObject toJSONObject() {
        try {
            jsonObject.put(BaseData.Bluetooth.BLUETOOTH_ADDRESS, isEmpty(bluetoothAddress));
            jsonObject.put(BaseData.Bluetooth.IS_ENABLED, isEnabled);
            jsonObject.put(BaseData.Bluetooth.DEVICE, new JSONArray(device));
            jsonObject.put(BaseData.Bluetooth.PHONE_NAME, isEmpty(phoneName));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.toJSONObject();
    }

    public static class DeviceBean extends BaseBean {

        /**
         * 连接手机的蓝牙地址
         */
        private String address;

        /**
         * 连接手机的蓝牙名字
         */
        private String name;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        protected JSONObject toJSONObject() {
            try {
                jsonObject.put(BaseData.Bluetooth.Device.NAME, isEmpty(name));
                jsonObject.put(BaseData.Bluetooth.Device.ADDRESS, isEmpty(address));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return super.toJSONObject();
        }
    }
}

public class BluetoothInfo {
    private static final String TAG = BluetoothInfo.class.getSimpleName();

    @SuppressLint("MissingPermission")
    public static JSONObject getMobBluetooth(Context context) {
        BluetoothBean bluetoothBean = new BluetoothBean();
        try {
            bluetoothBean.setBluetoothAddress(Settings.Secure.getString(context.getContentResolver(), "bluetooth_address"));
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                return bluetoothBean.toJSONObject();
            }
            bluetoothBean.setEnabled(bluetoothAdapter.isEnabled());
            bluetoothBean.setPhoneName(bluetoothAdapter.getName());
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            List<JSONObject> list = new ArrayList<>();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    BluetoothBean.DeviceBean deviceBean = new BluetoothBean.DeviceBean();
                    deviceBean.setAddress(device.getAddress());
                    deviceBean.setName(device.getName());
                    list.add(deviceBean.toJSONObject());
                }
            }
            bluetoothBean.setDevice(list);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return bluetoothBean.toJSONObject();
    }
    public static String getbtinfo(Context context){
        JSONObject info = getMobBluetooth(context);
        String res = "";
        try {
            res += "本机蓝牙地址:  " + info.getString("bluetoothAddress") + "\n";
            res += "本机蓝牙名称:  " + info.getString("phoneName") + "\n";
            res += "蓝牙是否开启:  " + info.getString("isEnabled") + "\n";
            res += "\n";
            JSONArray devices = info.getJSONArray("device");
            for (int i = 0; i < devices.length(); i++){
                JSONObject device = devices.getJSONObject(i);
                res += "蓝牙设备:  " + (i+1) +":\n";
                res += "蓝牙设备名称:  " + device.getString("name") + "\n";
                res += "蓝牙设备地址:  " + device.getString("address") + "\n";
                res += "\n";
            }
        }catch (JSONException e) {
        }
        return res;
    }
}