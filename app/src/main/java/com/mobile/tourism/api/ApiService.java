package com.mobile.tourism.api;

import com.mobile.tourism.models.AuthResponse;
import com.mobile.tourism.models.LoginRequest;
import com.mobile.tourism.models.Place;
import com.mobile.tourism.models.Comment;
import com.mobile.tourism.models.RegisterRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);
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
    @GET("/api/places/upvote/{id}")
    Call<Void> upvotePlace(@Header("Authorization") String token, @Path("id") Integer id);

    @GET("/api/places/downvote/{id}")
    Call<Void> downvotePlace(@Header("Authorization") String token, @Path("id") Integer id);

    @GET("/api/places/{id}")
    Call<Place> getPlaceDetails(@Header("Authorization") String token, @Path("id") Integer id);

    @POST("api/places/favorite")
    Call<Void> addToFavorites(
            @Header("Authorization") String token,
            @Query("placeId") Integer placeId,
            @Query("userEmail") String userEmail
    );
    @GET("api/places/favorite/{userEmail}")
    Call<List<Place>> getAllFavoritePlaces(@Header("Authorization") String token,@Path("userEmail") String userEmail);
}
