package com.example.readify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.readify.Discover.DiscoverFragment;
import com.example.readify.Library.LibraryFragment;
import com.example.readify.Models.Book;
import com.example.readify.Profile.ProfileFragment;
import com.example.readify.Reading.ReadingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
ReadingFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
LibraryFragment.OnFragmentInteractionListener, DiscoverFragment.OnFragmentInteractionListener,
SearchBookFragment.OnFragmentInteractionListener, BookViewFragment.OnFragmentInteractionListener{


    private BottomNavigationView navigation;
    private final ReadingFragment fragment1 = new ReadingFragment();
    private final Fragment fragment2 = new LibraryFragment();
    private  Fragment fragment3 = DiscoverFragment.newInstance();
    private final Fragment fragment4 = new ProfileFragment();
    private Fragment fragment5 = new SearchBookFragment();
    private BookViewFragment fragment6 = new BookViewFragment();
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

    public void notifyReadingListChanged() { fragment1.readingBooksChanged();}

    public void notifyPendingListChanged(){
        fragment1.pendingListChanged();
    }

    public void changeDiscoverFragment(){
        fragment5.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment5.setExitTransition(new Slide(Gravity.TOP));
        fm.beginTransaction().hide(active).show(fragment5).commit();
        active = fragment5;
    }

    public void backToDiscoverFragment(){
        fm.beginTransaction().hide(active).show(fragment3).commit();
        active = fragment3;
    }

    public void goToBookPage(Book book){
        fragment6.setBook(book);
        fragment6.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment6.setExitTransition(new Slide(Gravity.TOP));
        fm.beginTransaction().hide(active).show(fragment6).commit();
        active = fragment6;
    }

    public void focusDiscoverFragment(){
        fm.beginTransaction().hide(active).show(fragment3).commit();
        navigation.getMenu().getItem(2).setChecked(true);
        active = fragment3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.main_container, fragment6, "6").hide(fragment6).commit();
        fm.beginTransaction().add(R.id.main_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();
        navigation = findViewById(R.id.navigation);
        //changeIcons(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
