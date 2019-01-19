package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.Login;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.Session;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.Arrays;

import static com.kakao.auth.Session.getCurrentSession;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;

    private LoginButton btn_facebook_login;
    private com.kakao.usermgmt.LoginButton btn_kakao_login;
    private Button btn_customFacebook, btn_customKakao;

    private Login_FacebookActivity mLoginCallback;
 //   private KakaoSessionCallback kakaoSessionCallback;
    private CallbackManager callbackManager;
    private KakaoSessionCallback callback;

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

//        callback = new KakaoSessionCallback(LoginActivity.this);
//        getCurrentSession().addCallback(callback);
        //kakaocallback = new KakaoSessionCallback();


        btn_facebook_login =(LoginButton) findViewById(R.id.btn_facebook_login);
        btn_facebook_login.setReadPermissions(Arrays.asList("public_profile","email"));
        btn_facebook_login.registerCallback(callbackManager, mLoginCallback);

//        btn_kakao_login=(LoginButton)findViewById(R.id.btn_kakao_login);
        btn_customKakao=(Button)findViewById(R.id.btn_custom_kakao);
        btn_kakao_login = (com.kakao.usermgmt.LoginButton) findViewById(R.id.btn_kakao_login);

        btn_customFacebook = (Button)findViewById(R.id.btn_custom_facebook);
        btn_customKakao.setOnClickListener(this);
        btn_customFacebook.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_custom_kakao :
                btn_kakao_login.performClick();
                break;
            case R.id.btn_custom_facebook:
                btn_facebook_login.performClick();
                break;
        }
    }
}
