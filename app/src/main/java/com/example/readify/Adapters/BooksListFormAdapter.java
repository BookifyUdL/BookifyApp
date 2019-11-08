package com.example.readify.Adapters;

import android.content.Context;
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

import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.ListIterator;

public class BooksListFormAdapter extends RecyclerView.Adapter<BooksListFormAdapter.BookHolder> {
    private ArrayList<Book> booksList, originalSearchList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private User user;
    private boolean read;

    // data is passed into the constructor
    public BooksListFormAdapter(Context context, ArrayList<Book> booksList, User user, boolean read) {
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
        holder.bookCover.setImageResource(mInflater.getContext()
                .getResources().getIdentifier(book.getPicture(), "drawable", aux));
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
                    user.removeBookToLibrary(book);
                    Toast.makeText(view.getContext(), "Book removed from your library", Toast.LENGTH_SHORT).show();
                } else {
                    if (read) {
                        book.setRead(true);
                        user.addBookToLibrary(book);
                    } else {
                        book.setRead(false);
                        user.addBookToInterestedBooks(book);
                    }
                    Toast.makeText(view.getContext(), "Book added to your library", Toast.LENGTH_SHORT).show();
                }
                if (mClickListener != null) mClickListener.onItemClick(view);
            }
        });
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
