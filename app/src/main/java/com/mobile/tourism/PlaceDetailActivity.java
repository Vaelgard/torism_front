package com.mobile.tourism;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class PlaceDetailActivity extends AppCompatActivity {

    private TextView idTextView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private ImageView placeImageView; // New ImageView
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // Initialize the views
        idTextView = findViewById(R.id.idTextView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        locationTextView = findViewById(R.id.locationTextView);
        placeImageView = findViewById(R.id.placeImageView); // Initialize the ImageView
        backButton = findViewById(R.id.backButton);

        // Retrieve data from Intent
        long placeId = getIntent().getLongExtra("placeId", -1);
        String placeName = getIntent().getStringExtra("placeName");
        String placeDescription = getIntent().getStringExtra("placeDescription");
        String placeLocation = getIntent().getStringExtra("placeLocation");
        String placeImageUrl = getIntent().getStringExtra("placeImageUrl"); // Retrieve image URL

        // Set data to TextViews
        idTextView.setText("Place ID: " + placeId);
        nameTextView.setText("Place Name: " + (placeName != null ? placeName : "N/A"));
        descriptionTextView.setText("Description: " + (placeDescription != null ? placeDescription : "N/A"));
        locationTextView.setText("Location: " + (placeLocation != null ? placeLocation : "N/A"));

        // Load the image into ImageView using Glide
        if (placeImageUrl != null && !placeImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(placeImageUrl)
                    .into(placeImageView);// Default image
        }

        // Set a click listener for the back button
        backButton.setOnClickListener(v -> finish());
    }
}
