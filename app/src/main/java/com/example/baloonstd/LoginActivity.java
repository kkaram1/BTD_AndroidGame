package com.example.baloonstd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textRegister);

        SharedPreferences prefs = getSharedPreferences("player_session", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username != null) {
            // Auto-login
            PlayerManager.getInstance().setPlayer(new Player(username));
            startActivity(new Intent(this, GameActivity.class));
            finish();
        }

        textRegister.setOnClickListener(v ->{
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });
        loginButton.setOnClickListener(v -> {
            String enteredUsername = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (enteredUsername.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "https://yourserver.com/login.php"; // Replace with actual URL

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        SharedPreferences prefs1 = getSharedPreferences("player_session", MODE_PRIVATE);
                        prefs1.edit().putString("username", enteredUsername).apply();

                        PlayerManager.getInstance().setPlayer(new Player(enteredUsername));
                        startActivity(new Intent(this, GameActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Login failed: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", enteredUsername);
                    params.put("password", password);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);
        });
    }
    public void goToMainMenu(View v)
    {Intent i = new Intent(LoginActivity.this,MainActivity.class);
    startActivity(i);}

}
