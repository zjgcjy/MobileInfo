package ucas.iie.rd6.mobileinfo.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import ucas.iie.rd6.mobileinfo.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();
    }
}
