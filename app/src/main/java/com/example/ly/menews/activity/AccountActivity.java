package com.example.ly.menews.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ly.menews.R;
import com.example.ly.menews.domain.User;
import com.example.ly.menews.utils.CommonRequest;
import com.example.ly.menews.utils.CommonResponse;
import com.example.ly.menews.utils.Consts;
import com.example.ly.menews.utils.HttpUtil;
import com.example.ly.menews.utils.UserManager;
import com.example.ly.menews.utils.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AccountActivity extends BaseActivity implements View.OnClickListener{

    private TextView update_pwd;//新密码
    private TextView update_pwd_confirm;//确认新密码
    private Button account_confirm;//确认修改
    //标题栏的标题和返回
    private TextView account_title;
    private Button account_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        init();
        account_title.setText("修改密码");
        account_back.setOnClickListener(this);
        account_confirm.setOnClickListener(this);
    }


    //初始化组件
    private void init(){

        update_pwd=findViewById(R.id.update_pwd);
        update_pwd_confirm=findViewById(R.id.update_pwd_confirm);
        account_confirm=findViewById(R.id.account_confirm);
        account_title=findViewById(R.id.account_title).findViewById(R.id.title_text);
        account_back=findViewById(R.id.account_title).findViewById(R.id.title_back);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                Intent intent=new Intent(AccountActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.account_confirm:
                update();
                break;
        }

    }


    //密码校验
    private String checkDataValid(String pwd,String pwd_confirm){
        //密码为空
        if(TextUtils.isEmpty(pwd) | TextUtils.isEmpty(pwd_confirm))
            return getResources().getString(R.string.null_hint);
        //两次输入不相等
        if(!pwd.equals(pwd_confirm))
            return getResources().getString(R.string.not_equal_hint);
        return "";
    }

    private void update(){
        // 创建请求体对象
        CommonRequest request = new CommonRequest();

        // 前端参数校验，防SQL注入
        String pwd = Util.StringHandle(update_pwd.getText().toString());
        String pwd_confirm = Util.StringHandle(update_pwd_confirm.getText().toString());

        // 检查数据格式是否正确
        String resMsg = checkDataValid(pwd,pwd_confirm);
        if(!resMsg.equals("")){
            showResponse(resMsg);
            return;
        }

        User user= UserManager.getCurrentUser();
        // 填充参数
        request.addRequestParam("account",user.getAccount());
        request.addRequestParam("pwd",pwd);

        // POST请求
        HttpUtil.sendPost(Consts.URL_UpdatePwd, request.getJsonStr(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CommonResponse res = new CommonResponse(response.body().string());
                String resCode = res.getResCode();
                String resMsg = res.getResMsg();
                // 显示修改结果
                showResponse(resMsg);
                // 密码修改成功
                if (resCode.equals(Consts.SUCCESSCODE_UPDATEPWD)) {
                    Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
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
        //再UI主线程中操作UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AccountActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
