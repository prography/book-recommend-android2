package org.techtown.just;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfo_Added;
import org.techtown.just.model.LocalStore;
import org.techtown.just.model.Status;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.booknumber)
    TextView txt; //책정보 txt
    @BindView(R.id.book_img)
    ImageView BOOK_IMG;
    @BindView(R.id.book_title)
    TextView BOOK_TITLE;
    @BindView(R.id.book_author)
    TextView BOOK_AUTHOR;
    @BindView(R.id.book_country)
    TextView BOOK_COUNTRY;
    @BindView(R.id.book_content)
    TextView BOOK_CONTENT;
    @BindView(R.id.like_btn)
    ImageView BOOK_LIKE;
    @BindView(R.id.read_btn)
    ImageView BOOK_READ;

    List<BookInfo_Added> books;

    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    int like, read;

    String isbn;

    TagNames tagNames;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_my)
    ImageView btnMy;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.searchStr)
    EditText searchStr;

    String bookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);

        //검색창 보이지 않도록
        btnSearch.setVisibility(View.INVISIBLE);
        searchStr.setVisibility(View.INVISIBLE);

        btnMy.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        get_BOOKINFO();
        setBookFlags();

        BOOK_LIKE.setOnClickListener(this);
        BOOK_READ.setOnClickListener(this);


    }//end of oncreate

    private void postBookFlags(String isbn) {
        String userId = getLocalStore().getStringValue(LocalStore.UserId);
        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);

        Call<ResponseBody> call = getNetworkManager().getBookApi().postBookFlags(isbn, userId, 0, 0, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setBookFlags() {
        //bookFlag 가져오기
        String userId = getLocalStore().getStringValue(LocalStore.UserId);
        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);

        Call<List<Status>> call = getNetworkManager().getBookApi().getBookFlags(isbn, userId, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                try {
                    List<Status> status = response.body();

                    int like = status.get(0).getBe_interested();
                    if (like == 1) {
                        BOOK_LIKE.setSelected(true);
                        BOOK_LIKE.setImageResource(R.drawable.ic_like_full);
                    }
                    else {
                        BOOK_LIKE.setSelected(false);
                        BOOK_LIKE.setImageResource(R.drawable.ic_like_empty);
                    }

                    int read = status.get(0).getHad_read();
                    if (read == 1) {
                        BOOK_READ.setSelected(true);
                        BOOK_READ.setImageResource(R.drawable.ic_checked);
                    }
                    else {
                        BOOK_READ.setSelected(false);
                        BOOK_READ.setImageResource(R.drawable.ic_check);
                    }
                } catch (IndexOutOfBoundsException e) {
                    //post안된경우 post로 0 0 설정해주면 됨~
                    postBookFlags(isbn);
                    setBookFlags();
                }


            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void get_BOOKINFO() {
        Intent intent = getIntent();
        //BookInfo_Added bookInfoAdded = intent.getParcelableExtra("bookInfoList");
//        like = intent.getIntExtra("booklike",0);
//        read = intent.getIntExtra("bookread",0);

        isbn = intent.getStringExtra("isbn");
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");


        Call<List<BookInfo_Added>> bookInfoCall = NetworkManager.getBookApi().getBookInfoWithIsbn(isbn);
        bookInfoCall.enqueue(new Callback<List<BookInfo_Added>>() {
            @Override
            public void onResponse(Call<List<BookInfo_Added>> call, Response<List<BookInfo_Added>> response) {
                books = response.body();
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

                bookTitle = books.get(0).getBook_name();
                BOOK_TITLE.setText(books.get(0).getBook_name());
                BOOK_AUTHOR.setText(books.get(0).getAuthor());
                BOOK_COUNTRY.setText(books.get(0).getCountry());
                BOOK_CONTENT.setText(books.get(0).getContents());
//                String tagIdtoName = getTagNames(books.get(0).getTags(), tagNames);
//                BOOK_TAGS.setText(tagIdtoName);
                //flowlayout에 tag 적용하기
                setFlowLayoutWithTag(books.get(0).getTags());

            }

            @Override
            public void onFailure(Call<List<BookInfo_Added>> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        //txt.setText(isbn+" 책 정보");

    }

    public void setFlowLayoutWithTag(String s) {
        //tagNames이용
        List<Tag> allTags = tagNames.getAllTags();
        String booksTags[] = s.split(";");

        for (int i = 0; i < booksTags.length; i++) {
            try {
                int toInt = Integer.parseInt(booksTags[i]);
                flowLayout.addTag(allTags.get(toInt - 1));
            } catch (NumberFormatException e) {

            }
        }

        flowLayout.relayoutToAlign();
        flowLayout.setChecked(true);
        flowLayout.setCheckable(false);
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
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_my:
                //login 되어있으면 my, 안되어있으면 login
                // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
                String userId =  getLocalStore().getStringValue(LocalStore.UserId);
                if(userId != null)
                    intent = new Intent(this,MyPageActivity.class);
                else
                    intent = new Intent(this,LoginActivity.class);

                intent.putExtra("tagNames", tagNames);
                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
//                String search = searchStr.getText().toString();
//                intent = new Intent(BookDetailActivity.this, RecommendDetailActivity.class);
//                intent.putExtra("mode", 2);
//                intent.putExtra("search", search);
//                startActivity(intent);
//                break;
     
            case R.id.like_btn:
                if(BOOK_LIKE.isSelected()) {
                    //좋아요 취소
                    Toast.makeText(this, "\"" + bookTitle + "\"" + " 좋아요 취소", Toast.LENGTH_SHORT).show();
                    BOOK_LIKE.setSelected(false);
                    BOOK_LIKE.setImageResource(R.drawable.ic_like_empty);
                    like = 0;

                }else {
                    //좋아요
                    Toast.makeText(this, "\"" + bookTitle + "\"" + " 좋다!", Toast.LENGTH_SHORT).show();
                    BOOK_LIKE.setSelected(true);
                    BOOK_LIKE.setImageResource(R.drawable.ic_like_full);
                    like = 1;
                }

                //read 가져오고 서버에 저장
                if (BOOK_READ.isSelected() == true)
                    read = 1;
                else
                    read = 0;

                //서버에 저장
                saveBookStatus();

                break;
            case R.id.read_btn:
                if(BOOK_READ.isSelected()) {
                    //좋아요 취소
                    Toast.makeText(this, "\"" + bookTitle + "\"" + "  안읽었음", Toast.LENGTH_SHORT).show();
                    BOOK_READ.setSelected(false);
                    BOOK_READ.setImageResource(R.drawable.ic_check);
                    read = 0;

                }else {
                    //좋아요
                    Toast.makeText(this, "\"" + bookTitle + "\"" + " 읽었다!", Toast.LENGTH_SHORT).show();
                    BOOK_READ.setSelected(true);
                    BOOK_READ.setImageResource(R.drawable.ic_checked);
                    read = 1;
                }

                //read 가져오고 서버에 저장
                if (BOOK_LIKE.isSelected() == true)
                    like = 1;
                else
                    like = 0;

                //서버에 저장
                saveBookStatus();

                break;
        }
    }

    private void saveBookStatus() {
        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);
        String userId = getLocalStore().getStringValue(LocalStore.UserId);

        Call<ResponseBody> call = getNetworkManager().getBookApi().putBookFlags(isbn, userId, like, read, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                } catch (IndexOutOfBoundsException e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
