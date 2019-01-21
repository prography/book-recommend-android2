package org.techtown.just;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiServiceLogin {
    public static final String API_URL = "https://82uym5oh19.execute-api.ap-northeast-2.amazonaws.com/dev/auth/";

    @FormUrlEncoded
    @POST("register/")
    Call<Login> register(@Field("id") String id,
                         @Field("pw") String pw,
                         @Field("email") String email);

}
