package com.example.chatappdemo.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String imageMessage;
    private String time;
    private boolean isseen;
    private boolean deleteBySender;
    private boolean deleteByReceiver;

    private String key;
    public Chat(String sender, String receiver, String message,String imageMessage,String time,boolean isseen,boolean deleteBySender,boolean
                deleteByReceiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.imageMessage = imageMessage;
        this.time   = time;
        this.isseen = isseen;
        this.deleteBySender = deleteBySender;
        this.deleteByReceiver = deleteByReceiver;
    }

    public Chat (){

    }

    public boolean isDeleteBySender() {
        return deleteBySender;
    }

    public boolean isDeleteByReceiver() {
        return deleteByReceiver;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
