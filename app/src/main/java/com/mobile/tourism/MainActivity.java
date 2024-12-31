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

public class MainActivity extends AppCompatActivity implements PlaceAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SessionManager to manage login state
        sessionManager = new SessionManager(this);

        // Check if the user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the token from the SessionManager
        String token = sessionManager.getToken();

        // Ensure that the token is valid
        if (token == null || token.isEmpty()) {
            handleMissingToken();
        } else {
            // Initialize the ApiService to make network requests
            apiService = ApiClient.getClient(this).create(ApiService.class);
            loadPlaces();  // Load the places from the API
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);  // Inflate the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            sessionManager.clearToken();  // Clear the token on logout
            redirectToLogin();  // Redirect to login screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPlaces() {
        // Fetch the list of places from the server
        apiService.getAllPlaces(sessionManager.getToken())
                .enqueue(new Callback<List<Place>>() {
                    @Override
                    public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // If the request is successful, set up the adapter
                            placeAdapter = new PlaceAdapter(response.body(), MainActivity.this);
                            recyclerView.setAdapter(placeAdapter);
                        } else {
                            // Handle cases where the response is unsuccessful
                            Toast.makeText(MainActivity.this, "Failed to load places", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Place>> call, Throwable t) {
                        // Handle network failure
                        Toast.makeText(MainActivity.this, "Error loading places", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Handle the click event on a place item
    @Override
    public void onItemClick(Place place) {
        // Show a Toast or perform other actions upon clicking a place
        Toast.makeText(this, "Clicked: " + place.getName(), Toast.LENGTH_SHORT).show();

        // Navigate to PlaceDetailActivity and pass the place ID as an extra
        Intent intent = new Intent(MainActivity.this, PlaceDetailActivity.class);
        intent.putExtra("placeName", place.getName());  // Pass the Place ID
        startActivity(intent);  // Start the PlaceDetailActivity
    }


    // Redirect to the login screen
    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    // Handle the case where the token is missing
    private void handleMissingToken() {
        Toast.makeText(this, "Token is missing. Please log in again.", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }
}
