package com.example.baloonstd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.ColorInt;
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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (ranks.get(position) == 1) {
            holder.tvItemRank.setTextColor(Color.parseColor("#FFD700"));
        } else if (ranks.get(position) == 2) {
            holder.tvItemRank.setTextColor(Color.parseColor("#C0C0C0"));
        } else if (ranks.get(position) == 3) {
            holder.tvItemRank.setTextColor(Color.parseColor("#CD7F32"));
        } else {
            holder.tvItemRank.setTextColor(Color.BLACK);
        }
        Player p = players.get(position);
        holder.tvUsername.setText("          "+p.getUsername());
        holder.tvItemBalloons.setText(String.valueOf(p.getBalloonsPopped()));
        holder.tvItemGamesPlayed.setText(String.valueOf(p.getGamesPlayed()));
        holder.tvItemRank.setText("     "+ranks.get(position));
    }


    @Override
    public int getItemCount() {
        return players.size();
    }


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
