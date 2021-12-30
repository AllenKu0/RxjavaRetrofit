package com.example.rxjavaretrofit;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyAPIService {
    @GET("posts/1")
    Observable<Post> getPosts();

    @POST("posts")
    Observable<Response<Post>> createPost(@Body Post post); //用Body表示要傳送Body的資料
}
