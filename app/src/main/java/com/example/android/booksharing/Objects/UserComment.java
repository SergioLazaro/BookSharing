package com.example.android.booksharing.Objects;

/**
 * Created by Sergio on 6/6/16.
 */
public class UserComment {

    private String username, lastComment, date;

    public UserComment(String username, String lastComment, String date){
        this.username = username;
        this.lastComment = lastComment;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
