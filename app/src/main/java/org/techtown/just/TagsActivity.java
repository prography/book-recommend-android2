package org.techtown.just;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfo;
import org.techtown.just.model.Post;
import org.techtown.just.model.Tag;
import org.techtown.just.network.NetworkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagsActivity extends BaseActivity {

    @BindView(R.id.text)
    TextView text;

    Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        ButterKnife.bind(this);

        //tag 가져오기
//        Call<List<Tag>> list = NetworkManager.getBookApi().getAllTags();
//        list.enqueue(new Callback<List<Tag>>() {
//            @Override
//            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
//                List<Tag> tags = response.body();
//                String s = "";
//                for (int i = 0 ; i < tags.size(); i++)
//                    s += tags.get(i).toString() + "\n";
//
//                text.setText(s);
//            }
//
//            @Override
//            public void onFailure(Call<List<Tag>> call, Throwable t) {
//                Toast.makeText(TagsActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });

        //tag id로 book 가져오기
//        Call<List<BookInfo>> list = NetworkManager.getBookApi().getListWithTag(";3;4;");
//        list.enqueue(new Callback<List<BookInfo>>() {
//            @Override
//            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
//                List<BookInfo> books = response.body();
//                text.setText(books.get(0).toString());
//            }
//
//            @Override
//            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
//                Toast.makeText(TagsActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });

        //isbn으로 책 정보 가져오기
        Call<List<BookInfo>> bookInfo = getNetworkManager().getBookApi().getBookInfoWithIsbn("9788937460753");
        bookInfo.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books = response.body();
                text.setText(books.get(0).toString());
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(TagsActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
