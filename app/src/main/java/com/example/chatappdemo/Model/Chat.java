package com.example.chatappdemo.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String imageMessage;
    private String time;
    private boolean isseen;
    public Chat(String sender, String receiver, String message,String imageMessage,String time,boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.imageMessage = imageMessage;
        this.time   = time;
        this.isseen = isseen;
    }

    public Chat (){

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
