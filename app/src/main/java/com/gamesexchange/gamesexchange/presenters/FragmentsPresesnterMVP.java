package com.gamesexchange.gamesexchange.presenters;

import com.gamesexchange.gamesexchange.model.Game;

public class FragmentsPresesnterMVP {
    private String game;
    private View view;

    public FragmentsPresesnterMVP(View view) {
        this.game = "";
        this.view = view;
    }

    public void searchForGame(String foundGameName){
        game = foundGameName;
        view.filter(game);

    }


    public interface View{

        void filter(String gameName);

    }
}


