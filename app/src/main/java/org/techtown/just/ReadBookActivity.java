package org.techtown.just;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.techtown.just.model.BookInfo;

import java.util.ArrayList;

public class ReadBookActivity extends AppCompatActivity {

    //@BindView(R.id.recycler_read)
    RecyclerView mRecyclerView2;
    RecyclerView.LayoutManager mLayoutManager2;
    MyAdapter myAdapter2;

    ArrayList<BookInfo> BookInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

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
        mRecyclerView2.setAdapter(myAdapter2);
    }
}
