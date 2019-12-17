package com.example.readify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.readify.Discover.BooksSectionFragment;
import com.example.readify.Discover.DiscoverFragment;
import com.example.readify.Discover.UsersSectionFragment;
import com.example.readify.FirstTimeForm.FirstTimeFormActivity;
import com.example.readify.Library.LibraryFragment;
import com.example.readify.Login.LoginActivity;
import com.example.readify.Models.Book;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.ServerCallbackForBooks;
import com.example.readify.Models.User;
import com.example.readify.Profile.ProfileFragment;
import com.example.readify.Reading.ReadingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        ReadingFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        LibraryFragment.OnFragmentInteractionListener, DiscoverFragment.OnFragmentInteractionListener,
        SearchBookFragment.OnFragmentInteractionListener, BookViewFragment.OnFragmentInteractionListener,
        MainActivityLogOut, SearchBookFragment.ComunicateFragments,
        ConnectivityReceiver.ConnectivityReceiverListener{

    private BottomNavigationView navigation;
    private ReadingFragment fragment1 = new ReadingFragment();
    private LibraryFragment fragment2 = new LibraryFragment();
    private Fragment fragment3 = DiscoverFragment.newInstance();
    private ProfileFragment fragment4 = new ProfileFragment();
    private Fragment fragment5 = new SearchBookFragment();
    private BookViewFragment fragment6 = new BookViewFragment();
    private BooksSectionFragment fragment7 = new BooksSectionFragment();
    private FragmentManager fm = getSupportFragmentManager();
    private BroadcastReceiver connectivityReceiver = null;
    private SharedPreferences pref;

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
                    fragment4.setUserMain();
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        }
    };

    public void notifyLibraryListChanged(User user) {
        user.saveToFirebase();
        fragment2.notifyLibraryChanged();
    }

    public void notifyReadingListChanged(User user) {
        user.saveToFirebase();
        fragment1.readingBooksChanged();
    }

    public void notifyPendingListChanged(User user) {
        user.saveToFirebase();

        fragment1.pendingListChanged();
        notifyLibraryListChanged(user);
    }

    public void changeSearchBookFragment() {
        fragment5.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment5.setExitTransition(new Slide(Gravity.TOP));
        fm.beginTransaction().hide(active).show(fragment5).commit();
        active = fragment5;
    }

    public void changeDiscoverFragment() {
        fragment3.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment3.setExitTransition(new Slide(Gravity.TOP));
        fm.beginTransaction().hide(active).show(fragment3).commit();
        active = fragment3;
    }

    public void changeProfileFragment() {
        fragment4.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment4.setExitTransition(new Slide(Gravity.TOP));
        fm.beginTransaction().hide(active).show(fragment4).commit();
        active = fragment4;
    }

    public void backToDiscoverFragment() {
        fm.beginTransaction().hide(active).show(fragment3).commit();
        active = fragment3;
    }

    public void backToLibraryFragment() {
        fm.beginTransaction().hide(active).show(fragment2).commit();
        active = fragment2;
    }

    public void backToProfileFragment() {
        fm.beginTransaction().hide(active).show(fragment4).commit();
        active = fragment4;
    }

    public void backToReadingFragment() {
        fm.beginTransaction().hide(active).show(fragment1).commit();
        active = fragment1;
    }

    public void goToUserPage(User user) {
        fragment4.setUserVisitor(user);
        fm.beginTransaction().hide(active).show(fragment4).commit();
        active = fragment4;
    }

    public void goToBookPage(Book bookReceived, final Pages fromPage) {
        String id = bookReceived.getId();
        ApiConnector.getBookById(getApplicationContext(), id, new ServerCallbackForBooks() {
            @Override
            public void onSuccess(ArrayList<ArrayList<Book>> books) { }

            @Override
            public void onSuccess(final Book book) {
                 final ArrayList<Book> sameAuthorBooks = new ArrayList<Book>();
                 ApiConnector.getBooksByAuthor(getApplicationContext(), sameAuthorBooks, book.auth.getId(), book.getId(), new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ApiConnector.getBooksByGenre(getApplicationContext(), book.getGenre().getId(), new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    JSONArray jsonarray = new JSONArray(result.get("book").toString());
                                    ArrayList<Book> sameGenderBooks = ApiConnector.parseJsonArrayToBookList(jsonarray);
                                    fragment6.setSameGenderBooks(sameGenderBooks);
                                    fragment6.setSameAuthorBooks(sameAuthorBooks);
                                    fragment6.setBook(book);
                                    fragment6.setParent(fromPage);
                                    fragment6.setEnterTransition(new Slide(Gravity.BOTTOM));
                                    fragment6.setExitTransition(new Slide(Gravity.TOP));
                                    fm.beginTransaction().hide(active).show(fragment6).commit();
                                    active = fragment6;
                                } catch (Exception e) {

                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void focusDiscoverFragment() {
        fm.beginTransaction().hide(active).show(fragment3).commit();
        navigation.getMenu().getItem(2).setChecked(true);
        active = fragment3;
    }

    public void focusToReadingFragment() {
        fm.beginTransaction().hide(active).show(fragment1).commit();
        navigation.getMenu().getItem(0).setChecked(true);
        active = fragment1;
    }

    public void goToFirstForm() {
        Intent intent = new Intent(MainActivity.this, FirstTimeFormActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);

        MockupsValues.setPendingListBooks(MockupsValues.user.getInterested());

        fm.beginTransaction().add(R.id.main_container, fragment7, "7").hide(fragment7).commit();
        fm.beginTransaction().add(R.id.main_container, fragment6, "6").hide(fragment6).commit();
        fm.beginTransaction().add(R.id.main_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
        navigation = findViewById(R.id.navigation);
        //changeIcons(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        connectivityReceiver = new ConnectivityReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityReceiver.connectivityReceiverListener = this;
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void exitAccount() {
        Intent accountIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(accountIntent);
        finish();
    }

    @Override
    public void sendWord(String word, Fragment fragment, int active) {
        if (active == 0) {
            BooksSectionFragment booksSectionFragment = (BooksSectionFragment) fragment;
            booksSectionFragment.displayReceivedData(word);
        } else {
            UsersSectionFragment usersSectionFragment = (UsersSectionFragment) fragment;
            usersSectionFragment.displayReceivedData(word);
        }
    }

    @Override
    public void onNetworkConnectionChanged(String status) {
        Boolean connectivityFull = pref.getBoolean("com.example.readify.wifiAndData", false);

        if (status.equals(getResources().getString(R.string.wifi_ok))) {

        } else if (status.equals(getResources().getString(R.string.mobile_ok)) && connectivityFull) {

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
