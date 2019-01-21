package org.techtown.just.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApiService {
    /**
     * 모든 API에
     * accessToken, idToken, refreshToken
     * 필드를 추가해사 보내주세요!!
     */

    @FormUrlEncoded
    @GET("tags")
    Call<ResponseBody> getTags(@Field("accessToken") String accessToken,
                               @Field("idToken") String idToken,
                               @Field("refreshToken") String refreshToken);
}
