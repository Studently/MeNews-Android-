package com.example.ly.menews.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ly.menews.R;
import com.example.ly.menews.utils.CommonRequest;
import com.example.ly.menews.utils.CommonResponse;
import com.example.ly.menews.utils.Consts;
import com.example.ly.menews.utils.HttpUtil;
import com.example.ly.menews.utils.Util;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private ImageView back;
    private Button registerBtn;
    private Button cancelBtn;
    private EditText accountText;
    private EditText pwdText;
    private EditText confirmPwdText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
        initComponents();
        //设置监听
        setListeners();
    }

    void initComponents(){
        back = findViewById(R.id.back);
        registerBtn = findViewById(R.id.register_confirm);
        cancelBtn = findViewById(R.id.register_cancel);
        accountText = findViewById(R.id.register_account);
        pwdText = findViewById(R.id.register_pwd);
        confirmPwdText = findViewById(R.id.register_pwd_confirm);
        progressDialog = new ProgressDialog(RegisterActivity.this);
    }

    void setListeners(){
        // 设置返回键监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     *  POST方式Register
     */
    private void register() {
        // 创建请求体对象
        CommonRequest request = new CommonRequest();

        // 前端参数校验，防SQL注入
        String account = Util.StringHandle(accountText.getText().toString());
        String pwd = Util.StringHandle(pwdText.getText().toString());
        String pwd_confirm = Util.StringHandle(confirmPwdText.getText().toString());

        // 检查数据格式是否正确
        String resMsg = checkDataValid(account,pwd,pwd_confirm);
        if(!resMsg.equals("")){
            showResponse(resMsg);
            return;
        }

        // 填充参数
        request.addRequestParam("account",account);
        request.addRequestParam("pwd",pwd);

        // POST请求
        HttpUtil.sendPost(Consts.URL_Register, request.getJsonStr(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CommonResponse res = new CommonResponse(response.body().string());
                String resCode = res.getResCode();
                String resMsg = res.getResMsg();
                // 显示注册结果
                showResponse(resMsg);
                // 注册成功
                if (resCode.equals(Consts.SUCCESSCODE_REGISTER)) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                showResponse("Network ERROR");
            }
        });
    }

    private void showResponse(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String checkDataValid(String account,String pwd,String pwd_confirm){
        if(TextUtils.isEmpty(account) | TextUtils.isEmpty(pwd) | TextUtils.isEmpty(pwd_confirm))
            return getResources().getString(R.string.null_hint);
        if(!pwd.equals(pwd_confirm))
            return getResources().getString(R.string.not_equal_hint);
        if(account.length() != 11 && !account.contains("@"))
            return getResources().getString(R.string.account_invalid_hint);
        return "";
    }
}
