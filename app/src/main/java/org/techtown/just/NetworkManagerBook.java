package org.techtown.just;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManagerBook {

    private static Retrofit retrofit;
    private static ApiServiceBook apiServiceBook;

    private static Retrofit buildRetrofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        return new Retrofit.Builder()
                .client(client)
                .baseUrl(ApiServiceBook.API_URL).build();
    }

    private static Retrofit getRetrofit() {
        if (retrofit==null){
            retrofit = buildRetrofit();
        }
        return retrofit;
    }


    public static ApiServiceBook getApiService(){
        if (apiServiceBook==null){
            apiServiceBook = getRetrofit().create(ApiServiceBook.class);
        }
        return apiServiceBook;
    }
}
