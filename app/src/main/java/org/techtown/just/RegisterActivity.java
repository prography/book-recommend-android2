package org.techtown.just;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.techtown.just.base.BaseActivity;
import org.techtown.just.model.LoginResult;
import org.techtown.just.model.Tag;
import org.techtown.just.network.NetworkManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

import static org.techtown.just.R.id.btn_register;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.login_label)
    TextView loginLabel;
    @BindView(R.id.id)
    EditText id;
    @BindView(R.id.pw)
    EditText pw;
    @BindView(R.id.email)
    EditText email;
    @BindView(btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        btnRegister.setOnClickListener(this);

        final Call<JsonObject> register = getNetworkManager().getBookApi().register(id.getText().toString(), pw.getText().toString(), email.getText().toString());
        register.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//              List<JsonObject> tags = response.body();
                if (response.isSuccessful()) {
                    login(id.getText().toString(), pw.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입 과정에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void login(String id, String pw) {
        final Call<LoginResult> login = getNetworkManager().getBookApi().login(id, pw);
        login.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult loginResult = response.body();
                //user_id, LoginToken이 있다.
                if (loginResult != null && loginResult.getTokens() != null) {
                    getLocalStore().setStringValue(LocalStore.AccessToken, loginResult.getTokens().getAccessToken());
                    getLocalStore().setStringValue(LocalStore.IdToken, loginResult.getTokens().getIdToken());
                    getLocalStore().setStringValue(LocalStore.RefreshToken, loginResult.getTokens().getRefreshToken());
                    Toast.makeText(RegisterActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_register:
                intent = new Intent(RegisterActivity.this, Register2Activity.class);
                intent.putExtra("id", id.toString());
                intent.putExtra("pw", pw.toString());
                intent.putExtra("email", email.toString());
                startActivity(intent);
                break;
        }

    }
}
