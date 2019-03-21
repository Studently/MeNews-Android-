package com.example.ly.menews.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ly.menews.R;
import com.example.ly.menews.domain.User;
import com.example.ly.menews.utils.CommonRequest;
import com.example.ly.menews.utils.CommonResponse;
import com.example.ly.menews.utils.Consts;
import com.example.ly.menews.utils.HttpUtil;
import com.example.ly.menews.utils.SharedPreferencesUtil;
import com.example.ly.menews.utils.UserManager;
import com.example.ly.menews.utils.Util;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.DataSupportException;

import java.io.IOException;

import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    //应用上下文
    private Context context;
    //登陆进度条
    private ProgressBar progressBar;
    //登陆按钮
    private Button loginBtn;
    //登陆页面注册按钮
    private Button registerBtn;
    //游客访问控件
    private TextView visitorText;
    //账号
    private EditText accountText;
    //密码
    private EditText passwordText;
    //是否记住密码
    private CheckBox isRememberPwd;
    //是否自动登陆
    private CheckBox isAutoLogin;

    //用来存储账号和密码
    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件
        initView();
        //添加监听事件
        setListeners();

        // 自动填充
        SharedPreferencesUtil sp = new SharedPreferencesUtil(context);
        Boolean isRemember = (Boolean) sp.get("isRememberPwd",false);
        Boolean isAutoLogin = (Boolean) sp.get("isAutoLogin",false);
        // SharedPreference获取用户账号密码，存在则填充
        String account = (String) sp.get("account","");
        String pwd = (String)sp.get("pwd","");
        if(!account.equals("") && !pwd.equals("")){
            if(isRemember){
                accountText.setText(account);
                passwordText.setText(pwd);
                isRememberPwd.setChecked(true);
            }
            if(isAutoLogin)
                Login();
        }
    }

    //初始化控件
    private void initView(){

        context=getApplicationContext();
        loginBtn = findViewById(R.id.login);
        registerBtn = findViewById(R.id.register);
        visitorText = findViewById(R.id.visitor);
        accountText = findViewById(R.id.account);
        passwordText = findViewById(R.id.password);
        isRememberPwd = findViewById(R.id.login_remember);
        isAutoLogin = findViewById(R.id.login_auto);
        progressBar = findViewById(R.id.progressbar);

        LitePal.getDatabase();// 建立数据库
        UserManager.clear();//清空用户
    }


    void setListeners(){
        //登陆
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        //跳转到注册界面
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*visitorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 若已有游客账号则以游客身份登录，不存在则新建游客账号
                User visitor = LitePal.where("isVisitor = ?","1")
                        .findFirst(User.class);
                if(visitor == null){
                    visitor = new User();
                    visitor.setUsername("Visitor");
                    visitor.setPassword("Visitor");
                    visitor.setVisitor(true);
                    visitor.save();
                }
                UserManager.setCurrentUser(visitor);
                autoStartActivity(MainActivity.class);
            }
        });*/
    }


    /**
     *  POST方式Login
     */
    private void Login() {
        // 创建请求体对象
        CommonRequest request = new CommonRequest();

        // 前端参数校验，防SQL注入
        account = Util.StringHandle(accountText.getText().toString());
        password = Util.StringHandle(passwordText.getText().toString());

        // 检查数据格式是否正确
        String resMsg = checkDataValid(account,password);
        if(!resMsg.equals("")){
            showResponse(resMsg);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);// 显示进度条
        OptionHandle(account,password);// 处理自动登录及记住密码

        // 填充参数
        request.addRequestParam("account",account);
        request.addRequestParam("pwd",password);

        // POST请求
        HttpUtil.sendPost(Consts.URL_Login, request.getJsonStr(), new okhttp3.Callback() {

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                CommonResponse res = new CommonResponse(response.body().string());
                String resCode = res.getResCode();
                String resMsg = res.getResMsg();
                // 登录成功
                if (resCode.equals(Consts.SUCCESSCODE_LOGIN)) {
                    // 查找本地数据库中是否已存在当前用户,不存在则新建用户并写入
                    User user = LitePal.where("account=?",account).findFirst(User.class);
                    if(user == null){
                        user = new User();
                        user.setAccount(account);
                        user.setPassword(password);
                        user.setVisitor(false);
                        user.save();
                    }
                    UserManager.setCurrentUser(user);// 设置当前用户

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                showResponse(resMsg);
            }
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                showResponse("Network ERROR");
            }
        });
    }

    private void showResponse(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String checkDataValid(String account,String pwd){
        if(TextUtils.isEmpty(account) | TextUtils.isEmpty(pwd))
            return getResources().getString(R.string.null_hint);
        if(account.length() != 11 && !account.contains("@"))
            return getResources().getString(R.string.account_invalid_hint);
        return "";
    }

    void OptionHandle(String account,String pwd){
        SharedPreferences.Editor editor = getSharedPreferences("UserData",MODE_PRIVATE).edit();
        SharedPreferencesUtil spu = new SharedPreferencesUtil(context);
        if(isRememberPwd.isChecked()){
            editor.putBoolean("isRememberPwd",true);
            // 保存账号密码
            spu.put("account",account);
            spu.put("pwd",pwd);
        }else{
            editor.putBoolean("isRememberPwd",false);
        }
        if(isAutoLogin.isChecked()){
            editor.putBoolean("isAutoLogin",true);
        }else{
            editor.putBoolean("isAutoLogin",false);
        }
        editor.apply();
    }

}
