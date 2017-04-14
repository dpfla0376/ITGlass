package com.example.yelim.it_glass;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Yelim on 2017-04-05.
 */

public class ItemFriendView extends RelativeLayout {

    private TextView tvFriendID;
    private TextView tvFriendDrink;
    private TextView tvFriendLight;

    public ItemFriendView(Context context) {
        super(context);
    }

    public ItemFriendView(Context context, ItemFriend aItem) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_friend, this, true);

        tvFriendID = (TextView) findViewById(R.id.friend_id);
        tvFriendID.setText(aItem.getFriendID());
        tvFriendDrink = (TextView) findViewById(R.id.friend_drink);
        tvFriendDrink.setText(aItem.getFriendDrink());
        tvFriendLight = (TextView) findViewById(R.id.friend_light);
        tvFriendLight.setText(aItem.getFriendLight());

    }

    public void setText(int index, String data) {
        switch(index) {
            case 0:
                tvFriendID.setText(data);
            case 1:
                tvFriendDrink.setText(data);
            case 2:
                tvFriendLight.setText(data);
            default:
        }
    }

    public void setText(String[] data) {
        tvFriendID.setText(data[0]);
        tvFriendDrink.setText(data[1]);
        tvFriendLight.setText(data[2]);
    }
}
