package com.example.readify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;

import com.example.readify.Popups.BookReadedPopup;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.ListIterator;

public class BooksListVerticalAdapter extends RecyclerView.Adapter<BooksListVerticalAdapter.BookHolder> {
    private ArrayList<Book> booksList, originalSearchList;
    private MainActivity activity;
    private Context mContext;
    private User user;
    private boolean isInReadingList;
    private FragmentManager fragmentManager;

    // Counstructor for the Class
    public BooksListVerticalAdapter(MainActivity activity, Context context, ArrayList<Book> booksList, User user) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.activity = activity;
        this.mContext = context;
        this.user = user;
    }

    public BooksListVerticalAdapter(Context context, ArrayList<Book> booksList, User user) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        //this.activity = activity;
        this.mContext = context;
        this.user = user;
    }

    public BooksListVerticalAdapter(MainActivity activity, Context context, ArrayList<Book> booksList, boolean isInReadingList, FragmentManager manager) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.mContext = context;
        this.activity = activity;
        this.isInReadingList = true;
        this.fragmentManager = manager;
    }

    public BooksListVerticalAdapter(MainActivity activity, Context context, ArrayList<Book> booksList) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.mContext = context;
        this.activity = activity;
        this.isInReadingList = false;
    }

    // Counstructor for the Class
    public BooksListVerticalAdapter(Context context, ArrayList<Book> booksList) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.mContext = context;
        this.isInReadingList = false;

    }

    public Context getContext(){
        return mContext;
    }

    public void setBooksList(ArrayList<Book> books){
        this.booksList = books;
    }

    public void deleteItem(int position){
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
        return booksList == null ? 0 : booksList.size();
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

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull final BookHolder holder, final int position) {
        final Book book = booksList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        String aux = mContext.getPackageName();
        holder.bookCover.setImageResource(
                mContext.getResources().getIdentifier(book.getPicture(), "drawable", mContext.getPackageName()));

        if(isInReadingList) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookReadedPopup dialog =  new BookReadedPopup(fragmentManager);
                    //dialog = new ProfileDialog(post.getGuide(), this, post.getPlace());
                    FragmentTransaction ft2 = fragmentManager.beginTransaction();
                    dialog.show(ft2, "book_readed_popup");
                    //dialog.show(ft2, "profile_fragment_popup")
                }
            });
        }
        //if (user != null && user.containsBook(book))
        //holder.addButton.setText("Remove");
        //else
        //holder.addButton.setText("Add");
        /*holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.containsBook(book)) {
                    user.removeBookToLibrary(book);
                    Toast.makeText(view.getContext(), "Book removed on your library", Toast.LENGTH_LONG).show();
                    //holder.addButton.setText("Add");
                } else {
                    user.addBookToLibrary(book);
                    Toast.makeText(view.getContext(), "Book added on your library", Toast.LENGTH_LONG).show();
                    //holder.addButton.setText("Remove");
                }
            }
        });*/
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class BookHolder extends RecyclerView.ViewHolder {

        private ImageView bookCover;
        private TextView bookTitle;
        private TextView bookAuthor;
        private ImageButton addButton;
        private CardView cardView;

        public BookHolder(View itemView) {
            super(itemView);

            bookCover = (ImageView) itemView.findViewById(R.id.book_cover_image_view);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);
            addButton = (ImageButton) itemView.findViewById(R.id.addButton);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
      
        /*public void setContactName(String name) {
            txtName.setText(name);
        }

        public void setContactNumber(String number) {
            txtNumber.setText(number);
        }*/
    }
}
