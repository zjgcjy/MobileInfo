package ucas.iie.rd6.mobileinfo.hardware;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import ucas.iie.rd6.mobileinfo.R;

public class HardwareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();
    }
}
