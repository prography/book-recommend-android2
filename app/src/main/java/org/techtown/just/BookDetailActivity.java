package org.techtown.just;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class BookDetailActivity extends BaseActivity implements View.OnClickListener{

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
    @BindView(R.id.like_btn)
    ImageView BOOK_LIKE;
    @BindView(R.id.read_btn)
    ImageView BOOK_READ;

    int like , read;

    TagNames tagNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ButterKnife.bind(this);

        get_BOOKINFO();

        BOOK_LIKE.setOnClickListener(this);
        BOOK_READ.setOnClickListener(this);


    }//oncreate

    private void get_BOOKINFO(){
        Intent intent = getIntent();
        //BookInfo bookInfo = intent.getParcelableExtra("bookInfoList");
        like = intent.getIntExtra("booklike",0);
        read = intent.getIntExtra("bookread",0);

        String isbn = intent.getStringExtra("isbn");
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        Call<List<BookInfo>> bookInfoCall = NetworkManager.getBookApi().getBookInfoWithIsbn(isbn);
        bookInfoCall.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();
                //thumbnail 설정
                setThumbnail(BOOK_IMG, books.get(0).getThumbnail());
                if(like==1){
                    BOOK_LIKE.setSelected(true);
                }else{
                    BOOK_LIKE.setSelected(false);
                }

                if(read ==1){
                    BOOK_READ.setSelected(true);
                } else {
                    BOOK_READ.setSelected(false);
                }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.like_btn:
                Toast.makeText(BookDetailActivity.this, "좋아요 버튼을 눌렀습니다", Toast.LENGTH_SHORT).show();
                if(BOOK_LIKE.isSelected()==true){
                    BOOK_LIKE.setImageResource(R.drawable.ic_like_full);}
                else
                    BOOK_LIKE.setImageResource(R.drawable.ic_like_empty);
                break;
            case R.id.read_btn:
                Toast.makeText(BookDetailActivity.this, "읽었어요 버튼을 눌렀습니다", Toast.LENGTH_SHORT).show();
                if(BOOK_READ.isSelected()==true){
                    BOOK_READ.setImageResource(R.drawable.ic_checked);}
                else
                    BOOK_READ.setImageResource(R.drawable.ic_check);
                break;
        }
    }
}
