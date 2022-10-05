package com.gamesexchange.gamesexchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AmazonUtils;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.trading.TradingUserActivity;
import com.gamesexchange.gamesexchange.model.TradeModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TradeModelAdapter extends RecyclerView.Adapter<TradeModelAdapter.GameViewHolder> {
    private ArrayList<TradeModel> mExchangeList;
    private Context mContext;

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView mNameOfExchange, mLocationOfExchange, mWantedGames, mOwnerStatus;
        ImageView mProfileImageOfExchange;


        GameViewHolder(View itemView) {
            super(itemView);
            mNameOfExchange = itemView.findViewById(R.id.ID_item_name_of_exchange);
            mLocationOfExchange = itemView.findViewById(R.id.ID_item_location_of_exchange);
            mWantedGames = itemView.findViewById(R.id.ID_item_wanted_games_of_exchange);
            mOwnerStatus = itemView.findViewById(R.id.ID_item_online_circle_of_exchange);
            mProfileImageOfExchange = itemView.findViewById(R.id.ID_item_image_of_exchange);
        }
    }

    public TradeModelAdapter(Context context, ArrayList<TradeModel> gamesList) {
        mExchangeList = gamesList;
        mContext = context;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trade_model,
                parent, false);
        return new GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, final int position) {
        final TradeModel currentItem = mExchangeList.get(position);
        holder.mNameOfExchange.setText(currentItem.getNameOfUser());


        String wantedGames = getGames(currentItem.getGamesIWant());
        holder.mWantedGames.setText(wantedGames);


        if (!currentItem.getDistrict().toLowerCase().equals("null")) {
            try {
                holder.mLocationOfExchange
                        .setText(
                                AppUtils.LocationUtils.getDistrictFromIndex(Integer.parseInt(currentItem.getDistrict())) + ", "  + AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(currentItem.getLocationOfUser())));
            }catch (Exception e)
            {
                holder.mLocationOfExchange
                        .setText(
                                AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(currentItem.getLocationOfUser())));

            }
        } else {
            holder.mLocationOfExchange
                    .setText(
                            AppUtils.LocationUtils.getLocationFromIndex(Integer.parseInt(currentItem.getLocationOfUser())));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TradingUserActivity.class);
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_FIRST_NAME, currentItem.getFirstNameOfUser());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_LAST_NAME, currentItem.getLastNameOfUser());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_LOCATION, currentItem.getLocationOfUser());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_ID, currentItem.getIdOfCommonUser());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_GAMES, currentItem.getGamesIWant());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_DISTRICT, currentItem.getDistrict());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_PROFILE_IMAGE, currentItem.getProfileImage());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_GAMES, currentItem.getGamesOfCommonUser());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_WISHES, currentItem.getWishesOfCommonUser());
                intent.putExtra(Constants.INTENTS.INTENT_DIFFERENT_USER_TOKEN,currentItem.getToken());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);


            }
        });


        switch (currentItem.getOnlineStatus()) {
            case 0:
             /*   holder.mOwnerStatus.setText(mContext.getString(R.string.offline));
                holder.mOwnerStatus.setTextColor(mContext.getResources().getColor(R.color.cherry_red));*/
                break;
            case 1:
              /*  holder.mOwnerStatus.setText(mContext.getString(R.string.online));
                holder.mOwnerStatus.setTextColor(mContext.getResources().getColor(R.color.darker_green));*/
              holder.mOwnerStatus.setVisibility(View.VISIBLE);
                break;
            default:
               /* holder.mOwnerStatus.setText(mContext.getString(R.string.unavailable));
                holder.mOwnerStatus.setTextColor(mContext.getResources().getColor(R.color.cherry_red));*/
                break;
        }

        if (currentItem.getProfileImage().length() > 4)
        {
            Picasso.get().load(AmazonUtils.CLOUDFLARE + AmazonUtils.PROFILE_DIRECTORY + currentItem.getProfileImage()).into(holder.mProfileImageOfExchange);
        }

    }

    private String getGames(String games) {
        try {
            return games.substring(0, 120) + "...";
        } catch (IndexOutOfBoundsException e) {
            return games;
        }
    }


    private String getCommonGamesString(ArrayList<String> listOfCommonGames) {
        if (listOfCommonGames.size() < 3) {
            return listOfCommonGames.toString();
        } else {
            if (listOfCommonGames.size() - 3 == 1) {
                return listOfCommonGames.get(0) + ", "
                        + listOfCommonGames.get(1) + ", "
                        + listOfCommonGames.get(2) + ", "
                        + mContext.getString(R.string.and) + " "
                        + (listOfCommonGames.size() - 3)
                        + " "
                        + mContext.getString(R.string.more_game);
            } else {
                return listOfCommonGames.get(0) + ", "
                        + listOfCommonGames.get(1) + ", "
                        + listOfCommonGames.get(2) + ", "
                        + mContext.getString(R.string.and) + " "
                        + (listOfCommonGames.size() - 3)
                        + " "
                        + mContext.getString(R.string.more_games);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mExchangeList.size();
    }


}