package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Review;
import com.example.readify.Models.User;
import com.example.readify.Popups.BookReadedPopup;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;
import me.biubiubiu.justifytext.library.JustifyTextView;


public class ReviewsVerticalAdapter extends RecyclerView.Adapter<ReviewsVerticalAdapter.BookHolder> {

    private ArrayList<Review> reviewsList;
    private MainActivity activity;
    private Context mContext;

    // Counstructor for the Class
    public ReviewsVerticalAdapter(MainActivity activity, Context context, ArrayList<Review> reviews) {
        this.reviewsList = reviews;
        //this.originalSearchList = new ArrayList<>();
        //this.originalSearchList.addAll(booksList);
        this.activity = activity;
        this.mContext = context;
        //this.user = user;
    }

    public Context getContext(){
        return mContext;
    }

    /*public void setBooksList(ArrayList<Book> books){
        this.booksList = books;
    }*/

    /*public void deleteItem(int position){
        //MockupsValues.getPendingListBooks().remove(position);
        MockupsValues.removePendingListBook(booksList.get(position));
        if(!booksList.isEmpty())
            booksList.remove(position);
        notifyDataSetChanged();
    }

    public void readingListChanged(int position){
        Book book  = booksList.get(position);
        MockupsValues.addReadingBook(book);
        MockupsValues.removePendingListBook(book);
        activity.notifyPendingListChanged();
        activity.notifyReadingListChanged();
        //MockupsValues
    }

    public void pendingListChanged(int position){
        Book book  = booksList.get(position);
        MockupsValues.addPendingBook(book);
        MockupsValues.removeReadingListBook(book);
        activity.notifyReadingListChanged();
        activity.notifyPendingListChanged();
    }*/

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public ReviewsVerticalAdapter.BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.review_item, parent, false);
        return new ReviewsVerticalAdapter.BookHolder(view);
    }

    @Override
    public int getItemCount() {
        return reviewsList == null ? 0 : reviewsList.size();
    }

    /*public void filter(String newText) {
        String toSearch = newText.toLowerCase();

        booksList = new ArrayList<>();
        booksList.addAll(originalSearchList);

        if (toSearch.length() == 0) {
            notifyDataSetChanged();
            return;
        }

        ListIterator<Book> itr = booksList.listIterator();
        while (itr.hasNext()) {
            if (itr.next().getTitle().toLowerCase().contains(toSearch))
                continue;

            itr.remove();
        }
        notifyDataSetChanged();
    }*/

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull final ReviewsVerticalAdapter.BookHolder holder, final int position) {
        final Review review = reviewsList.get(position);


        //
        holder.userName.setText(review.getUser().getName());
        holder.userComment.setText(review.getComment());
        //String aux = mContext.getPackageName();
        holder.userImage.setImageResource(
                mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));



    }

    // This is your ViewHolder class that helps to populate data to the view
    public class BookHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImage;
        private TextView userName;
        private JustifyTextView userComment;

        public BookHolder(View itemView) {
            super(itemView);

            userImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userComment =  itemView.findViewById(R.id.user_comment);
        }
    }

}
