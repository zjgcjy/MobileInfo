package ucas.iie.rd6.mobileinfo.hardware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import ucas.iie.rd6.mobileinfo.R;

import static android.Manifest.permission.READ_PHONE_STATE;

public class HardwareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        String message2 = DeviceInfoUtils.getDeviceAllInfo(getApplicationContext());
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Activity.TELEPHONY_SERVICE);
        try {
            message2 = message2 + SimCardUtils.getSimCardInfo(tm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textView4 = findViewById(R.id.textView);
        textView4.setText(message2);
        //textView4.setText("why not?");
    }
}
