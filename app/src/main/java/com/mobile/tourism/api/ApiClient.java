package com.mobile.tourism.api;

import android.content.Context;
import com.mobile.tourism.utils.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8081/"; // Update with your backend URL
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        // Get the token from SessionManager
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getAuthorizationHeader();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Build the OkHttpClient with the Authorization header if the token is available
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (token != null) {
            httpClientBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", token)  // Add the Bearer token
                        .build();
                return chain.proceed(request);
            });
        }

        OkHttpClient client = httpClientBuilder.addInterceptor(interceptor).build();

        // Build the Retrofit client
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
