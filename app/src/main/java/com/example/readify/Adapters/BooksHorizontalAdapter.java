package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Models.Book;
import com.example.readify.R;

import java.util.List;

public class BooksHorizontalAdapter extends RecyclerView.Adapter<BooksHorizontalAdapter.ViewHolder> {

    private List<Book> mViewBooks;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private boolean mHasDiscoverButtons;

    // data is passed into the constructor
    public BooksHorizontalAdapter(Context context, List<Book> books, boolean hasDiscoverButton) {
        this.mInflater = LayoutInflater.from(context);
        this.mViewBooks = books;
        this.mHasDiscoverButtons = hasDiscoverButton;
        if(hasDiscoverButton)
            mViewBooks.add(new Book());
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.horizontal_book_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == mViewBooks.size() - 1 && mHasDiscoverButtons){
            holder.myImageView.setVisibility(View.INVISIBLE);
            holder.lastView.setVisibility(View.VISIBLE);

        } else {
            holder.myImageView.setImageResource(holder.myImageView.getContext().getResources()
                    .getIdentifier(mViewBooks.get(position).getPicture(), "drawable", holder.myImageView.getContext().getPackageName()));
        }
    }

    public void onItemClicked(){
        /*LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup,null);

        closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);

        //instantiate popup window
        popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        //display the popup window
        popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });*/

    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mViewBooks.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;
        View lastView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.coverImageView);
            lastView = itemView.findViewById(R.id.coverView);
            //myTextView = itemView.findViewById(R.id.tvAnimalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    /*public String getItem(int id) {
        return mViewBooks.get(id);
    }*/

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
