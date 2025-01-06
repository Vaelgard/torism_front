package com.mobile.tourism;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.tourism.adapters.PlaceAdapter;
import com.mobile.tourism.api.ApiClient;
import com.mobile.tourism.api.ApiService;
import com.mobile.tourism.models.Place;
import com.mobile.tourism.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavorisActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApiService apiService;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            handleMissingToken();
        } else {
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
            Intent homeIntent = new Intent(FavorisActivity.this, MainActivity.class);
            startActivity(homeIntent);
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            Intent homeIntent = new Intent(FavorisActivity.this, FavorisActivity.class);
            startActivity(homeIntent);
            return true;
        }else if (item.getItemId() == R.id.action_logout) {
            sessionManager.clearToken();
            redirectToLogin();
            return true;
        }else if (item.getItemId()==R.id.action_profile){
            Intent profileIntent = new Intent(FavorisActivity.this, ProfilActivity.class);
            startActivity(profileIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPlaces() {
        String userEmail= sessionManager.getUserId();
        apiService.getAllFavoritePlaces(sessionManager.getToken(),userEmail).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlaceAdapter adapter = new PlaceAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(FavorisActivity.this, "Failed to load places", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Toast.makeText(FavorisActivity.this, "Error loading places", Toast.LENGTH_SHORT).show();
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