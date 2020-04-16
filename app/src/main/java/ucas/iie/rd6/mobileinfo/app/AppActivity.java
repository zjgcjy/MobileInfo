package ucas.iie.rd6.mobileinfo.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ucas.iie.rd6.mobileinfo.R;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();
    }
}
