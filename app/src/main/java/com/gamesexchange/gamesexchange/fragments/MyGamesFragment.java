package com.gamesexchange.gamesexchange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.noInternetConnection.NoInternetConnection;
import com.gamesexchange.gamesexchange.adapter.GamesAdapter;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.presenters.FragmentsPresesnterMVP;

import com.gamesexchange.gamesexchange.viewModel.SearchedGamesViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.mGamesPts;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.myGamesSearchEditText;


public class MyGamesFragment extends Fragment implements FragmentsPresesnterMVP.View {



    private GamesAdapter mGamesAdapter;
    private ValueEventListener listener;




    public MyGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_games, container, false);


        setupViews(v);
        filter("");
        return v;
    }


    private void setupViews(View v) {
        RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mGamesAdapter = new GamesAdapter(getActivity().getApplicationContext(), new ArrayList<Game>());

        mRecyclerView.setLayoutManager(mLayoutManager);


        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mGamesAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!AppUtils.isNetworkAvailable(getContext())) {
                    Intent i = new Intent(getContext(), NoInternetConnection.class);
                    startActivity(i);
//                    getActivity().finish();
                }  super.onScrollStateChanged(recyclerView, newState);
            }
        });


        /*                                            MVP                              */

        final FragmentsPresesnterMVP presesnter = new FragmentsPresesnterMVP(this);


        myGamesSearchEditText.addTextChangedListener(new TextWatcher() {
            String searchedGames = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                 searchedGames = s.toString();

                                        // TODO: do what you need here (refresh list)
                                    // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                    presesnter.searchForGame(searchedGames);






            }
        });



        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (isAdded() && getActivity() != null) {
                        Integer gamePtsCount = dataSnapshot.getValue(Integer.class);
                        mGamesPts.setText(String.valueOf(gamePtsCount)/* + " " + getString(R.string.game_points)*/);
                        mGamesPts.setVisibility(View.VISIBLE);
                        if (gamePtsCount < 1) {
                            mGamesPts.setTextColor(getResources().getColor(R.color.cherry_red));
                        } else {
                            mGamesPts.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                .child(currentUser.getFirebase_uid()).addValueEventListener(listener);

    }


    /*
---------------------------------------------------------------------------------


                                      VOLLEY


--------------------------------------------------------------------------------
     */

    @Override
    public void filter(String gameName) {
        if (isAdded() && getActivity() != null) {
            final SearchedGamesViewModel searchedGamesViewModel = ViewModelProviders.of(this)
                    .get(SearchedGamesViewModel.class);
            searchedGamesViewModel.setGameList(gameName);

            searchedGamesViewModel.getGameList().observe(this, new Observer<List<Game>>() {

                private boolean initialized = false;
                private List<Game> lastObj = null;

                @Override
                public void onChanged(@Nullable List<Game> list) {


                    if (!initialized) {
                        initialized = true;
                        lastObj = list;
                        mGamesAdapter.searchGameList(lastObj);
                    } else if ((list == null && lastObj != null)
                            || list != lastObj) {

                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isAdded() && getActivity() != null)
        {
            if (currentUser.getFirebase_uid() != null) Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS).child(currentUser.getFirebase_uid()).removeEventListener(listener);
        }

    }
}
