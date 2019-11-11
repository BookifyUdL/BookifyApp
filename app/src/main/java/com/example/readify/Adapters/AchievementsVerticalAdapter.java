package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.readify.Models.Achievement;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.List;

public class AchievementsVerticalAdapter extends RecyclerView.Adapter<AchievementsVerticalAdapter.ViewHolder> {

    private List<Achievement> mData;
    private LayoutInflater mInflater;

    public AchievementsVerticalAdapter(Context context, List<Achievement> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void setAchivementsList(ArrayList<Achievement> data){
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.achievements_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement achievement = mData.get(position);

        holder.myTextViewTitle.setText(achievement.getTitle());
        holder.myTextViewDescription.setText(achievement.getDescription());
        holder.myImageView.setImageResource(mInflater.getContext()
                .getResources().getIdentifier(achievement.getImage(), "drawable", mInflater.getContext().getPackageName()));
        holder.numberProgressBar.setMax(achievement.getTotalValue());
        holder.numberProgressBar.setProgress(achievement.getProgressValue());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextViewTitle;
        TextView myTextViewDescription;
        ImageView myImageView;
        NumberProgressBar numberProgressBar;

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewTitle = itemView.findViewById(R.id.achievement_title);
            myTextViewDescription = itemView.findViewById(R.id.achievement_description);
            myImageView = itemView.findViewById(R.id.achievement_image);
            numberProgressBar = itemView.findViewById(R.id.achievement_progress_bar);
        }
    }
}
