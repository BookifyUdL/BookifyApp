package com.example.readify.FirstTimeForm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Models.Genre;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.util.ArrayList;

public class RecyclerViewAdapterGenres extends RecyclerView.Adapter<RecyclerViewAdapterGenres.ViewHolder> {
    private ArrayList<Genre> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private User user;

    // data is passed into the constructor
    RecyclerViewAdapterGenres(Context context, ArrayList<Genre> data, User user) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.user = user;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_template, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Genre genre = mData.get(position);
        holder.myTextView.setText(genre.getName());
        holder.myImageView.setImageResource(mInflater.getContext()
                .getResources().getIdentifier(genre.getPicture(), "drawable", mInflater.getContext().getPackageName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.containsGenre(genre)) {
                    user.removeGenreToGenres(genre);
                    Toast.makeText(view.getContext(), "Genre removed on your favourite genres", Toast.LENGTH_SHORT).show();
                } else {
                    user.addGenreToGenres(genre);
                    Toast.makeText(view.getContext(), "Genre added on your favourite genres", Toast.LENGTH_SHORT).show();
                }
                if (mClickListener != null) mClickListener.onItemClick(view);
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.cardViewText);
            myImageView = itemView.findViewById(R.id.cardViewImage);

        }
    }

    // convenience method for getting data at click position
    Genre getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view);
    }
}
