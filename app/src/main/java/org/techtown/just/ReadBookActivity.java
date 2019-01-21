package org.techtown.just;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadBookActivity extends AppCompatActivity implements MyAdapter.MyRecyclerViewClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    //@BindView(R.id.recycler_read)
    RecyclerView mRecyclerView2;
    RecyclerView.LayoutManager mLayoutManager2;
    MyAdapter myAdapter2;

    ArrayList<BookInfo> BookInfoArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        ButterKnife.bind(this);

        BookInfoArrayList.add(new BookInfo("가"));
        BookInfoArrayList.add(new BookInfo("나"));
        BookInfoArrayList.add(new BookInfo("다"));
        BookInfoArrayList.add(new BookInfo("라"));
        BookInfoArrayList.add(new BookInfo("마"));
        BookInfoArrayList.add(new BookInfo("바"));
        BookInfoArrayList.add(new BookInfo("사"));

        mRecyclerView2 = (RecyclerView)findViewById(R.id.recycler_read);
        mRecyclerView2.setHasFixedSize(true);
//        mLayoutManager = new GridLayoutManager(this,3);
        mLayoutManager2 = new GridLayoutManager(this,3);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        myAdapter2 = new MyAdapter(BookInfoArrayList);
        myAdapter2.setOnClickListener(this); //버튼 연결
        mRecyclerView2.setAdapter(myAdapter2);

        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }};
        btnBack.setOnClickListener(mClickListener);

    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(),position+" 번 아이템이 클릭됨",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);
        intent.putExtra("bookID",position);
        startActivity(intent);

    }
}
