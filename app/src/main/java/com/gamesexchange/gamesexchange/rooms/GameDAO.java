package com.gamesexchange.gamesexchange.rooms;


import com.gamesexchange.gamesexchange.model.Game;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface GameDAO {

    // Games List

    @Query("SELECT * FROM games_table")
    LiveData<List<Game>> getAll();

    @Query("SELECT * FROM games_table where game_name LIKE '%' || :gameName || '%' LIMIT 100")
    LiveData<List<Game>> getGamesByName(String gameName);

//    @Query("SELECT * FROM games_table ORDER BY RANDOM() LIMIT 50")
@Query("SELECT * FROM games_table ORDER BY game_name ASC LIMIT 50")
LiveData<List<Game>> getFiftyOwnedRandomGames();

    @Query("SELECT * FROM games_table WHERE is_selected = :isSelectedState AND flag = :flag")
    LiveData<List<Game>> loadMyWishesOrGames(boolean isSelectedState, String flag);


    @Query("SELECT COUNT(*) from games_table")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Game... games);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIfFoundReplace(Game... games);


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAllGames(List<Game>... games);

    @Delete
    void delete(Game game);


    @Query("UPDATE games_table SET is_selected = :isSelected, flag = :flag WHERE game_name = :gameName")
    void updateCertainGames(String gameName, boolean isSelected, String flag);


    @Query("UPDATE games_table SET is_selected = :isSelected, flag = :flag WHERE flag = 'o'")
    void removeAllGames(boolean isSelected, String flag);

    @Query("UPDATE games_table SET is_selected = :isSelected, flag = :flag WHERE flag = 'w'")
    void removeAllWishes(boolean isSelected, String flag);

    @Query("UPDATE games_table SET is_selected = :isSelected, flag = :flag")
    void removeAllGamesAndWishes(boolean isSelected, String flag);

    @Query("UPDATE games_table SET is_selected = :isSelected, flag = :flag WHERE game_name = :gameName")
    void restoreGamesOrWishes(String gameName, boolean isSelected, String flag);

    @Query("UPDATE games_table SET is_selected = :isSelected, flag = :flag WHERE flag = 'o' OR flag = 'w'")
    void removeAllGamesAndWishesWhereYouSelectedBefore(boolean isSelected, String flag);
}
