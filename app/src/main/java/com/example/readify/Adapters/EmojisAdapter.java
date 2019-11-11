package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Models.Emoji;
import com.example.readify.Models.Review;
import com.example.readify.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmojisAdapter extends RecyclerView.Adapter<EmojisAdapter.EmojiHolder> {

    private ArrayList<Emoji> recylcerList;
    private Context context;

    public EmojisAdapter(Context context, ArrayList<Emoji> list){
        recylcerList = list;
        this.context = context;

    }

    @Override
    public EmojisAdapter.EmojiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.emoji_item, parent, false);
        return new EmojisAdapter.EmojiHolder(view);
    }

    @Override
    public int getItemCount() {
        return recylcerList == null ? 0 : recylcerList.size();
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
    public void onBindViewHolder(@NonNull final EmojisAdapter.EmojiHolder holder, final int position) {
        final Emoji emoji = recylcerList.get(position);
        holder.emojiTitle.setText(emoji.getName());
        holder.emoji.setImageResource(context.getResources().getIdentifier(emoji.getEmoji(), "drawable", context.getPackageName()));
        holder.emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiClicked(holder);
            }
        });
        /*
        holder.userName.setText(review.getUser().getName());
        holder.userComment.setText(review.getComment());
        //String aux = mContext.getPackageName();
        holder.userImage.setImageResource(
                mContext.getResources().getIdentifier(review.getUser().getPicture(), "drawable", mContext.getPackageName()));

        */

    }

    public void emojiClicked(EmojisAdapter.EmojiHolder holder){
        float dim = context.getResources().getDimension(R.dimen.card_view_dimen);
        CardView.LayoutParams cardViewParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewParams.setMargins((int) dim,(int) dim,(int) dim, (int) dim);
        holder.cardView.setLayoutParams(cardViewParams);
        holder.cardView.requestLayout();
        holder.emojiTitle.setText("9%");
    }

    public class EmojiHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView emojiTitle;
        private ImageView emoji;

        public EmojiHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            emoji = (ImageView) itemView.findViewById(R.id.emoji_image_view);
            emojiTitle = (TextView) itemView.findViewById(R.id.emoji_name);
        }
    }
}
