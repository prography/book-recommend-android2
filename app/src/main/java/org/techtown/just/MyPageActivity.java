package org.techtown.just;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
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
import org.techtown.just.model.BookInfo_Added;
import org.techtown.just.model.LocalStore;
import org.techtown.just.model.Tag;
import org.techtown.just.model.TagNames;
import org.techtown.just.model.UserSelectedTags;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.GridLayout.HORIZONTAL;

public class MyPageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    ImageView btn_main;
    @BindView(R.id.tv_profile_name)
    TextView user_name;
    @BindView(R.id.tv_mytags)
    TextView mytags;
    @BindView(R.id.btn_mod_profile)
    Button btn_modprofile;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_more_read)
    Button btn_mRead;
    @BindView(R.id.btn_more_interest)
    Button btn_mInterest;
    @BindView(R.id.recycler_readbook)
    RecyclerView rc_readbook;
    @BindView(R.id.recycler_interest_book)
    RecyclerView rc_intbook;
    @BindView(R.id.btn_setMyTags)
    Button btnSetMyTags;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    @BindView(R.id.recyler_rcbook)
    RecyclerView recyclerRcbook;



    private ProfilePictureView profilePictureView;

    Boolean LoggedIn_FB = false;
    Boolean LoggedIn_KK = false;

    //    Profile facebookProfile = Profile.getCurrentProfile();
    KakaoTalkProfile kakaoTalkProfile;

    Profile facebookProfile;
    String link;
    //Profile facebookProfile = Profile.getCurrentProfile();
    //String link = facebookProfile.getProfilePictureUri(200,200).toString();


    //    final String link = facebookProfile.getProfilePictureUri(200, 200).toString();
    Handler handler = new Handler(); //외부쓰레드에서 메인 ui화면을 그릴 때 사용

    RecyclerView.LayoutManager mLayoutManager_1, mLayoutManager_2, mLayoutManager_3;
    MyAdapter myAdapter_1, myAdapter_2, myAdapter_3;

    TagNames tagNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        tagNames = (TagNames) intent.getSerializableExtra("tagNames");

        getMySelectedTags();

        profilePictureView = (ProfilePictureView) findViewById(R.id.img_profile);
        //round facebookProfile image
        profilePictureView.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= 21) {
            profilePictureView.setClipToOutline(true);
        }

        //access token 유효성 확인 - 최초 1번
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        LoggedIn_FB = accessToken != null && !accessToken.isExpired();
        Log.d("Mypage :: LoggedIn_FB", "" + LoggedIn_FB);

        user_name.setText(userId);

        btn_main.setOnClickListener(this);
        btn_modprofile.setOnClickListener(this);
        btn_mRead.setOnClickListener(this);
        btn_mInterest.setOnClickListener(this);
        btnSetMyTags.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        rcbook_setting();
        readbook_setting();
        intbook_setting();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //페북 로그인 체크
        LoggedIn_FB = LoggedIn_FB && getLocalStore().getBooleanValue(LocalStore.my, true);

    }

    public void rcbook_setting() {
        loadRcBooks();
        recyclerRcbook.setHasFixedSize(true);
        mLayoutManager_3 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager_3).setOrientation(HORIZONTAL);
        recyclerRcbook.setLayoutManager(mLayoutManager_3);
    }



    public void readbook_setting() {
        loadReadBooks();

        rc_readbook.setHasFixedSize(true);

        mLayoutManager_1 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager_1).setOrientation(HORIZONTAL);
        rc_readbook.setLayoutManager(mLayoutManager_1);

//        myAdapter_1 = new MyAdapter(BookInfoArrayList);
//        rc_readbook.setAdapter(myAdapter_1);

    }

    public void intbook_setting() {
        loadInterestBook();
        rc_intbook.setHasFixedSize(true);

        mLayoutManager_2 = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager_2).setOrientation(HORIZONTAL);
        rc_intbook.setLayoutManager(mLayoutManager_2);

//        myAdapter_2 = new MyAdapter(BookInfoArrayList2);
//        rc_intbook.setAdapter(myAdapter_2);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.btn_mod_profile:
//                Intent intent1 = new Intent(getApplicationContext(), Mod_ProfileActivity.class);
//                startActivity(intent1);
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;

            case R.id.btn_more_read:
                Intent intent2 = new Intent(getApplicationContext(), ReadBookActivity.class);
                intent2.putExtra("tagNames", tagNames);
                startActivity(intent2);
                break;

            case R.id.btn_more_interest:
                Intent intent3 = new Intent(getApplicationContext(), FavoriteBookActivity.class);
                intent3.putExtra("tagNames", tagNames);
                startActivity(intent3);
                break;

            case R.id.btn_setMyTags:
                Intent intent4 = new Intent(getApplicationContext(), SetMyTagsActivity.class);
                intent4.putExtra("tagNames", tagNames);
                startActivity(intent4);
                break;

            case R.id.btn_logout:
                final Intent intent5 = new Intent(getApplicationContext(), LoginActivity.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                new AlertDialog.Builder(this)
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setIcon(R.drawable.ic_people)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 확인시 처리 로직
                                showShortToastMsg("로그아웃되었습니다.");
                                getLocalStore().clearTokenValues();
                                startActivity(intent5);
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 취소시 처리 로직
                                showShortToastMsg("취소하였습니다.");
                            }})
                        .show();


                break;
        }
    }//onClick


    private void loadRcBooks() {
        //userId로 책 추천 정보 가져오기
        Call<List<BookInfo_Added>> bookInfo = getNetworkManager().getBookApi().getUserRecommendBookList(userId, accessToken, idToken, refreshToken);
        bookInfo.enqueue(new Callback<List<BookInfo_Added>>() {
            @Override
            public void onResponse(Call<List<BookInfo_Added>> call, Response<List<BookInfo_Added>> response) {
                List<BookInfo_Added> books_3 = response.body();
                if (response.isSuccessful()) {
                    myAdapter_3 = new MyAdapter(getApplicationContext(), books_3, tagNames);
                    recyclerRcbook.setAdapter(myAdapter_3);
                } else {
                    showShortToastMsg("response 오류가 발생했습니다.");
                    Log.e("response error :: ", "" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo_Added>> call, Throwable t) {
                showShortToastMsg("fail 오류가 발생했습니다.");
                Log.e("response onFailure :: ", t.getMessage());
            }
        });
    }

    private void loadReadBooks() {
        //user_id으로 책 정보 가져오기
        Call<List<BookInfo_Added>> bookInfo = getNetworkManager().getBookApi().getListUserRead(userId, accessToken, idToken, refreshToken);
        bookInfo.enqueue(new Callback<List<BookInfo_Added>>() {
            @Override
            public void onResponse(Call<List<BookInfo_Added>> call, Response<List<BookInfo_Added>> response) {
                List<BookInfo_Added> books_1 = response.body();
                if (response.isSuccessful()) {
                    myAdapter_1 = new MyAdapter(getApplicationContext(), books_1, tagNames);
                    rc_readbook.setAdapter(myAdapter_1);
                } else {
                    Toast.makeText(MyPageActivity.this, "response 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("response error :: ", "" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo_Added>> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "fail 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();

                Log.e("response onFailure :: ", t.getMessage());
            }
        });

    }

    private void loadInterestBook() {
        Call<List<BookInfo_Added>> bookInfo = getNetworkManager().getBookApi().getListUserInterested(userId, accessToken, idToken, refreshToken);
        bookInfo.enqueue(new Callback<List<BookInfo_Added>>() {
            @Override
            public void onResponse(Call<List<BookInfo_Added>> call, Response<List<BookInfo_Added>> response) {
                List<BookInfo_Added> books_2 = response.body();
                if (response.isSuccessful()) {
                    myAdapter_2 = new MyAdapter(getApplicationContext(), books_2, tagNames);
                    rc_intbook.setAdapter(myAdapter_2);
                } else {
                    Toast.makeText(MyPageActivity.this, "a오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo_Added>> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "b오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMySelectedTags() {
        Call<List<UserSelectedTags>> call = getNetworkManager().getBookApi().getUserSelectedTags(userId, accessToken, idToken, refreshToken);
        call.enqueue(new Callback<List<UserSelectedTags>>() {
            @Override
            public void onResponse(Call<List<UserSelectedTags>> call, Response<List<UserSelectedTags>> response) {

                try {
                    if (response.body() != null && response.isSuccessful() == true) {
                        List<UserSelectedTags> userSelectedTags = response.body();
                        String s = userSelectedTags.get(0).getTags();

                        setFlowLayoutWithTag(s);
                        Toast.makeText(MyPageActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(MyPageActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IndexOutOfBoundsException e) {

                }


            }

            @Override
            public void onFailure(Call<List<UserSelectedTags>> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "b오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFlowLayoutWithTag(String s) {
        //tagNames이용
        List<Tag> allTags = tagNames.getAllTags();
        String booksTags[] = s.split(";");

        for (int i = 0; i < booksTags.length; i++) {
            try {
                int toInt = Integer.parseInt(booksTags[i]);
                flowLayout.addTag(allTags.get(toInt - 1));
            } catch (NumberFormatException e) {

            }
        }

        flowLayout.relayoutToAlign();
        flowLayout.setChecked(true);
        flowLayout.setCheckable(false);
    }



    public void basic_setting() {

        if (LoggedIn_FB == true) {
//            //if facebook로그인시
//            facebookProfile = Profile.getCurrentProfile();
//            link = facebookProfile.getProfilePictureUri(200, 200).toString();
//
//            user_name.setText(facebookProfile.getName());
//            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
//            //id값으로 facebookProfile uri설정
//            profilePictureView.setProfileId(facebookProfile.getId());
        } else if (LoggedIn_KK == true) {
//            user_name.setText(kakaoTalkProfile.getNickName());
//            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
//            profilePictureView.setProfileId(kakaoTalkProfile.getProfileImageUrl());

        } else { //일반로그인
            //user_name <- user_id
//            userId = getLocalStore().getStringValue(LocalStore.UserId);
//            user_name.setText(userId);
        }
    }
}


