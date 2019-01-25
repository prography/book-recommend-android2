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
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfo;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.searchStr)
    EditText editText;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_my)
    ImageView btnMy;
    @BindView(R.id.btn)
    Button btn;

    String sfName = "myFile";

    SearchResultFragment searchResultFragment;
    @BindView(R.id.text)
    TextView text;

    List<BookInfo> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String searchStr = intent.getStringExtra("searchStr");

        //책 가져오기 (test)
        Call<List<BookInfo>> list = getNetworkManager().getBookApi().getListWithSearch("한"); //searchStr을 넣으면 안됨
        list.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                books = response.body();
                text.setText(books.get(0).getBook_name());

            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btn.setOnClickListener(this);





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
                if (i == 0) {
                    intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else
                    intent = new Intent(this, MyPageActivity.class);

                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
                //검색 결과 밑에 프래그먼트에 나오게
                break;

            case R.id.btn:
                intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                intent.putExtra("isbn", books.get(0).getIsbn());
                startActivity(intent);
                break;
        }
    }
}
