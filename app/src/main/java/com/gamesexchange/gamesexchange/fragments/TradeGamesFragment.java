package com.gamesexchange.gamesexchange.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.model.TradeModel;
import com.gamesexchange.gamesexchange.adapter.TradeModelAdapter;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.districtSearched;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.searchAutoComplete;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.searchView;


public class TradeGamesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;

    private RecyclerView mRecyclerView2;
    private TradeModelAdapter mCommonUserExchangeAdapter;

    private ArrayList<TradeModel> mCommonUserList = new ArrayList<>();
    private RelativeLayout mCannotTradeRL;
    private RelativeLayout mCannotFindMutualTradeRL;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isLoading = false;
    private ProgressBar pb;
    private Button refreshBtn;
    private int refreshCount = 1;
    private boolean IsNoMoreItems = false;
    //    private int refreshCount = 0;

    public TradeGamesFragment() {
        // Required empty public constructor
    }

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trade_games, container, false);
        mContext = getActivity().getApplicationContext();

         pb = v.findViewById(R.id.ID_refresh_trades_pb);
         refreshBtn = v.findViewById(R.id.ID_refresh_trades_btn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshBtn.setEnabled(false);
                pb.setVisibility(View.VISIBLE);
                setVolley(0, Constants.TRADE_LIMIT);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        mCannotTradeRL = v.findViewById(R.id.ID_cannot_trade);
        mCannotFindMutualTradeRL = v.findViewById(R.id.ID_cannot_find_mutual_trade);
        mRecyclerView2 = v.findViewById(R.id.ID_recyclerView);
        mRecyclerView2.setLayoutManager(mLayoutManager);
        mRecyclerView2.setHasFixedSize(true);


        mRecyclerView2.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),
                DividerItemDecoration.VERTICAL));

//        setVolley();

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
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

                    if (!isLoading) {
                        mSwipeRefreshLayout.setRefreshing(true);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Fetching data from server
                                setVolley(0,Constants.TRADE_LIMIT);
                            }
                        }, Constants.TRADE_REFRESH_TIME);
                    }
                }
            });




        mRecyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            if (!IsNoMoreItems)
                            {
                            loading = false;
//                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            refreshCount++;
                            addNewItems(refreshCount * Constants.TRADE_LIMIT, ((refreshCount * Constants.TRADE_LIMIT) + Constants.TRADE_LIMIT));
                        }
                        }
                    }
                }
            }
        });


        setupSearchItems();
        return v;
    }

    private void setupSearchItems()
    {
        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                districtSearched = AppUtils.LocationUtils.getDistrictIndex(queryString);
                setVolley(0,2500);
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(mContext, getString(R.string.please_select_a_district), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




    }

    private void addNewItems(int startingLimit, int endingLimit)
    {
//        Toast.makeText(mContext, "Added a new item", Toast.LENGTH_SHORT).show();
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);

        final String url;
        if (searchView.isIconified()) {
             url =
                    Constants.SERVER_BASE + "rel_get_trades.php?search="
                            + MVPHomePageActivity.currentUser.getFirebase_uid()
                            + "&location=" + currentUser.getLocation()
                            + "&platform=" + currentUser.getPlatform()
                            + "&district=" + "null"
                            + "&starting_limit=" + startingLimit
                            + "&ending_limit=" + endingLimit
                            + Constants.SERVER_AND_GET_API_KEY;
        }else{
         url =
                    Constants.SERVER_BASE + "rel_get_trades.php?search="
                            + MVPHomePageActivity.currentUser.getFirebase_uid()
                            + "&location=" + currentUser.getLocation()
                            + "&platform=" + currentUser.getPlatform()
                            + "&district=" + districtSearched
                            + "&starting_limit=" + startingLimit
                            + "&ending_limit=" + endingLimit
                            + Constants.SERVER_AND_GET_API_KEY;
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray responseJSONArray) {


                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            for (int i = 0; i < responseJSONArray.length(); i++) {

                                JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                                String gameName = jsonObject.getString(Constants.SQL_DATABASE.DB_GAME_NAME);
                                String myUid = jsonObject.getString(Constants.SQL_DATABASE.DB_MY_USER_ID);
                                String commonUserId = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_ID);
                                String gameOwnerFirstName = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_FIRST_NAME);
                                String gameOwnerLastName = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_LAST_NAME);
                                String gameOwnerLocation = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_LOCATION);
                                String gameOwnerDistrict = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMOM_USER_DISTRICT);
                                String gameOwnerProfileImage = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMOM_USER_PROFILE_IMAGE);
                                String gameOwnerToken = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMOM_USER_TOKEN);
                                String gameOwnerGames = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_GAMES);
                                String gameOwnerWishes = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_WISHES);
                                int gameOwnerOnlineStatus = 2; // OFFLINE
                                try {
                                    gameOwnerOnlineStatus = Integer.parseInt(jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_ONLINE_STATUS));
                                } catch (NumberFormatException e) {
                                }
                                TradeModel tradeModel = new TradeModel();
                                tradeModel.setGamesIWant(gameName);
                                tradeModel.setIdOfMyUser(myUid);
                                tradeModel.setIdOfCommonUser(commonUserId);
                                tradeModel.setLocationOfUser(gameOwnerLocation);
                                tradeModel.setOnlineStatus(gameOwnerOnlineStatus);
                                tradeModel.setFirstNameOfUser(gameOwnerFirstName);
                                tradeModel.setLastNameOfUser(gameOwnerLastName);
                                tradeModel.setDistrict(gameOwnerDistrict);
                                tradeModel.setToken(gameOwnerToken);
                                tradeModel.setProfileImage(gameOwnerProfileImage);
                                tradeModel.setGamesOfCommonUser(gameOwnerGames);
                                tradeModel.setWishesOfCommonUser(gameOwnerWishes);
                                mCommonUserList.add(tradeModel);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            refreshBtn.setEnabled(true);
                            IsNoMoreItems = true;
                        }
                        return null;
                    }


                    @Override
                    protected void onPostExecute(Void aVoid) {

                        // check if this is the last mutual common user

                       /* mCommonUserExchangeAdapter = new TradeModelAdapter(mContext, mCommonUserList);
                        mRecyclerView2.setAdapter(mCommonUserExchangeAdapter);*/
                       if (mCommonUserExchangeAdapter != null) {
                           mCommonUserExchangeAdapter.notifyDataSetChanged();
                           if (mCommonUserList.size() == 0) {
                               mCannotFindMutualTradeRL.setVisibility(View.VISIBLE);
                           } else {
                               mRecyclerView2.setVisibility(View.VISIBLE);
                               mCannotFindMutualTradeRL.setVisibility(View.GONE);
                           }
//                                    addDummyData();
                           isLoading = false;
                           pb.setVisibility(View.GONE);
                           refreshBtn.setEnabled(true);
                       } super.onPostExecute(aVoid);
                    }


                }.execute();


                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mCommonUserList.size() == 0) {
                    mCannotFindMutualTradeRL.setVisibility(View.VISIBLE);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                refreshBtn.setEnabled(true);
                pb.setVisibility(View.GONE);

            }
        });

        requstQueue.add(jsonArrayRequest);
    }

   /* @Override
    public void onStart() {
        super.onStart();
        if (mCommonUserList.size() ==0 && !isLoading)
        {
            setVolley();
        }
    }*/

   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
//                Log.d("MyFragment", "Not visible anymore.  Stopping audio.");
            }else{
//                Log.d("MyFragment", "Visible.");

                if (mSwipeRefreshLayout != null){
                    mSwipeRefreshLayout.setRefreshing(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setVolley();
                        }
                    },Constants.TRADE_REFRESH_TIME);
                }
            }
        }
    }*/

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setVolley(0, Constants.TRADE_LIMIT);
            }
        },Constants.TRADE_REFRESH_TIME);

    }



    public void setVolley(int startingLimit, int endingLimit) {
//        Toast.makeText(mContext, "SETVOLLEY", Toast.LENGTH_SHORT).show();
        if (!isLoading) {
            isLoading = true;
            mSwipeRefreshLayout.setRefreshing(false);
            mCommonUserList.clear();
            if (mCommonUserExchangeAdapter != null) {
                mCommonUserExchangeAdapter.notifyDataSetChanged();
            }

            if (currentUser.isTradeable()) {
                mRecyclerView2.setVisibility(View.VISIBLE);
                mCannotTradeRL.setVisibility(View.GONE);


                RequestQueue requstQueue = Volley.newRequestQueue(mContext);
                final String url;
                if (searchView.isIconified()) {

                     url =
                            Constants.SERVER_BASE + "rel_get_trades.php?search="
                                    + MVPHomePageActivity.currentUser.getFirebase_uid()
                                    + "&location=" + currentUser.getLocation()
                                    + "&platform=" + currentUser.getPlatform()
                                    + "&district=" + "null"
                                    + "&starting_limit=" + startingLimit
                                    + "&ending_limit=" + endingLimit
                                    + Constants.SERVER_AND_GET_API_KEY;
                }else{
                     url =
                            Constants.SERVER_BASE + "rel_get_trades.php?search="
                                    + MVPHomePageActivity.currentUser.getFirebase_uid()
                                    + "&location=" + currentUser.getLocation()
                                    + "&district=" + districtSearched
                                    + "&platform=" + currentUser.getPlatform()
                                    + "&starting_limit=" + startingLimit
                                    + "&ending_limit=" + endingLimit
                                    + Constants.SERVER_AND_GET_API_KEY;
                }
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseJSONArray) {
                    IsNoMoreItems = false;

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... voids) {
                                try {
                                    for (int i = 0; i < responseJSONArray.length(); i++) {

                                        JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                                        String gameName = jsonObject.getString(Constants.SQL_DATABASE.DB_GAME_NAME);
                                        String myUid = jsonObject.getString(Constants.SQL_DATABASE.DB_MY_USER_ID);
                                        String commonUserId = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_ID);
                                        String gameOwnerFirstName = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_FIRST_NAME);
                                        String gameOwnerLastName = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_LAST_NAME);
                                        String gameOwnerLocation = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_LOCATION);
                                        String gameOwnerDistrict = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMOM_USER_DISTRICT);
                                        String gameOwnerProfileImage = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMOM_USER_PROFILE_IMAGE);
                                        String gameOwnerGames = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_GAMES);
                                        String gameOwnerWishes = jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_WISHES);
                                        int gameOwnerOnlineStatus = 2; // OFFLINE
                                        try {
                                            gameOwnerOnlineStatus = Integer.parseInt(jsonObject.getString(Constants.SQL_DATABASE.DB_COMMON_USER_ONLINE_STATUS));
                                        } catch (NumberFormatException e) {
                                        }
                                        TradeModel tradeModel = new TradeModel();
                                        tradeModel.setGamesIWant(gameName);
                                        tradeModel.setIdOfMyUser(myUid);
                                        tradeModel.setIdOfCommonUser(commonUserId);
                                        tradeModel.setLocationOfUser(gameOwnerLocation);
                                        tradeModel.setOnlineStatus(gameOwnerOnlineStatus);
                                        tradeModel.setFirstNameOfUser(gameOwnerFirstName);
                                        tradeModel.setLastNameOfUser(gameOwnerLastName);
                                        tradeModel.setDistrict(gameOwnerDistrict);
                                        tradeModel.setProfileImage(gameOwnerProfileImage);
                                        tradeModel.setGamesOfCommonUser(gameOwnerGames);
                                        tradeModel.setWishesOfCommonUser(gameOwnerWishes);
                                        mCommonUserList.add(tradeModel);


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    refreshBtn.setEnabled(true);
                                }
                                return null;
                            }


                            @Override
                            protected void onPostExecute(Void aVoid) {

                                // check if this is the last mutual common user

                                mCommonUserExchangeAdapter = new TradeModelAdapter(mContext, mCommonUserList);
                                mRecyclerView2.setAdapter(mCommonUserExchangeAdapter);
                                if (mCommonUserList.size() == 0) {
                                    mCannotFindMutualTradeRL.setVisibility(View.VISIBLE);
                                } else {
                                    mRecyclerView2.setVisibility(View.VISIBLE);
                                    mCannotFindMutualTradeRL.setVisibility(View.GONE);
                                }
//                                    addDummyData();
                                isLoading = false;
                                pb.setVisibility(View.GONE);
                                refreshBtn.setEnabled(true);
                                super.onPostExecute(aVoid);
                            }


                        }.execute();


                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mCommonUserList.size() == 0) {
                            mCannotFindMutualTradeRL.setVisibility(View.VISIBLE);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                        refreshBtn.setEnabled(true);
                        pb.setVisibility(View.GONE);

                    }
                });

                requstQueue.add(jsonArrayRequest);


            } else {
                mRecyclerView2.setVisibility(View.GONE);
                mCannotTradeRL.setVisibility(View.VISIBLE);
                isLoading = false;
            }
        }
    }






    /**
     * Check whether the last item in RecyclerView is being displayed or not
     *
     * @param recyclerView which you would like to check
     * @return true if last position was Visible and false Otherwise
     */
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }




}
