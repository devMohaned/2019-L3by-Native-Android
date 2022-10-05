package com.gamesexchange.gamesexchange.model;

import com.google.firebase.database.ServerValue;

import java.util.Map;

public class ChatMessageVer2 {
    private String chatText, chatSender, roomId;
    private String chatSenderUid;
    private long time;

    public ChatMessageVer2() {
    }


    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    public String getChatSender() {
        return chatSender;
    }

    public void setChatSender(String chatSender) {
        this.chatSender = chatSender;
    }

    public String getChatSenderUid() {
        return chatSenderUid;
    }

    public void setChatSenderUid(String chatSenderUid) {
        this.chatSenderUid = chatSenderUid;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, String> getTimeStamp() {
        return ServerValue.TIMESTAMP;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
