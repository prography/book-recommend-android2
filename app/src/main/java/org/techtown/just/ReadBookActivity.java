package org.techtown.just;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.TagNames;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadBookActivity extends BaseActivity implements RecyclerViewAdapter_BookAlign.
        MyRecyclerViewClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.searchStr)
    EditText editText;
    @BindView(R.id.btn_search)
    ImageView search;
    //@BindView(R.id.recycler_read)
    RecyclerView mRecyclerView2;
    RecyclerView.LayoutManager mLayoutManager2;
    RecyclerViewAdapter_BookAlign recyclerViewAdapterBookAlign2;

    TagNames tagNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        mRecyclerView2 = (RecyclerView)findViewById(R.id.recycler_read);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager2 = new GridLayoutManager(this,3);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        loadReadBooks(null);

        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }};
        btnBack.setOnClickListener(mClickListener);
        View.OnClickListener mClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchname = editText.getText().toString();
                loadReadBooks(searchname);
            }};
        search.setOnClickListener(mClickListener2);

    }

    private void loadReadBooks(String string){
//        String userId = getLocalStore().getStringValue(LocalStore.UserId);
//        String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
//        String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
//        String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);
//
//        //id으로 책 정보 가져오기
//        Call<List<BookInfo_Added>> bookInfoAdded=null;
//        if(string == null) {
//            bookInfoAdded = getNetworkManager().getBookApi().getListUserRead(userId, accessToken, idToken, refreshToken);
//        }else if(string != null){
//            bookInfoAdded = getNetworkManager().getBookApi().getListWithSearch(string);
//        }bookInfoAdded.enqueue(new Callback<List<BookInfo_Added>>() {
//            @Override
//            public void onResponse(Call<List<BookInfo_Added>> call, Response<List<BookInfo_Added>> response) {
//                List<BookInfo_Added> books_1 = response.body();
//                if (response.isSuccessful()) {
//                    recyclerViewAdapterBookAlign2 = new RecyclerViewAdapter_BookAlign(getApplicationContext(),books_1, tagNames);
//                    recyclerViewAdapterBookAlign2 .setOnClickListener(ReadBookActivity.this);
//                    mRecyclerView2.setAdapter(recyclerViewAdapterBookAlign2);
//
//                } else {
//                    Toast.makeText(ReadBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//            @Override
//            public void onFailure(Call<List<BookInfo_Added>> call, Throwable t) {
//                Toast.makeText(ReadBookActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    @Override
    public void onItemClicked(int position) {
        showShortToastMsg(position+" 번 아이템이 클릭됨");
    }
}
