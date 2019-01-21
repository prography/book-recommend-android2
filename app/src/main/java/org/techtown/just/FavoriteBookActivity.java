package org.techtown.just;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.techtown.just.model.BookInfo;

import java.util.ArrayList;

public class FavoriteBookActivity extends AppCompatActivity {

    //@BindView(R.id.recycler_int)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyAdapter myAdapter;

    ArrayList<BookInfo> BookInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book);

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
        mRecyclerView.setAdapter(myAdapter);

    }
}
