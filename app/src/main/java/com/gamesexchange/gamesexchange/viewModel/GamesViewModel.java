package com.gamesexchange.gamesexchange.viewModel;

import android.app.Application;

import android.util.Log;

import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GamesViewModel extends AndroidViewModel {
    private LiveData<List<Game>> gameList;

    public GamesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        Log.d("GamesViewModel:", "Retreiving Data From Room");
        gameList = appDatabase.gameDAO().getFiftyOwnedRandomGames();

    }

    public LiveData<List<Game>> getGameList() {
        return gameList;
    }
}
