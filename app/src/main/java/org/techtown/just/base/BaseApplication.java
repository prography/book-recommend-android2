package org.techtown.just.base;


import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;

import org.techtown.just.KakaoSDKAdapter;
import org.techtown.just.LocalStore;
import org.techtown.just.network.NetworkManager;

public class BaseApplication extends Application {

    private static  volatile BaseApplication instance;
    private static LocalStore localStore;
    private static NetworkManager networkManager;

   // private static volatile BaseApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate(){
        super.onCreate();
        instance=this;
        //obj = this;
        //kakao sdk초기화
        KakaoSDK.init(new KakaoSDKAdapter());
        localStore = new LocalStore(this);
        networkManager = new NetworkManager(localStore);
    }


    public static BaseApplication getLoginKakaoContext(){
//        if(instance==null){
//            throw new IllegalStateException("This Application doesn't inherit com.kakao.BaseApplication");
//        }
        return instance;
    }

    public static LocalStore getLocalStore(){
        return localStore;
    }

    public static NetworkManager getNetworkManager() {
        return networkManager;
    }

    public static Activity getCurrentActivity(){ return currentActivity;   }

    //activity가 올라올때마다 activity의 oncreate에서 호출
    public static void setCurrentActivity(Activity currentActivity){
        BaseApplication.currentActivity = currentActivity;
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        instance=null;
    }
}
