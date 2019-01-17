package org.techtown.just;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.Profile;
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

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Login_FacebookActivity implements FacebookCallback<LoginResult> {

    MyPageActivity myPageActivity;
    //로그인 성공시 호출
    @Override
    public void onSuccess(LoginResult loginResult) {

        Log.e("Callback::", "onSuccess");
        requestMe(loginResult.getAccessToken());

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

                //myPageActivity = new MyPageActivity();
                Log.e("result", object.toString());
                //myPageActivity.basic_setting(object);
                try{
//                    String email = object.getString("email");
//                    String name = object.getString("name");
//                    String gender = object.getString("gender");
//                    String birth = object.getString("birthday");

                    String id = response.getJSONObject().getString("id").toString();
                    String name = response.getJSONObject().getString("name").toString();
                    String email = response.getJSONObject().getString("email").toString();
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
