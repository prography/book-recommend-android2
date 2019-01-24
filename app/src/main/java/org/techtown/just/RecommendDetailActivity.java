package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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

        TagNames tagNames = (TagNames) intent.getSerializableExtra("tagNames");
        String tagsStr = "";
        for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
            tagsStr += tagNames.getSelectedTags().get(i).getTag_id() + ";";
        textView.setText(tagsStr);


        setRecyclerView();
        load_RecommendBooks(tagsStr);

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


    private void load_RecommendBooks(String tagsStr){

        //id으로 책 정보 가져오기
        Call<List<BookInfo>> bookInfoCall = NetworkManager.getBookApi().getListWithTag(tagsStr);
//        Call<List<BookInfo>> bookInfoCall = NetworkManager.getBookApi().getBookInfoWithIsbn("9788972758426");
        bookInfoCall.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();
                //thumbnail 설정
                setThumbnail(books);
//                Toast.makeText(RecommendDetailActivity.this, books.get(0).getBook_name(), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    adapter = new RecyclerViewAdapter(getApplicationContext(), books);
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

    Call<List<BookInfo>> bookInfoWithIsbn;
    BookInfo bookInfo;

    public void setThumbnail(List<BookInfo> books) {
        for (int i = 0; i < books.size(); i++) {
            //isbn으로 책정보 가져와서
            bookInfo = books.get(i);
            bookInfoWithIsbn = NetworkManager.getBookApi().getBookInfoWithIsbn(bookInfo.getIsbn());
            bookInfoWithIsbn.enqueue(new Callback<List<BookInfo>>() {
                @Override
                public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                    List<BookInfo> books2 = response.body();
                    //bookInfo에 때려넣기~
                    bookInfo.setThumbnail(books2.get(0).getThumbnail());
                }
                @Override
                public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                    Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
//                intent.putExtra("searchStr", searchStr.toString());
                startActivity(intent);
                break;
        }
    }


}
