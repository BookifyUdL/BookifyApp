package com.example.readify.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Models.Genre;
import com.example.readify.R;

import java.util.ArrayList;

public class GenresHoritzontalAdapter extends RecyclerView.Adapter<GenresHoritzontalAdapter.ViewHolder> {

    private ArrayList<Genre> mData;
    private LayoutInflater mInflater;
    private int itemCount;
    private ItemClickListener mClickListener;

    public GenresHoritzontalAdapter(Context context, ArrayList<Genre> data) {
        this.mData = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.itemCount = mData.size();
        if (mData.size() == 0)
            mData.add(new Genre("Add genre", "add_book"));

    }

    public void setList(ArrayList<Genre> list){
        this.mData = list;
    }

    @NonNull
    @Override
    public GenresHoritzontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.genre_preferences_profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresHoritzontalAdapter.ViewHolder holder, int position) {
        Genre genre = mData.get(position);
        holder.myTextView.setText(genre.getName());
        holder.myImageView.setImageResource(mInflater.getContext()
                .getResources().getIdentifier(genre.getPicture(), "drawable", mInflater.getContext().getPackageName()));
        if (genre.getName().equals("Add genre")){
            holder.myLinearLayout.setBackgroundColor(Color.BLACK);
            holder.myTextView.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;
        LinearLayout myLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.cardViewText);
            myImageView = itemView.findViewById(R.id.cardViewImage);
            myLinearLayout = itemView.findViewById(R.id.LayoutBackgroundCardView);
            if (myTextView.getText().equals("add genre") || myTextView.getText().equals(""))
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view);
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view);
    }
}
