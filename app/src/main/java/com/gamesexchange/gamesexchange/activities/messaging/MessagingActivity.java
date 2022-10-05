package com.gamesexchange.gamesexchange.activities.messaging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.profile.ProfileActivity;
import com.gamesexchange.gamesexchange.model.ChatMessage;
import com.gamesexchange.gamesexchange.model.ChatMessageVer2;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;

public class MessagingActivity extends AppCompatActivity {



    private TextView mGameOwner, mGameOwnerStatus;
    private EditText mMessageEditText;
    private String roomId;
    private LinearLayout layout;
    DatabaseReference mRoomChatDatabaseReference;
    ImageView mDoneButton;
    private ScrollView mScrollView;
    private String receiverID, receiverName, receiverFirstName, receiverLastName;
    private String recieverPhoto;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Toolbar toolbar = findViewById(R.id.ID_messaging_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);


        mGameOwner = findViewById(R.id.ID_game_owner_name);
        mGameOwnerStatus = findViewById(R.id.ID_messaging_status);


        Bundle extras = getIntent().getExtras();

        roomId = extras.getString(Constants.INTENT_ROOM_ID);
        receiverID = extras.getString(Constants.INTENT_RECIEVER_ID);
        receiverFirstName = extras.getString(Constants.INTENT_RECEIVER_FIRST_NAME);
        receiverLastName = extras.getString(Constants.INTENT_RECEIVER_LAST_NAME);
        recieverPhoto = extras.getString(Constants.INTENT_RECIEVER_PHOTO);
        receiverName = receiverFirstName + " " + receiverLastName;
        userToken = extras.getString(Constants.INTENT_RECIEVER_TOKEN);

        mGameOwner.setText(receiverName);
        checkReadibility(receiverName);
        getUserStatus(receiverID);





        setupWidgits();

        setupDatabase();

        setupClicks();

    }


    private void checkReadibility(String userName) {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_MESSAGES, MODE_PRIVATE);
        String restoredText = prefs.getString(userName, Constants.SHARED_PREF_MESSAGING_READ);
        if (restoredText.equals(Constants.SHARED_PREF_MESSAGING_UNREAD)) {
            SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF_MESSAGES, MODE_PRIVATE).edit();
            editor.remove(userName);
            editor.apply();
        } else {
            // READ CASE -- DO NOTHING
        }
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

    String token = null;

    private void confirmNotification(String userID, String title, String notification) {
        if (userToken.length() > 5) {
            sendNotificationToappServer(userToken,title,notification);
        }else {
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
        }}


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
                        Toast.makeText(MessagingActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_DB_ONLINE_STATUS)
                .child(currentUser.getFirebase_uid())
                .setValue(Constants.FIREBASE_DB_ONLINE_STATUS_IS_ONLINE);
      /*  startRepeatingTask();
        setOnlineStatus(Constants.FIREBASE_DB_ONLINE_STATUS_IS_ONLINE);
  */
    }

    @Override
    protected void onPause() {
        super.onPause();
/*
        setOnlineStatus(Constants.FIREBASE_DB_ONLINE_STATUS_IS_OFFLINE);
*/

        FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_DB_ONLINE_STATUS)
                .child(currentUser.getFirebase_uid())
                .setValue(Constants.FIREBASE_DB_ONLINE_STATUS_IS_OFFLINE);

        FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_DB_ONLINE_STATUS)
                .child(receiverID).removeEventListener(listener);
    }

    private void setupWidgits() {
        mMessageEditText = (EditText) findViewById(R.id.ID_message_edit_text);
        mMessageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE)) {

                    sendMessage();
                    return true;

                }
                return false;
            }
        });
        layout = (LinearLayout) findViewById(R.id.ID_messaging_linear_layout);

        mDoneButton = (ImageView) findViewById(R.id.ID_done_my_message);
        mGameOwner = findViewById(R.id.ID_game_owner_name);
        mScrollView = (ScrollView) findViewById(R.id.ID_messaging_scroll_view);

        AppCompatImageView imageView = findViewById(R.id.ID_mesaging_view_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessagingActivity.this, ProfileActivity.class);
                intent.putExtra(Constants.INTENT_PROFILE_FIRST_NAME, receiverFirstName);
                intent.putExtra(Constants.INTENT_PROFILE_LAST_NAME, receiverLastName);
                intent.putExtra(Constants.INTENT_PROFILE_USER_ID, receiverID);
                intent.putExtra(Constants.INTENT_PROFILE_PHOTO, recieverPhoto);
                startActivity(intent);
            }
        });
    }


    private void setupClicks() {
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMessageEditText.getText().toString().length() > 0) {

                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        // Do what you want when you send a message
        String pushedKey = mRoomChatDatabaseReference.push().getKey();
        String chatText = mMessageEditText.getText().toString();
        long chatMessageTime = SystemClock.currentThreadTimeMillis();


        ChatMessageVer2 chatMessage = new ChatMessageVer2();
        chatMessage.setChatText(chatText);
        chatMessage.setChatSenderUid(currentUser.getFirebase_uid());
//        chatMessage.setChatSender(currentUser.getName());
        chatMessage.setTime(chatMessageTime);


        if (!mMessageEditText.getText().toString().trim().isEmpty()) {
            mRoomChatDatabaseReference.child(pushedKey).setValue(chatMessage);
            mMessageEditText.setText("");
        }

        if (!mGameOwnerStatus.getText().equals(getString(R.string.online))) {
            confirmNotification(receiverID, currentUser.getName(), chatMessage.getChatText());

        } else {
            // User is online, so why do you notify him?
        }

    }

    private void setupDatabase() {
        mRoomChatDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_DB_MESSAGES)
                .child(roomId);


        mRoomChatDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatMessageVer2 chatMessage = dataSnapshot.getValue(ChatMessageVer2.class);
                addNewMessageWithPhoto(chatMessage);
                mScrollView.post(new Runnable() {

                    @Override
                    public void run() {
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addMessageBox(ChatMessageVer2 chatMessage) {
        TextView textView = new TextView(MessagingActivity.this);
        textView.setText(chatMessage.getChatText());
        textView.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // It's the other one who's chatting
        if (!chatMessage.getChatSenderUid().equals(currentUser.getFirebase_uid())) {
            lp.setMargins(15, 5, 15, 10);
            lp.gravity = Gravity.START;
            textView.setLayoutParams(lp);


            textView.setBackgroundResource(R.drawable.left_messaging_shape);
        } else {
            // It's me who's chatting
            lp.setMargins(15, 5, 15, 10);
            lp.gravity = Gravity.END;
            textView.setLayoutParams(lp);
            textView.setBackgroundResource(R.drawable.right_messaging_shape);
        }

        layout.addView(textView);
//        mMessagingScrollView.fullScroll(View.FOCUS_DOWN);
    }


    private void addNewMessageWithPhoto(ChatMessageVer2 chatMessageVer2)
    {


        if (!chatMessageVer2.getChatSenderUid().equals(currentUser.getFirebase_uid())) {

            try {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout rel = (RelativeLayout) inflater.inflate(R.layout.item_message, null);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 50, 0, 0);
                TextView txt = (TextView) rel.findViewById(R.id.message_text);
                txt.setText(chatMessageVer2.getChatText());

                CircleImageView img = (CircleImageView) rel.findViewById(R.id.img);
                Picasso.get().load(recieverPhoto).into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        img.setImageResource(R.drawable.trevor);
                    }
                });

                layout.addView(rel);

            }catch (NullPointerException e)
            {

            }
        }else{
            // It's me who's chatting
            TextView textView = new TextView(MessagingActivity.this);
            textView.setText(chatMessageVer2.getChatText());
            textView.setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 5, 15, 10);
            lp.gravity = Gravity.END;
            textView.setLayoutParams(lp);
            textView.setBackgroundResource(R.drawable.right_messaging_shape);
            layout.addView(textView);

        }

    }


    ValueEventListener listener;

    private void getUserStatus(String userID) {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    switch (dataSnapshot.getValue(Integer.class)) {
                        case 0:
                            mGameOwnerStatus.setText(getString(R.string.offline));
                            mGameOwnerStatus.setTextColor(getResources().getColor(R.color.cherry_red));
                            mGameOwnerStatus.setVisibility(View.GONE);
                            break;
                        case 1:
                            mGameOwnerStatus.setText(getString(R.string.online));
                            mGameOwnerStatus.setTextColor(getResources().getColor(R.color.white));
                            mGameOwnerStatus.setVisibility(View.VISIBLE);
                            break;
                        default:
                            mGameOwnerStatus.setText(getString(R.string.unavailable));
                            mGameOwnerStatus.setTextColor(getResources().getColor(R.color.cherry_red));
                            mGameOwnerStatus.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    mGameOwnerStatus.setText(getString(R.string.unavailable));
                    mGameOwnerStatus.setTextColor(getResources().getColor(R.color.cherry_red));
                    mGameOwnerStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_DB_ONLINE_STATUS)
                .child(userID).addValueEventListener(listener);
    }


}



