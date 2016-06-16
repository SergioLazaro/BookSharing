package com.example.android.booksharing.Objects;

/**
 * Created by Sergio on 17/6/16.
 */
public class UnreadMessage {

    private int messageID;
    private boolean unread;

    public UnreadMessage(int messageID, boolean unread){
        this.messageID = messageID;
        this.unread = unread;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
