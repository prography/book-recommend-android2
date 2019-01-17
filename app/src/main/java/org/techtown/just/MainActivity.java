package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    String sfName = "myFile";
    Boolean isLoggedIn ;

    @BindView(R.id.btn_my)
    Button btnMy;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.checkBox_anything)
    CheckBox checkBox_anything;
    @BindView(R.id.checkBox1)
    CheckBox checkBox1;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    @BindView(R.id.checkBox3)
    CheckBox checkBox3;

    @BindView(R.id.button)
    Button button;

    CheckBox[] cb;


    TagNames tagNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //checkbox의 text <- tagNames의 text 대입
        tagNames = new TagNames();
        cb = new CheckBox[] {checkBox1, checkBox2, checkBox3};
        for (int i = 0; i < tagNames.getTags().length; i++)
            cb[i].setText(tagNames.getTags()[i]);

        //btnMy
        btnMy.setOnClickListener(this);
        button.setOnClickListener(this);

        //아무거나를 선택하면 나머지는 false로
        checkBox_anything.setOnCheckedChangeListener(this);

        //access token 유효성 확인
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();


        //jdk 사용이 달라서 직접 코드에서 해시키 생성.
/*        try {
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
        Intent intent;
        Random random = new Random();
        switch (view.getId()) {
            case R.id.btn_my:
                // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
                SharedPreferences sf = getSharedPreferences(sfName, MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요

                editor.putBoolean("my", isLoggedIn); // 입력
//                editor.putInt("my", 1); // 입력
                editor.commit(); // 파일에 최종 반영함

                boolean Login_success = sf.getBoolean("my", true);
                if (Login_success == false)
                    intent = new Intent(this, LoginActivity.class);
                else
                    intent = new Intent(this, MyPageActivity.class);

                startActivity(intent);

                break;

            case R.id.button:
                //true인 애들만 추가,,, check1 check3만일 경우 0, 2
                String s = "";
                intent = new Intent(this, RecommendDetailActivity.class);

                if (checkBox_anything.isChecked()) { //아무거나 선택 시
                    int randomNum = random.nextInt(cb.length);
                    intent.putExtra("randomNum", randomNum);
                }
                else {
                    for (int j = 0; j < tagNames.getTags().length; j++)
                        if (cb[j].isChecked() == true)
                            tagNames.setTagIndex(j);
                }

                intent.putExtra("tagNames", tagNames);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (checkBox_anything.isChecked())
            for (int i = 0; i < cb.length; i++) {
                cb[i].setChecked(false);
                cb[i].setClickable(false);
            }
        else
            for (int i = 0; i < cb.length; i++)
                cb[i].setClickable(true);
    }
}



