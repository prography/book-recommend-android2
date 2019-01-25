package org.techtown.just;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.gson.JsonObject;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.LoginResult;
import org.techtown.just.model.Tag;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    String sfName = "myFile";
    Boolean isLoggedIn, id;

    @BindView(R.id.btn_my)
    ImageView btnMy;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.button)
    Button button;

    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    TagNames tagNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //btnMy
        btnMy.setOnClickListener(this);
        button.setOnClickListener(this);

        //checkbox의 text <- tagNames의 text 대입
        tagNames = new TagNames();

        //tagNames 가져오기
        Call<List<Tag>> list = getNetworkManager().getBookApi().getTags();
        list.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                List<Tag> tags = response.body();
                tagNames.setTags(tags);
                for (int i = 0; i < tagNames.getTags().size(); i++)
                    flowLayout.addTag(tagNames.getTags().get(i));

            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        flowLayout.relayoutToAlign();

        //kakao session 확인
//         Session session = Session.getCurrentSession();
//         session.open(AuthType.KAKAO_LOGIN_ALL,MainActivity.this);

        //access token 유효성 확인 - 최초 1번
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
/*
        //직접 코드에서 해시키 생성.
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "org.techtown.just",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
*/
    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();

        isLoggedIn = getLocalStore().getBooleanValue(LocalStore.my, isLoggedIn);
        Log.d("Main_onResume :: ", "isLoggedIn : " + isLoggedIn);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //token재확인
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        isLoggedIn = accessToken != null && !accessToken.isExpired();

        isLoggedIn = getLocalStore().getBooleanValue(LocalStore.my, isLoggedIn);
        Log.d("Main_onRestart :: ", "isLoggedIn : " + isLoggedIn);
        //세션 재확인
//        Session session = Session.getCurrentSession();
//        session.open(AuthType.KAKAO_LOGIN_ALL,MainActivity.this);
//        id = session.isOpened();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        //aaaa
        Intent intent;
        Random random = new Random();
        switch (view.getId()) {
            case R.id.btn_my:
                // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기

//                getLocalStore().setBooleanValue(LocalStore.my, isLoggedIn);
                isLoggedIn = getLocalStore().getBooleanValue(LocalStore.my, isLoggedIn);
                
                //getLocalStore().setBooleanValue(LocalStore.my, id);
                if (isLoggedIn == true) {
                    //checkTokenIsValid();
                } else {
                    intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            case R.id.button:
                //true인 애들만 추가,,, check1 check3만일 경우 0, 2
                String s = "";
                intent = new Intent(this, RecommendDetailActivity.class);

                tagNames.updateSelectedTags(flowLayout.getCheckedTagValues());

                intent.putExtra("tagNames", tagNames);
                startActivity(intent);
                break;
            case R.id.btn_general_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }




    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        if (checkBox_anything.isChecked())
//            for (int i = 0; i < cb.length; i++) {
//                cb[i].setChecked(false);
//                cb[i].setClickable(false);
//            }
//        else
//            for (int i = 0; i < cb.length; i++)
//                cb[i].setClickable(true);
    }

    void doJSONParser() {
        StringBuffer sb = new StringBuffer();

        String str =
                "[{'name':'배트맨','age':43,'address':'고담'}," +
                        "{'name':'슈퍼맨','age':36,'address':'뉴욕'}," +
                        "{'name':'앤트맨','age':25,'address':'LA'}]";

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String address = jObject.getString("address");
                String name = jObject.getString("name");
                int age = jObject.getInt("age");

                sb.append(
                        "주소:" + address +
                                "이름:" + name +
                                "나이:" + age + "\n"
                );
            }
            //tv.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // end doJSONParser()
}



