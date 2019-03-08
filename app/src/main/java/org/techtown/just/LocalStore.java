package org.techtown.just;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStore {
    String sfName = "today";

    /*
    Key
    */
    public static String my = "my";
    public static String UserId = "userId";
    public static String UserPw = "userPw";
    public static String nickname = "nickname";
    public static String login = "login";
    public static String AccessToken = "accessToken";
    public static String IdToken = "idToken";
    public static String RefreshToken = "refreshToken";


    private SharedPreferences sf;

    public LocalStore(Context context) {
        sf = context.getSharedPreferences(sfName, Context.MODE_PRIVATE);
    }


    public void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        editor.putInt(key, value); // 입력
        editor.commit(); // 파일에 최종 반영함]
    }

    public int getIntValue(String key, int defaultValue) {
        return sf.getInt(key, defaultValue);
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        editor.putString(key, value); // 입력
        editor.commit(); // 파일에 최종 반영함]
    }

    public String getStringValue(String key) {
        return sf.getString(key, null);
    }

    public void setBooleanValue(String key, Boolean value) {
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        editor.putBoolean(key, value); // 입력
        editor.commit(); // 파일에 최종 반영함]
    }

    public Boolean getBooleanValue(String key, Boolean defaultValue) {
        return sf.getBoolean(key, defaultValue);
    }

    public void clearTokenValues() { //logout
        setStringValue(AccessToken, null);
        setStringValue(IdToken, null);
        setStringValue(RefreshToken, null);

    }


}
