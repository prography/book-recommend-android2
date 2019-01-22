package org.techtown.just.base;

import android.support.v7.app.AppCompatActivity;

import org.techtown.just.LocalStore;

public class BaseActivity extends AppCompatActivity {

    public LocalStore getLocalStore(){
        return ((BaseApplication)getApplication()).getLocalStore();
    }

}
