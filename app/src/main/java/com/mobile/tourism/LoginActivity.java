package com.mobile.tourism;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.mobile.tourism.api.ApiClient;
import com.mobile.tourism.api.ApiService;
import com.mobile.tourism.models.AuthResponse;
import com.mobile.tourism.models.LoginRequest;
import com.mobile.tourism.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        sessionManager = new SessionManager(this);

        // Initialize ApiService with the current token (if any)
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // If already logged in, go to MainActivity
        if (sessionManager.isLoggedIn()) {
            startMainActivity();
        }

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate the input fields
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perform login
                performLogin(email, password);
            }
        });
    }

    private void performLogin(String email, String password) {
        // Create the login request object
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Make the login API request
        apiService.login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save the token in session and proceed to MainActivity
                    sessionManager.saveToken(response.body().getToken());

                    // Reinitialize the ApiService with the new token
                    apiService = ApiClient.getClient(LoginActivity.this).create(ApiService.class);

                    startMainActivity();
                } else {
                    // Show error message if login fails
                    String errorMessage = "Login failed";
                    if (response.errorBody() != null) {
                        // If the API provides an error body, display it
                        errorMessage = response.message();
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Show error message in case of failure
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMainActivity() {
        // Start MainActivity and clear the back stack
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
