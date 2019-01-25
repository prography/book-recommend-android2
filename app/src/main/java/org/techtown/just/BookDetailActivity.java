package org.techtown.just;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfo;
import org.techtown.just.model.Tag;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends BaseActivity {

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

    TagNames tagNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ButterKnife.bind(this);

        get_BOOKINFO();
    }//oncreate

    private void get_BOOKINFO(){
        Intent intent = getIntent();
        //BookInfo bookInfo = intent.getParcelableExtra("bookInfoList");

        String isbn = intent.getStringExtra("isbn");
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        Call<List<BookInfo>> bookInfoCall = NetworkManager.getBookApi().getBookInfoWithIsbn(isbn);
        bookInfoCall.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();
                //thumbnail 설정
                setThumbnail(BOOK_IMG, books.get(0).getThumbnail());
                BOOK_TITLE.setText(books.get(0).getBook_name());
                BOOK_AUTHOR.setText(books.get(0).getAuthor());
                BOOK_CONTENT.setText(books.get(0).getContents());
//                BOOK_TAGS.setText(splitTags(books.get(0).getTags()));
                BOOK_TAGS.setText(books.get(0).getTags());
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

//        String book_thumbnail = intent.getStringExtra("book_thumbnail");
//        String book_name = intent.getStringExtra("book_name");
//        String book_author = intent.getStringExtra("book_author");
//        String book_content = intent.getStringExtra("book_content");
//        String book_country = intent.getStringExtra("book_country");
//        String book_tags = intent.getStringExtra("book_tags");

        //txt.setText(isbn+" 책 정보");

    }

    public String splitTags(String fullTags) {
        String tags[] = fullTags.split(";");
        String s = "";
        for (int i = 0; i < tags.length; i++) {
            int key = 0;
            key = Integer.parseInt(tags[i]);
            s += tagNames.getTags().get(key).getTag_name() + " ";
        }
        return s;
    }

    URL url;
    Bitmap bitmap;

    public void setThumbnail(ImageView imageView, final String thumbnail) {
        //ImageView url 설정

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    url = new URL(thumbnail);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try {
            mThread.join();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
