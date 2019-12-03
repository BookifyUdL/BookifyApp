package com.example.readify.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BooksHorizontalAdapter extends RecyclerView.Adapter<BooksHorizontalAdapter.ViewHolder> {

    private List<Book> mViewBooks;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private boolean mHasDiscoverButtons;
    private Context context;
    private MainActivity activity;

    private User user;
    private SharedPreferences pref;

    // data is passed into the constructor
    public BooksHorizontalAdapter(MainActivity activity, Context context, List<Book> books, boolean hasDiscoverButton, User user) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mViewBooks = books;
        this.mHasDiscoverButtons = hasDiscoverButton;
        this.activity = activity;
        this.user = user;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.horizontal_book_recycler_view, parent, false);
        pref = view.getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if((position == mViewBooks.size() - 1 && mHasDiscoverButtons)){
            holder.layout.setVisibility(View.INVISIBLE);
            holder.lastView.setVisibility(View.VISIBLE);
        } else {
            String namePicture = mViewBooks.get(position).getPicture();
            holder.imageLayout.setBackground(ContextCompat.getDrawable(holder.imageLayout.getContext(),
                    holder.imageLayout.getContext().getResources().getIdentifier(namePicture, "drawable", holder.layout.getContext().getPackageName())));
            if(user.getInterested().contains(mViewBooks.get(position))){
                setAddButtonIcon(holder);
            } else {
                holder.addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setAddButtonIcon(holder);
                        Book book = mViewBooks.get(position);

                        ArrayList<Book> pending = user.getInterested();
                        pending.add(book);
                        user.setInterested(pending);

                        String interestedToPref = new Gson().toJson(user.getInterested());
                        pref.edit().putString("com.example.readify.interested", interestedToPref).apply();

                        //MockupsValues.addPendingBook(book);
                        activity.notifyPendingListChanged(user);
                        Toast.makeText(context, book.getTitle() + " " + context.getString(R.string.book_added_correctly_message), Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }

    private void setAddButtonIcon(ViewHolder holder){
        holder.addButton.setImageResource(R.drawable.ic_added_book);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mViewBooks.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton addButton;
        View lastView;
        RelativeLayout layout, imageLayout;

        ViewHolder(View itemView) {
            super(itemView);
            lastView = itemView.findViewById(R.id.coverView);
            layout = itemView.findViewById(R.id.relative_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
            addButton = itemView.findViewById(R.id.add_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
