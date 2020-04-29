package ucas.iie.rd6.mobileinfo.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ucas.iie.rd6.mobileinfo.BasicActivity;
import ucas.iie.rd6.mobileinfo.R;


public class AppInfoActivity extends AppCompatActivity {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(AppInfoActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppInfoActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(AppInfoActivity.this,
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

