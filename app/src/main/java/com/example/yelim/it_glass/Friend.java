package com.example.yelim.it_glass;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yelim on 2017-03-26.
 */

@IgnoreExtraProperties
public class Friend {

    private String fID;
    private String fLight;
    //public Map<String, Boolean> stars = new HashMap<>();

    public Friend() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Friend(String ID, String light) {
        fID = ID;
        fLight = light;
    }

    public String getfID() {
        return fID;
    }

    public String getfLight() {
        return fLight;
    }

    public void setfID(String fID) {

        this.fID = fID;
    }

    public void setfLight(String fLight) {
        this.fLight = fLight;
    }
/*
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(fID, fLight);

        return result;
    }
*/
}