package com.gamesexchange.gamesexchange.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.noInternetConnection.NoInternetConnection;
import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.model.User;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.presenters.LoginPresenterMVP;
import com.gamesexchange.gamesexchange.activities.register.MVPRegisterActivity;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;
import com.gamesexchange.gamesexchange.rooms.AppExecuter;
import com.gamesexchange.gamesexchange.services.AddingAllGamesService;
import com.gamesexchange.gamesexchange.services.GamesTasks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.gamesexchange.gamesexchange.Utils.AppUtils.buildAlertDialog;

/**
 * A login screen that offers login via email/password.
 */
public class MVPLoginActivity extends AppCompatActivity implements LoginPresenterMVP.View {



    private EditText mEmailOrPhoneNo;
    private EditText mPassword;
    private LoginPresenterMVP presenter;
    private ProgressBar progressBar;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.z_login);
        additionOfGamesToOfflineDatabase();

        if (AppUtils.isNetworkAvailable(this)) {
            FirebaseApp.initializeApp(this);
            mAuth = FirebaseAuth.getInstance();
            setupWidgits();
        } else {
            Intent i = new Intent(this, NoInternetConnection.class);
            startActivity(i);
            finish();
        }

    }


    private void setupWidgits() {
        presenter = new LoginPresenterMVP(this);
        progressBar = findViewById(R.id.ID_log_progress_bar);

        mEmailOrPhoneNo = (EditText) findViewById(R.id.ID_log_email_or_phone);

        mPassword = (EditText) findViewById(R.id.ID_log_password);
        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        TextView mDontHaveAccount = (TextView) findViewById(R.id.ID_donthaveAccount_register);
        mDontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MVPLoginActivity.this, MVPRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                setContentView(R.layout.test_login);
            }
        });
        // Set up the login form.

        setupFirebaseAuth();

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE)) {

                    //         Log.i(TAG, "Here you can write the code");
                    logIn();
                    return true;

                }
                return false;
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();

            }
        });


    }

    private void logIn() {
        mLoginButton.setEnabled(false);
        AppUtils.hideSoftKeyboard(MVPLoginActivity.this);

        presenter.performLoginProcess(
                new User(
                        String.valueOf(mEmailOrPhoneNo.getText()),
                        String.valueOf(mPassword.getText())));

    }


    //--------------------------------Firebase-----------------------
    // -------------------------------------Firebase---------------------------------

    private static final String TAG = "HomePage:";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private void setupFirebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //     if (user.isEmailVerified()) {
                    // User is signed in


                    Intent intent = new Intent(MVPLoginActivity.this, MVPHomePageActivity.class);
                    startActivity(intent);
                    finish();


                } else {
                    // User is signed out

                    //            Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void IsEmailVerified() {
        mLoginButton.setEnabled(true);
//        Toast.makeText(MVPLoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MVPLoginActivity.this, MVPHomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void logsIn(final User user) {
        if (AppUtils.isStringNull(user.getEmail()) || AppUtils.isStringNull(user.getPassword())) {
            Toast.makeText(MVPLoginActivity.this, getString(R.string.all_fields_are_requried), Toast.LENGTH_LONG).show();
            mLoginButton.setEnabled(true);
        } else {
            showProgressBar();

            if (user.getEmail().contains("@")) {
                mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                        .addOnCompleteListener(MVPLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //                 Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    mLoginButton.setEnabled(true);
                                    //                   Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(MVPLoginActivity.this, getString(R.string.couldnot_sign_in), Toast.LENGTH_LONG).show();
                                } else {
                                    IsEmailVerified();
                                }
                                hideProgressBar();
                            }

                            // ...

                        });
            } else {
                // Phone Number Case
                loginFromEmailFromPhoneNo(mEmailOrPhoneNo.getText().toString(), user.getPassword());

            }
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void additionOfGamesToOfflineDatabase() {
        Intent intent = new Intent(this, AddingAllGamesService.class);
        intent.setAction(GamesTasks.ADD_ALL_GAMES);
        startService(intent);


    }


    private void loginFromEmailFromPhoneNo(String phone, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_email_from_phone.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String email = response;
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MVPLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    mLoginButton.setEnabled(true);
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(MVPLoginActivity.this, getString(R.string.couldnot_sign_in), Toast.LENGTH_LONG).show();
                                } else {
                                    IsEmailVerified();
                                }
                                hideProgressBar();
                            }

                            // ...

                        });
            }
        }
                , getPostErrorListener()) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                return params;
            }

            ;
        };
        requestQueue.add(stringRequest);


    }

    private Response.ErrorListener getPostErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MVPLoginActivity.this, getString(R.string.something_is_wrong), Toast.LENGTH_LONG).show();
            }
        };
    }


}

