package org.techtown.just;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class Login_FacebookActivity implements FacebookCallback<LoginResult> {

    //로그인 성공시 호출
    @Override
    public void onSuccess(LoginResult loginResult) {

        Log.e("Callback::", "onSuccess");
        requestMe(loginResult.getAccessToken());
//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.v("result", object.toString());
//                    }
//                });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                graphRequest.setParameters(parameters);
//                graphRequest.executeAsync();
    }

    @Override
    public void onCancel() {
        Log.e("Callback::", "onCancel");
    }


    @Override //로그인 실패시
    public void onError(FacebookException error) {
        Log.e("Callback::", "onError:" + error.getMessage());
    }

    public void requestMe(AccessToken token) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("result", object.toString());

                try{
                    String email = object.getString("email");
                    String name = object.getString("name");
                    String gender = object.getString("gender");
                    String birth = object.getString("birthday");

                    Log.e("Tag","페이스북 이메일 = "+email);
                    Log.e("Tag","페이스북 이름 = "+name);
                    Log.e("Tag","페이스북 성별 = "+gender);
                    Log.e("Tag","페이스북 생년월일 = "+birth);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
        }

    protected void onClick(){

    }

}
