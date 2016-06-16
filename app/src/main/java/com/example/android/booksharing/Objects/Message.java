package com.example.android.booksharing.Objects;

/**
 * Created by Sergio on 7/6/16.
 */
public class Message {

    private int id, read, publicationID;
    private String sender, receiver, text, date, title;

    public Message(int id, String sender, String receiver, String text, String date, int read,
                   int publicationID){
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.date = date;
        this.read = read;
        this.publicationID = publicationID;
        this.title = "";
    }
    public Message(int id, String sender, String receiver, int read, int publicationID, String title){
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.read = read;
        this.publicationID = publicationID;
        this.title = title;
        this.date = "";
        this.text = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationID() {
        return publicationID;
    }

    public void setPublicationID(int publicationID) {
        this.publicationID = publicationID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", read=" + read +
                ", publicationID=" + publicationID +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
