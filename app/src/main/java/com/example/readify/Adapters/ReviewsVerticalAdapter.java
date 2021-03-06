package com.example.readify.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.Layout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.readify.CommentActivity;
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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReviewsVerticalAdapter extends RecyclerView.Adapter<ReviewsVerticalAdapter.BookHolder> implements RichEditTextInterface {

    private ArrayList<Review> reviewsList;
    private MainActivity activity;
    private Context mContext;
    private FloatingActionButton floatingActionButton;
    private ImageView imageView;
    private User user;

    public ReviewsVerticalAdapter(MainActivity activity, Context context, ArrayList<Review> reviews, FloatingActionButton go, User user) {
        this.reviewsList = reviews;
        this.activity = activity;
        this.mContext = context;
        this.floatingActionButton = go;
        this.user = user;
    }

    public void addReview(Review newReview){
        reviewsList.add(newReview);
        notifyDataSetChanged();
    }

    public Context getContext(){
        return mContext;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public ReviewsVerticalAdapter.BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.review_item, parent, false);
        return new ReviewsVerticalAdapter.BookHolder(view);
    }

    @Override
    public int getItemCount() {
        return reviewsList == null ? 0 : reviewsList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull final ReviewsVerticalAdapter.BookHolder holder, final int position) {
        final Review review = reviewsList.get(position);
        holder.position = position;
        String name = review.getUser().getName();
        holder.userName.setText(review.getUser().getName());
        String comment = review.getComment();
        if(!comment.isEmpty())
            holder.userComment.setText(comment);

        //holder.userImage.setImageResource(
        //       mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));
        getProfileImage(review.getUser(), holder.userImage);

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

        if(review.likedComment(user))
            holder.likeButton.setLiked(true);

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //review.setLikes(review.getLikes() + 1);
                review.addLike(user);
                String aux = Integer.toString(review.getLikes());
                holder.likesNumber.setText(aux);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //review.setLikes(review.getLikes() - 1);
                review.removeLike(user);
                String aux = Integer.toString(review.getLikes());
                holder.likesNumber.setText(aux);
            }
        });
        String aux = Integer.toString(review.getLikes());
        holder.likesNumber.setText(aux);

        if(review.getUser().equals(user)){
            holder.editDeleteLayout.setVisibility(View.VISIBLE);
            holder.onDeleteButtonClicked();
            holder.onEditButtonClicked();
        }
    }

    public void setGifView(Uri uri){
        Glide.with(getContext()) // replace with 'this' if it's in activity
                .load(uri.toString())
                .asGif()
                .error(R.drawable.angry) // show error drawable if the image is not a gif
                .into(imageView);
    }

    public void setImageView(Uri uri){
        Glide.with(getContext())
                .load(uri.toString())
                .asBitmap()
                .error(R.drawable.angry)
                .into(imageView);
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class BookHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener {

        private ExpandableLayout expandableLayout;
        private ImageView expandButton;
        private CircleImageView userImage;
        private TextView userName;
        private TextView userComment;
        private LinearLayout subCommentsRecyclerView;
        private LinearLayout addCommentLayout;
        private boolean areSubCommentsLoaded = false;
        private LinearLayout gifContainer;
        private EditText editText;
        private LikeButton likeButton;
        private TextView likesNumber;
        private int position;
        private LinearLayout editDeleteLayout;
        private ImageView deleteButton;
        private ImageView editButton;

        public BookHolder(View itemView) {
            super(itemView);

            gifContainer =  (LinearLayout) itemView.findViewById(R.id.gif_container);
            userImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userComment =  itemView.findViewById(R.id.user_comment);
            editDeleteLayout = itemView.findViewById(R.id.edit_delete_layout);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_button);

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

            likeButton = itemView.findViewById(R.id.star_button);
            likesNumber = itemView.findViewById(R.id.likes_number);
            user = new User();
            user.readFromSharedPreferences(getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE));
        }

        private void onEditButtonClicked(){
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Review review = reviewsList.get(position);
                    Intent intent = new Intent(activity, CommentActivity.class);
                    intent.putExtra(Review.COMMENT_PARAMETER, review.getComment());
                    intent.putExtra(Review.COMMENT_TYPE_PARAMETER, review.getCommentType());
                    intent.putExtra(Review.POSITION_PARAMETER, position);
                    intent.putExtra(Review.URI_PARAMETER, review.getUri());
                    activity.startActivity(intent);
                }
            });
        }

        private void onDeleteButtonClicked(){
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogBuilder.setTitle(R.string.delete);
                    dialogBuilder.setMessage(R.string.delete_comment_alert_dialog);
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i == DialogInterface.BUTTON_POSITIVE){
                                Review review = reviewsList.get(position);
                                MockupsValues.deleteReview(review);
                                reviewsList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(getContext(), getContext().getResources().getString(R.string.delete_correct), Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    dialogBuilder.setPositiveButton(R.string.accept, dialogClickListener);
                    dialogBuilder.setNegativeButton(R.string.cancel, dialogClickListener);
                    final AlertDialog dialog = dialogBuilder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                        }
                    });
                    dialog.show();
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
                    areSubCommentsLoaded = true;
                }
                expandableLayout.expand();
            }
        }

        private void addSubComment(final LayoutInflater layoutInflater){
            View add_comment = layoutInflater.inflate(R.layout.add_comment_layout,
                            addCommentLayout,false);
            CircleImageView imageView = (CircleImageView) add_comment.findViewById(R.id.profile_image);

            /*imageView.setImageResource(
                    mContext.getResources().getIdentifier(MockupsValues.user.getPicture(), "drawable", mContext.getPackageName()));*/
            getProfileImage(MockupsValues.user, imageView);

            RelativeLayout relativeLayout = add_comment.findViewById(R.id.relative_layout);
            int height = relativeLayout.getHeight();

            LinearLayout linearLayout = add_comment.findViewById(R.id.edit_text_gif_layout);
            linearLayout.setMinimumHeight(height);

            editText = add_comment.findViewById(R.id.comment_box);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(!b)
                        floatingActionButton.setVisibility(View.VISIBLE);
                    if(b)
                        floatingActionButton.setVisibility(View.INVISIBLE);
                }
            });

            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        checkSubComment(layoutInflater);
                        //sendMessage();
                        handled = true;
                    }
                    return handled;
                }
            });

            ImageView sendButton = add_comment.findViewById(R.id.send_button);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //checkSubComment(layoutInflater);
                }
            });

            addCommentLayout.addView(add_comment);
        }

        private void checkSubComment(LayoutInflater inflater){
            String text = editText.getText().toString();
            if(text.isEmpty()){
                Toast.makeText(getContext(), "Text can't be empty!", Toast.LENGTH_SHORT).show();
            } else {
                Review reviewToAdd = new Review(user, text);
                View to_add = inflater.inflate(R.layout.review_item_without_options,
                        subCommentsRecyclerView,false);
                addSubComment(to_add, reviewToAdd);
                reviewsList.get(position).addSubReview(reviewToAdd);
                subCommentsRecyclerView.addView(to_add);

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(to_add.getWindowToken(), 0);
                editText.setText("");
                editText.setHint(getContext().getResources().getString(R.string.add_comment));
                editText.clearFocus();

            }
        }

        private void addCommentSubComments(LayoutInflater layoutInflater){
            ArrayList<Review> reviews = reviewsList.get(position).getSubReviews();
            if(reviews != null){
                for (int i = 0; i < reviews.size(); i++) {
                    View to_add = layoutInflater.inflate(R.layout.review_item_without_options,
                            subCommentsRecyclerView,false);
                    addSubComment(to_add, reviews.get(i));
                    subCommentsRecyclerView.addView(to_add);
                }
            }
        }

        private void addSubComment(View to_add, Review review){
            CircleImageView image = (CircleImageView) to_add.findViewById(R.id.profile_image);
            TextView name = (TextView) to_add.findViewById(R.id.user_name);
            TextView comment =  to_add.findViewById(R.id.user_comment);
            image.setImageResource(
                    mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));
            getProfileImage(review.getUser(), image);

            name.setText(review.getUser().getName());
            comment.setText(review.getComment());
            //return to_add;
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
        }
    }

    private void getProfileImage(final User user, final CircleImageView circleImageView) {
        String vrg = user.getPicture();
        Picasso.with(mContext) // Context
                .load(user.getPicture()) // URL or file
                .into(circleImageView);
        /*@SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>() {
            protected Bitmap doInBackground(Void... p) {
                Bitmap bmp = null;
                try {
                    URL aURL = new URL(user.getPicture());
                    URLConnection conn = aURL.openConnection();
                    conn.setUseCaches(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bmp = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            protected void onPostExecute(Bitmap bmp) {
                circleImageView.setImageBitmap(bmp);
            }
        };

        t.execute();*/
    }
}
