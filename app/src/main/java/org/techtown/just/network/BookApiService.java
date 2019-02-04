package org.techtown.just.network;

import com.google.gson.JsonObject;

import org.techtown.just.model.BookInfo;
import org.techtown.just.model.LoginResult;
import org.techtown.just.model.LoginToken;
import org.techtown.just.model.Post;
import org.techtown.just.model.Tag;
import org.techtown.just.model.UserSelectedTags;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    Call<List<BookInfo>> getListWithTag(@Path("tags") String tags);

    //3
    @GET("books/listwithsearch/{search}/")
    Call<List<BookInfo>> getListWithSearch(@Path("search") String search);

    //4
    @GET("books/read/{user_id}/")
    Call<List<BookInfo>> getListUserRead(@Path("user_id") String user_id);

    //5
    @GET("books/interest/{user_id}/")
    Call<List<BookInfo>> getListUserInterested(@Path("user_id") String user_id);

    //6
    @GET("books/{isbn}/")
    Call<List<BookInfo>> getBookInfoWithIsbn(@Path("isbn") String isbn);

    //7
    @GET("user/status/{isbn}/{user_id}/")
    Call<JsonObject> getBookFlag(@Path("isbn") String isbn,
                                 @Path("user_id") String user_id);

    //8
    @FormUrlEncoded
    @POST("user/{isbn}/status/{user_id}/")
    Call<JsonObject> saveStatus(@Path("isbn") String isbn,
                                @Field("flag_r") int flag_r,
                                @Field("flag_i") int flag_i,
                                @Path("user_id") String user_id);

    //9 로그인
    @FormUrlEncoded
    @POST("auth/login/")
    Call<LoginResult> login(@Field("id") String id,
                            @Field("pw") String pw);

    //10
    @POST("auth2/validate/")
    Call<JsonObject> validate();


    //11 회원가입
    @FormUrlEncoded
    @POST("auth/register/")
    Call<JsonObject> register(@Field("id") String id,
                                @Field("pw") String pw,
                                @Field("email") String email);



    //

    //Path는 {} Query는 ?=


//    4. User(/user)
//    가. /selected/:user_id : 유저가 고른 태그 출력
//    get
//    return : [{"tags":";1;2;3;"}]
    @GET("user/selected/{user_id}/")
    Call<List<UserSelectedTags>> getUserSelectedTags(@Path("user_id") String user_id,
                                               @Header("accessToken") String accessToken,
                                               @Header("idToken") String idToken,
                                               @Header("refreshToken") String refreshToken);

//    나. /status/tag/:tag/:user_id : 유저가 고른 태그 저장
//    post
    @FormUrlEncoded
    @POST("status/tag/{tags}/{user_id}/")
    Call<ResponseBody> setUserSelectedTags(@Path("tags") String tags,
                                           @Path("user_id") String user_id,
                                           @Field("accessToken") String accessToken,
                                           @Field("idToken") String idToken,
                                           @Field("refreshToken") String refreshToken);

//    다. /status/tag/:tag/:user_id : 유저가 고른 태그 추가
//    put

//    라. /delete/tag/:user_id : 유저가 고른 태그 삭제
//    delete
    @FormUrlEncoded
    @DELETE("delete/tag/{user_id}/")
    Call<ResponseBody> deleteUserSelectedTags(@Path("user_id") String user_id,
                                           @Field("accessToken") String accessToken,
                                           @Field("idToken") String idToken,
                                           @Field("refreshToken") String refreshToken);

//    마. /status/book/:isbn/:user_id : 유저가 ISBN값의 책과 관련된 flag값들(읽었어요/좋아요) 출력
//    get
//    return : [{"had_read":1,"be_interested":1}]

//    바. /status/book/:isbn/:user_id : 유저에 ISBN값의 책에 flag(읽었어요/좋아요) 정보 저장
//    post

//    사. /book/:user_id : 유저에 등록된 책리스트 출력
//    get
//    return : [{"isbn":"9788937460753","tags":";4;7;6;29;23;30;20;","book_name":"위대한 개츠비","author":"스콧 피츠제럴드","country":"영미소설"}]

}
