package com.mobile.tourism;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobile.tourism.api.ApiClient;
import com.mobile.tourism.api.ApiService;
import com.mobile.tourism.models.ourUsers;
import com.mobile.tourism.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonUpdate;

    private ourUsers currentUser; // This will hold the user data
    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Initialize sessionManager and apiService
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // Load user data asynchronously
        loadUser();

        // Handle update button click
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
    }

    // Update user profile
    private void updateUserProfile() {
        String updatedName = editTextName.getText().toString();
        String updatedEmail = editTextEmail.getText().toString();
        String updatedPassword = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if passwords match
        if (!updatedPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the updated user object
        ourUsers updatedUser = new ourUsers();
        updatedUser.setName(updatedName);
        updatedUser.setEmail(updatedEmail);
        updatedUser.setPassword(updatedPassword);
        updatedUser.setId(currentUser.getId());  // Keep the same user ID

        // Get token from session manager
        String token = "Bearer " + sessionManager.getToken();

        // Make API call to update the user
        Call<ourUsers> call = apiService.updateUser(currentUser.getId(), updatedUser, token);
        call.enqueue(new Callback<ourUsers>() {
            @Override
            public void onResponse(Call<ourUsers> call, Response<ourUsers> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfilActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    sessionManager.clearToken();
                    redirectToLogin();
                } else {
                    Toast.makeText(ProfilActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ourUsers> call, Throwable t) {
                Toast.makeText(ProfilActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Load user data from the API and update the currentUser
    private void loadUser() {
        String token = "Bearer " + sessionManager.getToken();
        apiService.getUserByEmail(token, sessionManager.getUserId()).enqueue(new Callback<ourUsers>() {
            @Override
            public void onResponse(Call<ourUsers> call, Response<ourUsers> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    // Update UI here
                    editTextName.setText(currentUser.getName());
                    editTextEmail.setText(currentUser.getEmail());
                } else {
                    Toast.makeText(ProfilActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ourUsers> call, Throwable t) {
                Toast.makeText(ProfilActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
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
            Intent homeIntent = new Intent(ProfilActivity.this, MainActivity.class);
            startActivity(homeIntent);
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            Intent favoritesIntent = new Intent(ProfilActivity.this, FavorisActivity.class);
            startActivity(favoritesIntent);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            sessionManager.clearToken();
            redirectToLogin();
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            Intent profileIntent = new Intent(ProfilActivity.this, ProfilActivity.class);
            startActivity(profileIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
