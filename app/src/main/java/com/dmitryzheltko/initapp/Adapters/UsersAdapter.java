package com.dmitryzheltko.initapp.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitryzheltko.initapp.Models.User;
import com.dmitryzheltko.initapp.R;

import java.util.List;

/**
 * Created by dmitry.zheltko on 3/25/2015.
 */
public class UsersAdapter extends MyListAdapter {

    public UsersAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_user, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.department = (TextView) convertView.findViewById(R.id.department);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = (User) getData().get(position);
        viewHolder.name.setText(user.getName());
        viewHolder.department.setText(user.getDepartment());
        imageLoader.displayImage(user.getPhotoURL(), viewHolder.image);
        return convertView;
    }


    private class ViewHolder {
        TextView name;
        TextView department;
        ImageView image;
    }
}
