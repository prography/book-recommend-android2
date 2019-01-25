package org.techtown.just.base;

import android.support.v7.app.AppCompatActivity;

import org.techtown.just.LocalStore;
import org.techtown.just.network.NetworkManager;

public class BaseActivity extends AppCompatActivity {

    public LocalStore getLocalStore() {
        return ((BaseApplication) getApplication()).getLocalStore();
    }

    public NetworkManager getNetworkManager() {
        return ((BaseApplication) getApplication()).getNetworkManager();
    }

}
