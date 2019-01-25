package org.techtown.just;

import android.content.Intent;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.kakao.kakaotalk.response.KakaoTalkProfile;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.BookInfo;
import org.techtown.just.model.TagNames;
import org.techtown.just.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.GridLayout.HORIZONTAL;
import static org.techtown.just.base.BaseApplication.getLocalStore;

public class MyPageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    ImageView btn_main;
    @BindView(R.id.profile_name)
    TextView user_name;
    @BindView(R.id.mod_profile)
    Button btn_modprofile;
    @BindView(R.id.more_read)
    Button btn_mRead;
    @BindView(R.id.more_interest)
    Button btn_mInterest;
    //@BindView(R.id.recycler_read)
    RecyclerView rc_Readbook;
    //@BindView(R.id.recycler_int)
    RecyclerView rc_Intbook;

    private ProfilePictureView profilePictureView;

    Boolean LoggedIn_FB = false;
    Boolean LoggedIn_KK = false;

//    Profile facebookProfile = Profile.getCurrentProfile();
    KakaoTalkProfile kakaoTalkProfile ;

    Profile facebookProfile;
    String link;
    String userId = "1";
    //Profile facebookProfile = Profile.getCurrentProfile();
    //String link = facebookProfile.getProfilePictureUri(200,200).toString();


    //    final String link = facebookProfile.getProfilePictureUri(200, 200).toString();
    Handler handler = new Handler(); //외부쓰레드에서 메인 ui화면을 그릴 때 사용

    RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2;
    MyAdapter myAdapter_1, myAdapter_2;

    TagNames tagNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_img);
        //round facebookProfile image
        profilePictureView.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21){
            profilePictureView.setClipToOutline(true);
        }

        //access token 유효성 확인 - 최초 1번
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        LoggedIn_FB = accessToken != null && !accessToken.isExpired();
        Log.d("Mypage :: LoggedIn_FB" , ""+LoggedIn_FB);

        basic_setting();

        btn_main.setOnClickListener(this);
        btn_modprofile.setOnClickListener(this);
        btn_mRead.setOnClickListener(this);
        btn_mInterest.setOnClickListener(this);

        readbook_setting();
        intbook_setting();

    }

    @Override
    protected void onResume(){
        super.onResume();
        //페북 로그인 체크
        LoggedIn_FB =  LoggedIn_FB && getLocalStore().getBooleanValue(LocalStore.my, true);

    }
    @Override
    protected void onRestart(){
        super.onRestart();


    }

    public void basic_setting(){

        if(LoggedIn_FB == true) {
            //if facebook로그인시
            facebookProfile=Profile.getCurrentProfile();
            link = facebookProfile.getProfilePictureUri(200,200).toString();

            user_name.setText(facebookProfile.getName());
            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            //id값으로 facebookProfile uri설정
            profilePictureView.setProfileId(facebookProfile.getId());
        }

        else if(LoggedIn_KK == true){
            user_name.setText(kakaoTalkProfile.getNickName());
            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(kakaoTalkProfile.getProfileImageUrl());

        }

        else { //일반로그인
            //user_name <- user_id
            userId = getLocalStore().getStringValue(LocalStore.UserId);
            user_name.setText(userId);
        }
    }

    public void readbook_setting(){

        loadReadBooks();

        rc_Readbook=(RecyclerView)findViewById(R.id.rc_readbook);
        rc_Readbook.setHasFixedSize(true);

        mLayoutManager_1 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager_1).setOrientation(HORIZONTAL);
        rc_Readbook.setLayoutManager(mLayoutManager_1);

//        myAdapter_1 = new MyAdapter(BookInfoArrayList);
//        rc_Readbook.setAdapter(myAdapter_1);

    }
    public void intbook_setting(){

        loadInterestBook();

        rc_Intbook=(RecyclerView)findViewById(R.id.rc_intbook);
        rc_Intbook.setHasFixedSize(true);

        mLayoutManager_2 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager_2).setOrientation(HORIZONTAL);
        rc_Intbook.setLayoutManager(mLayoutManager_2);

//        myAdapter_2 = new MyAdapter(BookInfoArrayList2);
//        rc_Intbook.setAdapter(myAdapter_2);

    }

    @Override
    public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.btn_back:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.mod_profile:
                        Intent intent1 = new Intent(getApplicationContext(), Mod_ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.more_read:
                        Intent intent2 = new Intent(getApplicationContext(),ReadBookActivity.class);
                        intent2.putExtra("tagNames", tagNames);
                        startActivity(intent2);
                        break;

                    case R.id.more_interest:
                        Intent intent3 = new Intent(getApplicationContext(),FavoriteBookActivity.class);
                        intent3.putExtra("tagNames", tagNames);
                        startActivity(intent3);
                        break;
                }
    }//onClick



    private void loadReadBooks(){

        //userId = getLocalStore().getStringValue(LocalStore.UserId);
        //user_id으로 책 정보 가져오기
        Call<List<BookInfo>> bookInfo = getNetworkManager().getBookApi().getListUserRead(userId);
        bookInfo.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books_1 = response.body();
                if (response.isSuccessful()) {
                    myAdapter_1 = new MyAdapter(getApplicationContext(), books_1, tagNames);
                    rc_Readbook.setAdapter(myAdapter_1);
                } else {
                    Toast.makeText(MyPageActivity.this, "response 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("response error :: " , ""+response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "fail 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();

                Log.e("response onFailure :: " ,t.getMessage());
            }
        });

    }

    private void loadInterestBook(){

        //userId = getLocalStore().getStringValue(LocalStore.UserId);
        Call<List<BookInfo>> bookInfo = getNetworkManager().getBookApi().getListUserInterested(userId);
        bookInfo.enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                List<BookInfo> books_2 = response.body();
                if(response.isSuccessful()) {
                    myAdapter_2 = new MyAdapter(getApplicationContext(),books_2, tagNames);
                    rc_Intbook.setAdapter(myAdapter_2);
                }else {
                    Toast.makeText(MyPageActivity.this, "a오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "b오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}


