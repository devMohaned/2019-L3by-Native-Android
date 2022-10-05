package com.gamesexchange.gamesexchange.services;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.activities.register.RegisterFailedActivity;
import com.gamesexchange.gamesexchange.activities.user_states.UnderMaintenanceActivity;
import com.gamesexchange.gamesexchange.activities.user_states.UpdateActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.mAuth;
public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        if (mAuth != null) updateToken(token);

//            FirebaseDatabase.getInstance().getReference().child("tokens").child(mAuth.getUid()).setValue(/*FirebaseInstanceId.getInstance().getToken()*/token);
    }


    private void updateToken(String token)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_update_token.php?firebase_uid=" + mAuth.getUid()
                        + Constants.SERVER_AND_GET_API_KEY
                        + "&token=" + token;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtils.checkVolleyError(getApplicationContext(),error);
            }
        });


        requestQueue.add(jsonArrayRequest);
    }
}

