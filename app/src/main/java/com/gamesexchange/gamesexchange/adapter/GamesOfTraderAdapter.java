package com.gamesexchange.gamesexchange.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.model.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GamesOfTraderAdapter extends RecyclerView.Adapter<GamesOfTraderAdapter.GameViewHolder> {



    private List<Game> mGamesList;
    private Context mContext;




    public class GameViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageOfGame;
        public TextView mNameOfGame;

        public GameViewHolder(final View itemView) {
            super(itemView);
            mImageOfGame = itemView.findViewById(R.id.ID_item_trading_game_image);
            mNameOfGame = itemView.findViewById(R.id.ID_item_trading_game_name);
        }



    }

    public GamesOfTraderAdapter(Context context, List<Game> gamesList) {
        mGamesList = gamesList;
        mContext = context;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trading_game,
                parent, false);
        GameViewHolder evh = new GameViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final GameViewHolder holder, final int position) {
        final Game currentItem = mGamesList.get(position);
        holder.mNameOfGame.setText(currentItem.getGame_name());

        holder.itemView.setClickable(false);

       /* if (currentItem.getFrontImg().length() > 5
                && currentItem.getFrontImg().contains(".")
                && currentItem.getFlag().equalsIgnoreCase("o"))
        {
            holder.itemView.setClickable(true);
        }else{
            holder.itemView.setClickable(false);
            holder.mImageOfGame.setVisibility(View.GONE);
        }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getFrontImg().length() > 5 && currentItem.getFlag().equalsIgnoreCase("o")) openGamesImages(currentItem);
            }
        });*/


    }

   /* private void openGamesImages(Game currentItem) {
        Intent intent = new Intent(mContext,GameImagesActivity.class);
        intent.putExtra(Constants.INTENTS.FRONT_IMAGE_URI, currentItem.getFrontImg());
        intent.putExtra(Constants.INTENTS.BACK_IMAGE_URI,currentItem.getBackImg());
        intent.putStringArrayListExtra(Constants.INTENTS.MORE_IMAGES_URI,replaceCommaToList(currentItem.getMoreImg()));
        intent.putExtra(Constants.INTENTS.MY_GAME_NAME,currentItem.getGame_name());
        intent.putExtra(Constants.INTENTS.MY_GAME_DESCRIPTION,currentItem.getGameDescription());
        mContext.startActivity(intent);
    }*/





    private ArrayList<String> replaceCommaToList(String moreImg)
    {
        String[] items = moreImg.split("\\?");

        return new ArrayList<>(Arrays.asList(items));
    }

    @Override
    public int getItemCount() {
        return mGamesList.size();
    }


}