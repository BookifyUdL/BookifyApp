package com.example.readify.Popups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.ReviewsVerticalAdapter;
import com.example.readify.ApiConnector;
import com.example.readify.CommentActivity;
import com.example.readify.FirstTimeForm.FirstTimeFormActivity;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Review;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.example.readify.RichEditTextInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsPopup extends DialogFragment implements Popup{

    private ReviewsVerticalAdapter pendingBooksAdapter;
    private ScrollView scrollView;
    private LinearLayout commentLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton addCommentButton;
    private User user;
    private SharedPreferences prefs;
    final private Book book;
    private LinearLayout emptyMessage;

    public ReviewsPopup(Book book){
        this.book = book;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reviews_layout, container);
        recyclerView = (RecyclerView) view.findViewById(R.id.comments_recyclerView);
        emptyMessage = (LinearLayout) view.findViewById(R.id.empty_message_layout);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        commentLayout = (LinearLayout) view.findViewById(R.id.comment_layout);
        addCommentButton = (FloatingActionButton) view.findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CommentActivity commentActivity = new CommentActivity();
                //commentActivity.setBook(book);
                MockupsValues.setCurrentBookViewing(book);
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                //intent.putExtra("Book", book);
                startActivity(intent);
            }
        });
        ImageButton closeArrow = (ImageButton) view.findViewById(R.id.close_arrow);
        closeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        return view;
    }

    public boolean hide(TextView v, int actionId ){
        if((actionId == EditorInfo.IME_ACTION_SEND)){
            addReview(v.getText().toString());
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user = MockupsValues.getUser();
        //user = new User();
        //user.readFromSharedPreferences(prefs);
        if(book.getComments().size() > 0){
            LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(vlm);
            final ArrayList<Review> reviews = book.getComments();
            final ArrayList<Review> finalReviews = new ArrayList<>();
            for(int i = 0; i < reviews.size(); i++){
                final int aux = i;
                ApiConnector.getUser(getContext(), reviews.get(i).userId, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Review rev = reviews.get(aux);
                        rev.setUser(new User(result));
                        finalReviews.add(rev);
                        if(aux + 1 == reviews.size()){
                            pendingBooksAdapter = new ReviewsVerticalAdapter((MainActivity) getActivity(), getContext(), finalReviews, addCommentButton, user);
                            recyclerView.setAdapter(pendingBooksAdapter);
                        }
                    }
                });
            }
            //reviews.addAll(MockupsValues.getReviews());
        } else {
            emptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

    }

    private void addReview(String message){
        pendingBooksAdapter.addReview(new Review(user, message));
        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(pendingBooksAdapter.getItemCount()-1,200);
        Toast.makeText(getContext(), getContext().getString(R.string.comment_added_correctly), Toast.LENGTH_LONG).show();
    }

    public void close(){
        this.dismiss();
    }
}
