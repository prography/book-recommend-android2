package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.searchStr)
    EditText editText;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_my)
    ImageView btnMy;
    @BindView(R.id.rc_recommend)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    private RecyclerViewAdapter adapter;
    TagNames tagNames;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        tagNames = (TagNames) intent.getSerializableExtra("tagNames");
        search = intent.getStringExtra("search");

        Toast.makeText(SearchActivity.this, search, Toast.LENGTH_SHORT).show();
        textView.setText(search);
        String tagsStr = getTagId();

        setRecyclerView();
        load_RecommendBooks(tagsStr,2);

        btnMy.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    private String getTagNames() {
        String s = "";
        for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
            s += tagNames.getSelectedTags().get(i).getTag_name();
        return s;
    }

    private String getTagId() {
        String s = "";
        s += tagNames.getSelectedTags().get(0).getTag_id();
        for (int i = 1; i < tagNames.getSelectedTags().size(); i++)
            s += ";" + tagNames.getSelectedTags().get(i).getTag_id();
        return s;
    }

    private void setRecyclerView(){

//        recyclerView = (RecyclerView)findViewById(R.id.recycler_read);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

//        adapter = new RecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);
    }


    private void load_RecommendBooks(String name, int mode){

        Call<List<BookInfo>> bookInfoCall=null;
        //id으로 책 정보 가져오기
        if(mode ==1){//tag
            bookInfoCall = getNetworkManager().getBookApi().getListWithTag(name);
        }
        else if(mode ==2){//search
            textView.setText(" ");
            bookInfoCall = getNetworkManager().getBookApi().getListWithSearch(name);
        }
        bookInfoCall.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();
                if (response.isSuccessful()) {
                    adapter = new RecyclerViewAdapter(getApplicationContext(), books, tagNames);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(SearchActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    Call<List<BookInfo>> bookInfoWithIsbn;
    BookInfo bookInfo;



    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_my:
                //login 되어있으면 my, 안되어있으면 login
                // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
                Boolean isLoggedIn =  getLocalStore().getBooleanValue(LocalStore.my, false);
                if(isLoggedIn == true)
                    intent = new Intent(this,MyPageActivity.class);
                else
                    intent = new Intent(this,LoginActivity.class);

                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
//                Intent refresh = new Intent(this, RecommendDetailActivity.class);
//                refresh.putExtra("tagNames", tagNames);
//                startActivity(refresh);
//                this.finish();
//                String search = editText.getText().toString();
//                load_RecommendBooks(search, 2);
//                break;
//                String search = editText.getText().toString();
//                load_RecommendBooks(search,2);
//                break;
        }
    }

}
