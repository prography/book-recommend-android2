package org.techtown.just;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookDetailActivity extends AppCompatActivity {
//zzz
    @BindView(R.id.booknumber)
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        int bookID = intent.getIntExtra("bookID",-1);

        txt.setText(bookID+"번 책 정보");

    }
}
