package org.techtown.just;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.kakao.auth.Session;

import org.json.JSONObject;
import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.LoginResult;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_general_login)
    Button btnGeneralLogin;
    @BindView(R.id.id)
    EditText editText_id;
    @BindView(R.id.pw)
    EditText editText_pw;
    @BindView(R.id.btn_register)
    Button btnRegister;
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
        ButterKnife.bind(this);

        mContext = getApplicationContext();
        FacebookSdk.sdkInitialize(mContext);
        btnGeneralLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        //자동로그인
        isLogined();

//        String userId =  getLocalStore().getStringValue(LocalStore.UserId);
//        if(userId != null) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }

        btn_facebook_login = (LoginButton) findViewById(R.id.btn_facebook_login);
        btn_kakao_login = (com.kakao.usermgmt.LoginButton) findViewById(R.id.btn_kakao_login);

        btn_customKakao = (Button) findViewById(R.id.btn_custom_kakao);
        btn_customFacebook = (Button) findViewById(R.id.btn_custom_facebook);

        btn_customKakao.setOnClickListener(this);
        btn_customFacebook.setOnClickListener(this);


//        if (getLocalStore().getStringValue(LocalStore.AccessToken) != null) {
//            checkTokenIsValid();
//        } else {
//            //facebook, kakaoTalk login
//            if (AccessToken.getCurrentAccessToken() != null) {
//                mAccessTokenTracker = new AccessTokenTracker() {
//                    @Override
//                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                        mAccessTokenTracker.stopTracking();
//                        if (currentAccessToken == null) {
//                            //(the user has revoked your permissions -
//                            //by going to his settings and deleted your app)
//                            //do the simple login to FaceBook
//                            //case 1
//                        } else {
//                            //you've got the new access token now.
//                            //AccessToken.getToken() could be same for both
//                            //parameters but you should only use "currentAccessToken"
//                            //case 2
//                            fetchProfile();
//                        }
//                    }
//                };
//                mAccessTokenTracker.startTracking();
//                AccessToken.refreshCurrentAccessTokenAsync();
//            }
//
//            //access token 유효성 확인 - 최초 1번
//            AccessToken accessToken = AccessToken.getCurrentAccessToken();
//            checkLogin = accessToken != null && !accessToken.isExpired();
//
//            callback_kakao = new KakaoSessionCallback();
//
//            Session.getCurrentSession().addCallback(callback_kakao);
//
//        }

    }

    public void isLogined() {
        String id = getLocalStore().getStringValue(LocalStore.UserId);
        String pw = getLocalStore().getStringValue(LocalStore.UserPw);
        if (id != null && pw != null) {
            Toast.makeText(mContext, "userid, pw있다", Toast.LENGTH_SHORT).show();
            login(id, pw);
        }
        else
            Toast.makeText(mContext, "userid 없다", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }



    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_custom_kakao:
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
            case R.id.btn_general_login:
                String id = this.editText_id.getText().toString();
                String pw = this.editText_pw.getText().toString();

                if (id.length() == 0 || pw.length() == 0) {
                    Toast.makeText(LoginActivity.this, "아이디, 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                login(id, pw);
                break;

            case R.id.btn_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void login(final String id, final String pw) {
        final Call<LoginResult> login = getNetworkManager().getBookApi().login(id, pw);
        Toast.makeText(LoginActivity.this, id + " " + pw, Toast.LENGTH_SHORT).show();
        login.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult loginResult = response.body();
                //user_id, LoginToken이 있다.
                if (loginResult != null && loginResult.getTokens() != null) {
                    getLocalStore().setStringValue(LocalStore.AccessToken, loginResult.getTokens().getAccessToken());
                    getLocalStore().setStringValue(LocalStore.IdToken, loginResult.getTokens().getIdToken());
                    getLocalStore().setStringValue(LocalStore.RefreshToken, loginResult.getTokens().getRefreshToken());
                    getLocalStore().setStringValue(LocalStore.UserId, id);
                    getLocalStore().setStringValue(LocalStore.UserPw, pw);

                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (response.code() == 401) {
                    Toast.makeText(LoginActivity.this, "이메일 인증이 필요합니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
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

    private void checkTokenIsValid() {
        final Call<JsonObject> login = getNetworkManager().getBookApi().validate();
        login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MyPageActivity.class);
                    startActivity(intent);
                }else{

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}
