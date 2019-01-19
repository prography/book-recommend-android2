package org.techtown.just;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity implements OnClickListener {

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
        myRecyclerViewAdapter.setClickListener(this);

        //retrofit
        Call<List<Post>> post = NetworkManager.getApiService().getPostList();
        post.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> posts = response.body();
                myRecyclerViewAdapter.addComments(posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(PostsActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPostClick(Post post) {
        Intent intent = new Intent(PostsActivity.this, PostDetailActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);
    }
}
