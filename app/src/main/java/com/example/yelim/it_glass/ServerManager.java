package com.example.yelim.it_glass;

/**
 * Created by Yelim on 2017-03-22.
 */

public class ServerManager {

    /**
     * check if the server has input ID.
     * Server has : true,
     * doesn't have : false.
     * @param ID
     * @return
     */
    public static boolean hasID(String ID) {
        return false;
    }

    /**
     * take an input variable(String ID) from user
     * and save it in Server Database
     * @param ID
     */
    public static void saveUserID(String ID) {

    }

    /**
     * add friend of userID with friendID to server database
     * @param userID
     * @param friendID
     */
    public static void addFriend(String userID, String friendID) {

    }

    /**
     * add friend of userID with friendID and color RGB to server database
     * @param userID
     * @param friendID
     * @param R
     * @param G
     * @param B
     */
    public static void addFriend(String userID, String friendID, int R, int G, int B) {

    }

    /**
     * delete friend with friendID from server database
     * @param friendID
     */
    public static void deleteFriend(String friendID) {

    }

    /**
     * change light color of friendID into RGB in server database
     * @param friendID
     * @param R
     * @param G
     * @param B
     */
    public static void changeLightColor(String friendID, int R, int G, int B) {

    }
}
