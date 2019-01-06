package org.techtown.just;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendDetailActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int randomNum = intent.getIntExtra("randomNum", -1);

        TagNames tagNames = (TagNames) intent.getSerializableExtra("tagNames");
        String s = "";
        if (randomNum == -1) {
            for (int i = 0; i < tagNames.getTagIndex().length; i++)
                if (tagNames.getTagIndex()[i] == 1)
                    s += tagNames.getTags()[i] + " ";
        }
        else { //아무거나 선택 시
            s = tagNames.getTags()[randomNum];
        }
        textView.setText(s);

    }
}
