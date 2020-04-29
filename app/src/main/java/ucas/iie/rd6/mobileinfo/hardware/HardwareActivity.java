package ucas.iie.rd6.mobileinfo.hardware;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import ucas.iie.rd6.mobileinfo.BasicActivity;
import ucas.iie.rd6.mobileinfo.R;
import ucas.iie.rd6.mobileinfo.hardware.util.NetWorkInfo;
import ucas.iie.rd6.mobileinfo.hardware.util.SensorInfo;
import ucas.iie.rd6.mobileinfo.hardware.util.BluetoothInfo;
import ucas.iie.rd6.mobileinfo.hardware.util.CpuInfo;

import static android.Manifest.permission.READ_PHONE_STATE;

public class HardwareActivity extends AppCompatActivity {
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NOT_NOTICE){
            myRequetPermission();//由于不知道是否选择了允许所以需要再次判断
        }
    }
    private void myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }else {
            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){//用户选择了禁止不再询问

                        AlertDialog.Builder builder = new AlertDialog.Builder(HardwareActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOT_NOTICE);
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();



                    }else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(HardwareActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(HardwareActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();
        myRequetPermission();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final LinearLayout linearLayout = findViewById(R.id.ly_hardware);

        Button bt_bluetooth = findViewById(R.id.bt_bluetooth);
        bt_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setTextSize(20);
                textView.setText(BluetoothInfo.getbtinfo(getApplicationContext()));
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                linearLayout.addView(textView);
            }
        });

        Button bt_network = findViewById(R.id.bt_network);
        bt_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setTextSize(20);
                textView.setText(getNetwork());
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                linearLayout.addView(textView);
            }
        });

        Button bt_sensor = findViewById(R.id.bt_sensor);
        bt_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                /*----------- get sensor list example -----------*/
                String showText = "传感器列表:\n";
                showText+= SensorInfo.getSensorList(getApplicationContext());
                TextView textView = new TextView(v.getContext());
                textView.setTextSize(20);
                textView.setText(showText);
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                linearLayout.addView(textView);
            }
        });

        Button bt_rt_sensor = findViewById(R.id.bt_rt_sensor);
        bt_rt_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HardwareActivity.this, sensordata.class);
                startActivity(intent);
            }
        });

        Button bt_cpu = findViewById(R.id.bt_cpu);
        bt_cpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setTextSize(20);
                textView.setText(CpuInfo.getCpuInfo(getApplicationContext()));
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                linearLayout.addView(textView);
            }
        });

        Button bt_others = findViewById(R.id.bt_others);
        bt_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                textView.setTextSize(20);

                if (ContextCompat.checkSelfPermission(v.getContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    String message2 = DeviceInfoUtils.getDeviceAllInfo(getApplicationContext());
                    TelephonyManager tm = (TelephonyManager) v.getContext().getSystemService(Activity.TELEPHONY_SERVICE);
                    try {
                        message2 = message2 + SimCardUtils.getSimCardInfo(tm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textView.setText(message2);

                }
                else {
                    ActivityCompat.requestPermissions(HardwareActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
                //textView.setText("*******************test for others*************************");
                linearLayout.addView(textView);
            }
        });

        Button bt_battery = findViewById(R.id.bt_battery);
        bt_battery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                textView.setTextSize(20);

                Map Battery = BatteryUtils.getBatteryInfo(getApplicationContext());
                String batteryText = "";
                for(Object key : Battery.keySet()){
                    batteryText = batteryText+key.toString()+Battery.get(key)+"\n";
                }
                textView.setText(batteryText);
                //textView.setText("*******************test for others*************************");
                linearLayout.addView(textView);
            }
        });


        Button bt_band = findViewById(R.id.bt_band);
        bt_band.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                textView.setTextSize(20);
                Map Band = BandUtils.getVersionInfo(getApplicationContext());
                String bandText = "";
                for(Object key : Band.keySet()){
                    bandText = bandText+key.toString()+Band.get(key)+"\n";
                }
                textView.setText(bandText);
                //textView.setText("*******************test for others*************************");
                linearLayout.addView(textView);
            }
        });
    }


    private String getNetwork()
    {
        String showText = "";
        /*----------- is network available example --------------*/
        showText += "网络是否可用:";
        showText += NetWorkInfo.isNetworkAvailable(getApplicationContext()) ? "True" : "False";
        showText += "\n";

        /*----------- net type example --------------*/
        showText += "当前使用网络类型:";
        showText += NetWorkInfo.networkType(getApplicationContext());
        showText += "\n";
        /*----------- mobile network type example --------------*/
        showText += "移动网络类型:";
        showText += NetWorkInfo.networkTypeMobile(getApplicationContext());
        showText += "\n";

        /*----------- WiFi rssi example --------------*/
        showText += "WIFI信号强度:";
        showText += String.valueOf(NetWorkInfo.getWifiRssi(getApplicationContext()));
        showText += "\n";

        /*----------- WiFi signal level example --------------*/
        int rssi = NetWorkInfo.getWifiRssi(getApplicationContext());
        showText += "WIFI信号等级:";
        showText += String.valueOf(NetWorkInfo.calculateSignalLevel(rssi));
        showText += "\n";

        /*----------- WiFi signal level example --------------*/
        rssi = NetWorkInfo.getWifiRssi(getApplicationContext());
        int level = NetWorkInfo.calculateSignalLevel(rssi);
        showText += "WIFI信号评价:";
        showText += NetWorkInfo.checkSignalRssi(level);
        showText += "\n";

        /*----------- get local IP example --------------*/
        showText += "本地IP地址:";
        showText += NetWorkInfo.getLocalIp(getApplicationContext());
        showText += "\n";

        /*----------- get out IP example --------------*/
        showText += "出口IP地址:";
        showText += NetWorkInfo.getOutNetIP();
        showText += "\n";

        /*----------- get MAC Address example --------------*/
        showText += "MAC地址:";
        showText += NetWorkInfo.getMacAddress(getApplicationContext());
        showText += "\n";
        /*------------ read dns server ip example -----*/
        showText += "DNS服务器IP:";
        String[] dnss = NetWorkInfo.readDnsServers(getApplicationContext());
        if(dnss.length!=0) {
            showText += dnss[0];
        }else{
            showText +="未检测到DNS服务器IP";
        }
        showText+="\n";

        /*----------- get UserAgent example --------------*/
        showText += "User Agent:";
        showText += NetWorkInfo.getUserAgent(getApplicationContext());
        showText += "\n";
        /*----------- check roaming example --------------*/
        showText += "漫游状态:";
        showText += NetWorkInfo.checkIsRoaming(getApplicationContext()) ? "True" : "False";
        showText += "\n";
        /*------------ get phone signal info example -----*/
        Map dict = NetWorkInfo.getMobileDbm(getApplicationContext());
        showText += "手机信号强度:";
        showText += String.valueOf(dict.get("dbm"));
        showText += "\n";
        showText += "手机信号等级:";
        showText += String.valueOf(dict.get("level"));
        showText += "\n";
        showText += "手机信号评价:";
        showText += NetWorkInfo.checkSignalRssi((Integer) dict.get("level"));
        showText += "\n";

        /*----------- wifi list info example ------------*/
        showText += "附近WIFI列表:\n";
        List<String> mWifiListInfo = NetWorkInfo.getWifiListInfo(getApplicationContext());
        for (int i = 0; i < mWifiListInfo.size(); i++) {
            showText += "\t" + mWifiListInfo.get(i) + "\n";
        }

        return  showText;
    }
}
