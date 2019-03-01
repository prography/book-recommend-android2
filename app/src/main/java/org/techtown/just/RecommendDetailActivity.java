package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookFlag;
import org.techtown.just.model.BookInfo;
import org.techtown.just.model.BookInfoWithBool;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.just.base.BaseApplication.getLocalStore;

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
            load_RecommendBooks(search, 2);
        }
        else {

            //flowlayout
            for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
                flowLayout.addTag(tagNames.getSelectedTags().get(i));
            flowLayout.relayoutToAlign();
            flowLayout.setChecked(true);
            flowLayout.setCheckable(false);

//        textView.setText(getTagNames());
            String tagsStr = getTagId();
            load_RecommendBooks(tagsStr,1);
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


    private void load_RecommendBooks(String name, int mode){
        if(mode ==1){//tag
            Call<List<BookInfo>> call = getNetworkManager().getBookApi().getListWithTag(name);
            call.enqueue(new Callback<List<BookInfo>>() {
                @Override
                public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                    List<BookInfo> books = response.body();
                    if (response.isSuccessful()) {
                        adapter = new RecyclerViewAdapter(getApplicationContext(), books, tagNames);
                        recyclerView.setAdapter(adapter);

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
        else if(mode ==2){//search
//            textView.setText(" ");
            Toast.makeText(RecommendDetailActivity.this, "진입", Toast.LENGTH_SHORT).show();

            Call<ResponseBody> call = getNetworkManager().getBookApi().getListWithSearch(name);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(RecommendDetailActivity.this, "response는 받음", Toast.LENGTH_SHORT).show();

                    try {
                        String result = response.body().string();
                        Log.v("Test", result); //받아온 데이터
                        Toast.makeText(RecommendDetailActivity.this, "첫 try 진입", Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(result);
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Toast.makeText(RecommendDetailActivity.this, "두번째 try 진입", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(RecommendDetailActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
//                            data.setPostId(jsonObject.getInt("postId"));
//                            data.setId(jsonObject.getInt("id"));
//                            data.setName(jsonObject.getString("name"));
//                            data.setEmail(jsonObject.getString("email"));
//                            data.setBody(jsonObject.getString("body"));
//                            //dataArrayList.add(data);
//                            text.setText(data.toString());
//                            Log.v("Test", jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RecommendDetailActivity.this, "안쪽 catch :(", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(RecommendDetailActivity.this, "첫 catch :(", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
//                    List<BookInfoWithBool> bookInfoWithBool = response.body();
//
//                    Boolean bl = bookInfoWithBool.get(0).getBl();
//                    List<BookInfo> bookInfoList = bookInfoWithBool.get(1).getBookInfoList();
//
//                    String s = "" + bl;
//                    Toast.makeText(RecommendDetailActivity.this, s, Toast.LENGTH_SHORT).show();
//
//                    if (response.isSuccessful()) {
//                        adapter = new RecyclerViewAdapter(getApplicationContext(), bookInfoList, tagNames);
//                        recyclerView.setAdapter(adapter);
//
//                    } else {
//                        Toast.makeText(RecommendDetailActivity.this, "a오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(RecommendDetailActivity.this, "b오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
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
                String search = editText.getText().toString();
                load_RecommendBooks(search,2);
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