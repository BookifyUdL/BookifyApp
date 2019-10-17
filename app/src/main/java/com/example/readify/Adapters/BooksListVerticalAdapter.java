package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Models.Book;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.ListIterator;

public class BooksListVerticalAdapter extends RecyclerView.Adapter<BooksListVerticalAdapter.BookHolder> {
    private ArrayList<Book> booksList, originalSearchList;
    private Context mContext;

    // Counstructor for the Class
    public BooksListVerticalAdapter(Context context, ArrayList<Book> booksList) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.book_list_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public int getItemCount() {
        return booksList == null? 0: booksList.size();
    }

    public void filter(String newText) {
        String toSearch = newText.toLowerCase();

        booksList = new ArrayList<>();
        booksList.addAll(originalSearchList);

        if (toSearch.length() == 0)
        {
            notifyDataSetChanged();
            return;
        }

        ListIterator<Book> itr = booksList.listIterator();
        while (itr.hasNext())
        {
            if (itr.next().getTitle().toLowerCase().contains(toSearch))
                continue;

            itr.remove();
        }

        notifyDataSetChanged();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, final int position) {
        final Book book = booksList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        String aux = mContext.getPackageName();
        holder.bookCover.setImageResource(
                mContext.getResources().getIdentifier(book.getPicture(), "drawable", mContext.getPackageName()));

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class BookHolder extends RecyclerView.ViewHolder {

        private ImageView bookCover;
        private TextView bookTitle;
        private TextView bookAuthor;

        public BookHolder(View itemView) {
            super(itemView);

            bookCover = (ImageView) itemView.findViewById(R.id.book_cover_image_view);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor= (TextView) itemView.findViewById(R.id.book_author);
        }



        /*public void setContactName(String name) {
            txtName.setText(name);
        }

        public void setContactNumber(String number) {
            txtNumber.setText(number);
        }*/
    }
}
