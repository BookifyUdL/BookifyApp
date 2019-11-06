package com.example.readify.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;
import me.biubiubiu.justifytext.library.JustifyTextView;


public class ReviewsVerticalAdapter extends RecyclerView.Adapter<ReviewsVerticalAdapter.BookHolder> {

    private ArrayList<Review> reviewsList;
    private MainActivity activity;
    private Context mContext;
    //private ReviewWithOutOptionAdapter adapter;

    // Counstructor for the Class
    public ReviewsVerticalAdapter(MainActivity activity, Context context, ArrayList<Review> reviews) {
        this.reviewsList = reviews;
        //this.originalSearchList = new ArrayList<>();
        //this.originalSearchList.addAll(booksList);
        this.activity = activity;
        this.mContext = context;
        //this.user = user;
    }

    public void addReview(Review newReview){
        reviewsList.add(newReview);
        notifyDataSetChanged();
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
        //View view = layoutInflater.inflate(R.layout.comment_final_design, parent, false);
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


        holder.userName.setText(review.getUser().getName());
        holder.userComment.setText(review.getComment());
        //String aux = mContext.getPackageName();
        holder.userImage.setImageResource(
                mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));
        /*if(position == 0){
            holder.commentLayout.setVisibility(View.VISIBLE);
            holder.commentItem.setVisibility(View.INVISIBLE);
        } else {
            holder.userName.setText(review.getUser().getName());
            holder.userComment.setText(review.getComment());
            //String aux = mContext.getPackageName();
            holder.userImage.setImageResource(
                    mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));

        }*/
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class BookHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener {

        private ExpandableLayout expandableLayout;
        private ImageView expandButton;
        private CircleImageView userImage;
        private TextView userName;
        private JustifyTextView userComment;
        private LinearLayout recyclerView;
        private LinearLayout commentLayout;
        private CardView commentItem;

        public BookHolder(View itemView) {
            super(itemView);

            //commentLayout =  (LinearLayout) itemView.findViewById(R.id.comment_item);
            commentItem = (CardView) itemView.findViewById(R.id.reviewed_comment);
            userImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userComment =  itemView.findViewById(R.id.user_comment);

            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(this);
            expandButton = itemView.findViewById(R.id.see_comments_button);
            recyclerView = itemView.findViewById(R.id.sub_comments_recycler_view);
            //setRecyclerAdapter();
            //expandableLayout.collapse();
            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(expandableLayout.isExpanded()) {
                        //recyclerView.setAdapter(null);
                        expandableLayout.collapse();
                    } else {
                        //String[] options = activity.getResources().getStringArray(R.array.options);
                        LayoutInflater layoutInflater = LayoutInflater.from(recyclerView.getContext());
                        for (int i = 0; i < reviewsList.size(); i++) {
                            View to_add = layoutInflater.inflate(R.layout.review_item_without_options,
                                    recyclerView,false);

                            //TextView text = (TextView) to_add.findViewById(R.id.text);
                            //text.setText(options[i]);
                            //text.setTypeface(FontSelector.getBold(getActivity()));
                            recyclerView.addView(to_add);
                        }

                        expandableLayout.expand();
                    }
                }
            });
        }

        /*@Override
        public void onClick(View view) {
            if(expandableLayout.isExpanded()) {
                expandableLayout.collapse();
            } else {
                expandableLayout.expand();
            }
            /*ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.expandButton.setSelected(false);
                holder.expandableLayout.collapse();
            }

            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                expandButton.setSelected(true);
                expandableLayout.expand();
                selectedItem = position;
            }
        }*/

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            /*Log.d("ExpandableLayout", "State: " + state);
            if (state == ExpandableLayout.State.EXPANDING) {
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }*/
        }
    }

}
