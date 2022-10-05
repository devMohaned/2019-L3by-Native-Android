package com.gamesexchange.gamesexchange.adapter;

import android.content.Context;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.fragments.MyGamesFragment;
import com.gamesexchange.gamesexchange.fragments.MyWishListFragment;
import com.gamesexchange.gamesexchange.fragments.RewardsFragment;
import com.gamesexchange.gamesexchange.fragments.TradeGamesFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerFragmentsAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public ViewPagerFragmentsAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new MyGamesFragment();
            case 1:
                return new MyWishListFragment();
            case 2:
                return new TradeGamesFragment();
            case 3:
                return new RewardsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return mContext.getString(R.string.game);
            case 1:
                return  mContext.getString(R.string.wish);
            case 2:
                return  mContext.getString(R.string.tab_trade);
            case 3:
                return  mContext.getString(R.string.coins);
            default:
                return null;
        }
    }
}
