package com.gamesexchange.gamesexchange.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.gamesexchange.gamesexchange.Utils.DistributedRandomNumberGenerator;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.dialogs.RewardsDialog;
import com.gamesexchange.gamesexchange.services.DailyLoginBroadCaster;
import com.gamesexchange.gamesexchange.views.LuckyWheelView;
import com.gamesexchange.gamesexchange.model.LuckyItem;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.gamesexchange.gamesexchange.Utils.Constants.DEFAULT_DIFFERENCE_LAST_CLICKED;
import static com.gamesexchange.gamesexchange.Utils.Constants.DEFAULT_LAST_CLICKED;
import static com.gamesexchange.gamesexchange.Utils.Constants.SHARED_PREF_DAILY_LOGIN;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;


public class RewardsFragment extends Fragment implements RewardedVideoAdListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Button spinBtn;
    private Button dailyLoginBtn;
    private TextView dailyLoginCount;
    private Button getMorePointsBtn;
    private TextView mGamesTextView, mWishesTextView;
    private int wishesRewardsCount = 0;
    private int gamesRewardsCount = 0;
    private int placeHolderWishesRewardsCount = 0;
    private int placeHolderGamesRewardsCount = 0;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private boolean isInAd = false;

    public RewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rewards, container, false);
        loadInterstitialAd();
        addRewardedVideoAd();
        setupViews(v);
        createMyWheel(v);
        getAccumulatedDays();

        return v;


    }

    private int rewardedCount = 0;

    private void setupViews(View v) {
        mGamesTextView = v.findViewById(R.id.ID_rewards_games_count);
        mWishesTextView = v.findViewById(R.id.ID_rewards_wishes_count);


        getMorePointsBtn = v.findViewById(R.id.ID_get_more_points);
        getMorePointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTestDevice()) {
                    if (mRewardedVideoAd.isLoaded()) {

                        long lastAdClickedInMillis = getLastAdClickedSharedPref();
                        if (lastAdClickedInMillis != DEFAULT_LAST_CLICKED) {


                            long lastCollectedIsThirtySecsOrNot = (((System.currentTimeMillis() - lastAdClickedInMillis) / 1000));
                            if (lastCollectedIsThirtySecsOrNot >= DEFAULT_DIFFERENCE_LAST_CLICKED) {
                                rewardedCount = getMorePointsRandomProbablitiy();
                                createCustomDialog(getString(R.string.you_have_won) + " " + rewardedCount + " GP & " + rewardedCount + " WP");

                                getMorePointsBtn.setEnabled(false);
                                spinBtn.setEnabled(false);
                            } else {
                                if (isAdded() && getActivity() != null)
                                    Toast.makeText(getContext(), getString(R.string.try_again_in) + " " + (DEFAULT_DIFFERENCE_LAST_CLICKED - lastCollectedIsThirtySecsOrNot) + " " + getString(R.string.sec), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            rewardedCount = getMorePointsRandomProbablitiy();
                            createCustomDialog(getString(R.string.you_have_won) + " " + rewardedCount + " GP & " + rewardedCount + " WP");

                            getMorePointsBtn.setEnabled(false);
                            spinBtn.setEnabled(false);
                        }




                    } else {
                        if (isAdded() && getActivity() != null) {
                            Toast.makeText(getContext(), getString(R.string.cannot_find_ad_please_wait), Toast.LENGTH_SHORT).show();
                            loadRewardedVideoAd();
                        }
                    }
                }
            }
        });

        dailyLoginBtn = v.findViewById(R.id.ID_daily_login_btn);
        dailyLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTestDevice()) {
                    if (mInterstitialAd.isLoaded()) {
                        addAccumulatedDays();
                    } else {
                        if (isAdded() && getActivity() != null) {
                            Toast.makeText(getContext(), getString(R.string.cannot_find_ad_please_wait), Toast.LENGTH_SHORT).show();
                            loadInterstitialAd();
                        }
                    }
                }
            }
        });

        dailyLoginCount = v.findViewById(R.id.ID_daily_login_count);


    }


    private int getProbablitiyIntegar() {
        DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
        drng.addNumber(1, 0.32);
        drng.addNumber(2, 0.15);
        drng.addNumber(3, 0.025);
        drng.addNumber(4, 0.005);
        drng.addNumber(5, 0.32);
        drng.addNumber(6, 0.15);
        drng.addNumber(7, 0.025);
        drng.addNumber(8, 0.005);

        return drng.getDistributedRandomNumber();
    }


    private int getMorePointsRandomProbablitiy() {
        DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
        drng.addNumber(1, 0.25); // 25%
        drng.addNumber(2, 0.68); // 68% TOTAL = 93%
        drng.addNumber(3, 0.025);// 2.5%
        drng.addNumber(4, 0.025);// 2.5% TOTAL = 98%
        drng.addNumber(5, 0.0191); // 1.91% TOTAL = 99.91%
        // BELOW  = 0.0009
        drng.addNumber(10, 0.0001);
        drng.addNumber(15, 0.0001);
        drng.addNumber(20, 0.0001);
        drng.addNumber(25, 0.0001);
        drng.addNumber(30, 0.0001);
        drng.addNumber(35, 0.0001);
        drng.addNumber(40, 0.0001);
        drng.addNumber(45, 0.0001);
        drng.addNumber(50, 0.0001);


        return drng.getDistributedRandomNumber();
    }


    private void createMyWheel(View v) {


        spinBtn = v.findViewById(R.id.spinBtn);
        final LuckyWheelView luckyWheelView = v.findViewById(R.id.luckyWheel);
        List<LuckyItem> data = new ArrayList<>();
        data.add(new LuckyItem(2 + " W-P", R.drawable.ic_heart, 0xffe5dac9));
        data.add(new LuckyItem(4 + " W-P", R.drawable.ic_heart, 0xffe5dac9));
        data.add(new LuckyItem(6 + " W-P", R.drawable.ic_heart, 0xffe5dac9));
        data.add(new LuckyItem(8 + " W-P", R.drawable.ic_heart, 0xffe5dac9));
        data.add(new LuckyItem(2 + " G-P", R.drawable.ic_game_controller, 0xffe5dac9));
        data.add(new LuckyItem(4 + " G-P", R.drawable.ic_game_controller, 0xffe5dac9));
        data.add(new LuckyItem(6 + " G-P", R.drawable.ic_game_controller, 0xffe5dac9));
        data.add(new LuckyItem(8 + " G-P", R.drawable.ic_game_controller, 0xffe5dac9));

        luckyWheelView.setData(data);
        luckyWheelView.setRound(8);


        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTestDevice()) {


                    if (mInterstitialAd.isLoaded()) {

                        long lastAdClickedInMillis = getLastAdClickedSharedPref();
                        if (lastAdClickedInMillis != DEFAULT_LAST_CLICKED) {


                            long lastCollectedIsThirtySecsOrNot = (((System.currentTimeMillis() -lastAdClickedInMillis) / 1000));
                            if (lastCollectedIsThirtySecsOrNot >= DEFAULT_DIFFERENCE_LAST_CLICKED) {
                                // start
                                luckyWheelView.startLuckyWheelWithTargetIndex(/*RANDOM.nextInt(8)*/getProbablitiyIntegar());
                                spinBtn.setEnabled(false);
                                getMorePointsBtn.setEnabled(false);
                            } else {
                                if (isAdded() && getActivity() != null)
                                    Toast.makeText(getContext(), getString(R.string.try_again_in) + " " +(DEFAULT_DIFFERENCE_LAST_CLICKED - lastCollectedIsThirtySecsOrNot)+ " " + getString(R.string.sec), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // start
                            luckyWheelView.startLuckyWheelWithTargetIndex(/*RANDOM.nextInt(8)*/getProbablitiyIntegar());
                            spinBtn.setEnabled(false);
                            getMorePointsBtn.setEnabled(false);
                        }
                    } else {
                        if (isAdded() && getActivity() != null) {
                            Toast.makeText(getContext(), getString(R.string.cannot_find_ad_please_wait), Toast.LENGTH_SHORT).show();
                            loadInterstitialAd();
                        }
                    }
                }
            }

        });

// listener after finish lucky wheel
        luckyWheelView.setLuckyRoundItemSelectedListener(
                new LuckyWheelView.LuckyRoundItemSelectedListener() {
                    @Override
                    public void LuckyRoundItemSelected(int index) {
                        // do something with index
                        switch (index) {
                            case 1:
                                createDialog("2 WP");
                                placeHolderWishesRewardsCount = 2;
//                                wishesRewardsCount += 2;
                                break;
                            case 2:
                                createDialog("4 WP");
                                placeHolderWishesRewardsCount = 4;
//                                wishesRewardsCount += 4;
                                break;
                            case 3:
                                createDialog("6 WP");
                                placeHolderWishesRewardsCount = 6;
//                                wishesRewardsCount += 6;
                                break;
                            case 4:
                                createDialog("8 WP");
                                placeHolderWishesRewardsCount = 8;
//                                wishesRewardsCount += 8;
                                break;
                            case 5:
                                createDialog("2 GP");
                                placeHolderGamesRewardsCount = 2;
                                //                                gamesRewardsCount += 2;
                                break;
                            case 6:
                                createDialog("4 GP");
                                placeHolderGamesRewardsCount = 4;
                                //                               gamesRewardsCount += 4;
                                break;
                            case 7:
                                createDialog("6 GP");
                                placeHolderGamesRewardsCount = 6;
//                                gamesRewardsCount += 6;
                                break;
                            case 8:
                                createDialog("8 GP");
                                placeHolderGamesRewardsCount = 8;
//                                gamesRewardsCount += 8;
                                break;
                            default:
                                if (isAdded() && getActivity() != null) {
                                    Toast.makeText(getContext(), getString(R.string.error_has_occured), Toast.LENGTH_LONG).show();
                                }                                 //       Log.d("Error On Lucky Wheel : ", index + " GP");
                        }
                        getMorePointsBtn.setEnabled(true);
//                        setPointsText(gamesRewardsCount, wishesRewardsCount);
                    }
                }
        );


    }


    private void createDialog(final String message) {


        if (isAdded() && getActivity() != null) {
            final RewardsDialog rewardsDialog = new RewardsDialog();
            rewardsDialog.showDialog(getActivity(), getString(R.string.you_have_won) + " " + message);
            rewardsDialog.dialogGreatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mInterstitialAd.isLoaded()) {
                        isInAd = true;
                        mInterstitialAd.show();
                    } else {
                        //      Log.d("TAG", "The interstitial wasn't loaded yet.");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mInterstitialAd.isLoaded()) {
                                    spinBtn.setEnabled(true);
                                }
                            }
                        }, 3000);
                    }

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            if (isAdded() && getActivity() != null) {
                                Toast.makeText(getContext(), getString(R.string.no_ads_available), Toast.LENGTH_SHORT).show();
//                            spinBtn.setEnabled(true);
                            }
                        }

                        @Override
                        public void onAdOpened() {
                            super.onAdOpened();
//                            spinBtn.setEnabled(true);
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            spinBtn.setEnabled(true);
                        }

                        @Override
                        public void onAdClosed() {
                            // Load the next interstitial.
                            gamesRewardsCount += placeHolderGamesRewardsCount;
                            wishesRewardsCount += placeHolderWishesRewardsCount;
                            placeHolderGamesRewardsCount = 0;
                            placeHolderWishesRewardsCount = 0;
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            spinBtn.setEnabled(true);
                            isInAd = false;
                            setPointsText(gamesRewardsCount, wishesRewardsCount);
                        }

                        @Override
                        public void onAdLeftApplication() {
                            addLastClickedSharedPref(System.currentTimeMillis());
                            super.onAdLeftApplication();
                        }
                    });

                    rewardsDialog.dialog.dismiss();
                }
            });
        }
    }


    private void createCustomDialog(String message) {
        if (isAdded() && getActivity() != null) {
            final RewardsDialog rewardsDialog = new RewardsDialog();
            rewardsDialog.showDialog(getActivity(), getString(R.string.you_have_won) + " " + message);
            rewardsDialog.dialogGreatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mRewardedVideoAd.isLoaded()) {
                        isInAd = true;
                        mRewardedVideoAd.show();
                    } else {
                        loadRewardedVideoAd();
                    }
                    rewardsDialog.dialog.dismiss();
                }
            });
        }
    }


    private void loadInterstitialAd() {
        String adUnitId = /*"ca-app-pub-3940256099942544/1033173712"*/"ca-app-pub-7003413723788424/2073249361";
        if (isAdded() && getActivity() != null) {
            if (mInterstitialAd != null) {
                if (!mInterstitialAd.isLoading()) {
                    mInterstitialAd = new InterstitialAd(getContext());
                    mInterstitialAd.setAdUnitId(adUnitId);
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            } else {

                mInterstitialAd = new InterstitialAd(getContext());
                mInterstitialAd.setAdUnitId(adUnitId);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }
    }


    private void addRewardedVideoAd() {
        // Use an activity context to get the rewarded video instance.
        if (isAdded() && getActivity() != null) {
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
            mRewardedVideoAd.setRewardedVideoAdListener(this);

            loadRewardedVideoAd();
        }
    }


    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(/*"ca-app-pub-3940256099942544/5224354917"*/"ca-app-pub-7003413723788424/7728012670",
                new AdRequest.Builder().build());
    }

    private void sendRewardsToFirebaseDatabase() {

        if (gamesRewardsCount > 0) {
            final int count = gamesRewardsCount;

            Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                    .child(currentUser.getFirebase_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int value = dataSnapshot.getValue(Integer.class);

                        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                                .child(currentUser.getFirebase_uid()).setValue(value + count);
                        MVPHomePageActivity.gamePtsCount = value + count;
                    } else {
                        // User doesn't have any points before.
                        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                                .child(currentUser.getFirebase_uid()).setValue(count);
                        MVPHomePageActivity.gamePtsCount = count;
                    }
                    gamesRewardsCount = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (wishesRewardsCount > 0) {
            final int count = wishesRewardsCount;

            Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                    .child(currentUser.getFirebase_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int value = dataSnapshot.getValue(Integer.class);

                        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                                .child(currentUser.getFirebase_uid()).setValue(value + count);
                        MVPHomePageActivity.wishPtsCount = value + count;
                    } else {
                        // User doesn't have any points before.
                        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                                .child(currentUser.getFirebase_uid()).setValue(count);
                        MVPHomePageActivity.wishPtsCount = count;
                    }
                    wishesRewardsCount = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onResume() {
        if (getActivity() != null & isAdded()) {
            mRewardedVideoAd.resume(getContext());
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (getActivity() != null & isAdded()) {
            mRewardedVideoAd.pause(getContext());
        }

        if (!isInAd) sendRewardsToFirebaseDatabase();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null & isAdded()) {
            mRewardedVideoAd.destroy(getContext());
        }
        super.onDestroy();
    }


    private void getAccumulatedDays() {
        if (isAdded() && getActivity() != null) {
            String response = currentUser.getLast_collected();
            dailyLoginCount.setText(currentUser.getAccumlated_days());

            switch (response) {
                case Constants.FIRST_LOGIN:
                    dailyLoginBtn.setEnabled(true);
                    dailyLoginCount.setText("0");
                    break;
                case Constants.DAILY_LOGIN:
                    dailyLoginBtn.setEnabled(true);
                    break;
                case Constants.ALREADY_COLLECTED:
                    dailyLoginBtn.setEnabled(false);
                    dailyLoginBtn.setText(getString(R.string.collected_already));
                    break;
            }

        }


    }

    private void addDataInformationIntoSharedPref(String collectedAt) {

        if (isAdded() & getActivity() != null) {
            SharedPreferences.Editor editor = getContext().getSharedPreferences(SHARED_PREF_DAILY_LOGIN, MODE_PRIVATE).edit();
            editor.putString("collected_at", collectedAt);
            editor.apply();
        }
    }

    private void startAlert(int afterHowLongInSec) {
        if (isAdded() && getActivity() != null) {
            Intent intent = new Intent(getContext(), DailyLoginBroadCaster.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getContext(), 23478, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (afterHowLongInSec * 1000) /* EXTRA 30 SECS */, pendingIntent);
            }
//            Toast.makeText(getContext(), "Alarm set to after " + "5" + " seconds", Toast.LENGTH_LONG).show();
        }
    }

    private void addAccumulatedDays() {


        if (isAdded() && getActivity() != null) {
            dailyLoginBtn.setEnabled(false);
            int updatedCount = Integer.parseInt(dailyLoginCount.getText().toString()) + 1;
            RequestQueue requstQueue = Volley.newRequestQueue(getContext().getApplicationContext());


            final String url =
                    Constants.SERVER_BASE + "rel_add_accumulated_days.php" + Constants.SERVER_GET_API_KEY;


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    currentUser.setAccumlated_days(String.valueOf(updatedCount));
                    currentUser.setLast_collected(Constants.ALREADY_COLLECTED);
                    addDataInformationIntoSharedPref(String.valueOf(System.currentTimeMillis()));
                    startAlert(86400 + 100/*10*/);
                    dailyLoginCount.setText(String.valueOf(updatedCount));
                    dailyLoginBtn.setText(getString(R.string.congratulation));


                    int rewardedCount = updatedCount * 2;
                    createDialog(getString(R.string.you_have_won) + " " + rewardedCount + " GP & " + rewardedCount + " WP");
                 /*   gamesRewardsCount += rewardedCount;
                    wishesRewardsCount += rewardedCount;*/
                    placeHolderGamesRewardsCount = rewardedCount;
                    placeHolderWishesRewardsCount = rewardedCount;
//                    setPointsText(gamesRewardsCount, wishesRewardsCount);

                    dailyLoginBtn.setText(getString(R.string.collected));

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (isAdded() && getActivity() != null)
                        AppUtils.checkVolleyError(getContext(), error);
//                        Toast.makeText(getContext(), getContext().getString(R.string.couldnot_fetch), Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firebase_uid", currentUser.getFirebase_uid());
                    params.put("days", String.valueOf(updatedCount));
//                    params.put("last_collected", today);

                    return params;
                }

                ;
            };
//        stringRequest.setTag(REQ_TAG);
            requstQueue.add(stringRequest);
        }
    }


    private int rewarded = 0;

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {
 /*       getMorePointsBtn.setEnabled(true);
        spinBtn.setEnabled(true);*/
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if (rewarded == 1) {
            gamesRewardsCount += rewardedCount;
            wishesRewardsCount += rewardedCount;
        } else {
            if (isAdded() && getActivity() != null)
                Toast.makeText(getContext(), getString(R.string.you_have_not_watched_full_video), Toast.LENGTH_LONG).show();
        }
        rewarded = 0;
        setPointsText(gamesRewardsCount, wishesRewardsCount);
        loadRewardedVideoAd();
        getMorePointsBtn.setEnabled(true);
        spinBtn.setEnabled(true);
        isInAd = false;
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        rewarded = 1;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        addLastClickedSharedPref(System.currentTimeMillis());
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }


    private boolean isTestDevice() {
        return Boolean.valueOf(Settings.System.getString(getContext().getContentResolver(), "firebase.test.lab"));
    }


    private void setPointsText(int gamePts, int wishPts) {
        mGamesTextView.setText(gamePts + " GP");
        mWishesTextView.setText(wishPts + " WP");
    }

    private void addLastClickedSharedPref(long lastClickedInMillis) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("last_ad_clicked", MODE_PRIVATE).edit();
        editor.putLong("last_clicked", lastClickedInMillis);
        editor.apply();
    }

    private long getLastAdClickedSharedPref() {
        SharedPreferences prefs = getContext().getSharedPreferences("last_ad_clicked", MODE_PRIVATE);
        if (!prefs.getAll().isEmpty()) {
            return prefs.getLong("last_clicked", DEFAULT_LAST_CLICKED);
        } else {
            return DEFAULT_LAST_CLICKED;
        }
    }
}
