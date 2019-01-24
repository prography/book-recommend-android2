package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.model.BookInfo;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.just.base.BaseApplication.getLocalStore;

public class RecommendDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_my)
    ImageView btnMy;
    @BindView(R.id.rc_recommend)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    private RecyclerViewAdapter adapter;

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
            for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
                s += tagNames.getSelectedTags().get(i).getTag_name() + " ";
        } else { //아무거나 선택 시
            //s = tagNames.getTags()[randomNum];
        }
        textView.setText(s);


        setRecyclerView();
        load_RecommendBooks();

        btnMy.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    private void setRecyclerView(){

//        recyclerView = (RecyclerView)findViewById(R.id.recycler_read);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

//        adapter = new RecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);
    }


    private void load_RecommendBooks(){

        //id으로 책 정보 가져오기
        Call<List<BookInfo>> bookInfo = NetworkManager.getBookApi().getListWithSearch("한");

   //     Call<List<BookInfo>> bookInfo = NetworkManager.getBookApi().getListUserRead("1");
        bookInfo.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();
                if (response.isSuccessful()) {
                    adapter = new RecyclerViewAdapter(getApplicationContext(),books);
                    //adapter .setOnClickListener(RecommendDetailActivity.this);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

//                List<BookInfo> books1 = response.body();
//                tv_response.setText(books1.get(0).getBook_name());

            }
            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }



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
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }


}
