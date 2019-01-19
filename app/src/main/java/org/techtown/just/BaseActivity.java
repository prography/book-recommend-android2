package org.techtown.just;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public LocalStore getLocalStore(){
        return ((BaseApplication)getApplication()).getLocalStore();
    }

}
