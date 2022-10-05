package com.gamesexchange.gamesexchange.services;

import android.app.IntentService;
import android.content.Intent;

import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.annotation.Nullable;

public class AddingAllGamesService extends IntentService {


    public AddingAllGamesService() {
        super("AddingAllGamesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();

        int countsOfRows = AppDatabase.getAppDatabase(this).gameDAO().getCount();
        if (countsOfRows < Constants.GAMES_COUNT)
        {
            GamesTasks.executeAdditionOfGames(this,action);
        }

    }



}
