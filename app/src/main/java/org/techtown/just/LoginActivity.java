package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodSession;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.response.model.UserProfile;

import org.json.JSONObject;

import java.util.Arrays;

import static com.kakao.auth.Session.getCurrentSession;
import static org.techtown.just.LocalStore.my;
import static org.techtown.just.base.BaseApplication.getLocalStore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;

    private LoginButton btn_facebook_login;
    private com.kakao.usermgmt.LoginButton btn_kakao_login;

    private Button btn_customFacebook, btn_customKakao;

    private Login_FacebookActivity mLoginCallback;
    private KakaoSessionCallback callback_kakao;

    private CallbackManager callbackManager;
    private Boolean checkLogin = false;
    AccessTokenTracker mAccessTokenTracker;


    //access token 유효성 확인
    AccessToken accessToken = AccessToken.getCurrentAccessToken();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=getApplicationContext();
        FacebookSdk.sdkInitialize(mContext);

        if (AccessToken.getCurrentAccessToken() != null) {
            mAccessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    mAccessTokenTracker.stopTracking();
                    if(currentAccessToken == null) {
                        //(the user has revoked your permissions -
                        //by going to his settings and deleted your app)
                        //do the simple login to FaceBook
                        //case 1
                    }
                    else {
                        //you've got the new access token now.
                        //AccessToken.getToken() could be same for both
                        //parameters but you should only use "currentAccessToken"
                        //case 2
                        fetchProfile();
                    }
                }
            };
            mAccessTokenTracker.startTracking();
            AccessToken.refreshCurrentAccessTokenAsync();
        }
        //access token 유효성 확인 - 최초 1번
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        checkLogin = accessToken != null && !accessToken.isExpired();

      callback_kakao = new KakaoSessionCallback();

      Session.getCurrentSession().addCallback(callback_kakao);

//        getCurrentSession().addCallback(callback);
        //kakaocallback = new KakaoSessionCallback();

        btn_facebook_login =(LoginButton) findViewById(R.id.btn_facebook_login);
        btn_kakao_login = (com.kakao.usermgmt.LoginButton) findViewById(R.id.btn_kakao_login);

        btn_customKakao=(Button)findViewById(R.id.btn_custom_kakao);
        btn_customFacebook = (Button)findViewById(R.id.btn_custom_facebook);

        btn_customKakao.setOnClickListener(this);
        btn_customFacebook.setOnClickListener(this);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("Login_onRestart",": e");

        //checkLogin();

    }

    @Override
    protected void onResume(){
        super.onResume();
        checkLogin = getLocalStore().getBooleanValue(LocalStore.my, checkLogin);

        Log.d("Login_onResume :: ","checkLogin : "+checkLogin);
        if(checkLogin==true){
            Intent intent = new Intent(this, MyPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.setFlags(Intent.)
            startActivity(intent);
        }
        //checkLogin();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
            return;
        }
        super.onActivityResult(requestCode,resultCode,data);

        //if(accessToken != null && !accessToken.isExpired()){
        callbackManager.onActivityResult(requestCode,resultCode,data);
        //}

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_custom_kakao:

//                callbackManager = CallbackManager.Factory.create();
//                Session session = Session.getCurrentSession();
//                session.addCallback(new KakaoSessionCallback());
//                session.open(AuthType.KAKAO_LOGIN_ALL,LoginActivity.this);
                //callback_kakao = new KakaoSessionCallback();

//                btn_kakao_login.openSession(AuthType.KAKAO_LOGIN_ALL);

                btn_kakao_login.performClick();

                break;

            case R.id.btn_custom_facebook:

                //facebook login
                callbackManager = CallbackManager.Factory.create();
                mLoginCallback = new Login_FacebookActivity();
                btn_facebook_login.setReadPermissions(Arrays.asList("public_profile"));
                btn_facebook_login.registerCallback(callbackManager, mLoginCallback);

                btn_facebook_login.performClick();

                break;
        }

    }



    public void checkLogin(){

        //access token 유효성 확인
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //isLoggedIn = accessToken != null && !accessToken.isExpired();

//        LocalStore.getBooleanValue(LocalStore.my,isLoggedIn);
//        //로그인 성공시 mypage activity로
//        if(isLoggedIn==true) {
//            Intent intent = new Intent(this, MyPageActivity.class);
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        }


    }

    private void fetchProfile() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // this is where you should have the profile
                        Log.v("fetched info", object.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link"); //write the fields you need
        request.setParameters(parameters);
        request.executeAsync();
    }
}
