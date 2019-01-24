package org.techtown.just;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        int bookID = intent.getIntExtra("bookID",-1);
        String isbn = intent.getStringExtra("isbn");

        txt.setText(isbn+"번 책 정보");

        //상세 책 페이지로 넘기기
//        Call<List<BookInfo>> list = NetworkManager.getBookApi().getBookInfoWithIsbn(isbn);
//        list.enqueue(new Callback<List<BookInfo>>() {
//            @Override
//            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
//                List<BookInfo> books = response.body();
//                txt.setText(books.get(0).getBook_name());
//
//            }
//
//            @Override
//            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
//                Toast.makeText(BookDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
