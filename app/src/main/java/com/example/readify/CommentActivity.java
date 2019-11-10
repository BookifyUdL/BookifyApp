package com.example.readify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsconrad.richcontentedittext.RichContentEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RichEditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setOnCloseButtonClicked();
        setupRichContentEditText();
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(0, 0);
        /*editText.requestFocus();
        if(editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }*/

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

    public void setWebView(Uri uri){
        //WebView webView = findViewById(R.id.webView);
        //webView.loadUrl(uri.toString());
        /*ImageView img = findViewById(R.id.imageView);
        Glide.with(getApplicationContext()) // replace with 'this' if it's in activity
        .load(uri.toString())
        .asGif()
        .error(R.drawable.angry) // show error drawable if the image is not a gif
        .into(img);*/
    }

    public void setImageView(Uri uri){
        /*ImageView img = findViewById(R.id.imageView);
        Glide.with(getApplicationContext())
                .load(uri.toString())
                .asBitmap()
                .error(R.drawable.angry)
                .into(img);*/


    }

    private void setupRichContentEditText(){
        //EditText textView = findViewById(R.id.textamen);
        editText = new RichEditText(getApplicationContext(), this);
        editText.setHint(R.string.add_comment);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        editText.setLayoutParams(params);
        //editText.setFocusedByDefault(true);

        LinearLayout linearLayout= findViewById(R.id.comment_layout);
        linearLayout.addView(editText);

        /*editText.requestFocus();
        final  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                editText.requestFocus();
                imm.showSoftInput(editText, 0);
            }
        }, 100);*/


        /*EditText editText = findViewById(R.id.edit_text);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputConnection ic = editText.onCreateInputConnection(new EditorInfo());
                    keyboard.setInputConnection(ic); // custom keyboard method
                }
            }
        });
        //editText.onCreateInputConnection()
        /*editText = new EditText(this) {
            @Override
            public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
                final InputConnection ic = super.onCreateInputConnection(editorInfo);
                EditorInfoCompat.setContentMimeTypes(editorInfo,
                        new String [] {"image/png"});

                final InputConnectionCompat.OnCommitContentListener callback =
                        new InputConnectionCompat.OnCommitContentListener() {
                            @Override
                            public boolean onCommitContent(InputContentInfoCompat inputContentInfo,
                                                           int flags, Bundle opts) {
                                // read and display inputContentInfo asynchronously
                                if (BuildCompat.isAtLeastNMR1() && (flags &
                                        InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                                    try {
                                        inputContentInfo.requestPermission();
                                    }
                                    catch (Exception e) {
                                        return false; // return false if failed
                                    }
                                }

                                // read and display inputContentInfo asynchronously.
                                // call inputContentInfo.releasePermission() as needed.

                                return true;  // return true if succeeded
                            }
                        };
                return InputConnectionCompat.createWrapper(ic, editorInfo, callback);
            }
        };*/

    }

    /*private void setupRichContentEditText() {
        RichContentEditText editText = findViewById(R.id.edit_text);
        // The following line sets the listener that is called when rich content is received
        editText.setOnRichContentListener(new RichContentEditText.OnRichContentListener() {
            // Called when a keyboard sends rich content
            @Override
            public void onRichContent(Uri contentUri, ClipDescription description) {
                if (description.getMimeTypeCount() > 0) {
                    final String fileExtension = MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(description.getMimeType(0));
                    final String filename = "filenameGoesHere." + fileExtension;
                    final File richContentFile = new File(getFilesDir(), filename);
                    // See the library example app for an implementation of writeToFileFromContentUri
                    if (!writeToFileFromContentUri(richContentFile, contentUri)) {
                        // We are in the background right now, make sure to run this in the foreground
                        Toast.makeText(CommentActivity.this,
                                "fallo al copiarr ", Toast.LENGTH_LONG).show();
                    } else {
                        // We are in the background right now, make sure to run this in the foreground

                        final WebView displayView = findViewById(R.id.display_view);
                        displayView.post(new Runnable() {
                            @Override
                            public void run() {
                                displayView.loadUrl(richContentFile.getAbsolutePath());
                            }
                        });
                    }
                }
            }
        });
    }*/

    public boolean writeToFileFromContentUri(File file, Uri uri) {
        if (file == null || uri == null) return false;
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
            OutputStream output = new FileOutputStream(file);
            if (stream == null) return false;
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = stream.read(buffer)) != -1) output.write(buffer, 0, read);
            output.flush();
            output.close();
            stream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Couldn't open stream: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException on stream: " + e.getMessage());
        }
        return false;
    }
}

