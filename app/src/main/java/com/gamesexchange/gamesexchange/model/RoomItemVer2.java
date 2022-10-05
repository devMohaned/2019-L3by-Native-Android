package com.gamesexchange.gamesexchange.model;

public class RoomItemVer2 {
    private String room_id,room_created_by,room_created_at,room_sender_id,room_reciever_id;
    private String senderName, recieverFirstName,recieverLastName, recieverPhoto;
    private String onlineStatus;
    private String recieverToken;

    public RoomItemVer2()
    {}

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_created_by() {
        return room_created_by;
    }

    public void setRoom_created_by(String room_created_by) {
        this.room_created_by = room_created_by;
    }

    public String getRoom_created_at() {
        return room_created_at;
    }

    public void setRoom_created_at(String room_created_at) {
        this.room_created_at = room_created_at;
    }

    public String getRoom_sender_id() {
        return room_sender_id;
    }

    public void setRoom_sender_id(String room_sender_id) {
        this.room_sender_id = room_sender_id;
    }

    public String getRoom_reciever_id() {
        return room_reciever_id;
    }

    public void setRoom_reciever_id(String room_reciever_id) {
        this.room_reciever_id = room_reciever_id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecieverFirstName() {
        return recieverFirstName;
    }

    public void setRecieverFirstName(String recieverFirstName) {
        this.recieverFirstName = recieverFirstName;
    }

    public String getRecieverLastName() {
        return recieverLastName;
    }

    public void setRecieverLastName(String recieverLastName) {
        this.recieverLastName = recieverLastName;
    }

    public String getRecieverPhoto() {
        return recieverPhoto;
    }

    public void setRecieverPhoto(String recieverPhoto) {
        this.recieverPhoto = recieverPhoto;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getRecieverToken() {
        return recieverToken;
    }

    public void setRecieverToken(String recieverToken) {
        this.recieverToken = recieverToken;
    }
}
