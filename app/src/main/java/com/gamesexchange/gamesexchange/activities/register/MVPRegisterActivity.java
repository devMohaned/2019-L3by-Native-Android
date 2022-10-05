package com.gamesexchange.gamesexchange.activities.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.gamesexchange.gamesexchange.activities.legals.PrivacyPolicyActivity;
import com.gamesexchange.gamesexchange.activities.legals.EulaActivity;
import com.gamesexchange.gamesexchange.activities.legals.TermsOfUseActivity;
import com.gamesexchange.gamesexchange.activities.login.MVPLoginActivity;
import com.gamesexchange.gamesexchange.model.User;
import com.gamesexchange.gamesexchange.presenters.RegisterPresenterMVP;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
import static com.gamesexchange.gamesexchange.Utils.AppUtils.buildAlertDialog;

/**
 * A login screen that offers login via email/password.
 */
public class MVPRegisterActivity extends AppCompatActivity implements RegisterPresenterMVP.View {

    // the username of the user is placed in a EditText
    private EditText firstName,lastName;
    // the e-mail of the user is placed in a EditText
    private EditText email;
    // the password & confirm password of the user is placed in EditText
    private EditText passwordEditText;

    private EditText phoneNumber;

    Button registerButton;

    Context mContext = MVPRegisterActivity.this;
    private ProgressBar progressBar;

    private RegisterPresenterMVP presenter;
    private Spinner mLocationSpinner;
    Spinner mDistrictSpinner;
    // TODO (1): TO BE CHANGED LATER
    private String currentSelectedPlatform = "pc";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the activity to registeration activity
        setContentView(R.layout.z_register);

        Toolbar toolbar = findViewById(R.id.ID_register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        setUpWidgits();
        presenter = new RegisterPresenterMVP(this);
        setupFirebaseAuth();

    }


    private void setUpWidgits() {

        phoneNumber = findViewById(R.id.ID_reg_phone_number);

        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {

                    AppUtils.hideSoftKeyboard(MVPRegisterActivity.this);
//                    Log.i(TAG, "Here you can write the code");
//                    onRegistering();
                    return true;

                }
                return false;
            }
        });

        // Email is connected to EditText view.
        email = (EditText) findViewById(R.id.ID_reg_email);
        // username is connected to EditText view
        firstName = (EditText) findViewById(R.id.ID_reg_first_name);
        lastName = (EditText) findViewById(R.id.ID_reg_last_name);
        // password & passwordConfirmation are connected to EditText
        passwordEditText = (EditText) findViewById(R.id.ID_reg_password);

        progressBar = (ProgressBar) findViewById(R.id.ID_reg_progress_bar);
        progressBar.setVisibility(View.GONE);
        // inizialize the account database connector to this context.

        // Shows password if checkBox is checked and doesn't show password if CheckBox is unchecked.
        CheckBox passwordCheckBox = (CheckBox) findViewById(R.id.ID_reg_checkbox_password);
        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    passwordEditText.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    passwordEditText.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });

        //Register button doing a method "onRegistering" once it's clicked.
        registerButton = (Button) findViewById(R.id.ID_reg_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegistering();
            }
        });





        TextView termsAndAgreement = findViewById(R.id.ID_reg_terms);
        TextView privacyPolicy = findViewById(R.id.ID_reg_privacy_policy);
        TextView termsOfUser = findViewById(R.id.ID_reg_terms_of_use);
        termsAndAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MVPRegisterActivity.this, EulaActivity.class);
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MVPRegisterActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        termsOfUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MVPRegisterActivity.this, TermsOfUseActivity.class);
                startActivity(intent);
            }
        });



                mLocationSpinner = findViewById(R.id.ID_register_location_spinner);

        mLocationSpinner.setSelection(0);


         mDistrictSpinner = findViewById(R.id.ID_register_district_spinner);
        mDistrictSpinner.setSelection(0);


        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currentSelectedItemInSpinner = mLocationSpinner.getSelectedItem().toString();
             int districtIndex =   AppUtils.LocationUtils.getLocationIndex(currentSelectedItemInSpinner);
             AppUtils.LocationUtils.changeDistrictSpinner(mContext,districtIndex,mDistrictSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validateName(mContext,editable.toString());
                checkForError(firstName,errorOrNoError);
            }
        });


        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validateName(mContext,editable.toString());
                checkForError(lastName,errorOrNoError);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validateEmail(mContext,editable.toString());
                checkForError(email,errorOrNoError);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validatePassword(mContext,editable.toString());
                checkForError(passwordEditText,errorOrNoError);
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validatePhone(mContext,editable.toString());
                checkForError(phoneNumber,errorOrNoError);
            }
        });


    }

    private void checkForError(EditText editText, String error)
    {
        if (!error.equals(Constants.ERROR_FREE))
        {
            editText.setError(error);
        }else{
            editText.setError(null);
        }
    }



    private void onRegistering() {

        // obtain & convert everyText inserted in the userName/email/password/password confirmation into a String type.
        String emailText = email.getText().toString();
        String userPassword = passwordEditText.getText().toString();

        // Several condition 1/ if username is empty
        if (firstName.getText().toString().equals(""))
        {
            firstName.setError(getString(R.string.empty_user_name));
        }else if (firstName.getText().toString().length() <= 2)
        {
            firstName.setError(getString(R.string.small_name));
        }else if (firstName.getText().toString().length() > 20)
        {
            firstName.setError(getString(R.string.large_name));
        }else{
            firstName.setError(null);
        }

        // if email is empty
        if (emailText.matches("")) {
            email.setError(getString(R.string.empty_email));
        }
        // if userpassword is empty
        else if (userPassword.contains(" ")) {
            passwordEditText.setError(getString(R.string.password_cannot_have_space));
        }
        // if confirmpassword is empty
        // if userpassword is empty
        else if (userPassword.matches("")) {
            passwordEditText.setError(getString(R.string.password_is_empty));
            //   Toast.makeText(Registeration.this, "You cannot leave Passwords empty.", Toast.LENGTH_LONG).show();
        }
        // if confirmpassword is empty
        else if (userPassword.trim().length() < 8) {
            passwordEditText.setError(getString(R.string.password_must_be_above_eight));
        }
        // if all the above is wrong(Passwords are not matched), thus:

        if (phoneNumber.getText().toString().length() < 11) {
            phoneNumber.setError(getString(R.string.wrong_phone_number));
        }

        if (lastName.getText().toString().equals(""))
        {
            lastName.setError(getString(R.string.empty_user_name));
        }else if (lastName.getText().toString().length() <= 2)
        {
            lastName.setError(getString(R.string.small_name));
        }else if (lastName.getText().toString().length() > 20)
        {
            lastName.setError(getString(R.string.large_name));
        }else{
            lastName.setError(null);
        }


        if (firstName.getText().toString().length() > 2 && firstName.getText().toString().length() < 20
                && lastName.getText().toString().length() > 2 && lastName.getText().toString().length() < 20
                && emailText.trim().length() > 0
                && userPassword.trim().length() > 7
                && phoneNumber.getText().toString().length() == 11) {


            registerButton.setEnabled(false);
            AppUtils.hideSoftKeyboard(MVPRegisterActivity.this);
            User newUser = new User();
            newUser.setEmail(email.getText().toString());
            newUser.setPassword(userPassword);
            newUser.setFirst_name(firstName.getText().toString());
            newUser.setLast_name(lastName.getText().toString());
            newUser.setPhoneNumber(phoneNumber.getText().toString());
            newUser.setLocation(mLocationSpinner.getSelectedItem().toString());
            showProgressBar();
            presenter.performRegisterProcess(newUser);

        }


    }


    // Once the activity of Registeration is shown, it'll read the CURRENT database.
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //  readDatabase();
    }


    /*
       ------------------------------------ Firebase ---------------------------------------------
        */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * Setup the firebase auth object
     */
    private String TAG = "Registeration: ";

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void registerUser(User user) {
//        registerNewEmail(user.getEmail(), user.getPassword(), user);
        registerButton.setEnabled(false);
        user.setPhoneNumber(phoneNumber.getText().toString());
        user.setLocation(mLocationSpinner.getSelectedItem().toString());
        user.setPlatform(currentSelectedPlatform);
        user.setDistrict(mDistrictSpinner.getSelectedItem().toString());
        regsiterIfPhoneNoExists(user);

    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);

    }


    private void regsiterIfPhoneNoExists(User user) {

        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_get_if_user_exists.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.toLowerCase().equals("null")) {
                    Intent intent = new Intent(MVPRegisterActivity.this, PhoneNumberVerificationActivity.class);
                    intent.putExtra("mobile", user.getPhoneNumber());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("first_name", user.getFirst_name());
                    intent.putExtra("last_name", user.getLast_name());
                    intent.putExtra("password", user.getPassword());
                    intent.putExtra("location",user.getLocation());
                    intent.putExtra("district",user.getDistrict());
                    intent.putExtra("platform",user.getPlatform());
                    startActivity(intent);
//                    finish();
                } else {

                    Toast.makeText(mContext, getString(R.string.already_registered), Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }

//                Log.e("Phone", response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

              AppUtils.checkVolleyError(getApplicationContext(),error);

              if (!isFinishing()) {
                  buildAlertDialog(
                          MVPRegisterActivity.this,
                          getString(R.string.send_error)
                          , getString(R.string.developer_will_be_notified_with_the_error)
                          , getString(R.string.registering)
                          , getString(R.string.you_have_error_in) + " " + error);
              }


                registerButton.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", user.getPhoneNumber());
                return params;
            }


        };
        requstQueue.add(stringRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

