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
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity;
import com.gamesexchange.gamesexchange.model.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyGamesAdapter extends RecyclerView.Adapter<MyGamesAdapter.GameViewHolder> {



    private List<Game> mGamesList;
    private Context mContext;




    public class GameViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageOfGame,mRemovalOfGame,mCameraOfGame;
        public TextView mNameOfGame;

        public GameViewHolder(final View itemView) {
            super(itemView);
            mCameraOfGame = itemView.findViewById(R.id.ID_item_upload_camera);
            mImageOfGame = itemView.findViewById(R.id.ID_item_upload_image);
            mNameOfGame = itemView.findViewById(R.id.ID_item_name);
            mRemovalOfGame = itemView.findViewById(R.id.ID_item_delete_image);
        }



    }

    public MyGamesAdapter(Context context, List<Game> gamesList) {
        mGamesList = gamesList;
        mContext = context;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_game,
                parent, false);
        GameViewHolder evh = new GameViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final GameViewHolder holder, final int position) {
        final Game currentItem = mGamesList.get(position);
        holder.mNameOfGame.setText(currentItem.getGame_name());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary(currentItem);
            }
        });


        holder.mImageOfGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openGallary(currentItem);
            }
        });

        holder.mCameraOfGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(currentItem);
            }
        });

        holder.mRemovalOfGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, mContext.getString(R.string.removed), Toast.LENGTH_SHORT).show();
                MVPHomePageActivity.removeGameListStatic.add(currentItem);
                mGamesList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mGamesList.size());
            }
        });

    }

    private void openGallary(Game currentItem) {
      /*  Intent intent = new Intent(mContext,UploadingImagesMyGames.class);
        intent.putExtra(Constants.INTENTS.FRONT_IMAGE_URI, currentItem.getFrontImg());
        intent.putExtra(Constants.INTENTS.BACK_IMAGE_URI,currentItem.getBackImg());
        intent.putStringArrayListExtra(Constants.INTENTS.MORE_IMAGES_URI,replaceCommaToList(currentItem.getMoreImg()));
        intent.putExtra(Constants.INTENTS.MY_GAME_NAME,currentItem.getGame_name());
        intent.putExtra(Constants.INTENTS.MY_GAME_DESCRIPTION,currentItem.getGameDescription());
        mContext.startActivity(intent);*/
    }


    private void openCamera(Game currentItem) {
    /*    Intent intent = new Intent(mContext,UploadingCameraImagesMyGames.class);
        intent.putExtra(Constants.INTENTS.FRONT_IMAGE_URI, currentItem.getFrontImg());
        intent.putExtra(Constants.INTENTS.BACK_IMAGE_URI,currentItem.getBackImg());
        intent.putStringArrayListExtra(Constants.INTENTS.MORE_IMAGES_URI,replaceCommaToList(currentItem.getMoreImg()));
        intent.putExtra(Constants.INTENTS.MY_GAME_NAME,currentItem.getGame_name());
        intent.putExtra(Constants.INTENTS.MY_GAME_DESCRIPTION,currentItem.getGameDescription());
        mContext.startActivity(intent);*/
    }


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