package org.techtown.just;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.techtown.just.model.Tag;
import org.techtown.just.model.TagNames;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.just.base.BaseApplication.getNetworkManager;

public class SetMyTagsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    TagNames tagNames;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_tags);
        ButterKnife.bind(this);

        button.setOnClickListener(this);

        Intent intent = getIntent();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        //tagNames 가져오기
        Call<List<Tag>> list = getNetworkManager().getBookApi().getTags();
        list.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                List<Tag> tags = response.body();
                tagNames.setAllTags(tags);
                for (int i = 0; i < tagNames.getAllTags().size(); i++)
                    flowLayout.addTag(tagNames.getAllTags().get(i));

            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Toast.makeText(SetMyTagsActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        flowLayout.relayoutToAlign();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.button:
                intent = new Intent(SetMyTagsActivity.this, MyPageActivity.class);

                //나의 태그 내용 저장하기
                tagNames.updateSelectedTags(flowLayout.getCheckedTagValues());

                intent.putExtra("tagNames", tagNames);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
