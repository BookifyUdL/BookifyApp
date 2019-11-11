package com.example.readify;

import android.content.ClipDescription;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

public class RichEditText extends AppCompatEditText {
    private String[] imgTypeString;
    private KeyBoardInputCallbackListener keyBoardInputCallbackListener;
    private CommentActivity activity;

    public RichEditText(Context context) {
        super(context);
        initView();
    }

    public RichEditText(Context context, CommentActivity activity) {
        super(context);
        this.activity = activity;
        initView();



    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        imgTypeString = new String[]{"image/png",
                "image/gif",
                "image/jpeg",
                "image/webp"};
    }

    /*public Uri getUri(){
        /
    }*/

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        final InputConnection ic = super.onCreateInputConnection(outAttrs);
        EditorInfoCompat.setContentMimeTypes(outAttrs,
                imgTypeString);
        return InputConnectionCompat.createWrapper(ic, outAttrs, callback);
    }



    final InputConnectionCompat.OnCommitContentListener callback =
            new InputConnectionCompat.OnCommitContentListener() {
                private Uri uri;

                public Uri getUri(){
                    return uri;
                }

                @Override
                public boolean onCommitContent(InputContentInfoCompat inputContentInfo,
                                               int flags, Bundle opts) {


                    // read and display inputContentInfo asynchronously
                    ClipDescription description = inputContentInfo.getDescription();
                    //image/gif
                    //image/png

                    //CharSequence la = description.getLabel();
                    //int mimeTipeCount = description.getMimeTypeCount();
                    this.uri = inputContentInfo.getContentUri();
                    if(description.toString().contains("image/gif")){
                        activity.setGifView(this.uri);
                    } else {
                        activity.setImageView(this.uri);
                    }


                    //textView.setText(this.uri.toString());
                    if (BuildCompat.isAtLeastNMR1() && (flags &
                            InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                        try {
                            inputContentInfo.requestPermission();
                        } catch (Exception e) {
                            return false; // return false if failed
                        }
                    }
                    boolean supported = false;
                    for (final String mimeType : imgTypeString) {
                        if (inputContentInfo.getDescription().hasMimeType(mimeType)) {
                            supported = true;
                            break;
                        }
                    }
                    if (!supported) {
                        return false;
                    }

                    if (keyBoardInputCallbackListener != null) {
                        keyBoardInputCallbackListener.onCommitContent(inputContentInfo, flags, opts);
                    }
                    return true;  // return true if succeeded
                }
            };

    public interface KeyBoardInputCallbackListener {
        void onCommitContent(InputContentInfoCompat inputContentInfo,
                             int flags, Bundle opts);
    }

    public void setKeyBoardInputCallbackListener(KeyBoardInputCallbackListener keyBoardInputCallbackListener) {
        this.keyBoardInputCallbackListener = keyBoardInputCallbackListener;
    }

    public String[] getImgTypeString() {
        return imgTypeString;
    }

    public void setImgTypeString(String[] imgTypeString) {
        this.imgTypeString = imgTypeString;
    }
}
