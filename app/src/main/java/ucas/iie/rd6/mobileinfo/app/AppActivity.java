package ucas.iie.rd6.mobileinfo.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ucas.iie.rd6.mobileinfo.BasicActivity;
import ucas.iie.rd6.mobileinfo.R;

public class AppActivity extends AppCompatActivity {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(AppActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(AppActivity.this,
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

    File currentParent;
    File[] currentFiles;
    List<File> currentFilesList;
    private boolean isopen =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Toast.makeText(this, this.getLocalClassName(), Toast.LENGTH_SHORT).show();
        myRequetPermission();
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
