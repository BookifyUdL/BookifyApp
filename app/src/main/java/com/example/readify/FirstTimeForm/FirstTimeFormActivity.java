package com.example.readify.FirstTimeForm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.readify.ApiConnector;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.User;
import com.example.readify.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class FirstTimeFormActivity extends AppCompatActivity
        implements GenresFragment.OnFragmentInteractionListener, ComunicateFragmentsFirstForm {

    private final Fragment genreFragment = new GenresFragment();
    private final FragmentManager fm = getSupportFragmentManager();

    private static final String USERS = "users";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_form);

        // Get all genres for the first form tutorial
        ApiConnector.getGenres(getApplicationContext(), new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println("The obtaining of the genres works correctly");
                MockupsValues.setContext(getApplicationContext());

                // Get all books for the first form tutorial
                ApiConnector.getAllBooks(getApplicationContext(), new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println("The obtaining of the books works correctly");

                        // Get all users for the application
                        ApiConnector.getAllUsers(getApplicationContext(), new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference(USERS);
                                fm.beginTransaction().add(R.id.main_container_first_form, genreFragment, "1").commit();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void exitForm(User user) {
        // Save to Firebase and static content
        databaseReference.child(user.getFirebaseId()).setValue(user);
        MockupsValues.user = user;

        Toast.makeText(this, "Skipped the form", Toast.LENGTH_LONG).show();

        // Go to main page of the app
        Intent intent = new Intent(FirstTimeFormActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void doneForm(User user) {
        // Save to Firebase and static content
        databaseReference.child(user.getFirebaseId()).setValue(user);
        MockupsValues.user = user;

        Toast.makeText(this, "Form completed correctly", Toast.LENGTH_LONG).show();

        // Go to main page of the app
        Intent intent = new Intent(FirstTimeFormActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
