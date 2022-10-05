package com.gamesexchange.gamesexchange.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gamesexchange.gamesexchange.fragments.MyWishListFragment;
import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;
import com.gamesexchange.gamesexchange.rooms.AppExecuter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.mWishPts;
import static com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.wishPtsCount;


public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.GameViewHolder> {
    private List<Game> mGamesList;
    private Context mContext;

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mCheckBoxOfGame;
        TextView mNameOfGame;

        GameViewHolder(View itemView) {
            super(itemView);
            mCheckBoxOfGame = itemView.findViewById(R.id.ID_item_checkbox);
            mNameOfGame = itemView.findViewById(R.id.ID_item_name);

        }
    }

    public WishlistAdapter(Context context, List<Game> gamesList) {
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
    public void onBindViewHolder(final GameViewHolder holder, final int position) {
        final Game currentItem = mGamesList.get(position);
        holder.mNameOfGame.setText(currentItem.getGame_name());

        if (currentItem.isSelected() && currentItem.getFlag().equals("w"))
        {
            holder.mCheckBoxOfGame.setChecked(true);
        }else{
            holder.mCheckBoxOfGame.setChecked(false);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!holder.mCheckBoxOfGame.isChecked()) {
                        if (wishPtsCount > 0) {
                            wishPtsCount = wishPtsCount - Constants.ADDITION_OF_GAMES_COST;
                            mWishPts.setText(String.valueOf(wishPtsCount)/* + " WP"*/);

                            MVPHomePageActivity.wishgameListStatic.add(
                                    currentItem
                            );

                            if (MVPHomePageActivity.wishRemoveGameListStatic.contains(currentItem)) {
                                MVPHomePageActivity.wishRemoveGameListStatic.remove(currentItem);
                            }


                            holder.mCheckBoxOfGame.setChecked(true);
                        currentItem.setSelected(true);
                        currentItem.setFlag("w");
//                        postData(currentItem.getGame_name(), currentUser.getFirebase_uid(), position);
                    }else {
                            Toast.makeText(mContext, mContext.getString(R.string.no_wish_points), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        MVPHomePageActivity.wishRemoveGameListStatic.add(currentItem);

                        if (MVPHomePageActivity.wishgameListStatic.contains(currentItem)) {
                            MVPHomePageActivity.wishgameListStatic.remove(currentItem);
                            wishPtsCount = wishPtsCount + Constants.ADDITION_OF_GAMES_COST;
                            mWishPts.setText(String.valueOf(wishPtsCount)/* + " WP"*/);
                        }


                        holder.mCheckBoxOfGame.setChecked(false);
                        currentItem.setSelected(false);
                        currentItem.setFlag("w");
//                        removeData(currentItem.getGame_name(), currentUser.getFirebase_uid(), position);
                    }
                }
        });


    }






    @Override
    public int getItemCount() {
        return mGamesList.size();
    }

    public void searchWishlist(List<Game> list)
    {
        mGamesList.clear();
        mGamesList = list;
        notifyDataSetChanged();
    }

}