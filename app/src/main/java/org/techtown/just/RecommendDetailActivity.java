package org.techtown.just;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfoList_Added;
import org.techtown.just.model.BookInfoList_NotAdded;
import org.techtown.just.model.BookInfo_Added;
import org.techtown.just.model.BookInfo_NotAdded;
import org.techtown.just.model.IsExist;
import org.techtown.just.model.TagNames;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendDetailActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    @BindView(R.id.tv_boolean)
    TextView tvBoolean;

    private RecyclerViewAdapter adapter;
    TagNames tagNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);
        ButterKnife.bind(this);

        //데이터 로딩화면
        startProgress();

        Intent intent = getIntent();

        setRecyclerView();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        //BookDetail에서 왔다면
        int mode = intent.getIntExtra("mode", 0);
        if (mode != 0) { //BookDetail에서 왔다면
            String search = intent.getStringExtra("search");
            loadSearchResult(search);
        }
        else {

            //flowlayout
            for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
                flowLayout.addTag(tagNames.getSelectedTags().get(i));
            flowLayout.relayoutToAlign();
            flowLayout.setChecked(true);
            flowLayout.setCheckable(false);

//        textView.setText(getTagNames());
            String tags = getTagId();
            loadRecommendBooks(tags);
        }


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
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    //isExist가 true인지 false인지 판단
    int index = -1;
    //0 : false(검색한 책이 추가되지 않음 or 없음 - 따로 판단 필요 by isEmpty())
    //1 : true(검색한 책이 추가됨)

    private int isExistBook(String search) {
        Call<IsExist> call = getNetworkManager().getBookApi().getIsExist(search);
        call.enqueue(new Callback<IsExist>() {
            @Override
            public void onResponse(Call<IsExist> call, Response<IsExist> response) {
                IsExist isExist = response.body();
                Boolean bl = isExist.getIsExist();
                if (bl) index = 1; //이미 추가된 책
                else    index = 0;

            }
            @Override
            public void onFailure(Call<IsExist> call, Throwable t) {
                tvBoolean.setText(t.getMessage());
            }
        });
        return index;
    }

    //검색
    private void loadSearchResult(String search) {
        int isExist = isExistBook(search);
        tvBoolean.setText(String.valueOf(isExist));


        if (isExist == 1) { //책 존재o
            Call<BookInfoList_Added> call = getNetworkManager().getBookApi().getAddedListWithSearch(search);
            call.enqueue(new Callback<BookInfoList_Added>() {
                @Override
                public void onResponse(Call<BookInfoList_Added> call, Response<BookInfoList_Added> response) {
                    BookInfoList_Added bookInfoList_added = response.body();
                    List<BookInfo_Added> data = bookInfoList_added.getData();

                    adapter = new RecyclerViewAdapter(getApplicationContext(), data, tagNames);
                    recyclerView.setAdapter(adapter);
                }
                @Override
                public void onFailure(Call<BookInfoList_Added> call, Throwable t) {
                    tvBoolean.setText(t.getMessage());
                }
            });
        }
        else if (isExist == 0) { //책 존재x
            Call<BookInfoList_NotAdded> call = getNetworkManager().getBookApi().getNotAddedListWithSearch(search);
            call.enqueue(new Callback<BookInfoList_NotAdded>() {
                @Override
                public void onResponse(Call<BookInfoList_NotAdded> call, Response<BookInfoList_NotAdded> response) {
                    BookInfoList_NotAdded bookInfoList_notAdded = response.body();
                    List<BookInfo_NotAdded> data = bookInfoList_notAdded.getData();

                    if (data.isEmpty()) //"data" : []인경 우우
                        showLongToastMsg("yes empty");
                    else {
                        showLongToastMsg(data.get(0).getFullAuthor());
                    }


                }
                @Override
                public void onFailure(Call<BookInfoList_NotAdded> call, Throwable t) {
                    tvBoolean.setText(t.getMessage());
                }
            });

        }


        //==============================================================//
//            Call<BookInfoList_Added> call = getNetworkManager().getBookApi().getListWithSearch(search);
//            call.enqueue(new Callback<BookInfoList_Added>() {
//                @Override
//                public void onResponse(Call<BookInfoList_Added> call, Response<BookInfoList_Added> response) {
//                    String result = response.body().toString();
//                    Log.v("Test", result); //받아온 데이터
//                    try {
//                        JSONArray jsonArray = new JSONArray(result);
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        Boolean isExist = jsonObject.getBoolean("isExist");
//                        showLongToastMsg(isExist.toString());
////                            for (int i = 0 ; i < jsonArray.length(); i++) {
////
////                                data.setPostId(jsonObject.getInt("postId"));
////                                data.setId(jsonObject.getInt("id"));
////                                data.setName(jsonObject.getString("name"));
////                                data.setEmail(jsonObject.getString("email"));
////                                data.setBody(jsonObject.getString("body"));
////                                //dataArrayList.add(data);
////                                text.setText(data.toString());
////                                Log.v("Test", jsonObject.toString());
////
////
////                            }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    //                    if (response.isSuccessful()) {
////                        BookInfoList_Added bookInfoWithBools = response.body();
////                        Boolean isExist = bookInfoWithBools.getIsExist();
////
////                        if (isExist) {
////                            List<BookInfo_Added> bookInfoList = bookInfoWithBools.getData();
////                            tvBoolean.setText(isExist.toString());
//////                        Toast.makeText(RecommendDetailActivity.this, isExist + " & " + bookInfoList.get(0).getBook_name() + " !!", Toast.LENGTH_SHORT).show();
//////                        adapter = new RecyclerViewAdapter(getApplicationContext(), bookInfoList, tagNames);
//////                        recyclerView.setAdapter(adapter);
////                        } else {
////                            tvBoolean.setText(isExist.toString());
////                        }
////
////
////                    } else {
////                        Toast.makeText(RecommendDetailActivity.this, "else 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
////                    }
//                }
//                @Override
//                public void onFailure(Call<BookInfoList_Added> call, Throwable t) {
////                    Toast.makeText(RecommendDetailActivity.this, "onFailure 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                    tvBoolean.setText(t.getMessage());
//                }
//            });
    }

    private void loadRecommendBooks(String tag){
        Call<List<BookInfo_Added>> call = getNetworkManager().getBookApi().getListWithTag(tag);
        call.enqueue(new Callback<List<BookInfo_Added>>() {
            @Override
            public void onResponse(Call<List<BookInfo_Added>> call, Response<List<BookInfo_Added>> response) {
                List<BookInfo_Added> books = response.body();
                if (response.isSuccessful()) {
                    adapter = new RecyclerViewAdapter(getApplicationContext(), books, tagNames);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<BookInfo_Added>> call, Throwable t) {
                Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_my:
                //login 되어있으면 my, 안되어있으면 login
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
                String search = editText.getText().toString();
                loadSearchResult(search);
                break;
        }
    }

    private void startProgress() {
        progressON("Loading...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 3500);

    }

}