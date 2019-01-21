package org.techtown.just;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceBook {
    public static final String API_URL = "https://82uym5oh19.execute-api.ap-northeast-2.amazonaws.com/dev/";

    @GET("tags")
    Call<ResponseBody> getTags();
}
