package org.techtown.just;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfo;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteBookActivity extends BaseActivity implements MyAdapter.MyRecyclerViewClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    //@BindView(R.id.recycler_int)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyAdapter myAdapter;

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
//        myAdapter = new MyAdapter(BookInfoArrayList);
//        myAdapter.setOnClickListener(this); //버튼 연결
//        mRecyclerView.setAdapter(myAdapter);

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

        //id으로 책 정보 가져오기
        Call<List<BookInfo>> bookInfo = getNetworkManager().getBookApi().getListUserInterested("1");

        bookInfo.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books_1 = response.body();
                if (response.isSuccessful()) {
                    myAdapter = new MyAdapter(getApplicationContext(), books_1, tagNames);
                    myAdapter.setOnClickListener(FavoriteBookActivity.this);
                    mRecyclerView.setAdapter(myAdapter);

                } else {
                    Toast.makeText(FavoriteBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(FavoriteBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
