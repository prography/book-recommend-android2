package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookFlag;
import org.techtown.just.model.BookInfo;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.just.base.BaseApplication.getLocalStore;

public class RecommendDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.searchStr)
    EditText editText;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_my)
    ImageView btnMy;
    @BindView(R.id.rc_recommend)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    private RecyclerViewAdapter adapter;
    TagNames tagNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");


        textView.setText(getTagNames());
        String tagsStr = getTagId();

        setRecyclerView();
        load_RecommendBooks(tagsStr,1);

        btnMy.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    private String getTagNames() {
        String s = "";
        for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
            s += tagNames.getSelectedTags().get(i).getTag_name();
        return s;
    }

    private String getTagId() {
        String s = "";
        s += tagNames.getSelectedTags().get(0).getTag_id();
        for (int i = 1; i < tagNames.getSelectedTags().size(); i++)
            s += ";" + tagNames.getSelectedTags().get(i).getTag_id();
        return s;
    }

    private void setRecyclerView(){

//        recyclerView = (RecyclerView)findViewById(R.id.recycler_read);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

//        adapter = new RecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);
    }

    Call<List<BookInfo>> bookInfoCall=null;

    private void load_RecommendBooks(String name, int mode){

        //id으로 책 정보 가져오기
        if(mode ==1){//listwithtag
            bookInfoCall = getNetworkManager().getBookApi().getListWithTag(name);
        }
        else if(mode ==2){//search
            textView.setText(" ");
            bookInfoCall = getNetworkManager().getBookApi().getListWithSearch(name);
        }

        bookInfoCall.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();
                if (response.isSuccessful()) {
                    adapter = new RecyclerViewAdapter(getApplicationContext(), null, tagNames);
                    //adapter .setOnClickListener(RecommendDetailActivity.this);
                    recyclerView.setAdapter(adapter);

                    adapter.setBookListListener(new RecyclerViewAdapter.BookListListener() {
                        @Override
                        public void saveFlag(final int position, final BookInfo bookInfo, final int like, final int read) {
                            String userId = getLocalStore().getStringValue(LocalStore.UserId);

                            getNetworkManager().getBookApi().saveStatus(bookInfo.isbn, read, like, userId).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.isSuccessful()) {
                                        BookFlag flag = bookInfo.flag;
                                        flag.setBe_interested(like);
                                        flag.setHad_read(read);
                                        bookInfo.setFlag(flag);
                                        adapter.updateBookInfo(position, bookInfo);
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });

                        }
                    });

                    for (BookInfo info : books){
                        loadBookFlag(info);
                    }


                } else {
                    Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    Call<List<BookFlag>> bookflag = null;

    private void loadBookFlag(final BookInfo bookInfo){

        String userId = getLocalStore().getStringValue(LocalStore.UserId);
//        Call<List<BookFlag>> bookInfo = null;
        getNetworkManager().getBookApi().getBookFlag(bookInfo.isbn, userId).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject bookFlags = response.body();
                JsonObject jsonObject = bookFlags.getAsJsonArray().get(0).getAsJsonObject();
                //TODO json parsing
                int like =jsonObject.get("had_read").getAsInt();
                int read =jsonObject.get("be_interested").getAsInt();
                BookFlag flag = new BookFlag(read, like);
                bookInfo.setFlag(flag);
                adapter.addBookInfo(bookInfo);
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



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

                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
                String search = editText.getText().toString();
                load_RecommendBooks(search,2);
                break;
        }
    }

    private void checkLogin(String isbn, String user_id){


    }

    private String getTagId() {
        String s = "";
        s += tagNames.getSelectedTags().get(0).getTag_id();
        for (int i = 1; i < tagNames.getSelectedTags().size(); i++)
            s += ";" + tagNames.getSelectedTags().get(i).getTag_id();
        return s;
    }
}
