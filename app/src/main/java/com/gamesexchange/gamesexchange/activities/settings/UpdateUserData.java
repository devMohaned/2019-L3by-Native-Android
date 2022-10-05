package com.gamesexchange.gamesexchange.activities.settings;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;

public class UpdateUserData extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);

        Context mContext = this;

        Toolbar toolbar = findViewById(R.id.ID_update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);


        EditText firstName, lastName;
        Spinner mDistrictSpinner;
        firstName = findViewById(R.id.ID_update_first_name);
        lastName = findViewById(R.id.ID_update_last_name);
        mDistrictSpinner = findViewById(R.id.ID_update_district_spinner);

        Spinner mLocationSpinner = findViewById(R.id.ID_update_location_spinner);
        if (!currentUser.getLocation().equals("null")) {
            try {
                String myLocation = AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(currentUser.getLocation()));
                for (int i = 0; i < mLocationSpinner.getCount(); i++) {
                    if (myLocation.equalsIgnoreCase(mLocationSpinner.getItemAtPosition(i).toString())) {
                        mLocationSpinner.setSelection(i);
                    }

                }

            } catch (Exception e) {
//                Log.e("TAG",e.toString());
            }
        }

        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currentSelectedItemInSpinner = mLocationSpinner.getSelectedItem().toString();
                int districtIndex = AppUtils.LocationUtils.getLocationIndex(currentSelectedItemInSpinner);
                AppUtils.LocationUtils.changeDistrictSpinner(mContext, districtIndex, mDistrictSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (!currentUser.getDistrict().equals("null")) {
            try {
                String myLocation = AppUtils.LocationUtils.getDistrictFromIndex(Integer.parseInt(currentUser.getDistrict()));
                for (int i = 0; i < mDistrictSpinner.getCount(); i++) {
                    if (myLocation.equalsIgnoreCase(mDistrictSpinner.getItemAtPosition(i).toString())) {
                        mDistrictSpinner.setSelection(i);
                    }

                }

            } catch (Exception e) {
//                Log.e("TAG",e.toString());
            }
        }

        firstName.setText(currentUser.getFirst_name());
        lastName.setText(currentUser.getLast_name());

        Button updateButton = findViewById(R.id.ID_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNewUser(firstName.getText().toString()
                        , lastName.getText().toString()
                        , String.valueOf(AppUtils.LocationUtils.getLocationIndex(
                                mLocationSpinner.getSelectedItem().toString())),
                        String.valueOf(AppUtils.LocationUtils.getDistrictIndex(mDistrictSpinner.getSelectedItem().toString())), updateButton);
            }
        });


        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError = AppUtils.Validators.validateName(mContext, editable.toString());
                checkForError(firstName, errorOrNoError);
            }
        });


        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String errorOrNoError = AppUtils.Validators.validateName(mContext, editable.toString());
                checkForError(lastName, errorOrNoError);
            }
        });


    }

    private void checkForError(EditText editText, String error) {
        if (!error.equals(Constants.ERROR_FREE)) {
            editText.setError(error);
        } else {
            editText.setError(null);
        }
    }

    private void updateNewUser(final String firstName, final String lastName, final String location, final String district, Button updateBtn) {
        updateBtn.setEnabled(false);
        final ProgressBar progressBar = findViewById(R.id.ID_update_games_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_update_user.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                currentUser.setFirst_name(firstName);
                currentUser.setLast_name(lastName);
                currentUser.setDistrict(district);
                currentUser.setLocation(String.valueOf(location));
                Toast.makeText(UpdateUserData.this, getString(R.string.restart_the_app_to_confirm), Toast.LENGTH_LONG).show();
                finish();


            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateUserData.this, getString(R.string.something_is_wrong), Toast.LENGTH_LONG).show();
                updateBtn.setEnabled(true);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firebase_uid", currentUser.getFirebase_uid());
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("location", location);
                params.put("district", district);


                return params;
            }

            ;
        };
//        stringRequest.setTag(REQ_TAG);
        requstQueue.add(stringRequest);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
