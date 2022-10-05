package com.gamesexchange.gamesexchange.activities.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
import com.gamesexchange.gamesexchange.activities.legals.EulaActivity;
import com.gamesexchange.gamesexchange.activities.legals.PrivacyPolicyActivity;
import com.gamesexchange.gamesexchange.activities.legals.TermsOfUseActivity;
import com.gamesexchange.gamesexchange.activities.login.MVPLoginActivity;
import com.gamesexchange.gamesexchange.model.User;
import com.gamesexchange.gamesexchange.presenters.RegisterPresenterMVP;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
import static com.gamesexchange.gamesexchange.Utils.AppUtils.buildAlertDialog;
import static com.gamesexchange.gamesexchange.Utils.Constants.SHARED_PREF_USER_DATA;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.mAuth;

public class RegisterFailedActivity extends AppCompatActivity implements RegisterPresenterMVP.View {



    // the username of the user is placed in a EditText
    private EditText mLastName;
    // the e-mail of the user is placed in a EditText
    private EditText mEmail;
    // the password & confirm password of the user is placed in EditText
    private EditText mPassword;

    private EditText mPhoneNumber;
    private EditText mFirstName;
    Button mRegisterButton;

    Context mContext = RegisterFailedActivity.this;
    private ProgressBar mProgressBar;

    private RegisterPresenterMVP presenter;
    private Spinner mLocationSpinner, mDistrictSpinner;
    private TextView settingUpAccount;

    private boolean alreadyShownError = false;

    String platform = "pc";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the activity to registeration activity
        setContentView(R.layout.activity_register_failed);


        setUpWidgits();
        presenter = new RegisterPresenterMVP(this);

    }


    private void setUpWidgits() {

        Toolbar toolbar = findViewById(R.id.ID_failed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        mPhoneNumber = findViewById(R.id.ID_failed_phone_number);

        mPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {

                    AppUtils.hideSoftKeyboard(RegisterFailedActivity.this);
//                    Log.i(TAG, "Here you can write the code");
//                    onRegistering();
                    return true;

                }
                return false;
            }
        });

        mFirstName = findViewById(R.id.ID_failed_first_name);

        // Email is connected to EditText view.
        mEmail = (EditText) findViewById(R.id.ID_failed_email);
        // username is connected to EditText view
        mLastName = (EditText) findViewById(R.id.ID_failed_last_name);
        // password & passwordConfirmation are connected to EditText
        mPassword = (EditText) findViewById(R.id.ID_failed_password);

        mProgressBar = (ProgressBar) findViewById(R.id.ID_failed_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        // inizialize the account database connector to this context.

        // Shows password if checkBox is checked and doesn't show password if CheckBox is unchecked.
        CheckBox passwordCheckBox = (CheckBox) findViewById(R.id.ID_failed_checkbox_password);
        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    mPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });

        //Register button doing a method "onRegistering" once it's clicked.
        mRegisterButton = (Button) findViewById(R.id.ID_failed_register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegistering();
            }
        });



        TextView termsAndAgreement = findViewById(R.id.ID_failed_terms);
        TextView privacyPolicy = findViewById(R.id.ID_failed_privacy_policy);
        TextView termsOfUser = findViewById(R.id.ID_failed_terms_of_use);
        termsAndAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterFailedActivity.this, EulaActivity.class);
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterFailedActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        termsOfUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterFailedActivity.this, TermsOfUseActivity.class);
                startActivity(intent);
            }
        });



        mLocationSpinner = findViewById(R.id.ID_failed_location_spinner);

        mLocationSpinner.setSelection(0);
        mDistrictSpinner = findViewById(R.id.ID_failed_district_spinner);


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


        settingUpAccount = findViewById(R.id.ID_failed_settings);

        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_USER_DATA, MODE_PRIVATE);
        String firstName = prefs.getString("first_name", null);
        String lastName = prefs.getString("last_name", null);
        String email = prefs.getString("email", null);
        String phone = prefs.getString("phone_number", null);


      if (firstName != null) mFirstName.setText(firstName);
        if (lastName != null)  mLastName.setText(lastName);
//        if (email != null)  mEmail.setText(email);
        if (phone != null) {
            mPhoneNumber.setText(phone);
            mPhoneNumber.setEnabled(false);
        }

        mEmail.setText(mAuth.getCurrentUser().getEmail());
        mEmail.setEnabled(false);


        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validateName(mContext,editable.toString());
                checkForError(mFirstName,errorOrNoError);
            }
        });


        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validateName(mContext,editable.toString());
                checkForError(mLastName,errorOrNoError);
            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validateEmail(mContext,editable.toString());
                checkForError(mEmail,errorOrNoError);
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validatePassword(mContext,editable.toString());
                checkForError(mPassword,errorOrNoError);
            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError =    AppUtils.Validators.validatePhone(mContext,editable.toString());
                checkForError(mPhoneNumber,errorOrNoError);
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

        // obtain & convert everyText inserted in the mLastName/mEmail/password/password confirmation into a String type.
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String emailText = mEmail.getText().toString();
        String userPassword = mPassword.getText().toString();

        // Several condition 1/ if username is empty
        if (lastName.matches("") || firstName.matches("")) {
            mLastName.setError(getString(R.string.empty_user_name));
            mFirstName.setError(getString(R.string.empty_user_name));
            //  Toast.makeText(Registeration.this, "No username is found.", Toast.LENGTH_LONG).show();
        } else {
            mLastName.setError(null);
        }
        // if mEmail is empty
        if (emailText.matches("")) {
            mEmail.setError(getString(R.string.empty_email));
        }
        // if userpassword is empty
        else if (userPassword.contains(" ")) {
            mPassword.setError(getString(R.string.password_cannot_have_space));
        }
        // if confirmpassword is empty
        // if userpassword is empty
        else if (userPassword.matches("")) {
            mPassword.setError(getString(R.string.password_is_empty));
            //   Toast.makeText(Registeration.this, "You cannot leave Passwords empty.", Toast.LENGTH_LONG).show();
        }
        // if confirmpassword is empty
        else if (userPassword.trim().length() < 8) {
            mPassword.setError(getString(R.string.password_must_be_above_eight));
        }
        // if all the above is wrong(Passwords are not matched), thus:

        if (mPhoneNumber.getText().toString().length() < 11) {
            mPhoneNumber.setError(getString(R.string.wrong_phone_number));
        }

        if (firstName.trim().length() > 0 && lastName.trim().length() > 0 && emailText.trim().length() > 0
                && userPassword.trim().length() > 7
                && mPhoneNumber.getText().toString().length() == 11) {


            mRegisterButton.setEnabled(false);
            AppUtils.hideSoftKeyboard(RegisterFailedActivity.this);
            User newUser = new User();
            newUser.setFirst_name(firstName);
            newUser.setLast_name(lastName);
            newUser.setEmail(emailText);
            newUser.setPassword(userPassword);
            newUser.setPhoneNumber(mPhoneNumber.getText().toString());
            newUser.setLocation(mLocationSpinner.getSelectedItem().toString());
            newUser.setDistrict(mDistrictSpinner.getSelectedItem().toString());
            newUser.setPlatform(platform);
            showProgressBar();
            presenter.performRegisterProcess(newUser);

        }


    }


    // Once the activity of Registeration is shown, it'll read the CURRENT database.


    /*
       ------------------------------------ Firebase ---------------------------------------------
        */







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void registerUser(User user) {
//        registerNewEmail(user.getEmail(), user.getPassword(), user);
        mRegisterButton.setEnabled(false);
        user.setPhoneNumber(mPhoneNumber.getText().toString());
        user.setLocation(mLocationSpinner.getSelectedItem().toString());
        user.setDistrict(mDistrictSpinner.getSelectedItem().toString());
        user.setPlatform(platform);
        regsiterIfPhoneNoExists(user);

    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);

    }


    private void regsiterIfPhoneNoExists(User user) {

        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_get_if_user_exists.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.toLowerCase().equals("null")) {
                    registerNewUser(user.getFirst_name(),user.getLast_name(),user.getPassword(),user.getEmail(),mAuth.getUid(),user.getPhoneNumber()
                            , String.valueOf(AppUtils.LocationUtils.getLocationIndex(user.getLocation())), user.getPlatform(),
                            String.valueOf(AppUtils.LocationUtils.getDistrictIndex(user.getDistrict())));
                    //                    finish();
                } else {

                    Toast.makeText(mContext, getString(R.string.already_registered), Toast.LENGTH_SHORT).show();
                    mRegisterButton.setEnabled(true);
                    mProgressBar.setVisibility(View.GONE);
                }

//                Log.e("Phone", response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AppUtils.checkVolleyError(getApplicationContext(),error);

                if(!isFinishing() && !alreadyShownError) {
                    alreadyShownError =true;
                    buildAlertDialog(
                            RegisterFailedActivity.this,
                            getString(R.string.send_error)
                            , getString(R.string.developer_will_be_notified_with_the_error)
                            , getString(R.string.phone_number_page)
                            , "\n \n \n" +
                                    getString(R.string.you_have_error_in) + " " + error);
                }


                mRegisterButton.setEnabled(true);
                mProgressBar.setVisibility(View.GONE);
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

    private void registerNewUser(final String firstName,final String lastName
            , final String userPassword
            , final String email
            , final String firebaseUid,
                                 final String phoneNo, String location,String platform,String district) {
        mProgressBar.setVisibility(View.VISIBLE);
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_add_user.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, getPostResponseListener(firstName,lastName)
                , getPostErrorListener(firstName,lastName,userPassword,email,firebaseUid,phoneNo,location, district)) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("phone", phoneNo);
                params.put("firebase_uid", firebaseUid);
                params.put("email", email);
                params.put("password", userPassword);
                params.put("location", String.valueOf(AppUtils.LocationUtils.getLocationIndex(location)));
                params.put("district", String.valueOf(AppUtils.LocationUtils.getDistrictIndex(district)));
                params.put("platform", platform);
                return params;
            }

            ;
        };

        requstQueue.add(stringRequest);
//        addAccumulatedDaysForFirstTime(firebaseUid);


    }

    private Response.Listener<String> getPostResponseListener(String globalFirstName, String globalLastName) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(RegisterFailedActivity.this, getString(R.string.registered_successfully), Toast.LENGTH_LONG).show();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(globalFirstName + " " + globalLastName).build();

                        firebaseUser.updateProfile(profileUpdates);
                        removeDataFromInfoSharedPref();
                        mProgressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(RegisterFailedActivity.this, MVPLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        };
    }

    private int registeringCount = 0;
    private Response.ErrorListener getPostErrorListener(String globalFirstName,String globalLastName,String globalPass,String globalEmail,String globalId,String phoneNo,String location, String globalDistrict) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                registeringCount++;
//                    Toast.makeText(PhoneNumberVerificationActivity.this, getString(R.string.something_is_wrong), Toast.LENGTH_LONG).show();
                AppUtils.checkVolleyError(RegisterFailedActivity.this,error);
                if (registeringCount < 3) {
                    registerNewUser(globalFirstName,globalLastName, globalPass, globalEmail, globalId, phoneNo, location,platform,globalDistrict);
                    mProgressBar.setVisibility(View.VISIBLE);
                    settingUpAccount.setText(getString(R.string.registered_failed_retrying));
                } }
        };
    }

    private void removeDataFromInfoSharedPref()
    {
        SharedPreferences userPref = getSharedPreferences(SHARED_PREF_USER_DATA, MODE_PRIVATE);
        userPref.edit().clear().apply();
    }
}
