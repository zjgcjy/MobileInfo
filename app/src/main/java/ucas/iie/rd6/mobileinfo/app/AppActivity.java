package ucas.iie.rd6.mobileinfo.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ucas.iie.rd6.mobileinfo.R;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();


        Button bt_applist = findViewById(R.id.bt_commonapp);
        Button bt_syslist = findViewById(R.id.bt_systemapp);
        Button bt_file = findViewById(R.id.bt_sdfile);
        /*
         bt_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
         */


        final ListView list = findViewById(R.id.lv_app);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = (AppInfo) list.getItemAtPosition(position);
                //Toast.makeText(AppActivity.this, appInfo.getAppName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AppActivity.this, AppInfoActivity.class);

                byte[] bytes = appInfo.getBytesIcon();
                intent.putExtra("icon", bytes);
                intent.putExtra("appName", appInfo.getAppName());
                intent.putExtra("packageName", appInfo.getPackageName());
                intent.putExtra("packageSign", appInfo.getPackageSign());
                intent.putExtra("versionName", appInfo.getAppVersionName());
                intent.putExtra("versionCode", appInfo.getAppVersionCode());
                intent.putExtra("targetSdkVersion", appInfo.getTargetSdkVersion());
                intent.putExtra("minSdkVersion", appInfo.getMinSdkVersion());
                intent.putExtra("description", appInfo.getDescription());


                startActivity(intent);
            }
        });

        bt_applist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_LONG).show();
                Context context = v.getContext();
                List<AppInfo> appInfos = AppInfoHelper.getAppList(context, false);
                AppAdapter adapter = new AppAdapter(AppActivity.this, R.layout.list_app, appInfos);
                ListView listView = findViewById(R.id.lv_app);
                listView.setAdapter(adapter);
            }
        });

        bt_syslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_LONG).show();
                Context context = v.getContext();
                List<AppInfo> appInfos = AppInfoHelper.getAppList(context, true);
                AppAdapter adapter = new AppAdapter(AppActivity.this, R.layout.list_app, appInfos);
                ListView listView = findViewById(R.id.lv_app);
                listView.setAdapter(adapter);
            }
        });

    }
}
