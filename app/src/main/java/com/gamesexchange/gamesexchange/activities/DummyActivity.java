package com.gamesexchange.gamesexchange.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.adapter.GamesAdapter;
import com.gamesexchange.gamesexchange.adapter.GamesOfTraderAdapter;
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

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser;

public class DummyActivity extends AppCompatActivity {
    ArrayList<Game> wishes;
    ArrayList<Game> games;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_activity);

        RecyclerView gameRecyclerView = findViewById(R.id.ID_trading_game_recyclerview);
        gameRecyclerView.setHasFixedSize(true);
        gameRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        games = new ArrayList<>();
        GamesOfTraderAdapter gamesOfTraderAdapter = new GamesOfTraderAdapter(this, games);
        gameRecyclerView.setAdapter(gamesOfTraderAdapter);


        RecyclerView wishRecyclerView = findViewById(R.id.ID_trading_wish_recyclerview);
        wishRecyclerView.setHasFixedSize(true);
        wishRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         wishes = new ArrayList<>();
        GamesOfTraderAdapter gamesOfTraderAdapter2 = new GamesOfTraderAdapter(this, wishes);
        wishRecyclerView.setAdapter(gamesOfTraderAdapter2);


        addGameToAdapter(gamesOfTraderAdapter, "Assassin's Creed III");
        addGameToAdapter(gamesOfTraderAdapter, "Grand Theft Auto V");
        addGameToAdapter(gamesOfTraderAdapter, "Hitman Blood Money");

        i = 1;
        addGameToAdapter(gamesOfTraderAdapter2, "Far Cry 4");
        addGameToAdapter(gamesOfTraderAdapter2, "Batman Arkham City");



    }

    private void addGameToAdapter(GamesOfTraderAdapter adapter, String gameName)
    {
        Game game = new Game();
        game.setGame_name(gameName);
        if (i == 0)
        {
            games.add(game);
            adapter.notifyDataSetChanged();
        }else {
            wishes.add(game);
            adapter.notifyDataSetChanged();
        }
    }
}
