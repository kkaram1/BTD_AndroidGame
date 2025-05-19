package com.example.baloonstd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baloonstd.Player.Player;
import java.util.List;

public class LeaderBoardAdapter
        extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private final List<Player> players;
    private List<Integer> ranks;

    public LeaderBoardAdapter(List<Integer> ranks,List<Player> players) {
        this.players = players;
        this.ranks = ranks;
    }

    // 1) Inflate item layout and create ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(itemView);
    }

    // 2) Bind data to each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player p = players.get(position);
        holder.tvUsername.setText("          "+p.getUsername());
        holder.tvItemBalloons.setText(String.valueOf(p.getBalloonsPopped()));
        holder.tvItemGamesPlayed.setText(String.valueOf(p.getGamesPlayed()));
        holder.tvItemRank.setText("     "+ranks.get(position));
    }

    // 3) Report data size
    @Override
    public int getItemCount() {
        return players.size();
    }

    // ViewHolder holds references to each view in a row
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvUsername, tvItemBalloons, tvItemGamesPlayed,tvItemRank;

        ViewHolder(View itemView) {
            super(itemView);
            tvUsername         = itemView.findViewById(R.id.tvItemUsername);
            tvItemBalloons     = itemView.findViewById(R.id.tvItemBalloons);
            tvItemGamesPlayed  = itemView.findViewById(R.id.tvItemGamesPlayed);
            tvItemRank         =itemView.findViewById(R.id.tvItemRank);
        }
    }
}
