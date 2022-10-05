package com.gamesexchange.gamesexchange.activities.refers;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.gamesexchange.gamesexchange.dialogs.RewardsDialog;
import com.gamesexchange.gamesexchange.views.CustomButtonView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;

public class ReferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);

        Toolbar toolbar = findViewById(R.id.ID_referral_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
//        getSupportActionBar().setTitle(getString(R.string.refer_a_friend));

        ImageView choosingReferLayoutDone = findViewById(R.id.ID_choosing_refer_done);
        RelativeLayout choosingReferLayout = findViewById(R.id.ID_choosing_refer_layout);
        EditText choosingReferLayoutEditText = findViewById(R.id.ID_choosing_refer_edit_text);


        choosingReferLayoutEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    AppUtils.hideSoftKeyboard(ReferActivity.this);

                    if (choosingReferLayoutEditText.getText().toString().equals(String.valueOf(currentUser.getId()))) {
                        choosingReferLayoutEditText.setError(getString(R.string.cannot_be_same_as_yours));
                    } else {
                        choosingReferLayoutDone.setEnabled(false);
                        choosingReferLayoutEditText.setEnabled(false);
                        choosingWhoReferedYouVolley(choosingReferLayoutDone
                                , choosingReferLayout
                                , choosingReferLayoutEditText.getText().toString());
                    }
                }
                return true;


            }
        });


/*

      .setText(Html.fromHtml(text + "<font color=white>" + CepVizyon.getPhoneCode() + "</font><br><br>"
                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
*/


        TextView referCode = findViewById(R.id.ID_refer_code_text);
//        referCode.setText( getString(R.string.every_time_a_friend_use_your_code) + "(" +String.valueOf(currentUser.getId()) + "), " + getString(R.string.youwill_receive));
//        referCode.setText( getString(R.string.every_time_a_friend_use_your_code) + "(" + Html.fromHtml(  "<font color=\"#FF8C00\">" + String.valueOf(currentUser.getId()) + "</font><br><br>") + "), " + getString(R.string.youwill_receive));
        referCode.setText(Html.fromHtml(getString(R.string.every_time_a_friend_use_your_code) + "(" + "<font color=\"#00255e\">" + String.valueOf(currentUser.getId()) + "</font>"
                + "), "    + getString(R.string.youwill_receive)));

        getReferCounts(String.valueOf(currentUser.getId()));



        Button chooseYourRefer = findViewById(R.id.ID_refer_choose_your_referral);
        if (!currentUser.getRefer().toLowerCase().equals("null")) {
            chooseYourRefer.setVisibility(View.GONE);
        } else {
            chooseYourRefer.setVisibility(View.VISIBLE);
        }
        chooseYourRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosingReferLayout.setVisibility(View.VISIBLE);
                chooseYourRefer.setVisibility(View.GONE);
            }
        });


        CustomButtonView share = findViewById(R.id.ID_refer_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareText(String.valueOf(currentUser.getId()));
            }
        });

        AppCompatImageView compatImageView = findViewById(R.id.ID_copy_image);
        compatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("L3by Refer Code", "" + currentUser.getId());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.ID_root), getString(R.string.copied)
                            , Snackbar.LENGTH_LONG);
                    snackbar.setAction(getString(R.string.close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

                    snackbar.show();
                }
            }
        });

        choosingReferLayoutDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideSoftKeyboard(ReferActivity.this);

                if (choosingReferLayoutEditText.getText().toString().equals(String.valueOf(currentUser.getId()))) {
                    choosingReferLayoutEditText.setError(getString(R.string.cannot_be_same_as_yours));
                } else {
                    choosingReferLayoutDone.setEnabled(false);
                    choosingReferLayoutEditText.setEnabled(false);
                    choosingWhoReferedYouVolley(choosingReferLayoutDone
                                ,choosingReferLayout
                                , choosingReferLayoutEditText.getText().toString());
                }
            }
        });


    }



    private void shareText(String text) {

        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, text + " " + getString(R.string.is_my_refer_code)
        + "\n" + getString(R.string.download_the_app) + "\n " + Constants.APP_URL);
        startActivity(Intent.createChooser(intent2, getString(R.string.share_via)));
    }



    private void createDialog(final String message) {
        final RewardsDialog rewardsDialog = new RewardsDialog();
            rewardsDialog.showDialog(this, message);
            rewardsDialog.dialogGreatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rewardsDialog.dialog.dismiss();
                }
            });
        }



    private void choosingWhoReferedYouVolley(ImageView imageView, RelativeLayout refLayout, String referral)
    {
        imageView.setEnabled(false);
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_update_refer.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String referralUserFirebaseId) {
                imageView.setEnabled(true);
                refLayout.setVisibility(View.GONE);
                addPoints(referralUserFirebaseId,30);
                createDialog("30 GP & 30 WP " + getString(R.string.has_been_sent_to_your_referrer));
                currentUser.setRefer(referralUserFirebaseId);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.something_is_wrong), Toast.LENGTH_SHORT).show();
                imageView.setEnabled(true);

            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("referral_id",referral);
                params.put("firebase_uid",currentUser.getFirebase_uid());
                return params;
            }


        };
        requstQueue.add(stringRequest);
    }


    private void getReferCounts(String refId) {
        TextView referCount = findViewById(R.id.ID_refers_count);



        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_get_my_refer_code_count.php"  + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null)
                {
                                referCount.setText(response + " ");
                }else {
                    referCount.setText("0 ");
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.something_is_wrong), Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("my_ref_code", refId);
                return params;
            }


        };
        requstQueue.add(stringRequest);
    }

    private void addPoints(String uid, int giftCount)
    {
        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try
                    {int value = dataSnapshot.getValue(Integer.class);
                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                            .child(uid).setValue(value + giftCount);
                }catch (DatabaseException databaseException)
                    {
                        Toast.makeText(ReferActivity.this, getString(R.string.user_doesnt_exists), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                  try{  int value = dataSnapshot.getValue(Integer.class);

                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                            .child(uid).setValue(value + giftCount);
                }catch (DatabaseException databaseException)
                {
                    Toast.makeText(ReferActivity.this, getString(R.string.user_doesnt_exists), Toast.LENGTH_LONG).show();
                }
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
