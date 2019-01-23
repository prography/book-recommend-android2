package org.techtown.just;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class LocalStore {
    String sfName = "today";

    /*
    Key
    */
    public static String my = "my";

    public static String nickname = "nickname";
    public static String login = "login";


    private static SharedPreferences sf;

    public LocalStore(Context context) {
        sf = context.getSharedPreferences(sfName, Context.MODE_PRIVATE);
    }


    public static void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        editor.putInt(key, value); // 입력
        editor.commit(); // 파일에 최종 반영함]
    }

    public static int getIntValue(String key, int defaultValue) {
        return sf.getInt(key, defaultValue);
    }

    public static void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        editor.putString(key, value); // 입력
        editor.commit(); // 파일에 최종 반영함]
    }

    public static String getStringValue(String key) {
        return sf.getString(key, null);
    }

    public static void setBooleanValue(String key, Boolean value) {
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        editor.putBoolean(key, value); // 입력
        editor.commit(); // 파일에 최종 반영함]
    }

    public static Boolean getBooleanValue(String key, Boolean defaultValue) {
        return sf.getBoolean(key, defaultValue);
    }


}
