//package org.techtown.just;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//<<<<<<< HEAD
//import android.widget.TextView;
//=======
//import android.widget.Toast;
//>>>>>>> master
//
//import com.google.gson.JsonObject;
//
//import org.techtown.just.model.LoginResult;
//import org.techtown.just.model.LoginToken;
//import org.techtown.just.network.NetworkManager;
//
//<<<<<<< HEAD
//import butterknife.BindView;
//import butterknife.ButterKnife;
//=======
//>>>>>>> master
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Login2Activity extends AppCompatActivity {
//
//    Login login;
//    @BindView(R.id.text)
//    TextView text;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login2);
//        ButterKnife.bind(this);
//
//<<<<<<< HEAD
//        final Call<Login> loginInfo = NetworkManagerLogin.getApiService().register("dahee", "dahee123", "deg9810@naver.com");
//        loginInfo.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                String result = response.body().toString();
//                Log.v("Test", result); //받아온 데이터
//                try {
//                    JSONArray jsonArray = new JSONArray(result);
//                    login = new Login();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        login.setId(jsonObject.getString("id"));
//                        login.setPw(jsonObject.getString("pw"));
//                        login.setEmail(jsonObject.getString("email"));
//                        //dataArrayList.add(data);
//                        text.setText(login.toString());
//                        Log.v("Test", jsonObject.toString());
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//=======
//        String id = "id";
//        String pw = "pw!#@"; //PW는 반드시 특수문자 포함해야된다함
//        String email = "email@email.com";
//        register(id, pw, email);
//    }
//
//
//    private void register(final String id, final String pw, final String email) {
//        Call<JsonObject> login = NetworkManager.getLoginApi().register(id, pw, email);
//        login.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    login(id, pw, email);
//                } else {
//                    Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//>>>>>>> master
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//
//    private void login(String id, String pw, String email) {
//
//        final Call<LoginResult> login = NetworkManager.getLoginApi().login(id, pw, email);
//        login.enqueue(new Callback<LoginResult>() {
//            @Override
//            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
//                if (response.isSuccessful()) {
//                    LoginResult loginResult = response.body();
//                    LoginToken token = loginResult.getTokens();
//
//                    //TODO : token을 sharedPreference에 저장하기!!
//
//                    /**
//                     * 1.앱에서 로그아웃할때, sharedPreference에서 토큰 관련한 정보들을 삭제해야함~
//                     * 2.앱실행시 sharedPreference에 token들이 존재한다면,token값들이 유효한지 먼저 확인하고, 자동로그인 시켜주면됩니당
//                     * 2-1. 토큰 유효성 검증
//                     */
//                } else {
//                    Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<LoginResult> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void validateToken(String accessToken, String idToken, String refreshToken){
//        final Call<JsonObject> login = NetworkManager.getLoginApi().validateToken(accessToken, idToken, refreshToken);
//        login.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//
//                } else {
//                    //TODO : 저장되어있던 token들 sharedPreference에서 지우기!! + 로그인하는 화면으로 돌아가게 하기
//                    Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(Login2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//}
