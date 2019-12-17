package com.example.readify;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readify.Adapters.BooksHorizontalAdapter;
import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Models.Book;
import com.example.readify.Models.Item;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.ServerCallbackForBooks;
import com.example.readify.Models.User;
import com.example.readify.Popups.BookReadedPopup;
import com.example.readify.Popups.ReviewsPopup;
import com.example.readify.Popups.ShopsPopup;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookViewFragment extends Fragment {

    private Book book;
    private ArrayList<Book> prevoiusBooks;
    private ArrayList<Pages> parents;
    private View view;
    private ArrayList<Book> sameAuthorBooks;
    private List<Book> sameGenderBooks;
    private Pages parent;

    private User user;
    private SharedPreferences prefs;

    private OnFragmentInteractionListener mListener;

    public BookViewFragment() {
        // Required empty public constructor
    }

    public void setParent(Pages parent){
        if(this.parent != null)
            parents.add(this.parent);
        this.parent = parent;

    }

    public void setBook(Book book){
        if(this.book != null)
            prevoiusBooks.add(this.book);
        this.book = book;
        setContent();
    }

    public void setSameAuthorBooks(ArrayList<Book> books){
        //if(this.sameAuthorBooks )
        this.sameAuthorBooks = books;
        setSameAuthorBooksAdapter();
    }


    public void setSameGenderBooks(ArrayList<Book> books){
        this.sameGenderBooks = books;
        setSameGenderBooks();
    }


    public static BookViewFragment newInstance(Book book) {
        BookViewFragment fragment = new BookViewFragment();
        fragment.book = book;
        return fragment;
    }

    private void shopShopsPopup(){
        ApiConnector.getShopItemsByBookId(getContext(), book.getId(), new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
                    JSONArray array = result.getJSONArray("item");
                    ArrayList<Item> items = Item.getItemsFromJSONArray(array);
                    ShopsPopup dialog = new ShopsPopup(items, book);
                    FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                    dialog.show(ft2, "shops_popup");
                } catch (Exception e) {
                    System.out.println("Error parsing items");
                }
            }
        });
    }

    private void showReviewsPopup(){
        ReviewsPopup dialog =  new ReviewsPopup(this.book);
        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //dialog = new ProfileDialog(post.getGuide(), this, post.getPlace());
        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
        dialog.show(ft2, "reviews_fragment");
        //dialog.setW
        //dialog.setWindow(RelativeLayout.LayoutParams.MATCH_PARENT);
        //dialog.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user = MockupsValues.getUser();
        //user = new User();
        //user.readFromSharedPreferences(prefs);
        //User user = MockupsValues.getUser();

        prevoiusBooks = new ArrayList<>();
        parents = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_book_view, container, false);
        ImageView backButton = (ImageView) view.findViewById(R.id.go_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoBackButtonClicked();
            }
        });
        if(book != null){
           setContent();
        }

        ImageButton showComments = (ImageButton) view.findViewById(R.id.show_comment_button);
        showComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewsPopup();
            }
        });

        ImageButton showShops = (ImageButton) view.findViewById(R.id.show_shops_popup);
        showShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopShopsPopup();
            }
        });


        final ImageButton addBook = (ImageButton) view.findViewById(R.id.add_button);
        if(user.getInterested().contains(book)){
            Drawable drawable = ContextCompat.getDrawable(getContext(),
                    getContext().getResources().getIdentifier("ic_reading_white", "drawable", getContext().getPackageName()));
            //holder.addButton.setImageResource(R.drawable.ic_added_book);
            addBook.setImageDrawable(drawable);
        }
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!user.getInterested().contains(book)){
                    Drawable drawable = ContextCompat.getDrawable(getContext(),
                            getContext().getResources().getIdentifier("ic_reading_white", "drawable", getContext().getPackageName()));
                    //holder.addButton.setImageResource(R.drawable.ic_added_book);
                    addBook.setImageDrawable(drawable);
                    //Book book = mViewBooks.get(position);

                    ArrayList<Book> pending = user.getInterested();
                    pending.add(book);
                    user.setInterested(pending);
                    ArrayList<Book> library = user.getLibrary();
                    library.add(book);
                    user.setLibrary(library);
                    //MockupsValues.addPendingBook(book);
                    //String interestedToPref = new Gson().toJson(user.getInterested());
                    //prefs.edit().putString("com.example.readify.interested", interestedToPref).apply();

                    MainActivity activity = (MainActivity) getActivity();
                    activity.notifyPendingListChanged(user);
                    Toast.makeText(getContext(), book.getTitle() + " " + getContext().getString(R.string.book_added_correctly_message), Toast.LENGTH_LONG).show();

                } else {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.focusToReadingFragment();
                }
            }
        });


        /*Recyclers Views*/



        //sameAuthorBooks = MockupsValues.getSameAuthorBooks();
        /*LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAuthor.setLayoutManager(horizontalLayoutManager);
        BooksHorizontalAdapter adapterAuth = new BooksHorizontalAdapter((MainActivity) getActivity(),getContext(), sameAuthorBooks, false, user);
        adapterAuth.setClickListener(new BooksHorizontalAdapter.ItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {
               showBookFragment(sameAuthorBooks.get(position));
           }
        });*/
        //recyclerViewAuthor.setAdapter(adapterAuth);

        return view;
    }

    /*@Override
    public void onItemClick(View view, int position) {
        //showBookFragment();

    }*/

    private void setSameGenderBooks(){
        /*Same gender books*/
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gender_books_recycler_view);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        //sameGenderBooks = MockupsValues.getSameGenderBooks();
        LinearLayoutManager horizontalLayoutManagerGender
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagerGender);
        BooksHorizontalAdapter adapterGender = new BooksHorizontalAdapter((MainActivity) getActivity(), getContext(), sameGenderBooks, false, user);
        //adapterGender.setClickListener(this);
        adapterGender.setClickListener(new BooksHorizontalAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Book book, int size) {
                showBookFragment(sameGenderBooks.get(position));
            }
        });
        recyclerView.setAdapter(adapterGender);
    }

    private void setSameAuthorBooksAdapter(){
        /*Same author books*/
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerViewAuthor = (RecyclerView) view.findViewById(R.id.author_books_recycler_view);
        recyclerViewAuthor.setLayoutManager(horizontalLayoutManagaer);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAuthor.setLayoutManager(horizontalLayoutManager);
        BooksHorizontalAdapter adapterAuth = new BooksHorizontalAdapter((MainActivity) getActivity(),getContext(), sameAuthorBooks, false, user);
        adapterAuth.setClickListener(new BooksHorizontalAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Book book, int size) {
                showBookFragment(sameAuthorBooks.get(position));
            }
        });
        recyclerViewAuthor.setAdapter(adapterAuth);
    }

    private void showBookFragment(Book book){
        MainActivity activity = (MainActivity) getActivity();
        activity.goToBookPage(book, Pages.BOOK_VIEW_PAGE);
    }

    private void setBookCover(ImageView picture, String pictureUrl){
        Picasso.with(getContext()) // Context
                .load(pictureUrl) // URL or file
                .into(picture);
    }

    private void setContent(){
        //if(book != null)
        //    prevoiusBooks.add()
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        ImageView bookImageView = (ImageView) view.findViewById(R.id.book_cover_image_view);
        setBookCover(bookImageView, book.getPicture());
        //bookImageView.setImageResource(bookImageView.getContext().getResources().getIdentifier(book.getPicture(), "drawable", bookImageView.getContext().getPackageName()));
        TextView bookTitle = (TextView) view.findViewById(R.id.book_title);
        TextView bookAuthor = (TextView) view.findViewById(R.id.book_author);
        TextView textView = (TextView) view.findViewById(R.id.same_author_books_title);
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        String aux  = getString(R.string.more_books_of) + " " + book.getAuthor() + " :";
        textView.setText(aux);
        ImageButton addBook = (ImageButton) view.findViewById(R.id.add_button);
        if(user.getInterested().contains(book)){
            Drawable drawable = ContextCompat.getDrawable(getContext(),
                    getContext().getResources().getIdentifier("ic_reading_white", "drawable", getContext().getPackageName()));
            //holder.addButton.setImageResource(R.drawable.ic_added_book);
            addBook.setImageDrawable(drawable);
        }
        TextView genreName = (TextView) view.findViewById(R.id.text_genre_name);
        ImageView genreIcon = (ImageView) view.findViewById(R.id.genre_icon);
        TextView extensionNumber = (TextView) view.findViewById(R.id.extension_number);
        TextView ratingsAverage = (TextView) view.findViewById(R.id.ratings_average);

        TextView summatyTextView = (TextView) view.findViewById(R.id.book_summary_text_view);
        summatyTextView.setText(book.getSummary());

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        double ratings = book.getSumRatings() / book.getNumRatings();

        extensionNumber.setText(String.valueOf(book.getExtension()));
        ratingsAverage.setText(String.valueOf(decimalFormat.format(ratings)));
        genreName.setText(book.getGenre().getName());
        genreIcon.setImageResource(getContext().getResources().getIdentifier(book.getGenre().getPicture(), "drawable", getContext().getPackageName()));
        scrollView.setScrollY(0);
    }

    private void onGoBackButtonClicked(){
        if(!prevoiusBooks.isEmpty()) {
            book = prevoiusBooks.get(prevoiusBooks.size() - 1);
            parent = parents.get(parents.size() - 1);
            setContent();
            prevoiusBooks.remove(prevoiusBooks.size() - 1);
            parents.remove(parents.size() - 1);
        } else {
            this.book = null;
            prevoiusBooks = new ArrayList<>();
            goToParentPage();
            //activity.backToDiscoverFragment();
        }
    }

    public void goToParentPage(){
        MainActivity activity = (MainActivity) getActivity();
        switch (parent){
            case DISCOVER_PAGE:
                activity.backToDiscoverFragment();
                break;
            case LIBRARY_PAGE:
                activity.backToLibraryFragment();
                break;
            case PROFILE_PAGE:
                activity.backToProfileFragment();
                break;
            case READING_PAGE:
                activity.backToReadingFragment();
                break;
            default:
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
