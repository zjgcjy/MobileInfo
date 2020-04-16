package ucas.iie.rd6.mobileinfo.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import ucas.iie.rd6.mobileinfo.R;
import ucas.iie.rd6.mobileinfo.user.Phone;
import ucas.iie.rd6.mobileinfo.user.Contacts;
import ucas.iie.rd6.mobileinfo.user.UserSms;
import ucas.iie.rd6.mobileinfo.user.AudioSetting;
import ucas.iie.rd6.mobileinfo.user.Alarm;

public class UserActivity extends AppCompatActivity {

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


        Button bt_phone = findViewById(R.id.bt_phonenum);
        bt_phone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                linearLayout.removeAllViews();
                TextView textView = new TextView(v.getContext());
                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                textView.setTextSize(20);
                String number = Phone.getPhoneNumber(getApplicationContext());
                number = "本机号码\n"+number;
                textView.setText(number);
                //textView.setText("*******************test for others*************************");
                linearLayout.addView(textView);
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
                    String contactsText = "Your Contacts (showing first 3 of "+String.valueOf(contacts.length)+")"+"\n";
                    for (int i = 0; i < 3 && i < contacts.length; i++) {
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
        });
    }
}
