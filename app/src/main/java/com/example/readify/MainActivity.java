package com.example.readify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.readify.Discover.DiscoverFragment;
import com.example.readify.Library.LibraryFragment;
import com.example.readify.Profile.ProfileFragment;
import com.example.readify.Reading.ReadingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
ReadingFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
LibraryFragment.OnFragmentInteractionListener, DiscoverFragment.OnFragmentInteractionListener,
SearchBookFragment.OnFragmentInteractionListener{


    private BottomNavigationView navigation;
    private final Fragment fragment1 = new ReadingFragment();
    private final Fragment fragment2 = new LibraryFragment();
    private  Fragment fragment3 = DiscoverFragment.newInstance();
    private final Fragment fragment4 = new ProfileFragment();
    private Fragment fragment5 = new SearchBookFragment();
    private final FragmentManager fm = getSupportFragmentManager();

    private Fragment active = fragment1;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_reading_fragment:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_library_fragment:

                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.navigation_discover_fragment:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_profile_fragment:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        }
    };

    public void changeDiscoverFragment(){
        //fragment3 = new SearchBookFragment();
        fm.beginTransaction().hide(active).show(fragment5).commit();
        active = fragment5;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.main_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //changeIcons(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
