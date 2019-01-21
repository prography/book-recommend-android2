package org.techtown.just;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login2Activity extends AppCompatActivity {

    Login login;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);

        final Call<Login> loginInfo = NetworkManagerLogin.getApiService().register("dahee", "dahee123", "deg9810@naver.com");
        loginInfo.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                String result = response.body().toString();
                Log.v("Test", result); //받아온 데이터
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    login = new Login();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        login.setId(jsonObject.getString("id"));
                        login.setPw(jsonObject.getString("pw"));
                        login.setEmail(jsonObject.getString("email"));
                        //dataArrayList.add(data);
                        text.setText(login.toString());
                        Log.v("Test", jsonObject.toString());


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }
}
