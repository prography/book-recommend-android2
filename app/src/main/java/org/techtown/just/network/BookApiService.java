package org.techtown.just.network;

import com.google.gson.JsonObject;

import org.techtown.just.model.BookInfo;
import org.techtown.just.model.LoginResult;
import org.techtown.just.model.LoginToken;
import org.techtown.just.model.Post;
import org.techtown.just.model.Tag;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApiService {
    /**
     * 모든 API에
     * accessToken, idToken, refreshToken
     * 필드를 추가해사 보내주세요!!
     */

//    @FormUrlEncoded
//    @GET("tags")
//    Call<ResponseBody> getTags(@Field("accessToken") String accessToken,
//                               @Field("idToken") String idToken,
//                               @Field("refreshToken") String refreshToken);

    //1
    @GET("tags/")
    Call<List<Tag>> getTags();

    //2
    @GET("books/listwithtag/{tags}/")
    Call<List<BookInfo>> getListWithTag(@Query("tags") String tags);

    //3
    @GET("books/listwithsearch/{search}/")
    Call<List<BookInfo>> getListWithSearch(@Path("search") String search);

    //4
    @GET("/books/read/{user_id}/")
    Call<List<BookInfo>> getListUserRead(@Path("user_id") String user_id);

    //5
    @GET("/books/interest/{user_id}/")
    Call<List<BookInfo>> getListUserInterested(@Path("user_id") String user_id);

    //6
    @GET("books/listwithtag/{isbn}/")
    Call<List<BookInfo>> getBookInfoWithIsbn(@Path("isbn") String isbn);

    //7
    @GET("/books/{isbn}/status/{user_id}/")
    Call<JsonObject> getBookFlag(@Path("isbn") String isbn,
                                 @Path("user_id") String user_id);

    //8
    @FormUrlEncoded
    @POST("books/{isbn}/status/{user_id}/")
    Call<JsonObject> saveStatus(@Path("isbn") int isbn,
                                @Field("flag_r") int flag_r,
                                @Field("flag_i") int flag_i,
                                @Path("user_id") String user_id);

    //9 로그인
    @FormUrlEncoded
    @POST("auth/login/")
    Call<LoginResult> login(@Field("id") String id,
                            @Field("pw") String pw);

    //10
    @FormUrlEncoded
    @POST("auth2/validate/")
    Call<JsonObject> validate(@Field("accessToken") String accessToken,
                               @Field("idToken") String idToken,
                               @Field("refreshToken") String refreshToken);


    //11 회원가입
    @FormUrlEncoded
    @POST("auth/register/")
    Call<JsonObject> register(@Field("id") String id,
                                @Field("pw") String pw,
                                @Field("email") String email);


    //Path는 {} Query는 ?=

}
