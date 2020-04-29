package ucas.iie.rd6.mobileinfo.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import ucas.iie.rd6.mobileinfo.BasicActivity;
import ucas.iie.rd6.mobileinfo.R;

public class UserActivity extends AppCompatActivity {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(UserActivity.this,
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
        setContentView(R.layout.activity_user);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final LinearLayout linearLayout = findViewById(R.id.ly_user);
        myRequetPermission();

        Button bt_phone = findViewById(R.id.bt_phonenum);
        bt_phone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    TextView textView = new TextView(v.getContext());
                    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                    textView.setTextSize(20);
                    String number = Phone.getPhoneNumber(getApplicationContext());
                    number = "本机号码\n"+number;
                    textView.setText(number);
                    //textView.setText("*******************test for others*************************");
                    linearLayout.addView(textView);
                }
                else
                {
                    ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
            }
        });

        Button bt_contacts = findViewById(R.id.bt_contacts);
        bt_contacts.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    TextView textView = new TextView(v.getContext());
                    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                    textView.setTextSize(20);
                    String[] contacts = Contacts.getContacts(getApplicationContext());
                    String contactsText = "Your Contacts (showing first 10 of "+String.valueOf(contacts.length)+")"+"\n";
                    for (int i = 0; i < 10 && i < contacts.length; i++) {
                        contactsText = contactsText+contacts[i]+"\n";
                    }
                    textView.setText(contactsText);
                    //textView.setText("*******************test for others*************************");
                    linearLayout.addView(textView);
                }
                else
                {
                    ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

                }
            }
        });

        Button bt_sms = findViewById(R.id.bt_shortmsg);
        bt_sms.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    TextView textView = new TextView(v.getContext());
                    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                    textView.setTextSize(20);
                    String[] Sms = UserSms.getSms(getApplicationContext());
                    String SmsText = "Your Contacts (showing first 3 of "+String.valueOf(Sms.length)+")"+"\n##############\n";
                    for (int i = 0; i < 3 && i < Sms.length; i++) {
                        SmsText = SmsText + Sms[i]+"##############\n";
                    }
                    textView.setText(SmsText);
                    //textView.setText("*******************test for others*************************");
                    linearLayout.addView(textView);
                }
                else
                {
                    ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);

                }
            }
        });


        Button bt_calls = findViewById(R.id.bt_calllog);
        bt_calls.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                    TextView textView = new TextView(v.getContext());
                    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                    textView.setTextSize(20);
                    String[] calls = UserCalls.getCallLog(getApplicationContext());
                    String callText = "Your Calls (showing first 3 of "+String.valueOf(calls.length)+")"+"\n##############\n";
                    for (int i = 0; i < 3 && i < calls.length; i++) {
                        callText = callText + calls[i]+"##############\n";
                    }
                    textView.setText(callText);
                    //textView.setText("*******************test for others*************************");
                    linearLayout.addView(textView);
                }
                else
                {
                    ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);

                }
            }
        });


        Button bt_audio = findViewById(R.id.bt_AudioSetting);
        bt_audio.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    TextView textView = new TextView(v.getContext());
                    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                    textView.setTextSize(20);
                    Map audioSetting = AudioSetting.getAudioSetting(getApplicationContext());
                    String audioText = "";
                    for(Object key : audioSetting.keySet()){

                        audioText = audioText+key.toString()+audioSetting.get(key)+"\n";
                    }
                    textView.setText(audioText);
                    //textView.setText("*******************test for others*************************");
                    linearLayout.addView(textView);
                }
                else
                {
                    ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);

                }
            }
        });
    /*
        Button bt_alarm = findViewById(R.id.bt_Alarm);
        bt_alarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                textView.setTextSize(20);
                String alarm = Alarm.getAlarmSetting(getApplicationContext());
                alarm = "下个闹钟时间\n"+alarm;
                textView.setText(alarm);
                //textView.setText("*******************test for others*************************");
                linearLayout.addView(textView);
            }
        });*/
    }
}
