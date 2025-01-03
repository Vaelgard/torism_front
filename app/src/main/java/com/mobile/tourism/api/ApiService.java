package com.mobile.tourism.api;

import com.mobile.tourism.models.AuthResponse;
import com.mobile.tourism.models.LoginRequest;
import com.mobile.tourism.models.Place;
import com.mobile.tourism.models.Comment;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("api/places")
    Call<List<Place>> getAllPlaces(@Header("Authorization") String token);

    @POST("api/comments")
    Call<Comment> addComment(@Header("Authorization") String token, @Body Comment comment);
    @GET("api/comments/{id}")
    Call<List<Comment>> getAllcomments(@Header("Authorization") String token, @Path("id") Integer placeId);

    @POST("comments/upvote/{id}")
    Call<Comment> upvoteComment(@Header("Authorization") String token, @Path("id") Long id);

    @POST("comments/downvote/{id}")
    Call<Comment> downvoteComment(@Header("Authorization") String token, @Path("id") Long id);
}
