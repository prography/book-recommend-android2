package org.techtown.just;

import android.content.Context;
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

import com.facebook.AccessToken;
import com.kakao.auth.ISessionConfig;
import com.kakao.kakaotalk.response.KakaoTalkProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    String sfName = "myFile";
    Boolean isLoggedIn;

    @BindView(R.id.btn_my)
    ImageView btnMy;
    @BindView(R.id.text)
    TextView text;
//    @BindView(R.id.checkBox_anything)
//    CheckBox checkBox_anything;
//    @BindView(R.id.checkBox1)
//    CheckBox checkBox1;
//    @BindView(R.id.checkBox2)
//    CheckBox checkBox2;
//    @BindView(R.id.checkBox3)
//    CheckBox checkBox3;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.btn_posts)
    Button btnPosts;
    @BindView(R.id.btn_tags)
    Button btnTags;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    CheckBox[] cb;


    TagNames tagNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        doJSONParser();


        //checkbox의 text <- tagNames의 text 대입
        tagNames = new TagNames();



//        cb = new CheckBox[]{checkBox1, checkBox2, checkBox3};

        for (int i = 0; i < tagNames.getTags().length; i++)
            flowLayout.addTag(tagNames.getTags()[i]);

        flowLayout.relayoutToAlign();


        //btnMy
        btnMy.setOnClickListener(this);
        button.setOnClickListener(this);
        btnPosts.setOnClickListener(this);
        btnTags.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //아무거나를 선택하면 나머지는 false로
//        checkBox_anything.setOnCheckedChangeListener(this);

        //access token 유효성 확인
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        /*try {
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

                getLocalStore().setBooleanValue(LocalStore.my, isLoggedIn);
                if (isLoggedIn == false)
                    intent = new Intent(this, LoginActivity.class);
                else
                    intent = new Intent(this, LoginActivity.class);

                startActivity(intent);

                break;

            case R.id.button:
                //true인 애들만 추가,,, check1 check3만일 경우 0, 2
                String s = "";
                intent = new Intent(this, RecommendDetailActivity.class);

                tagNames.updateSelectedTags(flowLayout.getCheckedTagValues());

                intent.putExtra("tagNames", tagNames);
                startActivity(intent);
                break;
            case R.id.btn_posts:
                intent = new Intent(this, PostsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tags:
                intent = new Intent(this, TagsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
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



