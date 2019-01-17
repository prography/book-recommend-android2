package org.techtown.just;

import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.btn_main)
    Button btn_main;

    //public ImageView profile_img;
    private ProfilePictureView profilePictureView;
    private TextView user_name, user_email;
    private Button btn_modprofile;

    Profile profile = Profile.getCurrentProfile();
    final String link = profile.getProfilePictureUri(200,200).toString();

    Handler handler = new Handler(); //외부쓰레드에서 메인 ui화면을 그릴 때 사용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ButterKnife.bind(this);

        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_img);
        //round profile image
        profilePictureView.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21){
            profilePictureView.setClipToOutline(true);
        }

        user_name=findViewById(R.id.profile_name);
        //user_email=findViewById(R.id.);
        btn_modprofile = findViewById(R.id.mod_profile);

        basic_setting();

        btn_main.setOnClickListener(this);
        btn_modprofile.setOnClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    public void basic_setting(){
        user_name.setText(profile.getName());

        profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
        profilePictureView.setProfileId(profile.getId());

    }

    @Override
    public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.btn_main:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.mod_profile:
                        Intent intent1 = new Intent(getApplicationContext(), Mod_ProfileActivity.class);
                        startActivity(intent1);
                        break;
                }
    }
}


