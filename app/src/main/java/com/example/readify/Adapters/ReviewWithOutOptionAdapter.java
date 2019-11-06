package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.Models.Review;
import com.example.readify.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class ReviewWithOutOptionAdapter extends RecyclerView.Adapter<ReviewWithOutOptionAdapter.ReviewHolder> {

    private ArrayList<Review> reviewsList;
    //private MainActivity activity;
    private Context mContext;

    // Counstructor for the Class

    public ReviewWithOutOptionAdapter(Context context, ArrayList<Review> reviews) {
        this.reviewsList = reviews;
        //this.originalSearchList = new ArrayList<>();
        //this.originalSearchList.addAll(booksList);
        //this.activity = activity;
        this.mContext = context;
        //this.user = user;
    }
    /*public void addReview(Review newReview){
        reviewsList.add(newReview);
        notifyDataSetChanged();
    }

    public Context getContext(){
        return mContext;
    }*/

    @Override
    public ReviewWithOutOptionAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.review_item_without_options, parent, false);
        //View view = layoutInflater.inflate(R.layout.comment_final_design, parent, false);
        return new ReviewWithOutOptionAdapter.ReviewHolder(view);
    }

    @Override
    public int getItemCount() {
        return reviewsList == null ? 0 : reviewsList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewWithOutOptionAdapter.ReviewHolder holder, final int position) {
        final Review review = reviewsList.get(position);


        holder.userName.setText(review.getUser().getName());
        holder.userComment.setText(review.getComment());
        //String aux = mContext.getPackageName();
        holder.userImage.setImageResource(
                mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));

    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        //private ExpandableLayout expandableLayout;
        //private ImageView expandButton;
        private CircleImageView userImage;
        private TextView userName;
        private JustifyTextView userComment;
        private LinearLayout commentLayout;
        private CardView commentItem;

        public ReviewHolder(View itemView) {
            super(itemView);

            //commentLayout =  (LinearLayout) itemView.findViewById(R.id.comment_item);
            commentItem = (CardView) itemView.findViewById(R.id.reviewed_comment);
            userImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userComment =  itemView.findViewById(R.id.user_comment);
        }
    }
}
