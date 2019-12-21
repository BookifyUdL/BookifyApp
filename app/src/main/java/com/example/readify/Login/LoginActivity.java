package com.example.readify.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import com.example.readify.ApiConnector;
import com.example.readify.BuildConfig;
import com.example.readify.ConnectivityReceiver;
import com.example.readify.FirstTimeForm.FirstTimeFormActivity;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.felipecsl.gifimageview.library.GifImageView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String USERS = "users";
    private static final String TAG_F = "FACELOG";
    private static final String TAG_G = "GOOGLOG";
    private static final int RC_SIGN_IN = 1001;

    private ImageView bookIconImageView;
    private TextView bookITextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;
    private GifImageView gifImageView;

    private SharedPreferences pref;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private CallbackManager mCallbackManager;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;

    private BroadcastReceiver connectivityReceiver = null;
    private Boolean connectionEnabled;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // Connectivity receiver
        connectionEnabled = true;
        connectivityReceiver = new ConnectivityReceiver();

        // Fix a Facebook bug (token)
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Initialize all components in Login Layout
        initViews();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getUserInfoAndUpdateUi(currentUser);
        } else {
            // Create an initial animation
            initializeAnimation();

            // Create an animated background with gif
            initializeBackgroundGif();

            // Initialize Facebook Login button
            initializeSignInFacebook();

            // Initialize Google login button
            initializeSignInGoogle();
        }
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

    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.rootView);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        gifImageView = findViewById(R.id.gifImageView);

        pref = getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        ApiConnector.setPreferences(pref);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);

        signInButton = findViewById(R.id.logingoogle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Show the login components when the user returns to start
        afterAnimationView.setVisibility(VISIBLE);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_G, "Google sign in failed", e);
            }
        }
    }

    private void initializeAnimation() {
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                bookITextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorSplashText));
                bookIconImageView.setImageResource(R.drawable.bookify_icon_only);
                startAnimation();
                if (connectionEnabled) {
                    bookITextView.setVisibility(GONE);
                    loadingProgressBar.setVisibility(GONE);
                    rootView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorSplashText));
                    bookIconImageView.setImageResource(R.drawable.bookify_icon_only);
                    startAnimation();
                }
            }
        }.start();
    }

    // Animation when the app is open
    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();
        viewPropertyAnimator.y(100f);
        viewPropertyAnimator.setDuration(2000);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterAnimationView.setVisibility(VISIBLE);
                gifImageView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    //Method to init a background gif
    private void initializeBackgroundGif() {
        try {
            InputStream inputStream = getAssets().open("book_background.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Initialize a methods to do login on facebook
    private void initializeSignInFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.loginfacebook);
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG_F, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG_F, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG_F, "facebook:onError", error);
            }
        });
    }

    //Method to obtain the access token from facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG_F, "handleFacebookAccessToken:" + token);
        afterAnimationView.setVisibility(INVISIBLE);
        loadingProgressBar.setVisibility(VISIBLE);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_F, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null)
                                updateUI(user, task);

                            loadingProgressBar.setVisibility(INVISIBLE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_F, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            afterAnimationView.setVisibility(VISIBLE);
                            loadingProgressBar.setVisibility(INVISIBLE);
                        }
                    }
                });
    }

    //Initialize a methods to do login on Google
    private void initializeSignInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afterAnimationView.setVisibility(GONE);
                loadingProgressBar.setVisibility(VISIBLE);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    //Method to obtain a credential from Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG_G, "firebaseAuthWithGoogle:" + acct.getId());
        afterAnimationView.setVisibility(GONE);
        loadingProgressBar.setVisibility(VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_G, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            user.getEmail();

                            if (user != null)
                                updateUI(user, task);

                            loadingProgressBar.setVisibility(GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_G, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            afterAnimationView.setVisibility(VISIBLE);
                            loadingProgressBar.setVisibility(GONE);
                        }
                    }
                });
    }

    //Method to read from database a different params that will use in app after
    private void readDataFromFirebase(String uid, final Intent intent) {
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO Improve if the user didn't make the first form

                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    //If we don't complete de first form, it appear again
                    Intent firstFormAgain = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
                    startActivity(firstFormAgain);
                    finish();
                } else {
                    MockupsValues.user.setUid(user.getUid());
                    pref.edit().putString("com.example.readify._id", user.getUid()).apply();
                    //Obtain the rest of the data from database
                    ApiConnector.getInfoClientUser(getApplicationContext(), new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            System.out.println("The obtaining information of the user client works correctly");

                            pref.edit().putBoolean("com.example.readify.premium", MockupsValues.user.isPremium()).apply();

                            String genresToPref = new Gson().toJson(MockupsValues.user.getGenres());
                            pref.edit().putString("com.example.readify.genres", genresToPref).apply();

                            String libraryToPref = new Gson().toJson(MockupsValues.user.getLibrary());
                            pref.edit().putString("com.example.readify.library", libraryToPref).apply();

                            String interestedToPref = new Gson().toJson(MockupsValues.user.getInterested());
                            pref.edit().putString("com.example.readify.interested", interestedToPref).apply();

                            String readingToPref = new Gson().toJson(MockupsValues.user.getReading());
                            pref.edit().putString("com.example.readify.reading", readingToPref).apply();

                            String achievementsToPref = new Gson().toJson(MockupsValues.user.getAchievements());
                            pref.edit().putString("com.example.readify.achievements", achievementsToPref).apply();

                            //Go to app
                            startActivity(intent);
                            finish();
                        }
                    });

                    /*pref.edit().putBoolean("com.example.readify.premium", user.isPremium()).apply();

                    String genresToPref = new Gson().toJson(user.getGenres());
                    pref.edit().putString("com.example.readify.genres", genresToPref).apply();

                    String libraryToPref = new Gson().toJson(user.getLibrary());
                    pref.edit().putString("com.example.readify.library", libraryToPref).apply();

                    String interestedToPref = new Gson().toJson(user.getInterested());
                    pref.edit().putString("com.example.readify.interested", interestedToPref).apply();

                    String readingToPref = new Gson().toJson(user.getReading());
                    pref.edit().putString("com.example.readify.reading", readingToPref).apply();

                    String achievementsToPref = new Gson().toJson(user.getAchievements());
                    pref.edit().putString("com.example.readify.achievements", achievementsToPref).apply();

                    //Go to app
                    startActivity(intent);*/
                }
                //finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error to read the data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(FirebaseUser currentUser, Task<AuthResult> task) {
        // Load data to User
        MockupsValues.setContext(this);
        MockupsValues.user.setFirebaseId(currentUser.getUid());
        MockupsValues.user.setPicture(currentUser.getPhotoUrl().toString());
        MockupsValues.user.setName(currentUser.getDisplayName());
        MockupsValues.user.setEmail(currentUser.getEmail());

        pref.edit().putString("com.example.readify.idFirebase", currentUser.getUid()).apply();
        pref.edit().putString("com.example.readify.name", currentUser.getDisplayName()).apply();
        pref.edit().putString("com.example.readify.email", currentUser.getEmail()).apply();
        pref.edit().putString("com.example.readify.photo", currentUser.getPhotoUrl().toString() + "?type=large").apply();

        Intent intent;
        if (!task.getResult().getAdditionalUserInfo().isNewUser()) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
            readDataFromFirebase(currentUser.getUid(), intent);
        } else {
            intent = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // For on start method
    private void getUserInfoAndUpdateUi(final FirebaseUser currentUser) {
        MockupsValues.setContext(getApplicationContext());

        // API call solution
        ApiConnector.getGenres(getApplicationContext(), new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println("The obtaining of the genres works correctly");
                ApiConnector.getAllBooks(getApplicationContext(), new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println("The obtaining of the books works correctly");
                        ApiConnector.getAllUsers(getApplicationContext(), new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                System.out.println("The obtaining of the users works correctly");
                                updateUI(currentUser);
                                /*ApiConnector.getUser(getApplicationContext(), new ServerCallback() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        System.out.println("The obtaining of the user client works correctly");
                                        updateUI(currentUser);
                                    }
                                });*/
                            }
                        });
                    }
                });
            }
        });
    }

    //Methods to update an app after to do a login
    private void updateUI(FirebaseUser currentUser) {

        /*try {*/
        // Load data to User
        MockupsValues.setContext(this);
        MockupsValues.user.setFirebaseId(currentUser.getUid());
        MockupsValues.user.setPicture(currentUser.getPhotoUrl().toString());
        MockupsValues.user.setName(currentUser.getDisplayName());
        MockupsValues.user.setEmail(currentUser.getEmail());

        // Load data to Shared Preferences
        pref.edit().putString("com.example.readify.idFirebase", currentUser.getUid()).apply();
        pref.edit().putString("com.example.readify.name", currentUser.getDisplayName()).apply();
        pref.edit().putString("com.example.readify.email", currentUser.getEmail()).apply();
        pref.edit().putString("com.example.readify.photo", currentUser.getPhotoUrl().toString()).apply();

        // Read data from Firebase
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        readDataFromFirebase(currentUser.getUid(), intent);

            /*ApiConnector.getGenres(getApplicationContext(), new ServerCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    System.out.println("The obtaining of the genres works correctly");
                    MockupsValues.setContext(getApplicationContext());
                    ApiConnector.getAllBooks(getApplicationContext(), new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            System.out.println("The obtaining of the books works correctly");
                            ApiConnector.getAllUsers(getApplicationContext(), new ServerCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    System.out.println("The obtaining of the users works correctly");
                                    ApiConnector.getUser(getApplicationContext(), new ServerCallback() {
                                        @Override
                                        public void onSuccess(JSONObject result) {
                                            System.out.println("The obtaining of the client user works correctly");
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            readDataFromFirebase(uid, intent);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(getApplicationContext(), "Error in login 1", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onNetworkConnectionChanged(String status) {
        /*Boolean connectivityFull = pref.getBoolean("com.example.readify.wifiAndData", false);

        if (status.equals(getResources().getString(R.string.wifi_ok))) {
            connectionEnabled = true;

            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null)
                getUserInfoAndUpdateUi(currentUser);

            initializeAnimation();

        } else if (status.equals(getResources().getString(R.string.mobile_ok)) && connectivityFull) {
            connectionEnabled = true;

            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null)
                getUserInfoAndUpdateUi(currentUser);

            initializeAnimation();

        } else {
            connectionEnabled = false;
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("An error has ocurred to connect to the server. Check your internet connection.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }*/
    }
}
