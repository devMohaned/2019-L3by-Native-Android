package com.gamesexchange.gamesexchange.services;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GamesTasks {

    public static String ADD_ALL_GAMES = "add-all-games";
    public static String UPDATE_WISHES = "update-all-wishes";

    public static void executeAdditionOfGames(Context context, String action) {
        if (ADD_ALL_GAMES.equals(action)) {
            addGame(AppDatabase.getAppDatabase(context), context);
        }
    }


    private static void addGame(final AppDatabase db, Context context) {
        int countsOfRows = AppDatabase.getAppDatabase(context.getApplicationContext()).gameDAO().getCount();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("games.txt"), "UTF-8"));


            // do reading, usually loop until end of file reading
            String mLine;
//            while ((mLine = reader.readLine()) != null) {
                for (int i =1; i<=Constants.GAMES_COUNT;i++)
                {
                   mLine = reader.readLine();
                    if (i > countsOfRows)
                    {
                        Game gameAdded = new Game(mLine);
                        db.gameDAO().insertAll(gameAdded);
                    }
                }

         //   }


        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }


    }



    private static void addGames(final AppDatabase db,Context context) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("games.txt"), "UTF-8"));


            // do reading, usually loop until end of file reading
            String mLine;


            while ((mLine = reader.readLine()) != null) {
                Game gameAdded = new Game(mLine);
                db.gameDAO().insertAll(gameAdded);
            }


        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }


    }

}
