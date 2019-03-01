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

    public void progressON() {
        BaseApplication.getBaseApplication().progressON(this, null);
    }

    public void progressON(String message) {
        BaseApplication.getBaseApplication().progressON(this, message);
    }

    public void progressOFF() {
        BaseApplication.getBaseApplication().progressOFF();
    }

}
