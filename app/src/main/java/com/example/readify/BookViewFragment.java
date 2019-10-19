package com.example.readify;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readify.Adapters.BooksHorizontalAdapter;
import com.example.readify.Models.Book;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookViewFragment extends Fragment implements  BooksHorizontalAdapter.ItemClickListener{

    private Book book;
    private View view;
    private List<Book> sameAuthorBooks;
    private List<Book> samgeGenderBooks;

    private OnFragmentInteractionListener mListener;

    public BookViewFragment() {
        // Required empty public constructor
    }

    public void setBook(Book book){
        this.book = book;
        setContent();
    }


    public static BookViewFragment newInstance(Book book) {
        BookViewFragment fragment = new BookViewFragment();
        fragment.book = book;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        /*Recyclers Views*/
        /*Same author books*/
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewAuthor = (RecyclerView) view.findViewById(R.id.author_books_recycler_view);
        recyclerViewAuthor.setLayoutManager(horizontalLayoutManagaer);
        sameAuthorBooks = MockupsValues.getSameAuthorBooks();
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAuthor.setLayoutManager(horizontalLayoutManager);
        BooksHorizontalAdapter adapterAuth = new BooksHorizontalAdapter(getContext(), sameAuthorBooks, false);
        adapterAuth.setClickListener(this);
        recyclerViewAuthor.setAdapter(adapterAuth);

        /*Same gender books*/
        //LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gender_books_recycler_view);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        samgeGenderBooks = MockupsValues.getSameGenderBooks();
        LinearLayoutManager horizontalLayoutManagerGender
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagerGender);
        BooksHorizontalAdapter adapterGender = new BooksHorizontalAdapter(getContext(), samgeGenderBooks, false);
        adapterGender.setClickListener(this);
        recyclerView.setAdapter(adapterGender);


        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        /*if(position == list.size()-1){
            showSearchFragment();
        } else {
            showBookFragment(list.get(position));
        }*/
    }

    private void setContent(){
        ImageView bookImageView = (ImageView) view.findViewById(R.id.book_cover_image_view);
        bookImageView.setImageResource(bookImageView.getContext().getResources().getIdentifier(book.getPicture(), "drawable", bookImageView.getContext().getPackageName()));
        TextView bookTitle = (TextView) view.findViewById(R.id.book_title);
        TextView bookAuthor = (TextView) view.findViewById(R.id.book_author);
        TextView textView = (TextView) view.findViewById(R.id.same_author_books_title);
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        String aux  = textView.getText() + book.getAuthor() + " :";
        textView.setText(aux);
    }

    private void onGoBackButtonClicked(){
        MainActivity activity = (MainActivity) getActivity();
        activity.backToDiscoverFragment();
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
