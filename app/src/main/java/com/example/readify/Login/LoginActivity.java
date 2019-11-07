package com.example.readify.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.Base64;
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
import static android.view.View.VISIBLE;

import com.example.readify.FirstTimeForm.FirstTimeFormActivity;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "FacebookLogin";
    private static final String USERS = "users";
    private static final int RC_SIGN_IN = 9001;
    private static final int GOOGLE_SIGN_OUT = 4001;
    private static final int FB_SIGN_OUT = 4002;

    private ImageView bookIconImageView;
    private TextView bookITextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;
    private GifImageView gifImageView;
    private CallbackManager callbackManager;
    private LoginButton loginFacebook;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences pref;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User userLogin;
    private boolean userExists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initViews();

        // Create an initial animation
        new CountDownTimer(2000, 1000) {
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
            }
        }.start();

        // Create an animated background with gif
        try {
            InputStream inputStream = getAssets().open("book_background.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException ignored) {}

        // Initialize Google Login button
        findViewById(R.id.logingoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
                loadingProgressBar.setVisibility(VISIBLE);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Facebook Login button
        loginFacebook.setReadPermissions("email", "public_profile");
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                signInFacebook(loginResult);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {}
        });
    }

    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.rootView);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        gifImageView = findViewById(R.id.gifImageView);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginFacebook = findViewById(R.id.login_button);
        userLogin = new User();

        pref = getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Show the login components when the user returns to start
        afterAnimationView.setVisibility(VISIBLE);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Google Sign In was successful, authenticate with Firebase
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException ignored) {}

        } else if (requestCode == GOOGLE_SIGN_OUT) {
            logOutGoogle();
        } else if (requestCode == FB_SIGN_OUT) {
            logOutFacebook();
        }
    }

    // Google SignIn
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Function to login with Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        afterAnimationView.setVisibility(GONE);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Put the information about google
                            userLogin.setUid(user.getUid());
                            userLogin.setName(user.getDisplayName());
                            userLogin.setEmail(user.getEmail());
                            userLogin.writeOnSharedPreferences(pref);

                            try {
                                new DownloadImagesTask().execute(user.getPhotoUrl().toString()).get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }

                            MockupsValues.setContext(LoginActivity.this);

                            //Start activity with a Google logOut
                            Intent intent;
                            if (!task.getResult().getAdditionalUserInfo().isNewUser())
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            else
                                intent = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
                            startActivityForResult(intent, GOOGLE_SIGN_OUT);

                            loadingProgressBar.setVisibility(GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Google SignOut
    private void logOutGoogle() {
        // Firebase sign out
        mAuth.signOut();

        // Google LogOut
        mGoogleSignInClient.signOut();
        mGoogleSignInClient.revokeAccess();
    }

    // Facebook SignIn
    private void signInFacebook(LoginResult loginResult){
        Log.d(TAG, "facebook:onSuccess:" + loginResult);
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    // Function to login with Facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        afterAnimationView.setVisibility(GONE);
        loadingProgressBar.setVisibility(VISIBLE);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Put the information about Facebook
                            userLogin.setUid(user.getUid());
                            userLogin.setName(user.getDisplayName());
                            userLogin.setEmail(user.getEmail());
                            userLogin.writeOnSharedPreferences(pref);

                            try {
                                new DownloadImagesTask().execute((user.getPhotoUrl().toString() + "?type=large")).get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }

                            MockupsValues.setContext(LoginActivity.this);

                            //Start activity with a Facebook logOut
                            Intent intent;
                            if (!task.getResult().getAdditionalUserInfo().isNewUser())
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            else
                                intent = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
                            startActivityForResult(intent, FB_SIGN_OUT);

                            loadingProgressBar.setVisibility(GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            afterAnimationView.setVisibility(VISIBLE);
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Facebook LogOut
    private void logOutFacebook(){
        // Firebase LogOut
        mAuth.signOut();

        // Facebook LogOut
        LoginManager.getInstance().logOut();
    }

    // Animation when the app is open
    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();
        viewPropertyAnimator.y(100f);
        viewPropertyAnimator.setDuration(1000);
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

    // Class to provide a Bitmap from URL (Public access)
    public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getBitmapFromURL(strings[0]);
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            userLogin.setPicture(Base64.encodeToString(b, Base64.DEFAULT));
            databaseReference.child(userLogin.getUid()).setValue(userLogin);
            pref.edit().putString("com.example.readify.photo", Base64.encodeToString(b, Base64.DEFAULT)).apply();
        }


        private Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }
    }

    private boolean checkIfUserExists(final String uid){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userExists = snapshot.hasChild(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return userExists;

        /*databaseReference.equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    intentApp = new Intent(LoginActivity.this, MainActivity.class);
                    startActivityForResult(intentApp, result);
                } else {
                    intentApp = new Intent(LoginActivity.this, FirstTimeFormActivity.class);
                    databaseReference.child(userLogin.getUid()).setValue(userLogin);
                    startActivityForResult(intentApp, result);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });*/
    }
}
