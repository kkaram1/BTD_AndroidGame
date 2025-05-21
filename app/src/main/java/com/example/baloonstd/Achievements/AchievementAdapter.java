package com.example.baloonstd.Achievements;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.baloonstd.Player.PlayerManager;
import com.example.baloonstd.R;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private List<Achievements> achievements;

    public AchievementAdapter(List<Achievements> achievements) {
        this.achievements = achievements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Achievements achievement = achievements.get(position);
        if(achievement.shouldUnlock(PlayerManager.getInstance().getPlayer()))
        { holder.tvName.setTextColor(Color.parseColor("#FFD700"));}
        holder.tvName.setText(achievement.getName());
        holder.tvDescription.setText(achievement.getDescription());
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvAchievementName);
            tvDescription = itemView.findViewById(R.id.tvAchievementDescription);
        }
    }
}
