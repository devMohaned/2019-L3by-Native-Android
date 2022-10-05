package com.gamesexchange.gamesexchange.viewModel;

import android.app.Application;
import android.util.Log;

import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SearchedGamesViewModel extends AndroidViewModel {
    private LiveData<List<Game>> gameList;

    public SearchedGamesViewModel(@NonNull Application application) {
        super(application);
//        Log.d("GamesViewModel:", "Retreiving Data From Room");


    }

    public LiveData<List<Game>> getGameList() {
        return gameList;

    }

    public void setGameList(String gameName)
    {
        gameList = AppDatabase.getAppDatabase(this.getApplication()).gameDAO().getGamesByName(gameName);
         }
}
