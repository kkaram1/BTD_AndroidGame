package com.example.baloonstd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.baloonstd.Player.Player;
import com.example.baloonstd.Player.PlayerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        int balloonsPopped2 = prefs.getInt("balloonsPopped",0);
        int towersPlaced2 = prefs.getInt("towersPlaced",0);
        boolean guest2 = prefs.getBoolean("guest",true);
        int gamesPlayed2 = prefs.getInt("gamesPlayed",0);
        if (username != null) {
            // Auto-login
            Player player = new Player(username,balloonsPopped2,towersPlaced2,guest2,gamesPlayed2);
            PlayerManager.getInstance().setPlayer(player);
            startActivity(new Intent(this, MainActivity.class));
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
            String url = "https://studev.groept.be/api/a24pt301/Login/" + enteredUsername + "/" + hashPassword(password);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int playerId = jsonObject.getInt("idPlayer");
                        int balloonsPopped = jsonObject.getInt("balloonsPopped");
                        int towersPlaced = jsonObject.getInt("towersPlaced");
                        int gamesPlayed = jsonObject.getInt("gamesPlayed");
                        SharedPreferences prefs1 = getSharedPreferences("player_session", MODE_PRIVATE);
                        prefs1.edit()
                                .putString("username", enteredUsername)
                                .putInt("playerId", playerId)
                                .putInt("balloonsPopped", balloonsPopped)
                                .putInt("towersPlaced",towersPlaced)
                                .putInt("gamesPlayed",gamesPlayed)
                                .putBoolean("guest",false)
                                .apply();

                        Player player = new Player(enteredUsername,balloonsPopped,towersPlaced,false,gamesPlayed);
                        PlayerManager.getInstance().setPlayer(player);

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    } catch (JSONException e) {
                        Toast.makeText(this, "Login failed: wrong username/password", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show()
            ) {

            };

            Volley.newRequestQueue(this).add(stringRequest);
        });
    }
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such hashing algorithm");
        }
    }
    public void goToMainMenu(View v)
    {
        SharedPreferences prefs1 = getSharedPreferences("player_session", MODE_PRIVATE);
        prefs1.edit().putString("username", "guest").apply();
        Player player = new Player("guest",0,0,true,0);
        PlayerManager.getInstance().setPlayer(player);
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
    startActivity(i);

    }


}
