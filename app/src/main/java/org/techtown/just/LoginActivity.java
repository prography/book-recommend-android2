package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private Context mContext;

    private LoginButton btn_facebook_login;

    private Login_FacebookActivity mLoginCallback;
    private CallbackManager callbackManager;

    //access token 유효성 확인
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    Boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=getApplicationContext();
        FacebookSdk.sdkInitialize(mContext);

        callbackManager = CallbackManager.Factory.create();
        mLoginCallback=new Login_FacebookActivity();

        btn_facebook_login =(LoginButton) findViewById(R.id.login_button);
        btn_facebook_login.setReadPermissions(Arrays.asList("public_profile","email"));
        btn_facebook_login.registerCallback(callbackManager, mLoginCallback);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("Login_onRestart",": e");
        //로그인 성공시 mypage activity로
        if(isLoggedIn==true) {
            Intent intent = new Intent(this, MyPageActivity.class);
            startActivity(intent);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }


}
