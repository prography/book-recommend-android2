package org.techtown.just.network;

import org.techtown.just.model.LocalStore;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private LocalStore localStore;

    public AuthenticationInterceptor(LocalStore localStore) {
        this.localStore = localStore;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded");

        if (localStore.getStringValue(LocalStore.AccessToken) != null) {
            builder.addHeader("accessToken", localStore.getStringValue(LocalStore.AccessToken));
        }
        if (localStore.getStringValue(LocalStore.IdToken) != null) {
            builder.addHeader("idToken", localStore.getStringValue(LocalStore.IdToken));
        }
        if (localStore.getStringValue(LocalStore.RefreshToken) != null) {
            builder.addHeader("refreshToken", localStore.getStringValue(LocalStore.RefreshToken));
        }

        return chain.proceed(builder.build());
    }
}
