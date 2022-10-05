package com.gamesexchange.gamesexchange.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.model.Game;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyWishesAdapter extends RecyclerView.Adapter<MyWishesAdapter.GameViewHolder> {



    private List<String> mGamesList;
    private Context mContext;




    public class GameViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageOfGame;
        public TextView mNameOfGame;

        public GameViewHolder(final View itemView) {
            super(itemView);
            mImageOfGame = itemView.findViewById(R.id.ID_item_remove_my_wish);
            mNameOfGame = itemView.findViewById(R.id.ID_item_name);
        }



    }

    public MyWishesAdapter(Context context, List<String> gamesList) {
        mGamesList = gamesList;
        mContext = context;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_wishes,
                parent, false);
        GameViewHolder evh = new GameViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final GameViewHolder holder, final int position) {
        final String currentItem = mGamesList.get(position);
        holder.mNameOfGame.setText(currentItem);

        Game game = new Game();
        game.setGame_name(currentItem);

        holder.mImageOfGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "Remove this game", Toast.LENGTH_SHORT).show();
                MVPHomePageActivity.wishRemoveGameListStatic.add(game);
                mGamesList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mGamesList.size());
            }
        });

    }




    @Override
    public int getItemCount() {
        return mGamesList.size();
    }


}