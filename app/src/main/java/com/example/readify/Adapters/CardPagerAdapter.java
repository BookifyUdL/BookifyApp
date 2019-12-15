package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.readify.Adapters.CardAdapter;
import com.example.readify.MainActivity;
import com.example.readify.Models.Book;
import com.example.readify.Pages;
import com.example.readify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {
    private List<CardView> mViews;
    private List<Book> mData;
    private float mBaseElevation;
    private MainActivity activity;

    public CardPagerAdapter(MainActivity activity) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.activity = activity;
    }

    public void addCardItem(Book item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBook(mData.get(position));
            }
        });
        container.addView(view);
        bind(mData.get(position), view, container.getContext());
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    private void  showBook(Book book){
        activity.goToBookPage(book, Pages.DISCOVER_PAGE);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Book item, View view, Context context) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        ImageView bookImageView = (ImageView) view.findViewById(R.id.bookImageView);
        titleTextView.setText(item.getAuthor());
        contentTextView.setText(item.getTitle());
        setBookCover(bookImageView, item.getPicture());
        //bookImageView.setImageResource(bookImageView.getContext().getResources().getIdentifier(item.getPicture(), "drawable", bookImageView.getContext().getPackageName()));
        //bookImageView.setImageBitmap(context.getResources().getIdentifier(item.getPicture(), "drawable", context.getPackageName()));
    }

    private void setBookCover(ImageView image, String picture){
        Picasso.with(activity.getApplicationContext()) // Context
                .load(picture) // URL or file
                .into(image);
    }

}
