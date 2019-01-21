package org.techtown.just;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.techtown.just.model.LoginResult;
import org.techtown.just.model.LoginToken;
import org.techtown.just.network.NetworkManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        String id = "id";
        String pw = "pw!#@"; //PW는 반드시 특수문자 포함해야된다함
        String email = "email@email.com";
        register(id, pw, email);
    }


    private void register(final String id, final String pw, final String email) {
        Call<JsonObject> login = NetworkManager.getLoginApi().register(id, pw, email);
        login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    login(id, pw, email);
                } else {
                    Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void login(String id, String pw, String email) {

        final Call<LoginResult> login = NetworkManager.getLoginApi().login(id, pw, email);
        login.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                    LoginResult loginResult = response.body();
                    LoginToken token = loginResult.getTokens();

                    //TODO : token을 sharedPreference에 저장하기!!

                    /**
                     * 1.앱에서 로그아웃할때, sharedPreference에서 토큰 관련한 정보들을 삭제해야함~
                     * 2.앱실행시 sharedPreference에 token들이 존재한다면,token값들이 유효한지 먼저 확인하고, 자동로그인 시켜주면됩니당
                     * 2-1. 토큰 유효성 검증
                     */
                } else {
                    Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateToken(String accessToken, String idToken, String refreshToken){
        final Call<JsonObject> login = NetworkManager.getLoginApi().validateToken(accessToken, idToken, refreshToken);
        login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                } else {
                    //TODO : 저장되어있던 token들 sharedPreference에서 지우기!! + 로그인하는 화면으로 돌아가게 하기
                    Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
