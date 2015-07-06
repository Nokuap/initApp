package com.dmitryzheltko.initapp;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitryzheltko.initapp.Adapters.DevicesAdapter;
import com.dmitryzheltko.initapp.Models.Device;
import com.dmitryzheltko.initapp.Models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserInfoFragment extends ListFragment {

    final static String USER = "user";
    @InjectView(R.id.name) TextView nameView;
    @InjectView(R.id.department) TextView departmentView;
    @InjectView(R.id.photo) ImageView photoView;
    User user;
    List<Device> devices;
    DevicesAdapter adapter;

    public static UserInfoFragment newInstance(User user) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.inject(this, rootView);
        devices = Device.findByUserId(user.getId());
        adapter = new DevicesAdapter(getActivity(), devices);
        setListAdapter(adapter);

        nameView.setText(user.getName());
        departmentView.setText(user.getDepartment());
        ImageLoader.getInstance().displayImage(user.getPhotoURL(), photoView);

        return rootView;
    }
}
