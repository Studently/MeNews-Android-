package com.example.ly.menews.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Util {

    /**
     * @description 字符串处理，防止SQL注入
     * @param input
     * @return
     */
    public static String StringHandle(String input){
        String output;
        // 将包含有 单引号(')，分号(;) 和 注释符号(--)的语句替换掉
        output = input.trim().replaceAll(".*([';]+|(--)+).*", " ");
        return output;
    }

    /**
     * Toast封装
     * @param context
     * @param msg
     */
    public static void makeToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据生日推算年龄
     * @param birthDay
     * @return
     */
    public static int getAgeFromDate(Date birthDay){
        int age;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDay);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }


    /**
     * Date中提取年月日
     * @param date
     * @return
     */
    public static int getDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }
    public static int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 判断sd卡是否存在
     * @return
     */
    public static boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将字符串解析为json数据
     * @param jsonString
     * @return
     * @throws JSONException
     */
    public static Map<String, String> toMap(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, String> result = new HashMap<String, String>();
        Iterator<?> iterator = jsonObject.keys();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;
    }


    /**
     * 将map数据封装成json字符串
     * @param map
     * @return
     */
    public static String toJson(Map<String,Object> map){
        JSONObject mapJson = new JSONObject(map); // 传入Map类型
        return mapJson.toString();
    }


    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        //获取窗口管理服务
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        //屏幕参数对象
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getPackageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String getPackageVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String code = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

}
