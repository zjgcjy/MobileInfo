package ucas.iie.rd6.mobileinfo.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ucas.iie.rd6.mobileinfo.R;

public class AppActivity extends AppCompatActivity {

    File currentParent;
    File[] currentFiles;
    List<File> currentFilesList;
    private boolean isopen =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();

        Button bt_applist = findViewById(R.id.bt_commonapp);
        Button bt_syslist = findViewById(R.id.bt_systemapp);
        Button bt_file = findViewById(R.id.bt_sdfile);
        final ListView list = findViewById(R.id.lv_app);

         bt_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilesList = new ArrayList<File>();

                int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    File root = new File("/mnt/sdcard/");
                    if(root.exists()) {
                        currentParent = root;
                        currentFiles = root.listFiles();
                        for (File file : currentFiles) {
                            currentFilesList.add(file);
                        }
                        //Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
                        final FileAdapter adapter = new FileAdapter(AppActivity.this, R.layout.list_file, currentFilesList);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // file
                                if (currentFilesList.get(position).isFile()){
                                    File temp = currentFilesList.get(position);
                                    int dotIndex = temp.getName().lastIndexOf(".");
                                    if (dotIndex >= 0) {
                                        String end= temp.getName().substring(dotIndex,temp.getName().length()).toLowerCase();
                                        if (!end.equals("")){
                                            if (end.equals(".mp3") || end.equals(".mav")){
                                                Intent intent = FileInfo.getAudioFileIntent(temp.getPath());
                                                startActivity(intent);
                                            }
                                            else if (end.equals(".jpg")){
                                                Intent intent = FileInfo.getImageFileIntent(temp.getPath());
                                                startActivity(intent);
                                            }
                                            else if (end.equals(".pdf")){
                                                Intent intent = FileInfo.getPdfFileIntent(temp.getPath());
                                                startActivity(intent);
                                            }
                                            else if (end.equals(".mp4")){
                                                Intent intent = FileInfo.getVideoFileIntent(temp.getPath());
                                                startActivity(intent);
                                            }
                                            else if (end.equals(".doc")){
                                                Intent intent = FileInfo.getWordFileIntent(temp.getPath());
                                                startActivity(intent);
                                            }
                                            else{
                                                Intent intent = FileInfo.getTextFileIntent(temp.getPath(), true);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                    return;
                                }
                                currentParent=currentFilesList.get(position);
                                currentFiles = currentFilesList.get(position).listFiles();
                                currentFilesList.clear();
                                for (File file : currentFiles) {
                                    currentFilesList.add(file);
                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                        list.setAdapter(adapter);
                    }
                }else{
                    ActivityCompat.requestPermissions(AppActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }

            }
        });


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
