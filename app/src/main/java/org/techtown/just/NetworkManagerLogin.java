package org.techtown.just;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class NetworkManagerLogin {

    private static Retrofit retrofit;
    private static ApiServiceLogin apiServiceLogin;

    private static Retrofit buildRetrofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        return new Retrofit.Builder()
                .client(client)
                .baseUrl(ApiServiceLogin.API_URL).build();
    }

    private static Retrofit getRetrofit() {
        if (retrofit==null){
            retrofit = buildRetrofit();
        }
        return retrofit;
    }


    public static ApiServiceLogin getApiService(){
        if (apiServiceLogin==null){
            apiServiceLogin = getRetrofit().create(ApiServiceLogin.class);
        }
        return apiServiceLogin;
    }
}
