package com.example.readify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
ReadingFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
LibraryFragment.OnFragmentInteractionListener, DiscoverFragment.OnFragmentInteractionListener{


    private BottomNavigationView navigation;
    private final Fragment fragment1 = new ReadingFragment();
    private final Fragment fragment2 = new LibraryFragment();
    private final Fragment fragment3 = new DiscoverFragment();
    private final Fragment fragment4 = new ProfileFragment();
    private final FragmentManager fm = getSupportFragmentManager();

    private Fragment active = fragment1;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_read:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    //active = fragment1;
                    return true;
                case R.id.navigation_library:

                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    //active = fragment2;
                    return true;
                case R.id.navigation_discover:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    //active = fragment3;
                    return true;
                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    //active = fragment4;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //changeIcons(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
