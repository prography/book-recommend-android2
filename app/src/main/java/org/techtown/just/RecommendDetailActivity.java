package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.just.model.BookInfo;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_my)
    ImageView btnMy;

    String sfName = "myFile";
    @BindView(R.id.searchStr)
    EditText searchStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int randomNum = intent.getIntExtra("randomNum", -1);


//        String s = "";
//        if (randomNum == -1) {
//            for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
//                s += tagNames.getSelectedTags().get(i).getTag_name() + " ";
//        } else { //아무거나 선택 시
//            //s = tagNames.getTags()[randomNum];
//        }

        TagNames tagNames = (TagNames) intent.getSerializableExtra("tagNames");
        String tagsStr = "";
        for (int i = 0; i < tagNames.getSelectedTags().size(); i++)
            tagsStr += tagNames.getSelectedTags().get(i).getTag_id() + ";";
        textView.setText(tagsStr);

        //tagsStr 보내고 책 리스트 받아오기 1;3;5;
        Call<List<BookInfo>> list = NetworkManager.getBookApi().getListWithTag(tagsStr);
        list.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();


//                String s = "";
//                for (int i = 0 ; i < tags.size(); i++)
//                    s += tags.get(i).toString() + "\n";
//
//                text.setText(s);
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(RecommendDetailActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnMy.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_my:
                //login 되어있으면 my, 안되어있으면 login
                // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
                SharedPreferences sf = getSharedPreferences(sfName, 0);
                SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요

//                int i = sf.getInt("my", 0);
//                if (i == 0)
//                    intent = new Intent(this, LoginActivity.class);
//                else
//                    intent = new Intent(this, MyPageActivity.class);
//
//                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_search:
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra("searchStr", searchStr.toString());
                startActivity(intent);
                break;
        }
    }
}
