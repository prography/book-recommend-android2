package org.techtown.just;

import android.content.Context;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.techtown.just.model.LocalStore;

import static org.techtown.just.base.BaseApplication.getLocalStore;

public class KakaoSessionCallback implements ISessionCallback {

    Context mContext;

//    public KakaoSessionCallback(Context context){
//        mContext = context;
//    }

    @Override
    public void onSessionOpened() {
        requestMe();
    }
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.d("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
    }
    public void requestLogout(){
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
//                runOnUiThread(new Runnable(){
//                    @Override
//                    public void run() {
//                    }
//
//            });
            }
        });
    }

    public void requestMe() {

        // 사용자정보 요청 결과에 대한 Callback

        UserManagement.requestMe(new MeResponseCallback() {

            // 세션 오픈 실패. 세션이 삭제된 경우,
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
            }

            // 회원이 아닌 경우,
            @Override
            public void onNotSignedUp() {
                Log.e("SessionCallback :: ", "onNotSignedUp");
            }

            // 사용자정보 요청에 성공한 경우,
            @Override
            public void onSuccess(UserProfile userProfile) {
//                final Intent intent = new Intent(mContext, MyPageActivity.class);

                getLocalStore().setBooleanValue(LocalStore.my, true);

                Log.d("SessionCallback :: ", "onSuccess");
                Log.d("==> kakao MY :: ", ""+getLocalStore().getBooleanValue(LocalStore.my,false));

                String nickname = userProfile.getNickname();
                String email = userProfile.getEmail();
                String profileImagePath = userProfile.getProfileImagePath();
                String thumnailPath = userProfile.getThumbnailImagePath();
                String UUID = userProfile.getUUID();
                long id = userProfile.getId();

                Log.d("Profile nickname: ", nickname + "");
            //    Log.d("Profile : ", email + "");
                Log.d("Profile image: ", profileImagePath + "");
            //    Log.d("Profile : ", thumnailPath + "");
            //    Log.e("Profile : ", UUID + "");
                Log.d("Profile id: ", id + "");

            }

            // 사용자 정보 요청 실패
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
            }
        });
    }
}
