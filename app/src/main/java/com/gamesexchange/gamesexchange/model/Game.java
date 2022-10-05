package com.gamesexchange.gamesexchange.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "games_table")
public class Game {

    @PrimaryKey(autoGenerate = true)
    private int room_id;

    private String user_id;


    @ColumnInfo(name = "game_name")
    private String game_name;

    @ColumnInfo(name = "is_selected")
    private boolean isSelected = false;

    @ColumnInfo(name = "flag")
    private String flag = "";

    public Game()
    {}

    public Game(String name) {
        this.game_name = name;
    }

    /*public Game(String game_name, String user_id, String flag)
    {
        this.game_name = game_name;
        this.flag = flag;
        this.user_id = user_id;
    }*/

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
