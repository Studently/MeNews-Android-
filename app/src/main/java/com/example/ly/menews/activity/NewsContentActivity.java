package com.example.ly.menews.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ly.menews.R;
import com.example.ly.menews.domain.User;
import com.example.ly.menews.utils.CommonRequest;
import com.example.ly.menews.utils.CommonResponse;
import com.example.ly.menews.utils.Consts;
import com.example.ly.menews.utils.HttpUtil;
import com.example.ly.menews.utils.UserManager;
import com.example.ly.menews.utils.Util;

import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 用于展示新闻内容的活动，活动对应的布局文件中只包含一个webview控件，根据新闻url来展示网页新闻
 */
public class NewsContentActivity extends BaseActivity {

    private boolean isCollection=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        //从新闻列表活动中获得传过来的新闻url
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        //取得webview实例
        WebView webView=findViewById(R.id.news_webview);
        //设置webView支持javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //设置当打开需要跳转到另一个网页时，仍然在当前webview中显示，不会打开系统浏览器
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);//加载内容

        //在这里将新闻关键字添加到用户的兴趣列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建请求体对象
                CommonRequest request = new CommonRequest();

                // 传入当前用户账号和阅读新闻的url
                request.addRequestParam("account",UserManager.getCurrentUser().getAccount());
                request.addRequestParam("url",url);

                // POST请求
                HttpUtil.sendPost(Consts.URL_ReadNews, request.getJsonStr(), new okhttp3.Callback() {

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    }
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });


        //浮动按钮操作
        FloatingActionButton fb=findViewById(R.id.collection_fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCollection){
                    fb.setImageResource(R.drawable.uncollection);
                    isCollection=false;
                }else{
                    fb.setImageResource(R.drawable.collection_1);
                    isCollection=true;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //创建请求体对象
                        CommonRequest request = new CommonRequest();
                        // 填充参数:账号和新闻url和请求码
                        request.addRequestParam("account", UserManager.getCurrentUser().getAccount());
                        request.addRequestParam("url", url);
                        if(isCollection){
                            request.addRequestParam("requestCode", Consts.REQUEST_ADD_COLLECTION);
                        }else{
                            request.addRequestParam("requestCode", Consts.REQUEST_DELETE_COLLECTION);
                        }
                        // POST请求
                        HttpUtil.sendPost(Consts.URL_COLLTION, request.getJsonStr(), new okhttp3.Callback() {

                            @Override
                            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                            }

                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
            }
        });
    }
}
