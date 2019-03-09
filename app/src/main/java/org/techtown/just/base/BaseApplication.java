package org.techtown.just.base;


import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.KakaoSDK;

import org.techtown.just.KakaoSDKAdapter;
import org.techtown.just.model.LocalStore;
import org.techtown.just.R;
import org.techtown.just.network.NetworkManager;

public class BaseApplication extends Application {

    private static  volatile BaseApplication baseApplication;
    private static LocalStore localStore;
    private static NetworkManager networkManager;

   // private static volatile BaseApplication obj = null;
    private static volatile Activity currentActivity = null;



    @Override
    public void onCreate(){
        super.onCreate();
        baseApplication =this;
        //obj = this;
        //kakao sdk초기화
        KakaoSDK.init(new KakaoSDKAdapter());
        localStore = new LocalStore(this);
        networkManager = new NetworkManager(localStore);
    }


    public static BaseApplication getLoginKakaoContext(){
//        if(baseApplication==null){
//            throw new IllegalStateException("This Application doesn't inherit com.kakao.BaseApplication");
//        }
        return baseApplication;
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
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
        baseApplication =null;
    }

    AppCompatDialog progressDialog;

    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
//        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
//                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }


    }

    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }


        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
