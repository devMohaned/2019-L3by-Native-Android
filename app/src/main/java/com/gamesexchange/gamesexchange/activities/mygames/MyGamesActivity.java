package com.gamesexchange.gamesexchange.activities.mygames;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.adapter.MyGamesAdapter;
import com.gamesexchange.gamesexchange.model.Game;

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

public class MyGamesActivity extends AppCompatActivity {
    MyGamesAdapter myGamesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);

        Toolbar toolbar = findViewById(R.id.ID_my_games_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setTitle(getString(R.string.my_games));

                // Fetching data from server
        getMyGames();


        myGamesAdapter = new MyGamesAdapter(this, gameList);

        RecyclerView recyclerView = findViewById(R.id.ID_my_games_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myGamesAdapter);
    }

    private List<Game> gameList = new ArrayList<>();


    private void getMyGames() {
        gameList.clear();

        if (myGamesAdapter != null) {
            myGamesAdapter.notifyDataSetChanged();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_games_list.php?search=" + currentUser.getFirebase_uid() + Constants.SERVER_AND_GET_API_KEY;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {


                for (int i = 0; i < responseJSONArray.length(); i++) {
                    try {
                        JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                        String gameName = jsonObject.getString(/*Constants.SQL_DATABASE.GAME_NAME*/"game_name");
                        /*String gameDesc = jsonObject.getString(Constants.SQL_DATABASE.GAME_DESC);
                        String frontImg = jsonObject.getString(Constants.SQL_DATABASE.FRONT_IMAGE);
                        String backImg = jsonObject.getString(Constants.SQL_DATABASE.BACK_IMAGE);
                        String moreImg = jsonObject.getString(Constants.SQL_DATABASE.OTHER_IMG);*/
                        Game game = new Game();
                        game.setGame_name(gameName);
                       /* game.setFrontImg(frontImg);
                        game.setBackImg(backImg);
                        game.setMoreImg(moreImg);
                        game.setGameDescription(gameDesc);*/
                        gameList.add(game);
                        if (i == responseJSONArray.length() - 1) {
                            myGamesAdapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtils.checkVolleyError(MyGamesActivity.this,error);
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
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
