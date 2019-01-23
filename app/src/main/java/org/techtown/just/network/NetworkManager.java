package org.techtown.just.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static final String API_URL = "https://82uym5oh19.execute-api.ap-northeast-2.amazonaws.com/dev/";
    private static Retrofit retrofit;
    private static LoginApiService apiServiceLogin;
    private static BookApiService bookApiService;


    private static Retrofit buildRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL).build();
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = buildRetrofit();
        }
        return retrofit;
    }


    public static LoginApiService getLoginApi() {
        if (apiServiceLogin == null) {
            apiServiceLogin = getRetrofit().create(LoginApiService.class);
        }
        return apiServiceLogin;
    }

    public static BookApiService getBookApi() {
        if (bookApiService == null) {
            bookApiService = getRetrofit().create(BookApiService.class);
        }
        return bookApiService;
    }
}
