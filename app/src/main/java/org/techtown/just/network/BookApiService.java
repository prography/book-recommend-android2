package org.techtown.just.network;

import com.google.gson.JsonObject;

import org.techtown.just.model.BookFlag;
import org.techtown.just.model.BookInfoList_Added;
import org.techtown.just.model.BookInfoList_NotAdded;
import org.techtown.just.model.BookInfo_Added;
import org.techtown.just.model.IsExist;
import org.techtown.just.model.LoginResult;
import org.techtown.just.model.Tag;
import org.techtown.just.model.UserSelectedTags;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookApiService {
    /**
     * 모든 API에
     * accessToken, idToken, refreshToken
     * 필드를 추가해사 보내주세요!!
     *
     * Path는 {} Query는 ?=
     * Field = Body (in Postman)
     */

    //Tag List
    @GET("tags/")
    Call<List<Tag>> getTags();


    //Auth
    @FormUrlEncoded
    @POST("auth/login/")
    Call<LoginResult> login(@Field("id") String id,
                            @Field("pw") String pw);

    @POST("auth2/validate/")
    Call<JsonObject> validate();


    @FormUrlEncoded
    @POST("auth/register/")
    Call<JsonObject> register(@Field("id") String id,
                                @Field("pw") String pw,
                                @Field("email") String email);


    //Book
    @GET("books/listwithtag/{tags}/")
    Call<List<BookInfo_Added>> getListWithTag(@Path("tags") String tags);


    @GET("books/listwithsearch/{search}/")
    Call<BookInfoList_Added> getAddedListWithSearch(@Path("search") String search);

    @GET("books/listwithsearch/{search}/")
    Call<BookInfoList_NotAdded> getNotAddedListWithSearch(@Path("search") String search);

    @GET("books/listwithsearch/{search}/")
    Call<IsExist> getIsExist(@Path("search") String search);



    //user가 읽은 책 리스트
    @GET("books/read/{user_id}/")
    Call<List<BookInfo_Added>> getListUserRead(@Path("user_id") String user_id,
                                               @Header("accessToken") String accessToken,
                                               @Header("idToken") String idToken,
                                               @Header("refreshToken") String refreshToken);


    //user가 관심있는 책 리스트
    @GET("books/interest/{user_id}/")
    Call<List<BookInfo_Added>> getListUserInterested(@Path("user_id") String user_id,
                                                     @Header("accessToken") String accessToken,
                                                     @Header("idToken") String idToken,
                                                     @Header("refreshToken") String refreshToken);

    //isbn으로 책 검색
    @GET("books/{isbn}/")
    Call<List<BookInfo_Added>> getBookInfoWithIsbn(@Path("isbn") String isbn);



    //User
    //user가 고른 태그 출력
    @GET("user/selected/{user_id}/")
    Call<List<UserSelectedTags>> getUserSelectedTags(@Path("user_id") String user_id,
                                               @Header("accessToken") String accessToken,
                                               @Header("idToken") String idToken,
                                               @Header("refreshToken") String refreshToken);


    //user가 고른 태그 저장
    @FormUrlEncoded
    @POST("user/status/tag/{tags}/{user_id}/")
    Call<ResponseBody> setUserSelectedTags(@Path("tags") String tags,
                                           @Field("user_id") String user_id,
                                           @Header("accessToken") String accessToken,
                                           @Header("idToken") String idToken,
                                           @Header("refreshToken") String refreshToken);

    //user가 고른 태그 추가
    // put

    //user가 고른 태그 삭제
    @DELETE("user/delete/tag/{user_id}/")
    Call<ResponseBody> deleteUserSelectedTags(@Path("user_id") String user_id,
                                           @Header("accessToken") String accessToken,
                                           @Header("idToken") String idToken,
                                           @Header("refreshToken") String refreshToken);

    //user가 ISBN값의 책과 관련된 flag값들(읽었어요/좋아요) 출력
    @GET("user/status/book/{isbn}/{user_id}/")
    Call<List<BookFlag>> getBookFlags(@Path("isbn") String isbn,
                                      @Path("user_id") String user_id,
                                      @Header("accessToken") String accessToken,
                                      @Header("idToken") String idToken,
                                      @Header("refreshToken") String refreshToken);

    //user에 ISBN값의 책에 flag(읽었어요/좋아요) 정보 저장 (최초1회)
    @FormUrlEncoded
    @POST("user/status/book/{isbn}/{user_id}/")
    Call<ResponseBody> postBookFlags(@Path("isbn") String isbn,
                                @Path("user_id") String user_id,
                                @Field("flag_i") int flag_i,
                                @Field("flag_r") int flag_r,
                                @Header("accessToken") String accessToken,
                                @Header("idToken") String idToken,
                                @Header("refreshToken") String refreshToken);


    //user에 ISBN값의 책에 flag(읽었어요/좋아요) 정보 update (최초1회 제외)
    @FormUrlEncoded
    @PUT("user/status/book/{isbn}/{user_id}/")
    Call<ResponseBody> putBookFlags(@Path("isbn") String isbn,
                                     @Path("user_id") String user_id,
                                     @Field("flag_i") int flag_i,
                                     @Field("flag_r") int flag_r,
                                     @Header("accessToken") String accessToken,
                                     @Header("idToken") String idToken,
                                     @Header("refreshToken") String refreshToken);

    @GET("user/recommend/{user_id}")
    Call<List<BookInfo_Added>> getUserRecommendBookList(@Path("user_id") String user_id,
                                                        @Header("accessToken") String accessToken,
                                                        @Header("idToken") String idToken,
                                                        @Header("refreshToken") String refreshToken);


//    사. /book/:user_id : 유저에 등록된 책리스트 출력
//    get
//    return : [{"isbn":"9788937460753","tags":";4;7;6;29;23;30;20;","book_name":"위대한 개츠비","author":"스콧 피츠제럴드","country":"영미소설"}]

}
