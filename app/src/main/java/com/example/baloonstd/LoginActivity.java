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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        if (username != null) {
            // Auto-login
            Player player = new Player(username,balloonsPopped2,towersPlaced2);
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

            String url = "https://studev.groept.be/api/a24pt301/Login/"+enteredUsername+"/"+password;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int playerId = jsonObject.getInt("idPlayer");
                        int balloonsPopped = jsonObject.getInt("balloonsPopped");
                        int towersPlaced = jsonObject.getInt("towersPlaced");
                        SharedPreferences prefs1 = getSharedPreferences("player_session", MODE_PRIVATE);
                        prefs1.edit()
                                .putString("username", enteredUsername)
                                .putInt("playerId", playerId)
                                .putInt("balloonsPopped", balloonsPopped)
                                .putInt("towersPlaced",towersPlaced)
                                .apply();

                        Player player = new Player(enteredUsername,balloonsPopped,towersPlaced);
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
    public void goToMainMenu(View v)
    {Intent i = new Intent(LoginActivity.this,MainActivity.class);
    startActivity(i);}


}
