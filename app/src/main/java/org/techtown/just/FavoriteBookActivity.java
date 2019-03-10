package org.techtown.just;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfo_Added;
import org.techtown.just.model.LocalStore;
import org.techtown.just.model.TagNames;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteBookActivity extends BaseActivity implements RecyclerViewAdapter_BookAlign.MyRecyclerViewClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    //@BindView(R.id.recycler_int)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerViewAdapter_BookAlign recyclerViewAdapterBookAlign;

    TagNames tagNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book);

        Intent intent = getIntent();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");


        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_int);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadIntBooks();
//        recyclerViewAdapterBookAlign = new RecyclerViewAdapter_BookAlign(BookInfoArrayList);
//        recyclerViewAdapterBookAlign.setOnClickListener(this); //버튼 연결
//        mRecyclerView.setAdapter(recyclerViewAdapterBookAlign);

        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        btnBack.setOnClickListener(mClickListener);

    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(), position + " 번 아이템이 클릭됨", Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);
//        intent.putExtra("bookID",position);
//        startActivity(intent);
    }

    private void loadIntBooks() {
        String userId = getLocalStore().getStringValue(LocalStore.UserId);
        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);

        //id으로 책 정보 가져오기
        Call<List<BookInfo_Added>> bookInfo = getNetworkManager().getBookApi().getListUserInterested(userId, accessToken, idToken, refreshToken);

        bookInfo.enqueue(new Callback<List<BookInfo_Added>>() {
            @Override
            public void onResponse(Call<List<BookInfo_Added>> call, Response<List<BookInfo_Added>> response) {
                List<BookInfo_Added> books_1 = response.body();
                if (response.isSuccessful()) {
                    recyclerViewAdapterBookAlign = new RecyclerViewAdapter_BookAlign(getApplicationContext(), books_1, tagNames);
                    recyclerViewAdapterBookAlign.setOnClickListener(FavoriteBookActivity.this);
                    mRecyclerView.setAdapter(recyclerViewAdapterBookAlign);

                } else {
                    Toast.makeText(FavoriteBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo_Added>> call, Throwable t) {
                Toast.makeText(FavoriteBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
