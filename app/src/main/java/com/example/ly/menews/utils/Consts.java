package com.example.ly.menews.utils;

public class Consts {

    private static String URL = "http://192.168.43.64:8080/MeNews/";
    public static String URL_Register = URL + "RegisterServlet";//注册请求
    public static String URL_Login = URL + "LoginServlet";//登陆请求
    public static String URL_GetNews = URL + "GetNewsServlet";//获取新闻请求
    public static String URL_COLLTION = URL + "CollectionServlet";//获取收藏新闻请求

    public static String URL_UpdatePwd=URL + "UpdatePwdServlet";//修改密码请求
    public static String URL_UpdateVersion=URL + "UpdateVersionServlet";//更新版本请求
    public static String URL_ReadNews=URL + "ReadNewsServlet";//更新版本请求



    // 服务器相应代码
    public static String SUCCESSCODE_LOGIN = "100";//登陆成功
    public static String SUCCESSCODE_REGISTER = "101";//注册成功
    public static String SUCCESSCODE_GET_NEWS="102";//新闻获取成功
    public static String SUCCESSCODE_UPDATEPWD="103";//密码修改成功
    public static String ERRORCODE_ACCOUNTNOTEXIST = "202";//登陆账号不存在
    public static String ERRORCODE_ACCOUNTEXIST = "203";//注册账号已存在


    //请求码
    //新闻请求码
    public static String REQUEST_NEWS_CF="001";//请求推荐新闻
    public static String REQUEST_NEWS_NEW="002";//请求最新新闻
    public static String REQUEST_NEWS_POPULAR="003";//请求最热新闻
    public static String REQUEST_NEWS_COLLECTION="004";//请求收藏新闻

    public static String REQUEST_ADD_COLLECTION="005";//请求添加收藏
    public static String REQUEST_DELETE_COLLECTION="006";//请求删除收藏


}
