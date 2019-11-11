package com.example.readify.Adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.readify.CommentType;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Review;
import com.example.readify.Models.User;
import com.example.readify.Popups.BookReadedPopup;
import com.example.readify.R;
import com.example.readify.RichEditText;
import com.example.readify.RichEditTextInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;
import me.biubiubiu.justifytext.library.JustifyTextView;


public class ReviewsVerticalAdapter extends RecyclerView.Adapter<ReviewsVerticalAdapter.BookHolder> implements RichEditTextInterface {

    private ArrayList<Review> reviewsList;
    private MainActivity activity;
    private Context mContext;
    private FloatingActionButton floatingActionButton;
    private ImageView imageView;
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

    public ReviewsVerticalAdapter(MainActivity activity, Context context, ArrayList<Review> reviews, FloatingActionButton go) {
        this.reviewsList = reviews;
        //this.originalSearchList = new ArrayList<>();
        //this.originalSearchList.addAll(booksList);
        this.activity = activity;
        this.mContext = context;
        this.floatingActionButton = go;
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
        String comment = review.getComment();
        if(!comment.isEmpty())
            holder.userComment.setText(comment);

        holder.userImage.setImageResource(
                mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));

        if(review.getCommentType() != CommentType.COMMENT){
            imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams((int) getContext().getResources().getDimension(R.dimen.image_gif_height), (int) getContext().getResources().getDimension(R.dimen.image_gif_height));
            imageView.setLayoutParams(params);
            if(review.getCommentType() == CommentType.COMMENT_AND_GIF)
                setGifView(review.getUri());
            if(review.getCommentType() == CommentType.COMMENT_AND_IMAGE)
                setImageView(review.getUri());
            holder.gifContainer.addView(imageView);
        }
    }

    public void setGifView(Uri uri){
        //commentUri = uri;
        //commentType = CommentType.COMMENT_AND_GIF;
        //checkIfImageViewIsAdded();
        Glide.with(getContext()) // replace with 'this' if it's in activity
                .load(uri.toString())
                .asGif()
                .error(R.drawable.angry) // show error drawable if the image is not a gif
                .into(imageView);
        //enablePublishButton();
    }

    public void setImageView(Uri uri){
        //commentUri = uri;
        //commentType = CommentType.COMMENT_AND_IMAGE;
        //checkIfImageViewIsAdded();
        Glide.with(getContext())
                .load(uri.toString())
                .asBitmap()
                .error(R.drawable.angry)
                .into(imageView);
        //enablePublishButton();


    }

    // This is your ViewHolder class that helps to populate data to the view
    public class BookHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener, RichEditTextInterface {

        private ExpandableLayout expandableLayout;
        private ImageView expandButton;
        private CircleImageView userImage;
        private TextView userName;
        private JustifyTextView userComment;
        private LinearLayout subCommentsRecyclerView;
        private LinearLayout addCommentLayout;
        private boolean areSubCommentsLoaded = false;
        private LinearLayout gifContainer;
        private CardView commentItem;

        public BookHolder(View itemView) {
            super(itemView);

            gifContainer =  (LinearLayout) itemView.findViewById(R.id.gif_container);
            commentItem = (CardView) itemView.findViewById(R.id.reviewed_comment);
            userImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userComment =  itemView.findViewById(R.id.user_comment);

            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(this);
            expandButton = itemView.findViewById(R.id.see_comments_button);
            subCommentsRecyclerView = itemView.findViewById(R.id.sub_comments_recycler_view);
            addCommentLayout = itemView.findViewById(R.id.add_comment_layout);
            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onExpandButtonClicked();
                }

            });
        }

        private void onExpandButtonClicked(){
            if(expandableLayout.isExpanded()) {
                expandableLayout.collapse();
            } else {
                if(!areSubCommentsLoaded)
                {
                    LayoutInflater layoutInflater = LayoutInflater.from(subCommentsRecyclerView.getContext());
                    addCommentSubComments(layoutInflater);
                    addSubComment(layoutInflater);
                    expandableLayout.expand();
                }
            }
        }

        private void addSubComment(LayoutInflater layoutInflater){
            View add_comment = layoutInflater.inflate(R.layout.add_comment_layout,
                            addCommentLayout,false);
                    ImageView imageView = add_comment.findViewById(R.id.profile_image);
                    imageView.setImageResource(
                            mContext.getResources().getIdentifier(MockupsValues.user.getPicture(), "drawable", mContext.getPackageName()));
                    RelativeLayout relativeLayout = add_comment.findViewById(R.id.relative_layout);
                    int height = relativeLayout.getHeight();
                    ScrollView scrollView = add_comment.findViewById(R.id.scroll_view);
                    scrollView.setMinimumHeight(height);

                    RichEditText editText = new RichEditText(mContext, this);
                    editText.setHint(R.string.add_comment);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    editText.setGravity(Gravity.CENTER_VERTICAL);
                    editText.setLayoutParams(params);
                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if(!b)
                                floatingActionButton.setVisibility(View.VISIBLE);
                            if(b)
                                floatingActionButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    scrollView.addView(editText);


            addCommentLayout.addView(add_comment);
        }

        public void setGifView(Uri uri){
            /*commentUri = uri;
            commentType = CommentType.COMMENT_AND_GIF;
            checkIfImageViewIsAdded();
            Glide.with(getApplicationContext()) // replace with 'this' if it's in activity
                    .load(uri.toString())
                    .asGif()
                    .error(R.drawable.angry) // show error drawable if the image is not a gif
                    .into(imageView);
            enablePublishButton();*/
        }

        public void setImageView(Uri uri){
            /*commentUri = uri;
            commentType = CommentType.COMMENT_AND_IMAGE;
            checkIfImageViewIsAdded();
            Glide.with(getApplicationContext())
                    .load(uri.toString())
                    .asBitmap()
                    .error(R.drawable.angry)
                    .into(imageView);
            enablePublishButton();*/
        }

        private void addCommentSubComments(LayoutInflater layoutInflater){
            for (int i = 0; i < reviewsList.size(); i++) {
                View to_add = layoutInflater.inflate(R.layout.review_item_without_options,
                        subCommentsRecyclerView,false);

                CircleImageView image = (CircleImageView) to_add.findViewById(R.id.profile_image);
                TextView name = (TextView) to_add.findViewById(R.id.user_name);
                TextView comment =  to_add.findViewById(R.id.user_comment);
                Review review = reviewsList.get(i);
                image.setImageResource(
                        mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));

                name.setText(review.getUser().getName());
                comment.setText(review.getComment());
                subCommentsRecyclerView.addView(to_add);
            }
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
