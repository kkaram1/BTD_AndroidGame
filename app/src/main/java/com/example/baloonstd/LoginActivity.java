package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.editTextUsername);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (!username.isEmpty()) {
                // Save locally and go to game
                PlayerManager.getInstance().setPlayer(new Player(username));
                Intent intent = new Intent(this, gameActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
