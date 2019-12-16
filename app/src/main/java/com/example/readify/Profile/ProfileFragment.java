package com.example.readify.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readify.Adapters.AchievementsHoritzontalAdapter;
import com.example.readify.Adapters.BooksProfileHoritzontalAdapter;
import com.example.readify.Adapters.GenresHoritzontalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MainActivityLogOut;
import com.example.readify.Models.Achievement;
import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.Models.User;
import com.example.readify.Pages;
import com.example.readify.Popups.AchievementsPopup;
import com.example.readify.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements BooksProfileHoritzontalAdapter.ItemClickListener, GenresHoritzontalAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String USERS = "users";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Integer.toString(R.string.client_id_paypal));

    private User user;
    private CircleImageView userImage;
    private TextView textViewAchievements;
    private TextView readedBooksTextView;
    private ImageView imageViewPremiumBadge;
    private Dialog dialog;
    private Dialog dialogUpgrade;
    private CheckBox checkBox;
    private RadioButton radioButtonWifi;
    private RadioButton radioButtonData;
    private AchievementsHoritzontalAdapter adapterAchievements;
    private Button buttonUpgrade;
    private Button disconnectButton;
    private ImageButton buttonAchievements;
    private ImageButton buttonSettings;
    private TextView textViewNameUser;
    private TextView commentsNumber;


    private FirebaseAuth mAuth;

    private MainActivityLogOut exitActivity;
    private Activity activity;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public void setUserMain() {
        user = new User();
        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user.readFromSharedPreferences(prefs);

        buttonUpgrade.setVisibility(View.VISIBLE);
        disconnectButton.setVisibility(View.VISIBLE);
        buttonAchievements.setVisibility(View.VISIBLE);
        buttonSettings.setVisibility(View.VISIBLE);

        updateUI(user);
    }

    public void setUserVisitor(User user) {
        this.user = user;

        buttonUpgrade.setVisibility(View.GONE);
        disconnectButton.setVisibility(View.GONE);
        buttonAchievements.setVisibility(View.GONE);
        buttonSettings.setVisibility(View.GONE);

        updateUI(user);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null)
            updateUILogOut();
    }

    private void updateUI(User user) {
        getProfileImage();
        textViewNameUser.setText(user.getName());
        //Num of books readed
        readedBooksTextView.setText(Integer.toString(user.getNumReadedBooks()));
        //Num of the achievements
        textViewAchievements.setText(user.getNumCompletedAchievements() + getResources().getString(R.string.diagonalBar) + user.getAchievements().size());
        //Num of the comments
        commentsNumber.setText("10");
        //Show badge premium
        if (user.isPremium())
            imageViewPremiumBadge.setVisibility(View.VISIBLE);
        else
            imageViewPremiumBadge.setVisibility(View.INVISIBLE);

        //TODO get genres, books and achievements of the user
    }

    private void updateUILogOut() {
        Toast.makeText(getContext(), "You're logged out", Toast.LENGTH_SHORT).show();
        exitActivity.exitAccount();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Update the stadistics
        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user = new User();
        user.readFromSharedPreferences(prefs);
        readedBooksTextView.setText(Integer.toString(user.getReadedBooks().size()));
        textViewAchievements.setText(user.getNumCompletedAchievements() + getResources().getString(R.string.diagonalBar) + user.getAchievements().size());

        // Change the layout accord to the type of account
        if (user.isPremium())
            imageViewPremiumBadge.setVisibility(View.VISIBLE);
        else
            imageViewPremiumBadge.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = new User();
        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user.readFromSharedPreferences(prefs);

        // Change an user image
        userImage = (CircleImageView) view.findViewById(R.id.profile_image);
        getProfileImage();

        // Put the name of the user
        textViewNameUser = (TextView) view.findViewById(R.id.nameUserTextview);
        textViewNameUser.setText(user.getName());

        //Initialize the badge of the premium user
        imageViewPremiumBadge = (ImageView) view.findViewById(R.id.premiumBadgeProfile);

        // Num of readed books by the user
        readedBooksTextView = (TextView) view.findViewById(R.id.numReadedBooksText);
        readedBooksTextView.setText(Integer.toString(user.getReadedBooks().size()));

        // Num of comments
        commentsNumber = (TextView) view.findViewById(R.id.commentsNumber);
        commentsNumber.setText("0"); //TODO implement the comments values

        // Achievements of the user
        textViewAchievements = (TextView) view.findViewById(R.id.achievementsText);
        textViewAchievements.setText(user.getNumCompletedAchievements() + getResources().getString(R.string.diagonalBar) + user.getAchievements().size());

        // Create a genre preferences list on profile
        LinearLayoutManager genrePreferencesManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewGenrePreferences = (RecyclerView) view.findViewById(R.id.recycler_view_profile_genre_preferences);
        recyclerViewGenrePreferences.setLayoutManager(genrePreferencesManager);

        ArrayList<Genre> listGenresPreferences = user.getGenres();
        GenresHoritzontalAdapter adapterGenrePreferences = new GenresHoritzontalAdapter(getContext(), listGenresPreferences);
        adapterGenrePreferences.setClickListener(this);
        recyclerViewGenrePreferences.setAdapter(adapterGenrePreferences);

        // Create a readed books list on profile
        LinearLayoutManager readedBooksManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewReadedBooks = (RecyclerView) view.findViewById(R.id.recycler_view_profile_favourite_books);
        recyclerViewReadedBooks.setLayoutManager(readedBooksManager);

        List<Book> listReadedBooks = user.getReadedBooks();
        BooksProfileHoritzontalAdapter adapterReaderBooks =
                new BooksProfileHoritzontalAdapter(getContext(), listReadedBooks);
        adapterReaderBooks.setClickListener(this);
        recyclerViewReadedBooks.setAdapter(adapterReaderBooks);

        // Create a achievements list on profile
        final LinearLayoutManager achievementsManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerViewAchievements = (RecyclerView) view.findViewById(R.id.recycler_view_profile_achievements);
        recyclerViewAchievements.setLayoutManager(achievementsManager);

        List<Achievement> achievementsCompleted = user.getCompletedAchievements();
        adapterAchievements = new AchievementsHoritzontalAdapter(getContext(), achievementsCompleted);
        recyclerViewAchievements.setAdapter(adapterAchievements);

        //Settings button
        buttonSettings = (ImageButton) view.findViewById(R.id.buttonSettingsProfile);
        dialog = new Dialog(getContext());
        dialogUpgrade = new Dialog(getContext());
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings(view);
            }
        });

        //Achievements button
        buttonAchievements = (ImageButton) view.findViewById(R.id.buttonAchievementsProfile);
        buttonAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AchievementsPopup achievementsPopup = new AchievementsPopup(getActivity().getSupportFragmentManager());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                achievementsPopup.show(fragmentTransaction, "achievements_popup");
            }
        });

        /* Initialize a PayPal Services */
        Intent intent = new Intent(getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);

        //Upgrade account button
        buttonUpgrade = (Button) view.findViewById(R.id.upgrade_button);
        buttonUpgrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openUpgrade(view);
                    }
                });

        disconnectButton = (Button) view.findViewById(R.id.logOutButton);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                updateUILogOut();
            }
        });

        return view;
    }

    private void openSettings(View view) {
        dialog.setContentView(R.layout.settings_popup);
        checkBox = (CheckBox) dialog.findViewById(R.id.checkbox_notifiactions);
        radioButtonWifi = (RadioButton) dialog.findViewById(R.id.wifiOnly);
        radioButtonData = (RadioButton) dialog.findViewById(R.id.wifiAndData);

        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.close_achievements_popup);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button buttonSave = (Button) dialog.findViewById(R.id.saveSettingsButton);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putBoolean("com.example.readify.notifications", checkBox.isChecked()).apply();
                prefs.edit().putBoolean("com.example.readify.wifiAndData", radioButtonData.isChecked()).apply();
                Toast.makeText(getContext(), "The changes had been saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        loadValues();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }

    private void openUpgrade(View view) {
        dialogUpgrade.setContentView(R.layout.upgrade_popup);

        ImageButton buttonClose = (ImageButton) dialogUpgrade.findViewById(R.id.close_upgrade_popup);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpgrade.dismiss();
            }
        });

        Button buttonPay = (Button) dialogUpgrade.findViewById(R.id.paymentButton);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(Double.toString(3.99)),
                        "EUR", "Bookify", PayPalPayment.PAYMENT_INTENT_SALE);

                // send the same configuration for restart resiliency
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                startActivityForResult(intent, 0);
            }
        });

        dialogUpgrade.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialogUpgrade.show();
    }

    private void loadValues() {
        boolean notifications = prefs.getBoolean("com.example.readify.notifications", true);
        boolean connectivity = prefs.getBoolean("com.example.readify.wifiAndData", true);

        checkBox.setChecked(notifications);
        radioButtonWifi.setChecked(!connectivity);
        radioButtonData.setChecked(connectivity);
    }

    private void getProfileImage() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>() {
            protected Bitmap doInBackground(Void... p) {
                Bitmap bmp = null;
                try {
                    URL aURL = new URL(user.getPicture());
                    URLConnection conn = aURL.openConnection();
                    conn.setUseCaches(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bmp = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            protected void onPostExecute(Bitmap bmp) {
                userImage.setImageBitmap(bmp);
            }
        };

        t.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    dialogUpgrade.dismiss();

                    Toast.makeText(getContext(), "The payment had been completed", Toast.LENGTH_LONG).show();

                    user.setPremium(true);

                    user.getAchievements().get(4).incrementValue(1);
                    textViewAchievements.setText(user.getNumCompletedAchievements() + getResources().getString(R.string.diagonalBar) + user.getAchievements().size());

                    String achievementsToPref = new Gson().toJson(user.getAchievements());
                    prefs.edit().putString("com.example.readify.achievements", achievementsToPref).apply();

                    user.saveToFirebase();

                    adapterAchievements.setAchivementsList(user.getCompletedAchievements());
                    adapterAchievements.notifyDataSetChanged();

                    imageViewPremiumBadge.setVisibility(View.VISIBLE);
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "The user canceled the operation", Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(getContext(), "An invalid Payment or PayPalConfiguration was submitted. Please see the docs", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        MainActivity activity = (MainActivity) getActivity();
        if (user.getReadedBooks().size() == position)
            activity.focusDiscoverFragment();
        else
            activity.goToBookPage(user.getReadedBooks().get(position), Pages.PROFILE_PAGE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
            exitActivity = (MainActivityLogOut) activity;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getContext(), PayPalService.class));
    }

    @Override
    public void onItemClick(View view) {
        MainActivity activity = (MainActivity) getActivity();
        activity.goToFirstForm();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

