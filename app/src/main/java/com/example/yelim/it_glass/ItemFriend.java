package com.example.yelim.it_glass;

/**
 * Created by Yelim on 2017-03-26.
 */

public class ItemFriend {
    private String friendID;
    private String friendDrink;
    private String friendLight;

    public ItemFriend(String friendID, String friendDrink, String friendLight) {
        this.friendID = friendID;
        this.friendDrink = friendDrink;
        this.friendLight = friendLight;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public void setFriendDrink(String friendDrink) {
        this.friendDrink = friendDrink;
    }

    public void setFriendLight(String friendLight) {
        this.friendLight = friendLight;
    }

    public String getFriendID() {

        return friendID;
    }

    public String getFriendDrink() {
        return friendDrink;
    }

    public String getFriendLight() {
        return friendLight;
    }
}
