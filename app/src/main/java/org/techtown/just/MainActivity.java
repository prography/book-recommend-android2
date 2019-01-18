package org.techtown.just;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    String sfName = "myFile";
    @BindView(R.id.btn_my)
    Button btnMy;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.checkBox_anything)
    CheckBox checkBox_anything;
    @BindView(R.id.checkBox1)
    CheckBox checkBox1;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    @BindView(R.id.checkBox3)
    CheckBox checkBox3;

    @BindView(R.id.button)
    Button button;

    CheckBox[] cb;


    TagNames tagNames;


    Retrofit retrofit;
    ApiService apiService;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        doJSONParser();
        //recyclerview
        RecyclerView view = (RecyclerView) findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter();
        view.setLayoutManager(layoutManager);
        view.setAdapter(myRecyclerViewAdapter);

        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).build();
        apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> comment = apiService.getComment(1);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.v("Test", result); //받아온 데이터
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        data = new Data();
                        for (int i = 0 ; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            data.setPostId(jsonObject.getInt("postId"));
                            data.setId(jsonObject.getInt("id"));
                            data.setName(jsonObject.getString("name"));
                            data.setMail(jsonObject.getString("mail"));
                            data.setBody(jsonObject.getString("body"));
                            //dataArrayList.add(data);
                            text.setText(data.toString());
                            Log.v("Test", jsonObject.toString());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        //checkbox의 text <- tagNames의 text 대입
        tagNames = new TagNames();
        cb = new CheckBox[] {checkBox1, checkBox2, checkBox3};
        for (int i = 0; i < tagNames.getTags().length; i++)
            cb[i].setText(tagNames.getTags()[i]);

        //btnMy
        btnMy.setOnClickListener(this);
        button.setOnClickListener(this);

        //아무거나를 선택하면 나머지는 false로
        checkBox_anything.setOnCheckedChangeListener(this);

    } // end of onCreate

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Random random = new Random();
        switch (view.getId()) {
            case R.id.btn_my:
                // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
                SharedPreferences sf = getSharedPreferences(sfName, 0);
                SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요

                int i = sf.getInt("my", 0);
                if (i == 0)
                    intent = new Intent(this, LoginActivity.class);
                else
                    intent = new Intent(this, MyPageActivity.class);

                startActivity(intent);

                editor.putInt("my", 1); // 입력
                editor.commit(); // 파일에 최종 반영함
                break;

            case R.id.button:
                //true인 애들만 추가,,, check1 check3만일 경우 0, 2
                String s = "";
                intent = new Intent(this, RecommendDetailActivity.class);

                if (checkBox_anything.isChecked()) { //아무거나 선택 시
                    int randomNum = random.nextInt(cb.length);
                    intent.putExtra("randomNum", randomNum);
                }
                else {
                    for (int j = 0; j < tagNames.getTags().length; j++)
                        if (cb[j].isChecked() == true)
                            tagNames.setTagIndex(j);
                }

                intent.putExtra("tagNames", tagNames);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (checkBox_anything.isChecked())
            for (int i = 0; i < cb.length; i++) {
                cb[i].setChecked(false);
                cb[i].setClickable(false);
            }
        else
            for (int i = 0; i < cb.length; i++)
                cb[i].setClickable(true);
    }

    void doJSONParser(){
        StringBuffer sb = new StringBuffer();

        String str =
                "[{'name':'배트맨','age':43,'address':'고담'},"+
                        "{'name':'슈퍼맨','age':36,'address':'뉴욕'},"+
                        "{'name':'앤트맨','age':25,'address':'LA'}]";

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String address = jObject.getString("address");
                String name = jObject.getString("name");
                int age = jObject.getInt("age");

                sb.append(
                        "주소:" + address +
                                "이름:" + name +
                                "나이:" + age + "\n"
                );
            }
            //tv.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // end doJSONParser()
}



