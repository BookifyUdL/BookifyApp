package com.example.readify.FirstTimeForm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.readify.MainActivity;
import com.example.readify.R;

public class FirstTimeFormActivity extends AppCompatActivity
        implements GenresFragment.OnFragmentInteractionListener, ComunicateFragmentsFirstForm {

    private final Fragment genreFragment = new GenresFragment();
    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_form);

        fm.beginTransaction().add(R.id.main_container_first_form,genreFragment, "1").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void exitForm() {
        Intent intent = new Intent(FirstTimeFormActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void doneForm() {
        Toast.makeText(this, "Form completed correctly", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(FirstTimeFormActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
