package com.example.ly.menews.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ly.menews.utils.ActivityController;

/**
 * BaseActivity为所有activity的父类，方便新建的activity加入或删除栈，可以实现一键退出
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
