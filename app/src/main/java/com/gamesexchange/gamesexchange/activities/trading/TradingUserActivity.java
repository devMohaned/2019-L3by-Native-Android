package com.gamesexchange.gamesexchange.activities.trading;

import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.gamesexchange.gamesexchange.Utils.AmazonUtils;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.messaging.MessagingActivity;
import com.gamesexchange.gamesexchange.activities.safetyRules.SaftyRulesActivity;
import com.gamesexchange.gamesexchange.adapter.GamesOfTraderAdapter;
import com.gamesexchange.gamesexchange.model.ChatMessageVer2;
import com.gamesexchange.gamesexchange.model.Game;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;

public class TradingUserActivity extends AppCompatActivity {


    private boolean isLoading = false;
    private RecyclerView mGamesRecyclerView, mWishesRecyclerView;
    private List<Game> gameList = new ArrayList<>();
    private List<Game> wishList = new ArrayList<>();
    GamesOfTraderAdapter gamesAdapter, wishesAdapter;
    String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_user);

        mGamesRecyclerView = findViewById(R.id.ID_trading_game_recyclerview);
        mGamesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mGamesRecyclerView.setHasFixedSize(true);
        mGamesRecyclerView.setNestedScrollingEnabled(false);

        mWishesRecyclerView = findViewById(R.id.ID_trading_wish_recyclerview);
        mWishesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mWishesRecyclerView.setHasFixedSize(true);
        mWishesRecyclerView.setNestedScrollingEnabled(false);

        gamesAdapter = new GamesOfTraderAdapter(this, gameList);
        wishesAdapter = new GamesOfTraderAdapter(this, wishList);
        mGamesRecyclerView.setAdapter(gamesAdapter);
        mWishesRecyclerView.setAdapter(wishesAdapter);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.ID_different_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mCommonUserLocation = findViewById(R.id.ID_different_user_address);

        TextView mMyWishes = findViewById(R.id.ID_different_my_wishes);

        TextView mDummyGameListText2 = findViewById(R.id.ID_game_place_holder);
        TextView mDummyWishListText2 = findViewById(R.id.ID_wish_place_holder);
        ImageView profileImageView = findViewById(R.id.ID_different_user_profile_image);

        TextView mToolbarProfileName = findViewById(R.id.ID_different_user_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String myUserUid = currentUser.getFirebase_uid();
            String idOfCommonUser = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_ID);
            String firstNameOfCommonUser = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_FIRST_NAME);
            String lastNameOfCommonUser = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_LAST_NAME);
            String nameOfCommonUser = firstNameOfCommonUser + " " + lastNameOfCommonUser;
            String locationOfcommonUser = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_LOCATION);
            String districtOfcommonUser = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_DISTRICT);
            String gamesOfCommonUser = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_GAMES);
            String profileImage = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_PROFILE_IMAGE);
            String userGames = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_GAMES);
            String userWishes = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_WISHES);
             userToken = extras.getString(Constants.INTENTS.INTENT_DIFFERENT_USER_TOKEN);

            try {
                if (userGames.length() > 6) {
                    convertGamesFromStringToJsonArray(userGames);

                } else {
                    addEmptyGameName(gameList);
                }
                if (userWishes.length() > 6) {
                    convertWishesFromStringToJsonArray(userWishes);
                } else {
                    addEmptyGameName(wishList);
                }
            } catch (NullPointerException e) {
                getBothWishlistAndGameList(idOfCommonUser);
            }


            String photoUrl = AmazonUtils.CLOUDFLARE + AmazonUtils.PROFILE_DIRECTORY + profileImage;
            Picasso.get().load(photoUrl).into(profileImageView, new Callback() {
                @Override
                public void onSuccess() {
                    profileImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    profileImageView.setImageResource(R.drawable.trevor);
                }
            });


            mToolbarProfileName.setText(nameOfCommonUser);

            mDummyWishListText2.setText(firstNameOfCommonUser + getString(R.string.apostrif) + " "
                    + getString(R.string.wishes));

            mDummyGameListText2.setText(firstNameOfCommonUser + getString(R.string.apostrif) + " "
                    + getString(R.string.games));


            if (!districtOfcommonUser.toLowerCase().equals("null")) {
                try {
                    mCommonUserLocation.setText(
                        /*getString(R.string.location_at) + " "
                        + */
                             AppUtils.LocationUtils.getDistrictFromIndex(Integer.parseInt(districtOfcommonUser)) + ", "  + AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(locationOfcommonUser)));
                }catch (Exception e)
                {
                    mCommonUserLocation.setText(
                            /*getString(R.string.location_at) + " " +*/
                            AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(locationOfcommonUser)));
                }
            } else {
                mCommonUserLocation.setText(
                        /*getString(R.string.location_at) + " " +*/
                        AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(locationOfcommonUser)));
            }

            FloatingActionButton mMessageBtn = findViewById(R.id.ID_different_user_message);
            mMessageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String roomId = FirebaseDatabase.getInstance().getReference().push().getKey();
                    messageWithUser(roomId, myUserUid, idOfCommonUser, myUserUid,firstNameOfCommonUser,lastNameOfCommonUser,photoUrl,mMessageBtn);


                }
            });

            if (gamesOfCommonUser != null) {
                mMyWishes.setText(organizeText(gamesOfCommonUser));
            } else {
                mMyWishes.setText(getString(R.string.empty_wishes));
            }


        }


        TextView safetyMeasuresText = findViewById(R.id.ID_safety_measures_text);
        safetyMeasuresText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TradingUserActivity.this, SaftyRulesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void convertGamesFromStringToJsonArray(String string) {
        try {
            JSONArray jsonArray = new JSONArray(string);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Game game = new Game();
                game.setGame_name(jsonObject.getString("game_name"));
                gameList.add(game);


                if (i == jsonArray.length() - 1) {
                    gamesAdapter.notifyDataSetChanged();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void convertWishesFromStringToJsonArray(String string) {
        try {
            JSONArray jsonArray = new JSONArray(string);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Game game = new Game();
                game.setGame_name(jsonObject.getString("game_name"));
                wishList.add(game);

                if (i == jsonArray.length() - 1) {
                    wishesAdapter.notifyDataSetChanged();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String organizeText(String games) {

        return games.replace(",", "\n");
    }

    private void getBothWishlistAndGameList(String userId) {
        isLoading = true;


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_both_wishlist_and_gamelist.php?search=" + userId + Constants.SERVER_AND_GET_API_KEY;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {


                for (int i = 0; i < responseJSONArray.length(); i++) {
                    try {
                        JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                        String gameName = jsonObject.getString(Constants.SQL_DATABASE.GAME_NAME);
                        String flag = jsonObject.getString(Constants.SQL_DATABASE.FLAG);

                     /* String gameDesc = jsonObject.getString(Constants.SQL_DATABASE.GAME_DESC);
                        String frontImg = jsonObject.getString(Constants.SQL_DATABASE.FRONT_IMAGE);
                        String backImg = jsonObject.getString(Constants.SQL_DATABASE.BACK_IMAGE);
                        String moreImg = jsonObject.getString(Constants.SQL_DATABASE.OTHER_IMG);*/
                        Game game = new Game();
                        game.setGame_name(gameName);
                     /*   game.setFrontImg(frontImg);
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
                            isLoading = false;

                            if (wishList.size() == 0) {
                                addEmptyGameName(wishList);
                            }
                            if (gameList.size() == 0) {
                                addEmptyGameName(gameList);
                            }

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
                isLoading = false;
                AppUtils.checkVolleyError(TradingUserActivity.this, error);
            }
        });


        requestQueue.add(jsonArrayRequest);


    }

    private void addEmptyGameName(List<Game> list) {
        Game game = new Game();
        game.setGame_name(getString(R.string.no_games_found));
        list.add(game);
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

    private void sendNewMessage(String roomId, String idOfCommonUser, String recieverFirstName, String recieverLastName, String recieverPhoto) {
        String chatText = getString(R.string.i_am_willing_to_trade);

        ChatMessageVer2 chatMessage = new ChatMessageVer2();
        String chatSenderUid = currentUser.getFirebase_uid();
        String chatSenderName = currentUser.getName();
        long chatMessageTime = SystemClock.currentThreadTimeMillis();
        chatMessage.setChatText(chatText);
        chatMessage.setChatSender(chatSenderName);
        chatMessage.setChatSenderUid(chatSenderUid);
        chatMessage.setTime(chatMessageTime);


        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_DB_MESSAGES)
                .child(roomId)
                .child(roomId)
                .setValue(chatMessage);


        Intent intent = new Intent(TradingUserActivity.this, MessagingActivity.class);
        intent.putExtra(Constants.INTENT_ROOM_ID, roomId);
        intent.putExtra(Constants.INTENT_RECIEVER_ID, idOfCommonUser);
        intent.putExtra(Constants.INTENT_RECEIVER_FIRST_NAME, recieverFirstName);
        intent.putExtra(Constants.INTENT_RECEIVER_LAST_NAME, recieverLastName);
        intent.putExtra(Constants.INTENT_RECIEVER_PHOTO, recieverPhoto);
        intent.putExtra(Constants.INTENT_RECIEVER_TOKEN,userToken);

        startActivity(intent);
        confirmNotification(idOfCommonUser,
                currentUser.getName(),
                chatMessage.getChatText());
    }


    private void messageWithUser(String room_id, String sender_id, String reciever_id, String created_by
            , String firstNameOfCommonUser, String lastNameOfCommonUser, String photoUrl, FloatingActionButton btn) {

        btn.setEnabled(false);

        String url =
                Constants.SERVER_BASE + "rel_add_room.php" + Constants.SERVER_GET_API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case Constants.SQL_DATABASE.INSERTED:
                                sendNewMessage(room_id, reciever_id, firstNameOfCommonUser, lastNameOfCommonUser, photoUrl);
                                btn.setEnabled(true);
                                break;

                            default:
                                if (response.length() > 0) {
                                    Intent intent = new Intent(TradingUserActivity.this, MessagingActivity.class);
                                    intent.putExtra(Constants.INTENT_ROOM_ID, response);
                                    intent.putExtra(Constants.INTENT_RECEIVER_FIRST_NAME, firstNameOfCommonUser);
                                    intent.putExtra(Constants.INTENT_RECEIVER_LAST_NAME, lastNameOfCommonUser);
                                    intent.putExtra(Constants.INTENT_RECIEVER_ID, reciever_id);
                                    intent.putExtra(Constants.INTENT_RECIEVER_PHOTO, photoUrl);
                                    intent.putExtra(Constants.INTENT_RECIEVER_TOKEN, userToken);
                                    startActivity(intent);
                                    btn.setEnabled(true);
                                }
// Toast.makeText(TradingUserActivity.this, getString(R.string.it_seems_you_have_tried_to_connect_before), Toast.LENGTH_SHORT).show();

                                break;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(TradingUserActivity.this, getString(R.string.something_is_wrong), Toast.LENGTH_LONG).show();
                        AppUtils.checkVolleyError(TradingUserActivity.this, error);
                        btn.setEnabled(true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("room_id", room_id);
                params.put("room_sender_id", sender_id);
                params.put("room_reciever_id", reciever_id);
                params.put("room_created_by", created_by);
//                params.put("room_created_at", created_at);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    String token = null;

    private void confirmNotification(String userID, String title, String notification) {
        if (userToken.length() > 5) {
            sendNotificationToappServer(userToken, title, notification);
        } else {
            if (token == null) {
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FIREBASE_DB_TOKENS)
                        .child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        token = dataSnapshot.getValue(String.class);
                        sendNotificationToappServer(token, title, notification);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                sendNotificationToappServer(token, title, notification);
            }
        }
    }

    public void sendNotificationToappServer(final String token, final String title, final String notification) {
       String url =
               Constants.SERVER_BASE + "rel_send_notification.php?token=" + token + Constants.SERVER_AND_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                         Toast.makeText(MessagingActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TradingUserActivity.this, getString(R.string.something_is_wrong), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("message", notification);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}

