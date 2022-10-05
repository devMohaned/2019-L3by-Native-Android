package com.gamesexchange.gamesexchange.activities.profile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.adapter.GamesOfTraderAdapter;
import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.ID_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        TextView profileLocation = findViewById(R.id.ID_profile_location);
        TextView mDummyGameListText2 = findViewById(R.id.ID_game_place_holder);
        TextView mDummyWishListText2 = findViewById(R.id.ID_wish_place_holder);
        TextView nameOfProfile = findViewById(R.id.ID_profile_name);
        CircleImageView profileImage = findViewById(R.id.ID_profile_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String idOfUser = extras.getString(Constants.INTENT_PROFILE_USER_ID);
            String firstNameOfUser = extras.getString(Constants.INTENT_PROFILE_FIRST_NAME);
            String lastNameOfUser = extras.getString(Constants.INTENT_PROFILE_LAST_NAME);
            String photoOfUser = extras.getString(Constants.INTENT_PROFILE_PHOTO);

            Picasso.get().load(photoOfUser).into(profileImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    profileImage.setImageResource(R.drawable.trevor);
                }
            });

            if (Locale.getDefault().getLanguage().equals("ar"))
                nameOfProfile.setText(getString(R.string.profile) + firstNameOfUser);
            else
                nameOfProfile.setText(firstNameOfUser + getString(R.string.apostrif) + " " + getString(R.string.profile));

            mDummyGameListText2.setText(firstNameOfUser + getString(R.string.apostrif) + " " + getString(R.string.games));
            mDummyWishListText2.setText(firstNameOfUser + getString(R.string.apostrif) + " " + getString(R.string.wishes));

            getBothWishlistAndGameList(idOfUser);
            getLocationOfUser(idOfUser,profileLocation);
        }
    }


    private void getBothWishlistAndGameList(String userId) {

        RecyclerView mGamesRecyclerView, mWishesRecyclerView;
        List<Game> gameList = new ArrayList<>();
        List<Game> wishList = new ArrayList<>();
        GamesOfTraderAdapter gamesAdapter, wishesAdapter;

        mGamesRecyclerView = findViewById(R.id.ID_profile_game_recyclerview);
        mGamesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mGamesRecyclerView.setHasFixedSize(true);

        mWishesRecyclerView = findViewById(R.id.ID_profile_wish_recyclerview);
        mWishesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mWishesRecyclerView.setHasFixedSize(true);

        gamesAdapter = new GamesOfTraderAdapter(this, gameList);
        wishesAdapter = new GamesOfTraderAdapter(this, wishList);
        mGamesRecyclerView.setAdapter(gamesAdapter);
        mWishesRecyclerView.setAdapter(wishesAdapter);


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_both_wishlist_and_gamelist.php?search=" + userId + Constants.SERVER_AND_GET_API_KEY;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {

                StringBuilder wishListStringBuilder = new StringBuilder();
                StringBuilder gameListStringBuilder = new StringBuilder();


                for (int i = 0; i < responseJSONArray.length(); i++) {
                    try {
                        JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                        String gameName = jsonObject.getString(Constants.SQL_DATABASE.GAME_NAME);
                       /* String gameDesc = jsonObject.getString(Constants.SQL_DATABASE.GAME_DESC);
                        String frontImg = jsonObject.getString(Constants.SQL_DATABASE.FRONT_IMAGE);*/
                        String flag = jsonObject.getString(Constants.SQL_DATABASE.FLAG);
                       /* String backImg = jsonObject.getString(Constants.SQL_DATABASE.BACK_IMAGE);
                        String moreImg = jsonObject.getString(Constants.SQL_DATABASE.OTHER_IMG);*/
                        Game game = new Game();
                        game.setGame_name(gameName);
                       /* game.setFrontImg(frontImg);
                        game.setBackImg(backImg);
                        game.setMoreImg(moreImg);
                        game.setGameDescription(gameDesc);*/
                        game.setFlag(flag);

                        if (flag.equals("w")) {
                            wishList.add(game);
                        } else {
                            gameList.add(game);
                        }

                        if (i == responseJSONArray.length() - 1) {
                            wishesAdapter.notifyDataSetChanged();
                            gamesAdapter.notifyDataSetChanged();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtils.checkVolleyError(ProfileActivity.this,error);
            }
        });


        requestQueue.add(jsonArrayRequest);


    }

    private void getLocationOfUser(String uid,TextView locationTextView) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_user_location.php?firebase_uid=" + uid + Constants.SERVER_AND_GET_API_KEY;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {
                for (int i = 0; i < responseJSONArray.length(); i++) {
                    try {

                        String location = responseJSONArray.getJSONObject(i).getString("location");
                        String district = responseJSONArray.getJSONObject(i).getString("district");
                        if (!location.toLowerCase().equals("null")) {

                            if (!district.toLowerCase().equals("null")){
                                try {
                                    locationTextView.setText(
                                            AppUtils.LocationUtils.getDistrictFromIndex(Integer.parseInt(district)) + ", "
                                            + AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(location)));
                                }catch (Exception e)
                                {
                                    locationTextView.setText(AppUtils.LocationUtils
                                            .getLocationFromIndex(Integer.parseInt(location)));

                                }
                            }else{
                                locationTextView.setText(AppUtils.LocationUtils
                                        .getLocationFromIndex(Integer.parseInt(location)));
                            }
                        } else {
                            locationTextView.setText(getString(R.string.location_not_found));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtils.checkVolleyError(ProfileActivity.this,error);
            }
        });


        requestQueue.add(jsonArrayRequest);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
