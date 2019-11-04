package com.example.readify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Toast;

import com.gsconrad.richcontentedittext.RichContentEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setupRichContentEditText();
    }

    private void setupRichContentEditText() {
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
    }

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

