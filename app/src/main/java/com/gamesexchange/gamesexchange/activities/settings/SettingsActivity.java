package com.gamesexchange.gamesexchange.activities.settings;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.activities.legals.PrivacyPolicyActivity;
import com.gamesexchange.gamesexchange.activities.legals.EulaActivity;
import com.gamesexchange.gamesexchange.activities.legals.TermsOfUseActivity;
import com.gamesexchange.gamesexchange.activities.refers.ReferActivity;
import com.gamesexchange.gamesexchange.activities.reports.ReportActivity;
import com.gamesexchange.gamesexchange.model.User;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;
import com.gamesexchange.gamesexchange.rooms.AppExecuter;
import com.gamesexchange.gamesexchange.views.CustomButtonView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;


public class SettingsActivity extends AppCompatActivity {

    private boolean syncedWishes = false;
    private boolean syncedGames = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        setupToolbar();
        setupViews();
//        getUserData();
    }

    private void setupToolbar()
    {
        Toolbar toolbar = findViewById(R.id.ID_my_settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);



    }


    private void setupViews() {
        TextView myName, myPhone, myEmail, myLocation, myDistrict;
        myName = findViewById(R.id.ID_settings_my_name);
        myPhone = findViewById(R.id.ID_settings_my_phone);
        myEmail = findViewById(R.id.ID_settings_my_email);
        myLocation = findViewById(R.id.ID_settings_my_location);
        myDistrict = findViewById(R.id.ID_settings_my_district);

        myName.setText(currentUser.getFirst_name() + " " + currentUser.getLast_name());
        myPhone.setText(currentUser.getPhoneNumber());
        myEmail.setText(currentUser.getEmail());

        if (!currentUser.getLocation().equals("null")) {
            try {
                myLocation.setText(AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(currentUser.getLocation())));
            }catch (Exception e)
            {
                myLocation.setText(getString(R.string.location_not_found));
            }
        }else{
            myLocation.setText(getString(R.string.location_not_found));

        }

        try {
            myDistrict.setText(AppUtils.LocationUtils.getDistrictFromIndex(Integer.parseInt(currentUser.getDistrict())));
        }catch (Exception e)
        {
            myDistrict.setText(getString(R.string.location_not_found));
        }
        ImageView editSettings = findViewById(R.id.ID_settings_fab);
        editSettings.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this,UpdateUserData.class));
            finish();
        });

        CustomButtonView syncButton = findViewById(R.id.ID_sync_games);
        syncButton.setOnClickListener(view -> restoreGames(syncButton));





    }

    private void restoreGames(CustomButtonView syncBtn) {
        syncBtn.setEnabled(false);
        ProgressBar mPb = findViewById(R.id.ID_setting_progressbar);
        mPb.setVisibility(View.VISIBLE);
        List<String> restoreGameList = new ArrayList<>();
        List<String> restoreWishList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_both_wishlist_and_gamelist_names_only.php?search=" + currentUser.getFirebase_uid() + Constants.SERVER_AND_GET_API_KEY;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {


                for (int i = 0; i < responseJSONArray.length(); i++) {
                    try {
                        JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                        String gameName = jsonObject.getString("game_name");
                        if (jsonObject.getString("flag").equals("w")) {
                            restoreWishList.add(gameName);
                        } else if (jsonObject.getString("flag").equals("o")){
                            restoreGameList.add(gameName);
                        }
                        if (i == responseJSONArray.length() - 1) {
                            AppExecuter.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    // REMOVE ALL GAMES & WISHES
                                    AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().
                                            removeAllGamesAndWishesWhereYouSelectedBefore(
                                            false, "");


                                    // GAMELIST
                                    for (int i = 0; i < restoreGameList.size(); i++) {
                                        String gameName = restoreGameList.get(i);
                                        AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().restoreGamesOrWishes(
                                                gameName, true, "o");

                                        if (i == restoreGameList.size() - 1) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    syncedGames = true;
                                                    if (syncedGames && syncedWishes)
                                                    {
                                                        syncBtn.setEnabled(true);
                                                        mPb.setVisibility(View.GONE);
                                                        Toast.makeText(SettingsActivity.this, getString(R.string.restart_the_app_to_confirm), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }
                                    }


                                    // WISHLIST
                                    for (int i = 0; i < restoreWishList.size(); i++) {
                                        String gameName = restoreWishList.get(i);
                                        AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().restoreGamesOrWishes(
                                                gameName, true, "w");

                                        if (i == restoreWishList.size() - 1) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    syncedWishes = true;
                                                    if (syncedGames && syncedWishes)
                                                    {
                                                        syncBtn.setEnabled(true);
                                                        mPb.setVisibility(View.GONE);
                                                        Toast.makeText(SettingsActivity.this, getString(R.string.restart_the_app_to_confirm), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }
                                    }




                                }
                            });


                           /* AppExecuter.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });*/

                            if (syncedGames || syncedWishes)
                            {
                                syncBtn.setEnabled(true);
                                mPb.setVisibility(View.GONE);
                                Toast.makeText(SettingsActivity.this, getString(R.string.restart_the_app_to_confirm), Toast.LENGTH_LONG).show();
                            }

                            if (syncedGames && syncedWishes)
                            {
                                syncBtn.setEnabled(true);
                                mPb.setVisibility(View.GONE);
                                Toast.makeText(SettingsActivity.this, getString(R.string.restart_the_app_to_confirm), Toast.LENGTH_LONG).show();
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        syncBtn.setEnabled(true);
                        mPb.setVisibility(View.GONE);
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtils.checkVolleyError(SettingsActivity.this, error);
                syncBtn.setEnabled(true);
            }
        });


        requestQueue.add(jsonArrayRequest);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
