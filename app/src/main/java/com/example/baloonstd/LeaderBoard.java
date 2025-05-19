package com.example.baloonstd;

import android.os.Bundle;
import com.example.baloonstd.Player.Player;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.view.WindowCompat;
import androidx.activity.EdgeToEdge;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.baloonstd.Player.PlayerManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class LeaderBoard extends BaseActivity {
    private static final String LEADERBOARD_URL = "https://studev.groept.be/api/a24pt301/leaderBoard";
    private TextView tvUsername, tvBalloonsPopped, tvGamesPlayed;
    private RecyclerView rvLeaderboard;
    private LeaderBoardAdapter adapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);

        setContentView(R.layout.leaderboard_activity);


        tvUsername = findViewById(R.id.tvUsername);
        tvBalloonsPopped = findViewById(R.id.tvBalloonsPopped);
        tvGamesPlayed = findViewById(R.id.tvGamesPlayed);
        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        // set up RecyclerView with empty adapter
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeaderBoardAdapter(new ArrayList<>());
        rvLeaderboard.setAdapter(adapter);

        Player currentPlayer = PlayerManager.getInstance().getPlayer();
        if (currentPlayer != null) {
            tvUsername.setText("Username: "+currentPlayer.getUsername());
            tvBalloonsPopped.setText("Balloons Popped: "+currentPlayer.getBalloonsPopped());
            tvGamesPlayed.setText("Games Played: "+currentPlayer.getGamesPlayed());
        }

        fetchLeaderBoard();
    }

    private void fetchLeaderBoard() {
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            LEADERBOARD_URL,
            null,
            response -> {
                List<Player> players = new ArrayList<>();
                // Parse each entry in the returned JSON array
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String username = jsonObject.getString("username");
                        int balloonsPopped = jsonObject.getInt("balloonsPopped");
                        int gamesPlayed = jsonObject.optInt("gamesPlayed", 0);
                        players.add(new Player(username, balloonsPopped, gamesPlayed));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Bind data to RecyclerView
                adapter = new LeaderBoardAdapter(players);
                rvLeaderboard.setAdapter(adapter);

            },
            error -> Toast.makeText(
                LeaderBoard.this,
                "Error fetching leaderboard: " + error.getMessage(),
                Toast.LENGTH_LONG
            ).show()
        );

        requestQueue.add(jsonArrayRequest);
    }

}
