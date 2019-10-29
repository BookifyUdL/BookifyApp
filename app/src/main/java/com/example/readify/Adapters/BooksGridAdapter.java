package com.example.readify.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BooksGridAdapter extends RecyclerView.Adapter<BooksGridAdapter.ViewHolder> {

    private List<Book> mViewBooks, originalSearchList;
    private LayoutInflater mInflater;
    private BooksHorizontalAdapter.ItemClickListener mClickListener;
    //private boolean mHasDiscoverButtons;
    private Context context;
    private MainActivity activity;
    private boolean added = false;


    public BooksGridAdapter(MainActivity activity, Context context, List<Book> books) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mViewBooks = books;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(books);
        this.activity = activity;

    }

    public void filter(String newText) {
        String toSearch = newText.toLowerCase();
        mViewBooks = new ArrayList<>();
        mViewBooks.addAll(originalSearchList);

        if (toSearch.length() == 0) {
            notifyDataSetChanged();
            return;
        }

        ListIterator<Book> itr = mViewBooks.listIterator();
        while (itr.hasNext()) {
            if (itr.next().getTitle().toLowerCase().contains(toSearch))
                continue;

            itr.remove();
        }
        notifyDataSetChanged();
    }


    @Override
    @NonNull
    public BooksGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_grid_item, parent, false);
        /*if(mHasDiscoverButtons && !added){
            mViewBooks.add(mViewBooks.get(mViewBooks.size()-1));
            added = true;
        }*/
        return new BooksGridAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final BooksGridAdapter.ViewHolder holder, final int position) {
        String namePicture = mViewBooks.get(position).getPicture();
        holder.imageLayout.setBackground(ContextCompat.getDrawable(holder.imageLayout.getContext(),
                holder.imageLayout.getContext().getResources().getIdentifier(namePicture, "drawable", holder.layout.getContext().getPackageName())));
        holder.setAddButtonState(position);
    }

    /*private void setAddButtonIcon(BooksGridAdapter.ViewHolder holder){
        //Drawable drawable = ContextCompat.getDrawable(holder.addButton.getContext(),
        //        holder.addButton.getContext().getResources().getIdentifier("ic_added_book", "drawable", holder.addButton.getContext().getPackageName()));
        holder.addButton.setImageResource(R.drawable.ic_added_book);
    }*/

    @Override
    public int getItemCount() {
        return mViewBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton addButton;
        View lastView;
        RelativeLayout layout, imageLayout;

        ViewHolder(View itemView) {
            super(itemView);
            lastView = itemView.findViewById(R.id.coverView);
            layout = itemView.findViewById(R.id.relative_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
            addButton = itemView.findViewById(R.id.add_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        private void setAddButtonState(final int position){
            if(MockupsValues.getPendingListBooks().contains(mViewBooks.get(position))){
                setAddButtonIconToAdded();
            } else {
                setAddButtonIconToAdd();
            }
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(MockupsValues.getPendingListBooks().contains(mViewBooks.get(position))){
                        setAddButtonIconToAdded();
                        removeFromPendingList(position);
                    } else {
                        setAddButtonIconToAdd();
                        toPendingList(position);
                    }
                }
            });
        }

        private void removeFromPendingList(int position){
            Book book = mViewBooks.get(position);
            MockupsValues.removePendingListBook(book);
            activity.notifyPendingListChanged();
            Toast.makeText(context, book.getTitle() + " " + context.getString(R.string.book_removed_correctly_message), Toast.LENGTH_LONG).show();

        }

        private void toPendingList(int position){
            Book book = mViewBooks.get(position);
            MockupsValues.addPendingBook(book);
            activity.notifyPendingListChanged();
            Toast.makeText(context, book.getTitle() + " " + context.getString(R.string.book_added_correctly_message), Toast.LENGTH_LONG).show();
        }

        private void setAddButtonIconToAdd(){
            addButton.setImageResource(R.drawable.ic_add_book);
        }

        private void setAddButtonIconToAdded(){
            //Drawable drawable = ContextCompat.getDrawable(holder.addButton.getContext(),
            //        holder.addButton.getContext().getResources().getIdentifier("ic_added_book", "drawable", holder.addButton.getContext().getPackageName()));
            addButton.setImageResource(R.drawable.ic_added_book);
        }
    }

    public void setClickListener(BooksHorizontalAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
