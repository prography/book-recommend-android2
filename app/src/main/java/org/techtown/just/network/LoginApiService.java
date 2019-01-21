package org.techtown.just.network;

import com.google.gson.JsonObject;

import org.techtown.just.model.LoginResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApiService {

    @FormUrlEncoded
    @POST("/auth/register/")
    Call<JsonObject> register(@Field("id") String id,
                              @Field("pw") String pw,
                              @Field("email") String email);


    @FormUrlEncoded
    @POST("/auth/login/")
    Call<LoginResult> login(@Field("id") String id,
                            @Field("pw") String pw,
                            @Field("email") String email);

    @FormUrlEncoded
    @POST("/auth2/validate/")
    Call<JsonObject> validateToken(@Field("accessToken") String accessToken,
                                   @Field("idToken") String idToken,
                                   @Field("refreshToken") String refreshToken);

}
