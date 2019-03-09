package org.techtown.just;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.LocalStore;
import org.techtown.just.model.Tag;
import org.techtown.just.model.TagNames;
import org.techtown.just.model.UserSelectedTags;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.just.base.BaseApplication.getLocalStore;
import static org.techtown.just.base.BaseApplication.getNetworkManager;

public class SetMyTagsActivity extends BaseActivity implements View.OnClickListener {

//    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    TagNames tagNames;
    @BindView(R.id.button)
    Button button;

    @BindView(R.id.btn_back)
    ImageView btnBack;

    String userId;
    String accessToken;
    String idToken;
    String refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_tags);
        ButterKnife.bind(this);

        button.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        flowLayout = findViewById(R.id.flowLayout);

        //정보 가져오기
        userId = getLocalStore().getStringValue(LocalStore.UserId);
        accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
        idToken = getLocalStore().getStringValue(LocalStore.IdToken);
        refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);


        Intent intent = getIntent();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        //tagNames 가져오기
        setAllTagsToFlowLayout();

        //내가 선택한 태그 체크해두기
        setMySelectedTagsToFlowLayout();



    }

    private void setAllTagsToFlowLayout() {
        Call<List<Tag>> list = getNetworkManager().getBookApi().getTags();
        list.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
//                if (response.body() != null && response.isSuccessful()) {
                    List<Tag> tags = response.body();
                    tagNames.setAllTags(tags);
                    for (int i = 0; i < tagNames.getAllTags().size(); i++)
                        flowLayout.addTag(tagNames.getAllTags().get(i));
//                }
//                Toast.makeText(SetMyTagsActivity.this, "setAllTagsToFlowLayout 완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Toast.makeText(SetMyTagsActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        flowLayout.relayoutToAlign();
    }



    private void setMySelectedTagsChecked(String s) {
        //tagNames이용
        String[] booksTags = s.split(";");
        Toast.makeText(SetMyTagsActivity.this, booksTags.length + "개, 1번째는 " + booksTags[0] , Toast.LENGTH_SHORT).show();

        for (int i = 0; i < booksTags.length; i++) {
            if (!booksTags[i].equals("")) {
//                try {
                    int toInt = Integer.parseInt(booksTags[i]);
                    flowLayout.setCheckedOnlyOne(true, toInt - 1);
//                } catch (NullPointerException e) {
//                    Toast.makeText(SetMyTagsActivity.this, booksTags[i] + " : NullPointerException " + (i + 1) + "번째 오류가 났습니다. 다시 시도해주세요! 새로고침 만들기", Toast.LENGTH_SHORT).show();
//                }
            }
        }
//        Toast.makeText(SetMyTagsActivity.this, "setMySelectedTagsChecked 완료", Toast.LENGTH_SHORT).show();
    }

    private void setMySelectedTagsToFlowLayout() {
        //나의 태그에 내 태그들 가져오기

        Call<List<UserSelectedTags>> call = getNetworkManager().getBookApi().getUserSelectedTags(userId, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<List<UserSelectedTags>>() {
            @Override
            public void onResponse(Call<List<UserSelectedTags>> call, Response<List<UserSelectedTags>> response) {
                //tag x : response.body().toString() = [], not null

                //isSuccessful()
                //Returns true if code() is in the range [200..300).

                String body = response.body().toString();
                if (body.equals("[]")) { //tag 선택 x
                    Toast.makeText(SetMyTagsActivity.this, body + " do nothing", Toast.LENGTH_SHORT).show();
                    //do nothing
                } else {
                    List<UserSelectedTags> userSelectedTags = response.body();
                    String s = userSelectedTags.get(0).getTags();
                    Toast.makeText(SetMyTagsActivity.this, s, Toast.LENGTH_SHORT).show();
                    setMySelectedTagsChecked(s);
                }

            }

            @Override
            public void onFailure(Call<List<UserSelectedTags>> call, Throwable t) {
                Toast.makeText(SetMyTagsActivity.this, "b오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.button:
                //delete하고 post하기~!
                String mySelectedTags = flowLayout.getCheckedValuesInString();
                deleteTags();
//                if (mySelectedTags.equals(""))
                postTags(mySelectedTags);

                intent = new Intent(SetMyTagsActivity.this, MyPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("tagNames", tagNames);
                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void deleteTags() {
        Call<ResponseBody> call = getNetworkManager().getBookApi().deleteUserSelectedTags(userId, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SetMyTagsActivity.this, "delete success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SetMyTagsActivity.this, "deleteTags onResponse오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SetMyTagsActivity.this, "deleteTags onFailure오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void postTags(String mySelectedTags) {

//        if (mySelectedTags.equals(""))
//            deleteTags();

        Call<ResponseBody> call = getNetworkManager().getBookApi().setUserSelectedTags(mySelectedTags, userId, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SetMyTagsActivity.this, "post success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SetMyTagsActivity.this, "postTags onResponse에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SetMyTagsActivity.this, "postTags onFailure에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
