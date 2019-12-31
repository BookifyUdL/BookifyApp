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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MockupsValues;
import com.example.readify.Models.Author;
import com.example.readify.Models.Book;
import com.example.readify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BooksProfileHoritzontalAdapter extends RecyclerView.Adapter<BooksProfileHoritzontalAdapter.ViewHolder> {

    private List<Book> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public BooksProfileHoritzontalAdapter(Context context, List<Book> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = data;
        //mData.add(new Book("0","Add books", "", "add_book"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean found = false;
        if (position + 1 == getItemCount()) {
            holder.myTextViewTitle.setText("Add books");
            holder.myTextViewDescription.setText("");
            Picasso.with(mContext).load(R.drawable.add_book).into(holder.myImageView);
        } else {
            Book book = mData.get(position);

            holder.myTextViewTitle.setText(book.getTitle());
            holder.myTextViewDescription.setText(book.getAuthor());

            for (Author author : MockupsValues.getAuthors()) {
                if (author.getId().equals(book.getAuthor())) {
                    holder.myTextViewDescription.setText(author.getName());
                    found = true;
                }
            }
            if (found == false)
                holder.myTextViewDescription.setText(book.getAuthor());

            setBookCover(holder, book.getPicture());
        }

        if (position == mData.size()) {
            holder.myCardView.setBackgroundColor(ContextCompat.getColor(mInflater.getContext(), R.color.colorPrimary));
            holder.myTextViewTitle.setTextColor(ContextCompat.getColor(mInflater.getContext(), R.color.icons));
        } else {
            holder.myCardView.setBackgroundColor(ContextCompat.getColor(mInflater.getContext(), R.color.colorGrayCardBackground));
            holder.myTextViewTitle.setTextColor(ContextCompat.getColor(mInflater.getContext(), R.color.primaryText));
        }
    }

    private void setBookCover(@NonNull final BooksProfileHoritzontalAdapter.ViewHolder holder, String picture) {
        Picasso.with(mContext) // Context
                .load(picture) // URL or file
                .into(holder.myImageView);
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    public void setList(ArrayList<Book> readedBooks) {
        this.mData = readedBooks;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewTitle;
        TextView myTextViewDescription;
        ImageView myImageView;
        LinearLayout myCardView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewTitle = itemView.findViewById(R.id.cardViewTitle);
            myTextViewDescription = itemView.findViewById(R.id.cardViewDescription);
            myImageView = itemView.findViewById(R.id.cardViewImage);
            myCardView = itemView.findViewById(R.id.LayoutBackgroundCardView);
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
