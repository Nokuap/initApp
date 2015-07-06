package com.dmitryzheltko.initapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitryzheltko.initapp.Models.Device;
import com.dmitryzheltko.initapp.Models.User;
import com.dmitryzheltko.initapp.Utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceInfoFragment extends Fragment {
    static final String DEVICE = "device";
    Device device;
    @InjectView(R.id.model) TextView modelView;
    @InjectView(R.id.manufacturer) TextView manufacturerView;
    @InjectView(R.id.user) TextView userView;

    public static DeviceInfoFragment newInstance(Device device) {
        DeviceInfoFragment fragment = new DeviceInfoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_info, null);
        ButterKnife.inject(this, rootView);
        if (device != null) {
            modelView.setText(device.getModel());
            manufacturerView.setText(device.getManufacturer());
            User user = device.getUser();
            userView.setText(user == null ? getResources().getString(R.string.no_assigned_user) : user.getName());
        }
        return rootView;
    }

    @OnClick(R.id.user)
    public void userInfo() {
        if (device.getUser() != null) {
            Fragment fragment = UserInfoFragment.newInstance(device.getUser());
            getActivity().getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        } else {
            Utils.toast(getActivity(), getResources().getString(R.string.no_assigned_user));
        }
    }

    @OnClick(R.id.assign)
    public void assign() {
        Fragment fragment = UsersListFragment.newInstance(device);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
