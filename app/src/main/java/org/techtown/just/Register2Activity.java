package org.techtown.just;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register2Activity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.internet)
    Button internet;

    @BindView(R.id.refresh)
    Button refresh;

    String id, pw, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");
        email = intent.getStringExtra("email");

        //로그인 인증 성공
//        if () {

//        }
        //로그인 인증 실패
//        else {
//            Toast.makeText(Register2Activity.this, "실패", Toast.LENGTH_SHORT).show();
//        }

        text.setText(email + "로 전송된 인증 메일을 확인해주세요!");

        refresh.setOnClickListener(this);



        Call<JsonObject> jsonObjectCall = getNetworkManager().getBookApi().register(id, pw, email);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString();
                if (message.equals("success")) {
                    Toast.makeText(Register2Activity.this, "oh~~~", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register2Activity.this, MyPageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", id);
                    intent.putExtra("pw", pw);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Register2Activity.this, "oh...", Toast.LENGTH_SHORT).show();
                }
//                if (loginResult == null) //로그인 인증 실패
//                    Toast.makeText(Register2Activity.this, "실패", Toast.LENGTH_SHORT).show();
//                else { //성공
//                    Intent intent = new Intent(Register2Activity.this, MyPageActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Register2Activity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.refresh:
                Intent refresh = new Intent(this, Register2Activity.class);
                refresh.putExtra("email", email);
                startActivity(refresh);
                this.finish();
                break;
        }
    }
}
