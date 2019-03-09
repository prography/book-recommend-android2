package org.techtown.just.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.techtown.just.model.LocalStore;
import org.techtown.just.network.NetworkManager;

public class BaseActivity extends AppCompatActivity {

    public String userId = getLocalStore().getStringValue(LocalStore.UserId);
    public String accessToken = getLocalStore().getStringValue(LocalStore.AccessToken);
    public String idToken = getLocalStore().getStringValue(LocalStore.IdToken);
    public String refreshToken = getLocalStore().getStringValue(LocalStore.RefreshToken);

    public String getUserId() { return getLocalStore().getStringValue(LocalStore.UserId); }
    public String getAccessToken() { return getLocalStore().getStringValue(LocalStore.AccessToken); }
    public String getIdToken() { return getLocalStore().getStringValue(LocalStore.IdToken); }
    public String getRefreshToken() { return getLocalStore().getStringValue(LocalStore.RefreshToken); }

    public LocalStore getLocalStore() {
        return ((BaseApplication) getApplication()).getLocalStore();
    }

    public NetworkManager getNetworkManager() {
        return ((BaseApplication) getApplication()).getNetworkManager();
    }


    //Loading 화면
    public void progressON() {
        BaseApplication.getBaseApplication().progressON(this, null);
    }

    public void progressON(String message) {
        BaseApplication.getBaseApplication().progressON(this, message);
    }

    public void progressOFF() {
        BaseApplication.getBaseApplication().progressOFF();
    }

    //Toast Msg
    public void showShortToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
