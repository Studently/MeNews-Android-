package com.example.ly.menews.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.ly.menews.R;
import com.example.ly.menews.domain.News;
import com.example.ly.menews.utils.CommonRequest;
import com.example.ly.menews.utils.CommonResponse;
import com.example.ly.menews.utils.Consts;
import com.example.ly.menews.utils.HttpUtil;
import com.example.ly.menews.utils.UserManager;
import com.example.ly.menews.utils.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;


/**
 * 适配器    Created by Administrator on 2017/2/3.
 */

public class CollectionAdapter extends ArrayAdapter<News> {

    private int resourceId;
    //新闻list
    private List<News> mNewsList;
    //向下文
    private Context context;

    public CollectionAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        this.context=context;
        mNewsList=objects;
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        News news=getItem(position);
        View itemView;

         TextView btn_Delete;//删除按钮
         TextView newstitle;//新闻标题
         TextView newstime;//新闻时间
         TextView newscategory;//新闻分类
         ImageView newsimage;//新闻图片
         ViewGroup layout_content; //内容布局

        itemView=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            btn_Delete=itemView.findViewById(R.id.collection_tv_delete);
            newstitle=itemView.findViewById(R.id.collection_news_title);
            newsimage=itemView.findViewById(R.id.collection_img);
            newstime=itemView.findViewById(R.id.collection_news_time);
            newscategory=itemView.findViewById(R.id.collection_news_category);
            layout_content=itemView.findViewById(R.id.collection_layout_content);

        newstitle.setText(news.getTitle());
        //使用开源Glide项目将网络图片Url直接放入ImageView控件中
        Glide.with(context).load(news.getImageUrl()).into(newsimage);
        newscategory.setText(news.getCategory());
        newstime.setText(news.getNewsTime());
        //设置内容布局的宽为屏幕的宽
        layout_content.getLayoutParams().width = Util.getScreenWidth(context);


        //点击删除按钮删除
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewsList.remove(position);
                notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //创建请求体对象
                        CommonRequest request = new CommonRequest();
                        // 填充参数:账号和新闻url和请求码
                        request.addRequestParam("account", UserManager.getCurrentUser().getAccount());
                        request.addRequestParam("url", news.getUrl());
                        request.addRequestParam("requestCode", Consts.REQUEST_DELETE_COLLECTION);
                        // POST请求
                        HttpUtil.sendPost(Consts.URL_COLLTION, request.getJsonStr(), new okhttp3.Callback() {

                            @Override
                            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                CommonResponse res = new CommonResponse(response.body().string());
                                //将新闻list转换成news数组
                                Util.makeToast(getContext(),res.getResMsg());

                            }

                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                e.printStackTrace();
                                Util.makeToast(getContext(),"网络错误");
                            }
                        });
                    }
                });
            }
        });
        return itemView;
    }

}

