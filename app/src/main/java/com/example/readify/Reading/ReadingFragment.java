package com.example.readify.Reading;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.SwipeToReadOrDeleteCallback;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.R;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    BooksListVerticalAdapter pendingBooksAdapter;
    BooksListVerticalAdapter readingBooksAdapter;
    SwipeActionAdapter readingBooksSwipeAdapter;

    private OnFragmentInteractionListener mListener;

    public ReadingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReadingFragment newInstance() {
        ReadingFragment fragment = new ReadingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);
        LinearLayout discoverButton = view.findViewById(R.id.discover_layout);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.focusDiscoverFragment();
            }
        });
        /*RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        BooksListVerticalAdapter adapter = new BooksListVerticalAdapter(MockupsValues.getLastAddedBooks(), getContext());
        recyclerView.setAdapter(adapter);*/
        /*Llista baix*/
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reading_books_recycler_view);
        recyclerView.setLayoutManager(verticalLayoutManagaer);
        ArrayList<Book> list = new ArrayList<>();
        //list.add(MockupsValues.getLastAddedBooks().get(0));
        readingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(), list);
        recyclerView.setAdapter(readingBooksAdapter);
        ItemTouchHelper itemTouchHelperReading = new ItemTouchHelper(new SwipeToReadOrDeleteCallback(readingBooksAdapter, false));
        itemTouchHelperReading.attachToRecyclerView(recyclerView);

        LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.pending_books_recycler_view);
        recyclerView2.setLayoutManager(vlm);
        ArrayList<Book> pendingBooksList = new ArrayList<>();
        pendingBooksList.addAll(MockupsValues.getPendingListBooks());
        pendingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(), pendingBooksList);
        recyclerView2.setAdapter(pendingBooksAdapter);
        ItemTouchHelper itemTouchHelperDelete = new ItemTouchHelper(new SwipeToReadOrDeleteCallback(pendingBooksAdapter, true));
        itemTouchHelperDelete.attachToRecyclerView(recyclerView2);
        return view;
    }

    public void readingBooksChanged(){
        ArrayList<Book> readingBooks = MockupsValues.getReandingListBooks();
        readingBooksAdapter.setBooksList(readingBooks);
        readingBooksAdapter.notifyDataSetChanged();
    }

    public void pendingListChanged(){
        ArrayList<Book> pendingBooks = MockupsValues.getPendingListBooks();
        pendingBooksAdapter.setBooksList(pendingBooks);
        pendingBooksAdapter.notifyDataSetChanged();

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
