package com.example.baloonstd;

import android.os.Bundle;

import com.example.baloonstd.Achievements.AchievementManager;
import com.example.baloonstd.Achievements.AchievementRepository;
import com.example.baloonstd.Player.Player;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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
import com.example.baloonstd.Achievements.AchievementAdapter;


public class LeaderBoard extends BaseActivity {
    private static final String LEADERBOARD_URL = "https://studev.groept.be/api/a24pt301/leaderBoard";
    private TextView tvUsername, tvBalloonsPopped, tvGamesPlayed,achievementsText;
    private RecyclerView rvLeaderboard;
    private LeaderBoardAdapter adapter;
    private RequestQueue requestQueue;
    private AchievementManager achievementManager;
    private RecyclerView rvAchievements;
    private AchievementAdapter achievementAdapter;
    private ImageButton btnCloseAchievements;
    private Button btnOpenAchievements;
    private CardView layoutAchievementsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);

        setContentView(R.layout.leaderboard_activity);

        AchievementManager.init(this);
        achievementManager = AchievementManager.get();

        tvUsername = findViewById(R.id.tvUsername);
        tvBalloonsPopped = findViewById(R.id.tvBalloonsPopped);
        tvGamesPlayed = findViewById(R.id.tvGamesPlayed);
        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        achievementsText = findViewById(R.id.achievementsNum);
        btnCloseAchievements = findViewById(R.id.btnCloseAchievements);
        rvAchievements = findViewById(R.id.rvAchievementsList);
        btnOpenAchievements = findViewById(R.id.achievementsBtn);
        layoutAchievementsList = findViewById(R.id.layoutAchievementsList);

        rvAchievements.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        achievementAdapter = new AchievementAdapter(AchievementRepository.getAll());
        rvAchievements.setAdapter(achievementAdapter);


        btnCloseAchievements.setOnClickListener(v -> {
            rvAchievements.setVisibility(View.GONE);
            btnCloseAchievements.setVisibility(View.GONE);
            layoutAchievementsList.setVisibility(View.GONE);
        });
        btnOpenAchievements.setOnClickListener(v -> {
            rvAchievements.setVisibility(View.VISIBLE);
            btnCloseAchievements.setVisibility(View.VISIBLE);
            layoutAchievementsList.setVisibility(View.VISIBLE);
        });

        // set up RecyclerView with empty adapter
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeaderBoardAdapter(new ArrayList<>(),new ArrayList<>());
        rvLeaderboard.setAdapter(adapter);

        Player currentPlayer = PlayerManager.getInstance().getPlayer();
        if (currentPlayer != null) {
            tvUsername.setText("Username: "+currentPlayer.getUsername());
            tvBalloonsPopped.setText("Balloons Popped: "+currentPlayer.getBalloonsPopped());
            tvGamesPlayed.setText("Games Played: "+currentPlayer.getGamesPlayed());
            achievementsText.setText("Achievements Unlocked: " + achievementManager.getNumAchievements() + "/16");
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
                List<Integer> ranks = new ArrayList<>();
                // Parse each entry in the returned JSON array
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String username = jsonObject.getString("username");
                        int balloonsPopped = jsonObject.getInt("balloonsPopped");
                        int gamesPlayed = jsonObject.optInt("gamesPlayed", 0);
                        players.add(new Player(username, balloonsPopped, gamesPlayed));
                        ranks.add(i+1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Bind data to RecyclerView
                adapter = new LeaderBoardAdapter(ranks,players);
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
