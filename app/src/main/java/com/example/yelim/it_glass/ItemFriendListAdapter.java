package com.example.yelim.it_glass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Yelim on 2017-03-26.
 */

public class ItemFriendListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ItemFriend> itemFriends;
    private ItemFriend itemFriend;

    public ItemFriendListAdapter(Context context) {
        mContext = context;
        itemFriends = new ArrayList<ItemFriend>();
    }

    public ItemFriendListAdapter(Context context, List itemFriends) {
        mContext = context;
        this.itemFriends = itemFriends;
    }

    public void addItem(ItemFriend itemFriend) {
        itemFriends.add(itemFriend);
    }

    @Override
    public int getCount() {
        return itemFriends.size();
    }

    @Override
    public ItemFriend getItem(int position) {
        return itemFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        ItemFriendView itemView;

        /**
         * 사용자가 처음오면 convertview는 null
         */
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_friend, parent, false);
            //itemView = new ItemFriendView(mContext, itemFriends.get(position));

            holder = new ItemHolder();
            holder.tvFriendID = (TextView) convertView.findViewById(R.id.friend_id);
            holder.tvFriendDrink = (TextView) convertView.findViewById(R.id.friend_drink);
            holder.tvFriendLight = (TextView) convertView.findViewById(R.id.friend_light);

            holder.tvFriendID.setText(itemFriends.get(position).getFriendID());
            holder.tvFriendDrink.setText(itemFriends.get(position).getFriendDrink());
            holder.tvFriendLight.setText(itemFriends.get(position).getFriendLight());

            //convertview는 항상 tag를 업어간다
            convertView.setTag(holder);

        }
        /**
         * convertview != null 이면 tag를 꺼낸다
         */
        else {
            holder = (ItemHolder) convertView.getTag();
            itemView = (ItemFriendView) convertView;
        }

        itemFriend = getItem(position);
        convertView.setOnClickListener(clickDetail(mContext));

        return convertView;

/*
        itemView.setText(0, itemFriends.get(position).getFriendID());
        itemView.setText(1, itemFriends.get(position).getFriendDrink());
        itemView.setText(2, itemFriends.get(position).getFriendLight());

        return itemView;*/
    }

    private View.OnClickListener clickDetail(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //friend detail view with dialog (context.startActivityForResult 써야하는지 그냥 Activity써도 되는지...)
                Intent intent = new Intent(v.getContext(), FriendListDetailActivity.class);
                intent.putExtra("LAYOUT_TYPE", "DETAIL");
                context.startActivity(intent);

            }
        };
    }

    private class ItemHolder {
        private TextView tvFriendID;
        private TextView tvFriendDrink;
        private TextView tvFriendLight;
    }

}

