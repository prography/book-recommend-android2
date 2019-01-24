package org.techtown.just;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.model.BookInfo;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {
//zzz
    @BindView(R.id.booknumber)
    TextView txt; //책정보 txt
    @BindView(R.id.book_img)
    ImageView BOOK_IMG;
    @BindView(R.id.book_title)
    TextView BOOK_TITLE;
    @BindView(R.id.book_author)
    TextView BOOK_AUTHOR;
    @BindView(R.id.book_tags)
    TextView BOOK_TAGS;
    @BindView(R.id.book_content)
    TextView BOOK_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ButterKnife.bind(this);

        get_BOOKINFO();
    }//oncreate

    private void get_BOOKINFO(){
        Intent intent = getIntent();

        String isbn = intent.getStringExtra("isbn");
        String book_thumbnail = intent.getStringExtra("book_thumbnail");
        String book_name = intent.getStringExtra("book_name");
        String book_author = intent.getStringExtra("book_author");
        String book_content = intent.getStringExtra("book_content");
        String book_country = intent.getStringExtra("book_country");
        String book_tags = intent.getStringExtra("book_tags");

        //txt.setText(isbn+" 책 정보");
        BOOK_TITLE.setText(book_name);
        BOOK_AUTHOR.setText(book_author);
        BOOK_CONTENT.setText(book_content);
        BOOK_TAGS.setText(book_tags);

    }
}
