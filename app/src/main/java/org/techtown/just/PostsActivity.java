package org.techtown.just;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {

    MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        //recyclerview
        RecyclerView view = (RecyclerView) findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter();
        view.setLayoutManager(layoutManager);
        view.setAdapter(myRecyclerViewAdapter);

        //retrofit
        Call<List<Comment>> comment = NetworkManager.getApiService().getComments(1);
        comment.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                List<Comment> comments = response.body();
                myRecyclerViewAdapter.addComments(comments);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(PostsActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
