package com.example.chatappdemo.Model;

public class GroupChat {
    private String sender;
    private String senderImage;
    private String receiver;
    private String message;
    private String imageMessage;
    private String time;
    private String seenByReceiver0;
    private String seenByReceiver1;
    private String seenByReceiver2;
    private String seenByReceiver3;

    public GroupChat(String sender,String senderImage,String receiver, String message, String imageMessage, String time, String seenByReceiver0, String seenByReceiver1, String seenByReceiver2
    ,String seenByReceiver3) {
        this.sender = sender;
        this.senderImage = senderImage;
        this.receiver = receiver;
        this.message = message;
        this.imageMessage = imageMessage;
        this.time = time;
        this.seenByReceiver0 = seenByReceiver0;
        this.seenByReceiver1 = seenByReceiver1;
        this.seenByReceiver2 = seenByReceiver2;
        this.seenByReceiver3 = seenByReceiver3;
    }

    public GroupChat() {
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public String getTime() {
        return time;
    }

    public String getSeenByReceiver0() {
        return seenByReceiver0;
    }

    public String getSeenByReceiver1() {
        return seenByReceiver1;
    }

    public String getSeenByReceiver2() {
        return seenByReceiver2;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public String getSeenByReceiver3() {
        return seenByReceiver3;
    }
}
