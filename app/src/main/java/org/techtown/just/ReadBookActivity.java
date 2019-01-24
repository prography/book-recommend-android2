package org.techtown.just;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.techtown.just.model.BookInfo;
import org.techtown.just.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadBookActivity extends AppCompatActivity implements MyAdapter.MyRecyclerViewClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    //@BindView(R.id.recycler_read)
    RecyclerView mRecyclerView2;
    RecyclerView.LayoutManager mLayoutManager2;
    MyAdapter myAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        ButterKnife.bind(this);

        mRecyclerView2 = (RecyclerView)findViewById(R.id.recycler_read);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager2 = new GridLayoutManager(this,3);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        loadReadBooks();

        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }};
        btnBack.setOnClickListener(mClickListener);

    }

    private void loadReadBooks(){

        //id으로 책 정보 가져오기
        Call<List<BookInfo>> bookInfo = NetworkManager.getBookApi().getListUserRead("1");

        bookInfo.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books_1 = response.body();
                if (response.isSuccessful()) {
                    myAdapter2 = new MyAdapter(getApplicationContext(),books_1);
                    myAdapter2 .setOnClickListener(ReadBookActivity.this);
                    mRecyclerView2.setAdapter(myAdapter2);

                } else {
                    Toast.makeText(ReadBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(ReadBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(),position+" 번 아이템이 클릭됨",Toast.LENGTH_SHORT).show();

        //Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);
        //intent.putExtra("book_name",position);
        //intent.addFlags()

        //intent.putExtra("book_thumbnail",bookinfo.get(position).getThumbnail());
//        intent.putExtra("book_name",BookInfoList.get(position).getBook_name());
//        intent.putExtra("book_author",BookInfoList.get(position).getAuthor());
//        intent.putExtra("book_content",BookInfoList.get(position).getContents());
//        intent.putExtra("book_country",BookInfoList.get(position).getCountry());
//        intent.putExtra("book_tags",BookInfoList.get(position).getTags());


        //startActivity(intent);

    }
}
