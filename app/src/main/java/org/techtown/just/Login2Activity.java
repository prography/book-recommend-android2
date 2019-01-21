package org.techtown.just;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Call<Login> login = NetworkManagerLogin.getApiService().register("dahee", "dahee123", "deg9810@naver.com");
        login.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try {
                    String result = response.body().string();
                    Log.v("Test", result); //받아온 데이터
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        data = new Data();
                        for (int i = 0 ; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            data.setPostId(jsonObject.getInt("postId"));
                            data.setId(jsonObject.getInt("id"));
                            data.setName(jsonObject.getString("name"));
                            data.setEmail(jsonObject.getString("email"));
                            data.setBody(jsonObject.getString("body"));
                            //dataArrayList.add(data);
                            text.setText(data.toString());
                            Log.v("Test", jsonObject.toString());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }
}
