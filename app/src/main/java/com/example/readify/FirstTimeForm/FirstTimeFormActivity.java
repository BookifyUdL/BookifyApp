package com.example.readify.FirstTimeForm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstTimeFormActivity extends AppCompatActivity
        implements GenresFragment.OnFragmentInteractionListener, ComunicateFragmentsFirstForm {

    private final Fragment genreFragment = new GenresFragment();
    private final FragmentManager fm = getSupportFragmentManager();

    private static final String USERS = "users";

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_form);
        MockupsValues.setContext(this.getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);

        fm.beginTransaction().add(R.id.main_container_first_form,genreFragment, "1").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void exitForm(User user) {
        databaseReference.child(user.getUid()).setValue(user);
        Intent intent = new Intent(FirstTimeFormActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void doneForm(User user) {
        databaseReference.child(user.getUid()).setValue(user);
        Toast.makeText(this, "Form completed correctly", Toast.LENGTH_LONG).show();
        MockupsValues.user = user;
        Intent intent = new Intent(FirstTimeFormActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
