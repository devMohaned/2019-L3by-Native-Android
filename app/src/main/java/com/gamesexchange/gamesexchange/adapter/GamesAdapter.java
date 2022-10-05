package com.gamesexchange.gamesexchange.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.fragments.MyGamesFragment;
import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;
import com.gamesexchange.gamesexchange.rooms.AppExecuter;
import com.gamesexchange.gamesexchange.rooms.GameDAO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.gamePtsCount;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.mGamesPts;


public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GameViewHolder> {



    private List<Game> mGamesList;
    private Context mContext;




    public class GameViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mCheckBoxOfGame;
        public TextView mNameOfGame;

        public GameViewHolder(final View itemView) {
            super(itemView);
            mCheckBoxOfGame = itemView.findViewById(R.id.ID_item_checkbox);
            mNameOfGame = itemView.findViewById(R.id.ID_item_name);
        }



    }

    public GamesAdapter(Context context, List<Game> gamesList) {
        mGamesList = gamesList;
        mContext = context;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game,
                parent, false);
        GameViewHolder evh = new GameViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final GameViewHolder holder, final int position) {
        final Game currentItem = mGamesList.get(position);
        holder.mNameOfGame.setText(currentItem.getGame_name());


        if (currentItem.isSelected() && currentItem.getFlag().equals("o")) {
            holder.mCheckBoxOfGame.setChecked(true);
        } else {
            holder.mCheckBoxOfGame.setChecked(false);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!holder.mCheckBoxOfGame.isChecked()) {

                    if (gamePtsCount > 0) {
                        gamePtsCount = gamePtsCount - Constants.ADDITION_OF_GAMES_COST;
                        mGamesPts.setText(String.valueOf(gamePtsCount) /*+ " GP"*/);

                        MVPHomePageActivity.gameListStatic.add(
                                currentItem
                        );

                        if (MVPHomePageActivity.removeGameListStatic.contains(currentItem)) {
                            MVPHomePageActivity.removeGameListStatic.remove(currentItem);
                        }

                        holder.mCheckBoxOfGame.setChecked(true);
                        currentItem.setSelected(true);
                        currentItem.setFlag("o");


                        //    postData(currentItem.getGame_name()(), currentUser.getFirebase_uid(), position);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.no_games_point), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    MVPHomePageActivity.removeGameListStatic.add(currentItem);

                    if (MVPHomePageActivity.gameListStatic.contains(currentItem)) {
                        MVPHomePageActivity.gameListStatic.remove(currentItem);
                        gamePtsCount = gamePtsCount + Constants.ADDITION_OF_GAMES_COST;
                       mGamesPts.setText(String.valueOf(gamePtsCount)/* + " GP"*/);
                    }


                    holder.mCheckBoxOfGame.setChecked(false);
                    currentItem.setSelected(false);
                    currentItem.setFlag("");
//                    removeData(currentItem.getGame_name(), currentUser.getFirebase_uid(), position);

                }
            }
        });

    }




    @Override
    public int getItemCount() {
        return mGamesList.size();
    }

    public void searchGameList(List<Game> list)
    {
        mGamesList.clear();
        mGamesList = list;
        notifyDataSetChanged();
    }

}