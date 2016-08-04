package com.example.owner.edp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Krishna N. Deoram on 2016-03-07.
 * Gumi is love. Gumi is life.
 */
public class AdapterUserList extends BaseAdapter {

    private ArrayList<User> users;
    private final LayoutInflater mLayoutInflater;
    private final String TAG = "User List Adapter";

    public AdapterUserList(Context context) {
        super();
        users = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void replaceWith(Collection<User> newUsers) {

        this.users.clear();
        this.users.addAll(newUsers);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_users, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        else
            viewHolder = (ViewHolder)convertView.getTag();

        User u = users.get(position);
        viewHolder.username.setText(u.getUsername() + " ID: " + u.getUserId() + " Role: " + u.getRole());
        viewHolder.firstname.setText(u.getFirstName() + " " + u.getLastName());

        return convertView;
    }

    public static class ViewHolder {

        public TextView username, firstname;

        public ViewHolder(View convertView) {

            username = (TextView)convertView.findViewById(R.id.username);
            firstname = (TextView)convertView.findViewById(R.id.firstname);
        }
    }
}
