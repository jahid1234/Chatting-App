package com.example.chatappdemo.Model;

public class GroupList {

    String groupname;
    String groupId;
    String imageURL;
    String creatormember;
    String member0;
    String member1;
    String member2;
    boolean leavemember1;
    boolean leavemember2;
    boolean leavemember3;

    String key;
    public GroupList(String groupname,String groupId, String imageURL, String creatormember, String member0, String member1, String member2,
                     boolean leavemember1, boolean leavemember2, boolean leavemember3) {
        this.groupname = groupname;
        this.groupId = groupId;
        this.imageURL = imageURL;
        this.creatormember = creatormember;
        this.member0 = member0;
        this.member1 = member1;
        this.member2 = member2;
        this.leavemember1 = leavemember1;
        this.leavemember2 = leavemember2;
        this.leavemember3 = leavemember3;
    }

    public GroupList() {
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCreatormember() {
        return creatormember;
    }

    public String getMember0() {
        return member0;
    }

    public String getMember1() {
        return member1;
    }

    public String getMember2() {
        return member2;
    }

    public boolean isLeavemember1() {
        return leavemember1;
    }

    public boolean isLeavemember2() {
        return leavemember2;
    }

    public boolean isLeavemember3() {
        return leavemember3;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
