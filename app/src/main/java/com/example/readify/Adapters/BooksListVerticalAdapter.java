package com.example.readify.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.ApiConnector;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.ServerCallbackForBooks;
import com.example.readify.Models.User;

import com.example.readify.Popups.BookReadedPopup;
import com.example.readify.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ListIterator;

import tyrantgit.explosionfield.ExplosionField;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BooksListVerticalAdapter extends RecyclerView.Adapter<BooksListVerticalAdapter.BookHolder> {
    private ArrayList<Book> booksList, originalSearchList;
    private MainActivity activity;
    private Context mContext;
    private User user;
    private boolean isInReadingList;
    private boolean isInPendingList = false;
    private FragmentManager fragmentManager;
    private SharedPreferences prefs;

    // Counstructors for the Class
    /* Pending */
    public BooksListVerticalAdapter(MainActivity activity, Context context, ArrayList<Book> booksList, User user) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.activity = activity;
        this.mContext = context;
        this.isInReadingList = false;
        this.user = user;
        this.prefs = context.getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
    }

    /* Reading */
    public BooksListVerticalAdapter(MainActivity activity, Context context, ArrayList<Book> booksList, FragmentManager manager, User user) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.activity = activity;
        this.mContext = context;
        this.isInReadingList = true;
        this.fragmentManager = manager;
        this.user = user;
        this.prefs = context.getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
    }

    public BooksListVerticalAdapter(Context context, ArrayList<Book> booksList, User user) {
        this.booksList = booksList;
        this.originalSearchList = new ArrayList<>();
        this.originalSearchList.addAll(booksList);
        this.mContext = context;
        this.isInReadingList = false;
        this.user = user;
        this.prefs = context.getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
    }

    public void setIsInPendingList(boolean bool) {
        this.isInPendingList = bool;
    }

    public Context getContext() {
        return mContext;
    }

    public void setBooksList(ArrayList<Book> books) {
        this.booksList = books;
    }

    public void deleteItem(int position) {
        if (!booksList.isEmpty()) {
            booksList.remove(position);
            if (isInReadingList) {
                user.setReading(booksList);

                String readingToPref = new Gson().toJson(user.getReading());
                prefs.edit().putString("com.example.readify.reading", readingToPref).apply();
            } else {
                user.setInterested(booksList);

                String interestedToPref = new Gson().toJson(user.getInterested());
                prefs.edit().putString("com.example.readify.interested", interestedToPref).apply();
            }

            ApiConnector.updateUser(getApplicationContext(), new ServerCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    Toast.makeText(getApplicationContext(), "Book added correctly to library", Toast.LENGTH_LONG).show();
                }
            }, user);

            activity.notifyReadingListChanged(user);
            activity.notifyPendingListChanged(user);
        }

        notifyDataSetChanged();
    }

    public void readingListChanged(int position) {
        Book book = booksList.get(position);

        ArrayList<Book> reading = user.getReading();
        ArrayList<Book> pending = user.getInterested();

        reading.add(book);
        pending.remove(position);

        user.setReading(reading);
        user.setInterested(pending);

        //Save data to Database
        ApiConnector.updateUser(getApplicationContext(), new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(getApplicationContext(), "Book added correctly to library", Toast.LENGTH_LONG).show();
            }
        }, user);

        activity.notifyPendingListChanged(user);
        activity.notifyReadingListChanged(user);

        notifyDataSetChanged();
    }

    public void pendingListChanged(int position) {
        Book book = booksList.get(position);

        ArrayList<Book> reading = user.getReading();
        ArrayList<Book> pending = user.getInterested();

        pending.add(book);
        reading.remove(position);

        user.setReading(reading);
        user.setInterested(pending);

        //Save data to Database
        ApiConnector.updateUser(getApplicationContext(), new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(getApplicationContext(), "Book added correctly to library", Toast.LENGTH_LONG).show();
            }
        }, user);

        activity.notifyReadingListChanged(user);
        activity.notifyPendingListChanged(user);

        notifyDataSetChanged();
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.book_list_item, parent, false);
        //prefs = view.getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
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
        setBookCover(holder, book.getPicture());

        if (isInReadingList || isInPendingList)
            holder.addButton.setVisibility(View.GONE);

        if (isInReadingList) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ApiConnector.getBookById(getContext(), book.getId(), new ServerCallbackForBooks() {
                        @Override
                        public void onSuccess(ArrayList<ArrayList<Book>> books) {

                        }

                        @Override
                        public void onSuccess(Book b) {
                            BookReadedPopup dialog = new BookReadedPopup(activity, holder, fragmentManager, b, user);
                            FragmentTransaction ft2 = fragmentManager.beginTransaction();
                            dialog.show(ft2, "book_readed_popup");
                        }
                    });
                }
            });
            /* Personalized search */
        } else {
            if (user.getLibrary().contains(booksList.get(position))) {
                setAddButtonIcon(holder);
            } else {
                holder.addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setAddButtonIcon(holder);

                        Book book = booksList.get(position);
                        /* Add book to interested books */
                        ArrayList<Book> pending = user.getInterested();
                        pending.add(book);
                        user.setInterested(pending);

                        String interestedToPref = new Gson().toJson(user.getInterested());
                        prefs.edit().putString("com.example.readify.interested", interestedToPref).apply();

                        activity.notifyPendingListChanged(user);

                        /* Add book to library */
                        ArrayList<Book> library = user.getLibrary();
                        library.add(book);
                        user.setLibrary(library);

                        String libraryToPref = new Gson().toJson(user.getLibrary());
                        prefs.edit().putString("com.example.readify.library", libraryToPref).apply();

                        activity.notifyLibraryListChanged(user, true);

                        Toast.makeText(getContext(), book.getTitle() + " " + getContext().getString(R.string.book_added_correctly_message), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    private void setBookCover(@NonNull final BooksListVerticalAdapter.BookHolder holder, String picture){
        Picasso.with(mContext) // Context
                .load(picture) // URL or file
                .into(holder.bookCover);
    }

    private void setAddButtonIcon(BookHolder holder){
        holder.addButton.setImageResource(R.drawable.ic_added_book);
    }

    // This is your ViewHolder class that helps to populate data to the view
    public class BookHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView bookCover;
        private TextView bookTitle;
        private TextView bookAuthor;
        private ImageButton addButton;
        private CardView cardView;

        public BookHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            bookCover = (ImageView) itemView.findViewById(R.id.book_cover_image_view);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);
            addButton = (ImageButton) itemView.findViewById(R.id.addButton);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

        public void setBookTitle(String title){
            bookTitle.setText(title);

        }

        public void destroyView() {
            ExplosionField explosionField = ExplosionField.attach2Window(activity);
            explosionField.explode(itemView);
        }
    }
}
