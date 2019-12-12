package com.example.readify.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import com.example.readify.ApiConnector;
import com.example.readify.BuildConfig;
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

public class LoginActivity extends AppCompatActivity {

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

    //private boolean isInDatabase = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // Fix a Facebook bug (token)
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        // Initialize all components in Login Layout
        initViews();

        // Create an initial animation
        initializeAnimation();

        // Create an animated background with gif
        initializeBackgroundGif();

        // Initialize Facebook Login button
        initializeSignInFacebook();

        // Initialize Google login button
        initializeSignInGoogle();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            //GET user info ::
            ApiConnector.getUser(getApplicationContext(), new ServerCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    System.out.println("Get userInfo funko!!!!");
                    MockupsValues.setIsUserInDatabase(true);
                    updateUI(currentUser);
                }
            });

        }
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
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                /*boolean fg = false;
                if(fg){

                }*/
                bookITextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorSplashText));
                bookIconImageView.setImageResource(R.drawable.bookify_icon_only);
                startAnimation();
            }
        }.start();
    }

    // Animation when the app is open
    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();
        viewPropertyAnimator.y(100f);
        viewPropertyAnimator.setDuration(3000);
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

    //Method to read from database a different params that will use in app after
    private void readDataFromFirebase(String uid, final Intent intent) {
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO Improve if the user didn't make the first form

                User user = dataSnapshot.getValue(User.class);

                if (user == null){
                    //If we don't complete de first form, it appear again
                    Intent firstFormAgain = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
                    startActivity(firstFormAgain);
                } else {
                    pref.edit().putBoolean("com.example.readify.premium", user.isPremium()).apply();

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
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error to read the data from database", Toast.LENGTH_SHORT).show();
            }
        });
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

    //Methods to update an app after to do a login
    private void updateUI(FirebaseUser currentUser) {
        try {
            MockupsValues.setContext(this);
            MockupsValues.getUser().setFirebaseId(currentUser.getUid());
            Uri url =currentUser.getPhotoUrl();
            MockupsValues.getUser().setPicture(url.toString());

            /*pref.edit().putString("com.example.readify.uid", currentUser.getUid()).apply();
            pref.edit().putString("com.example.readify.name", currentUser.getDisplayName()).apply();
            pref.edit().putString("com.example.readify.email", currentUser.getEmail()).apply();
            pref.edit().putString("com.example.readify.photo", currentUser.getPhotoUrl().toString()).apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            readDataFromFirebase(currentUser.getUid(), intent);*/

            Intent intent = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(getApplicationContext(), "Error in login 1", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser currentUser, Task<AuthResult> task) {
        try {
            MockupsValues.setContext(this);
            MockupsValues.getUser().setFirebaseId(currentUser.getUid());
            Uri url =currentUser.getPhotoUrl();
            MockupsValues.getUser().setPicture(url.toString());
            Intent intent;
            /*pref.edit().putString("com.example.readify.uid", currentUser.getUid()).apply();
            pref.edit().putString("com.example.readify.name", currentUser.getDisplayName()).apply();
            pref.edit().putString("com.example.readify.email", currentUser.getEmail()).apply();
            pref.edit().putString("com.example.readify.photo", currentUser.getPhotoUrl().toString() + "?type=large").apply();


            if (!task.getResult().getAdditionalUserInfo().isNewUser()) {
                intent = new Intent(LoginActivity.this, MainActivity.class);
                readDataFromFirebase(currentUser.getUid(), intent);
            } else {*/
                intent = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
                startActivity(intent);
                finish();
                //}
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(getApplicationContext(), "Error in login 2", Toast.LENGTH_SHORT).show();
        }
    }
}
