package com.example.readify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ListIterator;

public class BooksListFormAdapter extends RecyclerView.Adapter<BooksListFormAdapter.BookHolder> {
    private ArrayList<Book> booksList, originalSearchList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private User user;
    private boolean read;
    private Context context;

    // data is passed into the constructor
    public BooksListFormAdapter(Context context, ArrayList<Book> booksList, User user, boolean read) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.user = user;
        this.read = read;
    }

    public void setBooksList(ArrayList<Book> books) {
        this.booksList = books;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_list_item, parent, false);
        return new BookHolder(view);
    }

    public void filter(String newText) {
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
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        final Book book = booksList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        String aux = mInflater.getContext().getPackageName();
        setBookCover(holder, book.getPicture());
        //holder.bookCover.setImageResource(null);
        /*holder.bookCover.setImageResource(mInflater.getContext()
                .getResources().getIdentifier(book.getPicture(), "drawable", aux));*/
        holder.addButton.setVisibility(View.GONE);

        if (holder.cardView.getTag() == mInflater.getContext().getString(R.string.cardView_unmark)) {
            holder.cardViewBackground.setBackgroundColor(mInflater.getContext().getColor(R.color.colorGrayCardBackground));
        } else {
            holder.cardViewBackground.setBackground(mInflater.getContext().getDrawable(R.drawable.cardview_selected));
        }

        if (user != null && user.containsBook(book))
            holder.cardViewBackground.setBackground(mInflater.getContext().getDrawable(R.drawable.cardview_selected));
        else
            holder.cardViewBackground.setBackgroundColor(mInflater.getContext().getColor(R.color.colorGrayCardBackground));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.containsBook(book)) {
                    if (read)
                        user.removeBookToLibrary(book);
                    else
                        user.removeBookInterestedBooks(book);
                } else {
                    if (read) {
                        book.setRead(true);
                        user.addBookToLibrary(book);
                    } else {
                        book.setRead(false);
                        user.addBookToInterestedBooks(book);
                        user.addBookToLibrary(book);
                    }
                }
                MockupsValues.user = user;
                if (mClickListener != null) mClickListener.onItemClick(view);
            }
        });
    }

    private void setBookCover(@NonNull final BookHolder holder, String coverUrl){
        Picasso.with(context) // Context
                .load(coverUrl) // URL or file
                .into(holder.bookCover);
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
                holder.bookCover.setImageBitmap(bmp);
            }
        };*/
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return booksList == null ? 0 : booksList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class BookHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView bookAuthor;
        ImageButton addButton;
        LinearLayout cardViewBackground;
        CardView cardView;

        BookHolder(View itemView) {
            super(itemView);
            bookCover = (ImageView) itemView.findViewById(R.id.book_cover_image_view);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);
            addButton = (ImageButton) itemView.findViewById(R.id.addButton);
            cardViewBackground = (LinearLayout) itemView.findViewById(R.id.linearLayoutCardView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    // convenience method for getting data at click position
    Book getItem(int id) {
        return booksList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view);
    }
}
