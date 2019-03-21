package com.example.ly.menews.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ly.menews.R;
import com.example.ly.menews.domain.News;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private int resourceId;
    //新闻list
    private List<News> mNewsList;
    private Context context;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        resourceId=resource;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        News news=getItem(position);
        View itemView= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        //新闻list显示需要一些数据
        ImageView newsImage;
        TextView newsTitle;

        newsImage=itemView.findViewById(R.id.news_image);
        newsTitle=itemView.findViewById(R.id.news_title);


        Glide.with(context).load(news.getImageUrl()).into(newsImage);
        newsTitle.setText(news.getTitle());
        return itemView;
    }
}
