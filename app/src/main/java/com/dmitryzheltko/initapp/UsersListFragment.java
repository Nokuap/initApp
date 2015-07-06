package com.dmitryzheltko.initapp;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dmitryzheltko.initapp.Adapters.UsersAdapter;
import com.dmitryzheltko.initapp.Models.Device;
import com.dmitryzheltko.initapp.Models.User;
import com.dmitryzheltko.initapp.Utils.Utils;

import java.util.List;

import butterknife.ButterKnife;


public class UsersListFragment extends ListFragment {

    static final String DEVICE = "device";
    List<User> users;
    UsersAdapter adapter;
    Device device;

    public static UsersListFragment newInstance(Device device) {
        UsersListFragment fragment = new UsersListFragment();
        Bundle args = new Bundle();
        args.putSerializable(DEVICE, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = (Device) getArguments().getSerializable(DEVICE);
        }
        users = User.listAll(User.class);
        adapter = new UsersAdapter(getActivity(), users);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_users, null);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        device.setUser((User) adapter.getItem(position));
        device.save();
        getActivity().onBackPressed();
    }


}
