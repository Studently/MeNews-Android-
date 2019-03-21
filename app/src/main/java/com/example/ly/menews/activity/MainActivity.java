package com.example.ly.menews.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ly.menews.R;
import com.example.ly.menews.adapter.MyViewpagerAdapter;
import com.example.ly.menews.fragment.NewsListFragment_CF;
import com.example.ly.menews.fragment.NewsListFragment_New;
import com.example.ly.menews.fragment.NewsListFragment_Popular;
import com.example.ly.menews.utils.ActivityController;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    //DrawerLayout对象
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    //导航条
    private ActionBar actionBar;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private MyViewpagerAdapter myViewpagerAdapter;


    //tab标题
    private List<String> titles = new ArrayList<>();

    //fragments
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();
        initEvents();



        if(actionBar!=null){
            //ToolBar最左侧的按钮就叫做 HomeAsUp
            //显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置导航按钮的图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //设置滑动菜单中菜单按钮中默认选中的是电话
        navigationView.setCheckedItem(R.id.nav_collection);

        //设置菜单项选中监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_collection:
                        Intent col_intent=new Intent(MainActivity.this,CollectionActivity.class);
                        startActivity(col_intent);
                        break;
                    case R.id.nav_setting:
                        Intent set_intent=new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(set_intent);
                        break;
                    case R.id.nav_exit:
                        //Toast.makeText(MainActivity.this,"!!!",Toast.LENGTH_SHORT).show();
                        ActivityController.finishAll();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            //HomeAsUp的id为android.R.id.home
            case android.R.id.home:
                //GravityCompat.START需要和xml文件中的Gravity一致
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
                default:

        }
        return true;
    }


    //初始化控件
    private void initViews() {


        mDrawerLayout=findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tl);
        mViewPager = findViewById(R.id.vp);
        navigationView=findViewById(R.id.nav_view);
        //Toolbar 通过 setSupportActionBar(toolbar) 被修饰成了actionbar
        setSupportActionBar(mToolbar);
        //getSupportActionBar()获得ActionBar实例
        actionBar=getSupportActionBar();


    }

    private void initDatas() {

        titles.add("最新");
        titles.add("热门");
        titles.add("推荐");

        //传递2个参数值，新建ContentFragment对象
        Fragment fragment_new=new NewsListFragment_New();
        Fragment fragment_popular=new NewsListFragment_Popular();
        Fragment fragment_cf = new NewsListFragment_CF();
        fragments.add(fragment_new);
        fragments.add(fragment_popular);
        fragments.add(fragment_cf);




    }

    private void initEvents() {

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_search:
                        //TODO
                        Toast.makeText(MainActivity.this,"搜索！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_about:
                        //TODO
                        Toast.makeText(MainActivity.this,"关于！",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        //初始化myViewpagerAdapter
        myViewpagerAdapter = new MyViewpagerAdapter(getSupportFragmentManager(), titles, fragments);

        //给viewpager设置adapter适配器
        mViewPager.setAdapter(myViewpagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
}
