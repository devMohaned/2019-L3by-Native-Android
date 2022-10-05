package com.gamesexchange.gamesexchange.activities.messaging;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AmazonUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.adapter.RoomAdapter;
import com.gamesexchange.gamesexchange.model.RoomItemVer2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;

public class UserListMessagesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RoomAdapter mChatAdapter;
    List<RoomItemVer2> dialogList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mEmptyUserList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_messages);
        setupUserlistMessagesViews();
        setupToolbar();
    }

    private void setupToolbar()
    {
        Toolbar toolbar = findViewById(R.id.ID_user_list_messages_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.messages));
    }


    private void setupUserlistMessagesViews() {
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.ID_user_list_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                // Fetching data from server
                getChats();
            }
        });


        dialogList = new ArrayList<>();
        mChatAdapter = new RoomAdapter(getApplicationContext(), dialogList);
        RecyclerView recyclerView = findViewById(R.id.ID_user_list_messages_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mChatAdapter);

        mEmptyUserList = findViewById(R.id.ID_empty_user_list_text);

    }

    private void stopRefreshing(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getChats() {
        dialogList.clear();

        if (mChatAdapter != null) {
            mChatAdapter.notifyDataSetChanged();
        }


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_rooms.php?sender=" + currentUser.getFirebase_uid() + Constants.SERVER_AND_GET_API_KEY;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {

                long count = responseJSONArray.length();
                if (count > 0) {
                    mEmptyUserList.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                } else {
                    mEmptyUserList.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }


                for (int i = 0; i < responseJSONArray.length(); i++) {
                    try {
                        JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                        String roomId = jsonObject.getString(Constants.SQL_DATABASE.ROOM_ID);
                        String roomSenderId = currentUser.getFirebase_uid();
                        String roomSenderName = currentUser.getName();
                        String roomRecieverFirstName = jsonObject.getString("first_name");
                        String roomRecieverLastName = jsonObject.getString("last_name");
                        String roomRecieverPhoto = jsonObject.getString("profile_image");
                        String roomRecieverId = jsonObject.getString(Constants.SQL_DATABASE.ROOM_RECIEVER_ID);
                        String onlineStatus = jsonObject.getString("online_status");
                        String roomRecieverToken = jsonObject.getString("token");


                        RoomItemVer2 roomItem = new RoomItemVer2();
                        roomItem.setRoom_id(roomId);
                        roomItem.setRoom_sender_id(roomSenderId);
                        roomItem.setSenderName(roomSenderName);
                        roomItem.setRecieverFirstName(roomRecieverFirstName);
                        roomItem.setRecieverLastName(roomRecieverLastName);
                        roomItem.setRoom_reciever_id(roomRecieverId);
                        roomItem.setRecieverPhoto(AmazonUtils.CLOUDFLARE + AmazonUtils.PROFILE_DIRECTORY + roomRecieverPhoto);
                        roomItem.setOnlineStatus(onlineStatus);
                        roomItem.setRecieverToken(roomRecieverToken);


                        dialogList.add(roomItem);
                        if (i == responseJSONArray.length() - 1) {
                            mChatAdapter.notifyDataSetChanged();
                        }


                        mSwipeRefreshLayout.setRefreshing(false);
                        stopRefreshing(mSwipeRefreshLayout);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        mEmptyUserList.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                    stopRefreshing(mSwipeRefreshLayout);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopRefreshing(mSwipeRefreshLayout);
                mEmptyUserList.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        getChats();
    }
}
