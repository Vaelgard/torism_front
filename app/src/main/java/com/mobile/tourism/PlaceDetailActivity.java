package com.mobile.tourism;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PlaceDetailActivity extends AppCompatActivity {
    private TextView placeIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // Get the Place ID passed via Intent
        String placeName = getIntent().getStringExtra("placeName");

        placeIdTextView = findViewById(R.id.placeIdTextView);
        placeIdTextView.setText("Place Name: " + placeName);  // Display the Place ID or fetch details
    }
}
