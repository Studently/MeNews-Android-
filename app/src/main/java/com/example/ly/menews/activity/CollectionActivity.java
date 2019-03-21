package com.example.ly.menews.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ly.menews.R;
import com.example.ly.menews.adapter.CollectionAdapter;
import com.example.ly.menews.domain.Collection;
import com.example.ly.menews.domain.News;
import com.example.ly.menews.utils.CommonRequest;
import com.example.ly.menews.utils.CommonResponse;
import com.example.ly.menews.utils.Consts;
import com.example.ly.menews.utils.HttpUtil;
import com.example.ly.menews.utils.UserManager;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class CollectionActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private ListView collectionListView;
    private CollectionAdapter adapter;
    private List<News> newsList=new ArrayList<>();

    //收藏标题
    private TextView titlename;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        initView();
        initData();
    }

    //初始化控件
    private void initView() {

        titlename=(TextView) findViewById(R.id.title_collection).findViewById(R.id.title_text);

        back=(Button) findViewById(R.id.title_collection).findViewById(R.id.title_back);

        collectionListView=findViewById(R.id.collectin_list);

        //初始化适配器
        adapter=new CollectionAdapter(CollectionActivity.this,R.layout.collection_listview_items,newsList);


    }


    //初始化数据
    private void initData(){
        //设置标题
        titlename.setText("我的收藏");
        //给返回按钮添加监听
        back.setOnClickListener(this);

        getNewsDatas();
        //初始化datas
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                collectionListView.setAdapter(adapter);
            }
        },3000);


        //给ListView添加监听
        collectionListView.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //如果是返回按钮，返回MainActivity
            case R.id.title_back:
                Intent intent=new Intent(CollectionActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String title=newsList.get(position).getTitle();
        // 查找本地数据库中是否已存在当前用户,不存在则新建用户并写入
        // User user = LitePal.where("username=?",account).findFirst(User.class);
        News news= LitePal.where("title=?",title).findFirst(News.class);
        //跳转到新闻内容活动中，并把新闻url传过去
        if(news!=null){
            Intent intent = new Intent(CollectionActivity.this, NewsContentActivity.class);
            intent.putExtra("url",news.getUrl());
            startActivity(intent);
        }

    }



    /**
     * 初始化datas（这里通常是网络请求获取初始数据）
     */
    public void getNewsDatas() {

        /**
         * 传入异步下载参数
         * Consts.URL_GetNews：下载请求url
         * Consts.REQUEST_NEWS_CF:请求码：001：代表请求推荐新闻
         */
        new DownloadNews().execute(Consts.URL_GetNews,Consts.REQUEST_NEWS_COLLECTION);
    }



    class DownloadNews extends AsyncTask<String,Void,Void> {

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private OkHttpClient client = new OkHttpClient();

        public DownloadNews(){
            super();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //创建请求体对象
            CommonRequest request = new CommonRequest();
            // 填充参数:账号和请求码
            request.addRequestParam("account", UserManager.getCurrentUser().getAccount());
            request.addRequestParam("requestCode",strings[1]);
            // POST请求
            HttpUtil.sendPost(strings[0], request.getJsonStr(), new okhttp3.Callback() {

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    CommonResponse res = new CommonResponse(response.body().string());
                    //将新闻list转换成news数组
                    News[] newsArray=new Gson().fromJson(res.getJSONArray(),News[].class);
                    for(News news:newsArray){
                        newsList.add(news);
                        news.save();
                    }

                }

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.printStackTrace();
                }
            });
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }
}
