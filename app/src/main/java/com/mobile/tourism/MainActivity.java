package com.mobile.tourism;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.mobile.tourism.adapters.PlaceAdapter;
import com.mobile.tourism.api.ApiClient;
import com.mobile.tourism.api.ApiService;
import com.mobile.tourism.models.Place;
import com.mobile.tourism.utils.SessionManager;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Check if the user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the token and validate it
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            handleMissingToken();
        } else {
            // Initialize the ApiService and load places
            apiService = ApiClient.getClient(this).create(ApiService.class);
            loadPlaces();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Use if-else to handle the item selection
        if (item.getItemId() == R.id.action_home) {
            // If Home button is clicked, return to MainActivity
            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(homeIntent);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            sessionManager.clearToken();
            redirectToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPlaces() {
        apiService.getAllPlaces(sessionManager.getToken()).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlaceAdapter adapter = new PlaceAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load places", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error loading places", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void handleMissingToken() {
        Toast.makeText(this, "Token is missing. Please log in again.", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }
}
