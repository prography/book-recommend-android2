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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteBookActivity extends AppCompatActivity  implements MyAdapter.MyRecyclerViewClickListener{

    @BindView(R.id.btn_back)
    ImageView btnBack;
    //@BindView(R.id.recycler_int)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyAdapter myAdapter;

    ArrayList<BookInfo> BookInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book);

        ButterKnife.bind(this);

        BookInfoArrayList.add(new BookInfo("A"));
        BookInfoArrayList.add(new BookInfo("B"));
        BookInfoArrayList.add(new BookInfo("C"));
        BookInfoArrayList.add(new BookInfo("D"));
        BookInfoArrayList.add(new BookInfo("E"));
        BookInfoArrayList.add(new BookInfo("F"));
        BookInfoArrayList.add(new BookInfo("G"));

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_int);
        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new GridLayoutManager(this,3);
        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myAdapter = new MyAdapter(BookInfoArrayList);
        myAdapter.setOnClickListener(this); //버튼 연결
        mRecyclerView.setAdapter(myAdapter);

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
