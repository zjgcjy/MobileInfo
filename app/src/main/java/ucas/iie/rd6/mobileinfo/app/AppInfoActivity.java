package ucas.iie.rd6.mobileinfo.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import ucas.iie.rd6.mobileinfo.R;


public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        //AppInfo appInfo = (AppInfo) getIntent().getSerializableExtra("app");
        Intent intent = getIntent();
        byte[] appIcons = intent.getByteArrayExtra("icon");
        Bitmap bitmap= BitmapFactory.decodeByteArray(appIcons,0,appIcons.length);

        String appName = intent.getStringExtra("appName");
        String packageName = intent.getStringExtra("packageName");
        String packageSign = intent.getStringExtra("packageSign");
        String versionName = intent.getStringExtra("versionName");
        Long versionCode = intent.getLongExtra("versionCode", 0);
        Long targetSdkVersion = intent.getLongExtra("targetSdkVersion", 0);
        Long minSdkVersion = intent.getLongExtra("minSdkVersion", 0);
        String description = intent.getStringExtra("description");


        ImageView ivappImage = (ImageView) findViewById(R.id.iv_appinfo_img);
        TextView tvappName = (TextView) findViewById(R.id.tv2);
        TextView tvpackageName = (TextView) findViewById(R.id.tv4);
        TextView tvpackageSign = (TextView) findViewById(R.id.tv6);
        TextView tvVersionName = (TextView) findViewById(R.id.tv8);
        TextView tvversionCode = (TextView) findViewById(R.id.tv10);
        TextView tvtargetSdkVersion = (TextView) findViewById(R.id.tv12);
        TextView tvminSdkVersion = (TextView) findViewById(R.id.tv14);
        TextView tvdescription = (TextView) findViewById(R.id.tv16);


        ivappImage.setImageBitmap(bitmap);
        tvappName.setText(appName);
        tvpackageName.setText(packageName);
        tvpackageSign.setText(packageSign);
        tvVersionName.setText(versionName);
        tvversionCode.setText(versionCode.toString());
        tvtargetSdkVersion.setText(targetSdkVersion.toString());
        tvminSdkVersion.setText(minSdkVersion.toString());
        tvdescription.setText(description);

    }
}

