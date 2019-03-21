package com.example.ly.menews.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.browse.MediaBrowser;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ly.menews.R;
import com.example.ly.menews.Service.DownloadVersionService;
import com.example.ly.menews.utils.ActivityController;
import com.example.ly.menews.utils.DataCleanManager;
import com.example.ly.menews.utils.Util;
import com.example.ly.menews.view.Item_view;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private Item_view setting_account;//账号设置
    private Item_view setting_buffer;//缓存设置
    private Item_view setting_update;//更新设置
    private Button setting_exit;//退出
    private TextView bufferSize;//缓存大小
    private TextView version;//版本号
    private Context context;//上下文

    //标题栏的标题和返回
    private TextView setting_title;
    private Button setting_back;

    private DownloadVersionService.DownloadBinder downloadBinder;


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadVersionService.DownloadBinder) service;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
        setting_exit.setOnClickListener(this);
        setting_account.setOnClickListener(this);
        setting_update.setOnClickListener(this);
        setting_buffer.setOnClickListener(this);
        setting_title.setText("设置");
        setting_back.setOnClickListener(this);
        try {
            bufferSize.setText(DataCleanManager.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }

        version.setText(Util.getPackageVersionName(context));


        Intent intent = new Intent(this, DownloadVersionService.class);
        startService(intent); // 启动服务
        bindService(intent, connection, BIND_AUTO_CREATE); // 绑定服务
        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        }
    }

    private void init(){
        context=getApplicationContext();
        setting_account=findViewById(R.id.setting_account);
        setting_buffer=findViewById(R.id.setting_buffer);
        setting_update=findViewById(R.id.setting_update);
        setting_exit=findViewById(R.id.setting_exit);
        bufferSize=setting_buffer.findViewById(R.id.num_view);
        version=setting_update.findViewById(R.id.num_view);
        setting_title=findViewById(R.id.setting_title).findViewById(R.id.title_text);
        setting_back=findViewById(R.id.setting_title).findViewById(R.id.title_back);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_account:
                Intent acc_intent=new Intent(SettingActivity.this,AccountActivity.class);
                startActivity(acc_intent);
                break;
            case R.id.setting_buffer://点击清理缓存
                AlertDialog.Builder dialog=new AlertDialog.Builder(SettingActivity.this);
                dialog.setTitle("是否要清空缓存？");//设置警告弹出框的标题
                dialog.setCancelable(true);//可以back键取消，如果位false,不可以
                //确定按钮
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清空缓存
                        bufferSize.setText("0k");
                        DataCleanManager.clearAllCache(context);
                    }
                });
                //拒绝按钮
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();//显示出来

                break;
            case R.id.setting_update:

                String url = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                downloadBinder.startDownload(url);
                break;
            case R.id.setting_exit:
                //一键退出
                ActivityController.finishAll();
                break;
            case R.id.title_back:
                Intent main_intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(main_intent);
                break;
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
