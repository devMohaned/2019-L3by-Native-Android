package com.gamesexchange.gamesexchange.activities.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.gamesexchange.gamesexchange.activities.login.MVPLoginActivity;
import com.gamesexchange.gamesexchange.model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.gamesexchange.gamesexchange.Utils.AppUtils.buildAlertDialog;
import static com.gamesexchange.gamesexchange.Utils.Constants.SHARED_PREF_USER_DATA;

public class PhoneNumberVerificationActivity extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;

    private String  phoneNo, email, password,location,firstName,lastName,platform,district;
    private User user;
    private ProgressBar progressBar;
    private TextView settingUpAccount;
    private String globalFirstName,globalLastName, globalPass, globalEmail, globalId, globalPhone,globalPlatform;
    private Button confirmCodeBtn;
    private boolean alreadyShownError = false;
    private int registeringCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();
        editTextCode = findViewById(R.id.editTextCode);
        editTextCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    AppUtils.hideSoftKeyboard(PhoneNumberVerificationActivity.this);
                    return true;
                }
                return false;

                        }
        });
        settingUpAccount = findViewById(R.id.ID_setting_up_your_account);
        progressBar = findViewById(R.id.ID_reg_progress_bar);

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
//        String mobile = intent.getStringExtra("mobile");
        phoneNo = intent.getStringExtra("mobile");
        firstName = intent.getStringExtra("first_name");
        lastName = intent.getStringExtra("last_name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        location = intent.getStringExtra("location");
        platform = intent.getStringExtra("platform");
        district = intent.getStringExtra("district");
        user = new User();
        user.setPhoneNumber(phoneNo);
        user.setEmail(email);
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setPassword(password);
        user.setLocation(location);
        user.setPlatform(platform);
        user.setDistrict(district);
        sendVerificationCode(/*mobile*/phoneNo);


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        confirmCodeBtn = findViewById(R.id.ID_pn_send_verification_sign_in);
        confirmCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(PhoneNumberVerificationActivity.this);
                confirmCodeBtn.setEnabled(false);
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError(getString(R.string.enter_vaild_code));
                    editTextCode.requestFocus();
                    confirmCodeBtn.setEnabled(true);
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+1" + /*mobile*/"6505553434",
                "+2" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneNumberVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

      //      Log.e("Phonee", e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        try {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            //signing the user
            signInWithPhoneAuthCredential(credential);
            confirmCodeBtn.setEnabled(false);
        }catch (IllegalArgumentException e)
        {
            Toast.makeText(this, getString(R.string.registered_failed_try_again), Toast.LENGTH_LONG).show();
            confirmCodeBtn.setEnabled(true);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneNumberVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            removePhoneAccountAndCreateAnEmail(credential);




                         /*   Intent intent = new Intent(PhoneNumberVerificationActivity.this, NoInternetConnection.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
*/
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = getString(R.string.couldnot_verify_phone_number);

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = getString(R.string.invalid_code);
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction(getString(R.string.dismiss), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                            confirmCodeBtn.setEnabled(true);
                        }
                    }
                });
    }


    void settingUpAccountViewsVisible() {
        progressBar.setVisibility(View.VISIBLE);
        settingUpAccount.setVisibility(View.VISIBLE);
    }

    void settingUpAccountViewsInVisible() {
        progressBar.setVisibility(View.GONE);
        settingUpAccount.setVisibility(View.GONE);
    }



    private void addDataInformationIntoSharedPref(
            String globalFirstName,
            String globalLastName,
            String globalEmail,
            String globalPhone,
            String globalPlatform) {

        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_USER_DATA, MODE_PRIVATE).edit();
        editor.putString("first_name", globalFirstName);
        editor.putString("last_name", globalLastName);
        editor.putString("email", globalEmail);
        editor.putString("phone_number", globalPhone);
        editor.putString("platform", globalPlatform);

        editor.apply();

    }

    private void removeDataFromInfoSharedPref()
    {
        SharedPreferences userPref = getSharedPreferences(SHARED_PREF_USER_DATA, MODE_PRIVATE);
        userPref.edit().clear().apply();
    }

    /*
     ------------------------------------ Firebase ---------------------------------------------
      */


    private void registerNewEmail(final String email, final String password, final User registeredUser) {
        settingUpAccountViewsVisible();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
// If sign up fails, display a message to the user. If sign up succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(PhoneNumberVerificationActivity.this, R.string.auth_register_failed,
                                    Toast.LENGTH_LONG).show();
                            confirmCodeBtn.setEnabled(true);
                            settingUpAccount.setVisibility(View.VISIBLE);
                            settingUpAccount.setText(getString(R.string.registered_failed_try_again));
                        } else if (task.isSuccessful()) {
                            globalFirstName = registeredUser.getFirst_name();
                            globalLastName = registeredUser.getLast_name();
                            globalPass = registeredUser.getPassword();
                            globalEmail = registeredUser.getEmail();
                            globalId = registeredUser.getFirebase_uid();
                            globalPhone = phoneNo;
                            globalPlatform = registeredUser.getPlatform();

                            String userID = mAuth.getCurrentUser().getUid();
                            registerNewUser(registeredUser.getFirst_name(),registeredUser.getLast_name(), registeredUser.getPassword(), registeredUser.getEmail(), userID, phoneNo,registeredUser.getLocation(),registeredUser.getPlatform());
                            addDataInformationIntoSharedPref(globalFirstName,globalLastName,globalEmail,globalPhone,globalPlatform);




                                                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                                                            .child(userID).setValue(50);


                                                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                                                            .child(userID).setValue(50 / 2);



                        }
                        // ...
                        settingUpAccountViewsInVisible();
                    }
                });

    }

    private void registerNewUser(final String firstName,
            final String lastName
            , final String userPassword
            , final String email
            , final String firebaseUid,
                                 final String phoneNo, String location
    ,String platform) {
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_add_user.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, getPostResponseListener()
                , getPostErrorListener()) {
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

    private Response.Listener<String> getPostResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                removeDataFromInfoSharedPref();
                String userName = firstName + " " + lastName;


                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userName).build();

                firebaseUser.updateProfile(profileUpdates);

//                Toast.makeText(PhoneNumberVerificationActivity.this, getString(R.string.registered_successfully), Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = getSharedPreferences("warning", MODE_PRIVATE).edit();
                editor.putString("warning_text", "already_warned");
                editor.apply();

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        Intent intent = new Intent(PhoneNumberVerificationActivity.this, MVPLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        };
    }

    private Response.ErrorListener getPostErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtils.checkVolleyError(getApplicationContext(),error);
                registeringCount++;
                if(!isFinishing() && !alreadyShownError) {
                    alreadyShownError =true;
                    buildAlertDialog(
                            PhoneNumberVerificationActivity.this,
                            getString(R.string.send_error)
                            , getString(R.string.developer_will_be_notified_with_the_error)
                            , getString(R.string.phone_number_page)
                            , "\n \n \n" +
                                    getString(R.string.you_have_error_in) + " " + error);
                }


//                    Toast.makeText(PhoneNumberVerificationActivity.this, getString(R.string.something_is_wrong), Toast.LENGTH_LONG).show();
                AppUtils.checkVolleyError(PhoneNumberVerificationActivity.this,error);
                if (registeringCount < 3) {
                    registerNewUser(globalFirstName,globalLastName, globalPass, globalEmail, globalId, phoneNo, location,globalPlatform);
                    settingUpAccount.setVisibility(View.VISIBLE);
                    settingUpAccount.setText(getString(R.string.registered_failed_retrying));
                } }
        };
    }


    private void removePhoneAccountAndCreateAnEmail(PhoneAuthCredential credential) {
        final FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.


        // Prompt the user to re-provide their sign-in credentials
        user2.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user2.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                          //                  Log.d("Phone", "User account deleted.");
                                            registerNewEmail(user.getEmail(), user.getPassword(), user);
                                        } else {
                                            Toast.makeText(PhoneNumberVerificationActivity.this, getString(R.string.registering_failed_try_again), Toast.LENGTH_SHORT).show();
                                            settingUpAccount.setVisibility(View.VISIBLE);
                                            settingUpAccount.setText(getString(R.string.registered_failed_trying_again));
                                        }
                                    }
                                });

                    }
                });
    }


}
