package org.techtown.just;


import android.app.Application;

import com.kakao.auth.KakaoSDK;

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private static LocalStore localStore;

    public static BaseApplication getLoginKakaoContext(){
        if(instance==null){
            throw new IllegalStateException("This Application doesn't inherit com.kakao.BaseApplication");
        }
        return instance;
    }

    public static LocalStore getLocalStore(){
        return localStore;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        instance=this;

        //kakao sdk초기화
        KakaoSDK.init(new KakaoSDKAdapter());

        localStore = new LocalStore(this);

    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        instance=null;
    }
}
