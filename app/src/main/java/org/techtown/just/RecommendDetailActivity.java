package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_my)
    Button btnMy;

    String sfName = "myFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int randomNum = intent.getIntExtra("randomNum", -1);

        TagNames tagNames = (TagNames) intent.getSerializableExtra("tagNames");
        String s = "";
        if (randomNum == -1) {
            for (int i = 0; i < tagNames.getTagIndex().length; i++)
                if (tagNames.getTagIndex()[i] == 1)
                    s += tagNames.getTags()[i] + " ";
        } else { //아무거나 선택 시
            s = tagNames.getTags()[randomNum];
        }
        textView.setText(s);

        btnMy.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_my:
                //login 되어있으면 my, 안되어있으면 login
                // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
                SharedPreferences sf = getSharedPreferences(sfName, 0);
                SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요

                int i = sf.getInt("my", 0);
                if (i == 0)
                    intent = new Intent(this, LoginActivity.class);
                else
                    intent = new Intent(this, MyPageActivity.class);

                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
