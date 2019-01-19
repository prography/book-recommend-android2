package org.techtown.just;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    public static final String API_URL = "http://jsonplaceholder.typicode.com/";

//    @GET("posts")
//    Call<List<Post>> getPostList();

//    @GET("posts/{id}/")
//    Call<Post> getPost(@Path("id") int id);


//    @GET("comments/{id}/")
//    Call<Comment> getComment(@Path("id") int id);



    @GET("comments")
    Call<List<Comment>>getComments(@Query("postId") int postId);

    @POST("comments")
    Call<ResponseBody>getPostComment(@Query("postId") int postId);

    @GET("comments")
    Call<ResponseBody>getComment(@Query("name") String name);

    @FormUrlEncoded
    @POST("comments")
    Call<ResponseBody>getPostCommentStr(@Field("postId") int postId);

}
