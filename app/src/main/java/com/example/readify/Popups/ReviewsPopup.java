package com.example.readify.Popups;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.ReviewsVerticalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Review;
import com.example.readify.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsPopup extends DialogFragment implements Popup {

    private ReviewsVerticalAdapter pendingBooksAdapter;
    private ScrollView scrollView;
    private LinearLayout commentLayout;
    private RecyclerView recyclerView;

    public ReviewsPopup(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reviews_layout, container);
        LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.comments_recyclerView);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        commentLayout = (LinearLayout) view.findViewById(R.id.comment_layout);
        recyclerView.setLayoutManager(vlm);
        ArrayList<Review> pendingBooksList = new ArrayList<>();
        pendingBooksList.addAll(MockupsValues.getReviews());
        pendingBooksAdapter = new ReviewsVerticalAdapter((MainActivity) getActivity(), getContext(), pendingBooksList);
        recyclerView.setAdapter(pendingBooksAdapter);
        ImageButton closeArrow = (ImageButton) view.findViewById(R.id.close_arrow);
        closeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        TextView userName = (TextView) view.findViewById(R.id.user_name);
        userName.setText(MockupsValues.user.getName());
        CircleImageView userPicture = (CircleImageView) view.findViewById(R.id.profile_image);
        userPicture.setImageResource(
                getContext().getResources().getIdentifier(MockupsValues.user.getPicture(), "drawable", getContext().getPackageName()));



        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return hide(v,  actionId,  event);

            }
        });
        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_comment);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReview(editText.getText().toString());

            }
        });
        //this.dismiss();
        return view;
    }

    public boolean hide(TextView v, int actionId, KeyEvent event){
        if((actionId == EditorInfo.IME_ACTION_SEND)){
            addReview(v.getText().toString());
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            //InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

            return true;

        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        //TODO:
    }

    private void addReview(String message){
        pendingBooksAdapter.addReview(new Review(MockupsValues.getUser(), message));
        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(pendingBooksAdapter.getItemCount()-1,200);
        //recyclerView.setScrollY(recyclerView.getHeight());
        //commentLayout.setVisibility(View.INVISIBLE);
        //recyclerView.scrollToPosition(pendingBooksAdapter.getItemCount() - 1);
        //recyclerView.smoothScrollToPosition(pendingBooksAdapter.getItemCount());
        //scrollView.setScrollY(scrollView.getHeight());
    }

    public void close(){
        this.dismiss();
    }
}
