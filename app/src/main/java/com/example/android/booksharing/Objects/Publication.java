package com.example.android.booksharing.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sergio on 3/6/16.
 */
public class Publication {

    private int publicationID;
    private String username, title, author, type, description;
    private float rate;

    public Publication(String username, String title, String author, String type,
                       float rate, String description){
        this.publicationID = -1;
        this.username = username;
        this.title = title;
        this.author = author;
        this.type = type;
        this.rate = rate;
        this.description = description;
    }

    public Publication(int publicationID, String username, String title, String author, String type,
                       float rate, String description){
        this.publicationID = publicationID;
        this.username = username;
        this.title = title;
        this.author = author;
        this.type = type;
        this.rate = rate;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getPublicationID() {
        return publicationID;
    }

    public void setPublicationID(int publicationID) {
        this.publicationID = publicationID;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String generateJSONObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("publicationID",publicationID);
            obj.put("username", username);
            obj.put("title", title);
            obj.put("author", author);
            obj.put("description", description);
            obj.put("type", type);
            obj.put("rate", rate);
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
