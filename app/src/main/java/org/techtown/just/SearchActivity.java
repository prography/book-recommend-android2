package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_my)
    ImageView btnMy;

    String sfName = "myFile";

    SearchResultFragment searchResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                // 입력이 끝났을 때

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // 입력하기 전에

            }
        });


        //search result fragment
        searchResultFragment = new SearchResultFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchResultFragment).commit();

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
                if (i == 0){
                    intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                else
                    intent = new Intent(this, MyPageActivity.class);

                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
                //검색 결과 밑에 프래그먼트에 나오게
                break;
        }
    }
}
