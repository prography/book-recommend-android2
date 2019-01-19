package org.techtown.just;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager{

    private static Retrofit retrofit;
    private static ApiService apiService;

    private static Retrofit buildRetrofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.API_URL).build();
    }

    private static Retrofit getRetrofit() {
        if (retrofit==null){
            retrofit = buildRetrofit();
        }
        return retrofit;
    }


    public static ApiService getApiService(){
        if (apiService==null){
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }
}
