package com.example.readify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.readify.Models.Review;
import com.example.readify.Models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gsconrad.richcontentedittext.RichContentEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class CommentActivity extends AppCompatActivity implements RichEditTextInterface {

    private static final String TAG = "MainActivity";
    private User user;
    LinearLayout linearLayout;
    RichEditText editText;
    ImageView imageView;
    ImageView deleteButton;
    Button publish;
    Uri commentUri;
    CommentType commentType;
    boolean isEditMode = false;
    int position;
    //RichEditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
        setContentView(R.layout.activity_comment);
        setOnCloseButtonClicked();
        setupRichContentEditText();
        Intent intent =getIntent();
        Bundle extrasBundle = intent.getExtras();
        if(extrasBundle != null){
            isEditMode = true;
            String comment = extrasBundle.getString(Review.COMMENT_PARAMETER);
            position = extrasBundle.getInt(Review.POSITION_PARAMETER);
            commentType = (CommentType) extrasBundle.get(Review.COMMENT_TYPE_PARAMETER);
            Uri uri = (Uri) extrasBundle.get(Review.URI_PARAMETER);
            if(commentType == CommentType.COMMENT_AND_IMAGE)
                setImageView(uri);
            if(commentType == CommentType.COMMENT_AND_GIF)
                setGifView(uri);
            editText.setText(comment);
        }
    }

    private void setOnCloseButtonClicked(){
        ImageView closeButton = findViewById(R.id.close_arrow);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }

    private void finishActivity(){
        this.finish();
    }

    public void setGifView(Uri uri){
        commentUri = uri;
        commentType = CommentType.COMMENT_AND_GIF;
        checkIfImageViewIsAdded();
        Glide.with(getApplicationContext()) // replace with 'this' if it's in activity
        .load(uri.toString())
        .asGif()
        .error(R.drawable.angry) // show error drawable if the image is not a gif
        .into(imageView);
        enablePublishButton();
    }

    public void setImageView(Uri uri){
        commentUri = uri;
        commentType = CommentType.COMMENT_AND_IMAGE;
        checkIfImageViewIsAdded();
        Glide.with(getApplicationContext())
                .load(uri.toString())
                .asBitmap()
                .error(R.drawable.angry)
                .into(imageView);
        enablePublishButton();


    }

    private void checkIfImageViewIsAdded(){
        if(imageView == null){
            RelativeLayout relativeLayout  = new RelativeLayout(getApplicationContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(params);
            imageView = new ImageView(getApplicationContext());
            imageView.setId(View.generateViewId());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.image_gif_height), (int) getResources().getDimension(R.dimen.image_gif_height));
            layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.margin_5dp), 0, 0 );
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            //layoutParams.addRule(Gravity.CENTER_HORIZONTAL);
            //layoutParams.addRule(RelativeLayout.BELOW, editText.getId());
            imageView.setLayoutParams(layoutParams);
            relativeLayout.addView(imageView);
            linearLayout.addView(relativeLayout);
            addDeleteImageGifButton(relativeLayout);
        } else if (imageView.getVisibility() == View.INVISIBLE) {
            imageView.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    private void addDeleteImageGifButton(RelativeLayout relativeLayout){
        deleteButton = new ImageView(getApplicationContext());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButton.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                commentUri = null;
                commentType = CommentType.COMMENT;
                disablePublishButton();
            }
        });
        deleteButton.setImageResource(R.drawable.ic_close_button);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // ALign right per ficar-lo a sobre
        layoutParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        deleteButton.setLayoutParams(layoutParams);
        relativeLayout.addView(deleteButton);
    }

    private void setupRichContentEditText(){
        publish = findViewById(R.id.info_text);
        editText = new RichEditText(this.getApplicationContext(), this);
        editText.setHint(R.string.add_comment);
        editText.setId(View.generateViewId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setLayoutParams(params);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                   enablePublishButton();
                } else {
                   disablePublishButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        linearLayout= findViewById(R.id.comment_layout);
        linearLayout.addView(editText);
    }

    private void enablePublishButton(){
        publish.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_publish_enabled));
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = editText.getText().toString();
                user.readFromSharedPreferences(getSharedPreferences("com.example.readify", Context.MODE_PRIVATE));
                Review review;

                if(commentUri == null){
                    review  = new Review(user, comment);
                } else {
                    review = new Review(user, comment, commentType, commentUri);
                }

                if(!isEditMode){
                    MockupsValues.addReview(review);
                } else {
                    MockupsValues.setReview(review, position);
                }
                finishActivity();
            }
        });
    }

    private void disablePublishButton(){
        publish.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_publish));
        publish.setOnClickListener(null);
    }
}

