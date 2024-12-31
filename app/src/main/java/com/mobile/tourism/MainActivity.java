package com.mobile.tourism;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mobile.tourism.api.ApiClient;
import com.mobile.tourism.api.ApiService;
import com.mobile.tourism.models.Place;
import com.mobile.tourism.utils.SessionManager;
import com.mobile.tourism.adapters.PlaceAdapter;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        // Check if the user is logged in
        if (!sessionManager.isLoggedIn()) {
            // If not logged in, redirect to the Login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the token from the SessionManager
        String token = sessionManager.getToken();

        // Ensure that the token is not empty or null
        if (token != null && !token.isEmpty()) {
            // Pass the token to ApiClient to create the ApiService instance
            apiService = ApiClient.getClient(this).create(ApiService.class);
        } else {
            // Handle the case where the token is missing, e.g., redirect to login
            Toast.makeText(this, "Token is missing. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Call the method to load places
        loadPlaces();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            sessionManager.clearToken();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPlaces() {
        apiService.getAllPlaces(sessionManager.getToken())
                .enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                System.out.println("hijjdjjd");
                System.out.println(response.body());
                if (response.isSuccessful() && response.body() != null) {


                    System.out.println(sessionManager.getToken());
                    placeAdapter = new PlaceAdapter(response.body());
                    recyclerView.setAdapter(placeAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error loading places", Toast.LENGTH_SHORT).show();
            }
        });
    }
}