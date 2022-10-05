package com.gamesexchange.gamesexchange.model;

import com.google.firebase.database.ServerValue;

import java.util.Map;

public class ChatMessage {

    private String chatText,chatSender,roomId, chatReceiver,chatReceiverUid;
    private String chatSenderUid;
    private long time;

    public ChatMessage(){}



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
    public Map<String, String> getTimeStamp()
    {
        return ServerValue.TIMESTAMP;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getChatReceiver() {
        return chatReceiver;
    }

    public void setChatReceiver(String chatReceiver) {
        this.chatReceiver = chatReceiver;
    }

    public String getChatReceiverUid() {
        return chatReceiverUid;
    }

    public void setChatReceiverUid(String chatReceiverUid) {
        this.chatReceiverUid = chatReceiverUid;
    }
}
