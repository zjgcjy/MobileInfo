package ucas.iie.rd6.mobileinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ucas.iie.rd6.mobileinfo.app.AppActivity;
import ucas.iie.rd6.mobileinfo.hardware.HardwareActivity;
import ucas.iie.rd6.mobileinfo.user.UserActivity;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        Button bt_user = findViewById(R.id.bt_user);
        bt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        Button bt_app = findViewById(R.id.bt_app);
        bt_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicActivity.this, AppActivity.class);
                startActivity(intent);
            }
        });

        Button bt_hardware = findViewById(R.id.bt_hardware);
        bt_hardware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicActivity.this, HardwareActivity.class);
                startActivity(intent);
            }
        });
    }
}
