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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.kakao.kakaotalk.response.KakaoTalkProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.GridLayout.HORIZONTAL;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener{

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

    Profile facebookProfile = Profile.getCurrentProfile();
    KakaoTalkProfile kakaoTalkProfile ;

    final String link = facebookProfile.getProfilePictureUri(200,200).toString();

    Handler handler = new Handler(); //외부쓰레드에서 메인 ui화면을 그릴 때 사용

    RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2;
    MyAdapter myAdapter_1, myAdapter_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ButterKnife.bind(this);

        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_img);
        //round facebookProfile image
        profilePictureView.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21){
            profilePictureView.setClipToOutline(true);
        }

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

    }

    public void basic_setting(){

        //if facebook로그인시
        user_name.setText(facebookProfile.getName());
        profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
        //id값으로 facebookProfile uri설정
        profilePictureView.setProfileId(facebookProfile.getId());

        //if kakao 로그인시
//        user_name.setText(kakaoTalkProfile.getNickName());
//        profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
//        profilePictureView.setProfileId(kakaoTalkProfile.getProfileImageUrl());
    }

    public void readbook_setting(){

        ArrayList<BookInfo> BookInfoArrayList = new ArrayList<>();

        BookInfoArrayList.add(new BookInfo("배"));
        BookInfoArrayList.add(new BookInfo("고"));
        BookInfoArrayList.add(new BookInfo("오"));
        BookInfoArrayList.add(new BookInfo("프"));
        BookInfoArrayList.add(new BookInfo("으"));
        BookInfoArrayList.add(new BookInfo("다"));
        BookInfoArrayList.add(new BookInfo("아"));

        rc_Readbook=(RecyclerView)findViewById(R.id.rc_readbook);
        rc_Readbook.setHasFixedSize(true);

        mLayoutManager_1 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager_1).setOrientation(HORIZONTAL);
        rc_Readbook.setLayoutManager(mLayoutManager_1);

        myAdapter_1 = new MyAdapter(BookInfoArrayList);
        rc_Readbook.setAdapter(myAdapter_1);

    }
    public void intbook_setting(){

        ArrayList<BookInfo> BookInfoArrayList2 = new ArrayList<>();

        BookInfoArrayList2.add(new BookInfo("가"));
        BookInfoArrayList2.add(new BookInfo("로"));
        BookInfoArrayList2.add(new BookInfo("로"));
        BookInfoArrayList2.add(new BookInfo("해"));
        BookInfoArrayList2.add(new BookInfo("주"));
        BookInfoArrayList2.add(new BookInfo("세"));
        BookInfoArrayList2.add(new BookInfo("요"));

        rc_Intbook=(RecyclerView)findViewById(R.id.rc_intbook);
        rc_Intbook.setHasFixedSize(true);

//        mLayoutManager = new GridLayoutManager(this,3);

        mLayoutManager_2 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager_2).setOrientation(HORIZONTAL);
        rc_Intbook.setLayoutManager(mLayoutManager_2);

        myAdapter_2 = new MyAdapter(BookInfoArrayList2);
        rc_Intbook.setAdapter(myAdapter_2);

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
                        startActivity(intent2);
                        break;

                    case R.id.more_interest:
                        Intent intent3 = new Intent(getApplicationContext(),FavoriteBookActivity.class);
                        startActivity(intent3);
                        break;
                }
    }//onClick




}


