package com.mobile.tourism;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.tourism.adapters.CommentsAdapter;
import com.mobile.tourism.api.ApiClient;
import com.mobile.tourism.api.ApiService;
import com.mobile.tourism.models.Comment;
import com.mobile.tourism.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private ImageView placeImageView; // ImageView for the place
    private EditText commentEditText; // EditText for adding comments
    private Button submitCommentButton; // Button to submit the comment
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private ApiService apiService;
    private Integer placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Check if the user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }


        // Initialize the views
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        locationTextView = findViewById(R.id.locationTextView);
        placeImageView = findViewById(R.id.placeImageView);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);

        // Retrieve data from Intent
        placeId = getIntent().getIntExtra("placeId",-1);
        if (placeId==-1){
            System.out.println("null value");
        }else {
            System.out.println("value="+placeId);
        }
        String placeName = getIntent().getStringExtra("placeName");
        String placeDescription = getIntent().getStringExtra("placeDescription");
        String placeLocation = getIntent().getStringExtra("placeLocation");
        String placeImageUrl = getIntent().getStringExtra("placeImageUrl");
        System.out.println("place id =");
        //load comment
        recyclerView = findViewById(R.id.commentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = ApiClient.getClient(this).create(ApiService.class);
        loadComments();

        // Set data to TextViews
        nameTextView.setText("Place Name: " + (placeName != null ? placeName : "N/A"));
        descriptionTextView.setText("Description: " + (placeDescription != null ? placeDescription : "N/A"));
        locationTextView.setText("Location: " + (placeLocation != null ? placeLocation : "N/A"));

        // Load the image into ImageView using Glide
        if (placeImageUrl != null && !placeImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(placeImageUrl)
                    .into(placeImageView);
        }

        // Set click listener for the submit comment button
        submitCommentButton.setOnClickListener(v -> addComment());
    }

    private void addComment() {
        String commentText = commentEditText.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Please write a comment before submitting", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create comment object
        Comment comment = new Comment();
        comment.setText(commentText);
        comment.setPlaceId(placeId);
        comment.setEmail(sessionManager.getUserId());
        System.out.println(sessionManager.getUserId());

        // Get the API token
        String token = "Bearer " + sessionManager.getToken();

        // Make API call
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<Comment> call = apiService.addComment(token, comment);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("tesstt"+placeId);
                    Toast.makeText(PlaceDetailActivity.this, "Comment added successfully!", Toast.LENGTH_SHORT).show();
                    commentEditText.setText(""); // Clear the comment field
                } else {
                    System.out.println("hi1");
                    Toast.makeText(PlaceDetailActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(PlaceDetailActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments() {
        apiService.getAllcomments(sessionManager.getToken(), placeId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("test1");
                    List<Comment> comments = response.body();
                    CommentsAdapter adapter = new CommentsAdapter(comments);
                    recyclerView.setAdapter(adapter);
                } else {
                    System.out.println("test2");
                    Toast.makeText(PlaceDetailActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                System.out.println("test3");
                Toast.makeText(PlaceDetailActivity.this, "Error loading comments", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Intent homeIntent = new Intent(PlaceDetailActivity.this, MainActivity.class);
            startActivity(homeIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
