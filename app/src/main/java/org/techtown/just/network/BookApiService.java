package org.techtown.just.network;

import com.google.gson.JsonObject;

import org.techtown.just.model.BookInfo;
import org.techtown.just.model.BookInfoWithBool;
import org.techtown.just.model.LoginResult;
import org.techtown.just.model.LoginToken;
import org.techtown.just.model.Post;
import org.techtown.just.model.Status;
import org.techtown.just.model.Tag;
import org.techtown.just.model.UserSelectedTags;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApiService {
    /**
     * 모든 API에
     * accessToken, idToken, refreshToken
     * 필드를 추가해사 보내주세요!!
     */

    //1
    @GET("tags/")
    Call<List<Tag>> getTags(@Header("accessToken") String accessToken,
                            @Header("idToken") String idToken,
                            @Header("refreshToken") String refreshToken);


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



    //////////////////////////////////////////////////////////////////////////////////////////

    //Path는 {} Query는 ?=

//    2. Book(/books)
//    가. /listwithtag/:tags : 태그가 포함된 책 리스트
//    get
//return: [{"isbn":"9787501220779","tags":";11;1;","book_name":"상도","author":"최인호","country":"국내소설"}]
    @GET("books/listwithtag/{tags}/")
    Call<List<BookInfo>> getListWithTag(@Path("tags") String tags);

//    나. /listwithsearch/:search : 책이름으로 책 검색
//            get
//return : [{"isbn":"9788937460753","tags":";4;7;6;29;23;30;20;","book_name":"위대한 개츠비","author":"스콧 피츠제럴드","country":"영미소설","contents":"20세기의 가장 뛰어난 미국 소설로 꼽히는 스콧 피츠제럴드의 작품 『위대한 개츠비』. 1991년 영국 케임브리지 대학교 출판부에서 출간한 ‘결정판’ 텍스트를 바탕으로 완역한 책이다. ‘재즈의 시대’였던 1920년대 미국을 배경으로 무너져 가는 아메리칸 드림을 예리한 필치로 그려냈다. 이 작품은 2013년 레오나르도 디카프리오 주연의 영화로 개봉되며 다시 한 번 뜨거운 관심을 받고 있는데, 3D로 제작된 영화는 제66회 칸 국제영화제 개막작으로 선정되기","thumbnail":"https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F540841%3Ftimestamp%3D20190123173033"}]
    @GET("books/listwithsearch/{search}/")
    Call<BookInfoWithBool> getListWithSearch(@Path("search") String search);

//    다. /read/:user_id : 사용자가 읽은 책 리스트
//    get
//return : [{"isbn":"9788937460753","tags":";4;7;6;29;23;30;20;","book_name":"위대한 개츠비","author":"스콧 피츠제럴드","country":"영미소설"}]
    @GET("books/read/{user_id}/")
    Call<List<BookInfo>> getListUserRead(@Path("user_id") String user_id,
                                         @Header("accessToken") String accessToken,
                                         @Header("idToken") String idToken,
                                         @Header("refreshToken") String refreshToken);

//    라. /interest/:user_id : 사용자가 관심있는 책 리스트
//    get
//return : [{"isbn":"9788937460753","tags":";4;7;6;29;23;30;20;","book_name":"위대한 개츠비","author":"스콧 피츠제럴드","country":"영미소설"}]
    @GET("books/interest/{user_id}/")
    Call<List<BookInfo>> getListUserInterested(@Path("user_id") String user_id,
                                               @Header("accessToken") String accessToken,
                                               @Header("idToken") String idToken,
                                               @Header("refreshToken") String refreshToken);


//    마. /:isbn : ISBN으로 책 검색
//            get
    @GET("books/{isbn}/")
    Call<List<BookInfo>> getBookInfoWithIsbn(@Path("isbn") String isbn);



    //////////////////////////////////////////////////////////////////////////////////////////////////


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
    @POST("user/status/tag/{tags}/{user_id}/")
    Call<ResponseBody> setUserSelectedTags(@Path("tags") String tags,
                                           @Field("user_id") String user_id,
                                           @Header("accessToken") String accessToken,
                                           @Header("idToken") String idToken,
                                           @Header("refreshToken") String refreshToken);

//    다. /status/tag/:tag/:user_id : 유저가 고른 태그 추가
//    put

//    라. /delete/tag/:user_id : 유저가 고른 태그 삭제
//    delete
    @DELETE("user/delete/tag/{user_id}/")
    Call<ResponseBody> deleteUserSelectedTags(@Path("user_id") String user_id,
                                           @Header("accessToken") String accessToken,
                                           @Header("idToken") String idToken,
                                           @Header("refreshToken") String refreshToken);

//    마. /status/book/:isbn/:user_id : 유저가 ISBN값의 책과 관련된 flag값들(읽었어요/좋아요) 출력
//    get
//    return : [{"had_read":1,"be_interested":1}]
    @GET("user/status/book/{isbn}/{user_id}/")
    Call<List<Status>> getBookFlags(@Path("isbn") String isbn,
                                    @Path("user_id") String user_id,
                                    @Header("accessToken") String accessToken,
                                    @Header("idToken") String idToken,
                                    @Header("refreshToken") String refreshToken);

//    바. /status/book/:isbn/:user_id : 유저에 ISBN값의 책에 flag(읽었어요/좋아요) 정보 저장 (최초1회)
//    post
    @FormUrlEncoded
    @POST("user/status/book/{isbn}/{user_id}/")
    Call<ResponseBody> postBookFlags(@Path("isbn") String isbn,
                                @Path("user_id") String user_id,
                                @Field("flag_i") int flag_i,
                                @Field("flag_r") int flag_r,
                                @Header("accessToken") String accessToken,
                                @Header("idToken") String idToken,
                                @Header("refreshToken") String refreshToken);


    //바-2. /status/book/:isbn/:user_id : 유저에 ISBN값의 책에 flag(읽었어요/좋아요) 정보 update (최초1회 제외)
    //put
    @FormUrlEncoded
    @PUT("user/status/book/{isbn}/{user_id}/")
    Call<ResponseBody> putBookFlags(@Path("isbn") String isbn,
                                     @Path("user_id") String user_id,
                                     @Field("flag_i") int flag_i,
                                     @Field("flag_r") int flag_r,
                                     @Header("accessToken") String accessToken,
                                     @Header("idToken") String idToken,
                                     @Header("refreshToken") String refreshToken);



//    사. /book/:user_id : 유저에 등록된 책리스트 출력
//    get
//    return : [{"isbn":"9788937460753","tags":";4;7;6;29;23;30;20;","book_name":"위대한 개츠비","author":"스콧 피츠제럴드","country":"영미소설"}]

}
