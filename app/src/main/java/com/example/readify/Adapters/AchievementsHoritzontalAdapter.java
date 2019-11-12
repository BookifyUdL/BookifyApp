package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Models.Achievement;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.List;

public class AchievementsHoritzontalAdapter extends RecyclerView.Adapter<AchievementsHoritzontalAdapter.ViewHolder> {

    private List<Achievement> mData;
    private LayoutInflater mInflater;

    public AchievementsHoritzontalAdapter(Context context, List<Achievement> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

        if (this.mData.size() == 0)
            mData.add(new Achievement(0,"Empty", "Complete an achievements to add", "empty", 0, 9999));
    }

    public void setAchivementsList(ArrayList<Achievement> data){
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.achievements_profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement achievement = mData.get(position);

        holder.myTextViewTitle.setText(achievement.getTitle());
        holder.myTextViewDescription.setText(achievement.getDescription());
        holder.myImageView.setImageResource(mInflater.getContext()
                .getResources().getIdentifier(achievement.getImage(), "drawable", mInflater.getContext().getPackageName()));
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

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewTitle = itemView.findViewById(R.id.cardViewTitle);
            myTextViewDescription = itemView.findViewById(R.id.cardViewDescription);
            myImageView = itemView.findViewById(R.id.cardViewImage);
        }
    }

}
