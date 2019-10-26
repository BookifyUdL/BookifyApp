package com.example.readify.Adapters;

import android.content.Context;
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
import com.example.readify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BooksHorizontalAdapter extends RecyclerView.Adapter<BooksHorizontalAdapter.ViewHolder> {

    private List<Book> mViewBooks;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private boolean mHasDiscoverButtons;
    private Context context;
    private MainActivity activity;
    private boolean added = false;
    // data is passed into the constructor
    public BooksHorizontalAdapter(MainActivity activity, Context context, List<Book> books, boolean hasDiscoverButton) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mViewBooks = books;
        this.mHasDiscoverButtons = hasDiscoverButton;
        this.activity = activity;

    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.horizontal_book_recycler_view, parent, false);
        /*if(mHasDiscoverButtons && !added){
            mViewBooks.add(mViewBooks.get(mViewBooks.size()-1));
            added = true;
        }*/
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if((position == mViewBooks.size() - 1 && mHasDiscoverButtons)){
            //holder.myImageView.setVisibility(View.INVISIBLE);
            holder.layout.setVisibility(View.INVISIBLE);
            holder.lastView.setVisibility(View.VISIBLE);

        } else {
            String namePicture = mViewBooks.get(position).getPicture();
            holder.imageLayout.setBackground(ContextCompat.getDrawable(holder.imageLayout.getContext(),
                    holder.imageLayout.getContext().getResources().getIdentifier(namePicture, "drawable", holder.layout.getContext().getPackageName())));
            if(MockupsValues.getPendingListBooks().contains(mViewBooks.get(position))){
                setAddButtonIcon(holder);
            } else {
                holder.addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setAddButtonIcon(holder);
                        Book book = mViewBooks.get(position);
                        MockupsValues.addPendingBook(book);
                        activity.notifyPendingListChanged();
                        Toast.makeText(context, book.getTitle() + " " + context.getString(R.string.book_added_correctly_message), Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }

    private void setAddButtonIcon(ViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(holder.addButton.getContext(),
                holder.addButton.getContext().getResources().getIdentifier("ic_added_book", "drawable", holder.addButton.getContext().getPackageName()));
        holder.addButton.setImageResource(R.drawable.ic_added_book);
        //holder.addButton.setBackgroundTintList(ColorStateList.valueOf(holder.addButton.getContext().getResources().getColor(R.color.colorAddedGreen)));
        //holder.addButton.setBackgroundColor(holder.addButton.getContext().getResources().getColor(R.color.colorAddedGreen));
        //holder.addButton.setBackgroundColor(holder.addButton.getContext().getResources().getColor(R.color.colorAddedGreen));
        //holder.addButton.setBackgroundDrawable(drawable);
        //holder.addButton.setImageResource(drawable);
        //holder.addButton.setIcon(drawable, true);
        //holder.addButton.setBackgroundDrawable(drawable);
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
        //ImageView myImageView;
        ImageButton addButton;
        View lastView;
        RelativeLayout layout, imageLayout;

        ViewHolder(View itemView) {
            super(itemView);
            //myImageView = itemView.findViewById(R.id.coverImageView);
            lastView = itemView.findViewById(R.id.coverView);
            layout = itemView.findViewById(R.id.relative_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
            addButton = itemView.findViewById(R.id.add_button);
            /*addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //addButton.setIcon(addButton.getContext().getResources().getDrawable(R.drawable.ic_added_book), true);
                    Drawable drawable = ContextCompat.getDrawable(addButton.getContext(),
                            addButton.getContext().getResources().getIdentifier("ic_added_book", "drawable", addButton.getContext().getPackageName()));
                    addButton.setIcon(drawable, true);
                }
            });*/
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
