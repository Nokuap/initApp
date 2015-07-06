package com.dmitryzheltko.initapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dmitryzheltko.initapp.Adapters.DevicesAdapter;
import com.dmitryzheltko.initapp.Models.Device;
import com.dmitryzheltko.initapp.Models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DevicesListFragment extends ListFragment {

    static final int ALL = 0;
    static final int ASSIGNED = 1;
    static final int FREE = 2;
    static final int START = 3;
    static final int DEPARTMENT = 4;
    List<Device> devices;
    DevicesAdapter adapter;
    View rootView;
    DataLoader dataLoader;
    @InjectView(R.id.assigned_devices) ToggleButton assignedDevicesButton;
    @InjectView(R.id.free_devices) ToggleButton freeDevicesButton;
    @InjectView(R.id.department_chooser) Button departmentChooserButton;
    @InjectView(R.id.progressBar) View progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_devices, null);
        ButterKnife.inject(this, rootView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (devices == null) {
            dataLoader = new DataLoader();
            dataLoader.execute(START);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Device device = (Device) adapter.getItem(position);
        Fragment fragment = DeviceInfoFragment.newInstance(device);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("device_info").commit();
    }

    @OnClick(R.id.assigned_devices)
    public void showAssigned() {
        freeDevicesButton.setChecked(false);
        updateDataLoader();
        dataLoader = new DataLoader();
        if (assignedDevicesButton.isChecked()) {
            dataLoader.execute(ASSIGNED);
        } else {
            dataLoader.execute(ALL);
        }
    }

    @OnClick(R.id.free_devices)
    public void showFree() {
        assignedDevicesButton.setChecked(false);
        updateDataLoader();
        dataLoader = new DataLoader();
        if (freeDevicesButton.isChecked()) {
            dataLoader.execute(FREE);
        } else {
            dataLoader.execute(ALL);
        }

    }

    @OnClick(R.id.department_chooser)
    public void departmentChooser() {
        final EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        editText.setLayoutParams(params);
        editText.setHint(R.string.search_hint);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        freeDevicesButton.setChecked(false);
                        assignedDevicesButton.setChecked(false);
                        updateDataLoader();
                        dataLoader = new DataLoader(editText.getText().toString());
                        dataLoader.execute(DEPARTMENT);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setNeutralButton(R.string.cancel, null)
                .setView(editText)
                .create()
                .show();
    }

    private void updateDataLoader() {
        if (dataLoader.getStatus() == AsyncTask.Status.RUNNING) {
            dataLoader.cancel(true);
        }
    }

    public class DataLoader extends AsyncTask<Integer, Void, Void> {

        String search;

        public DataLoader() {
        }

        public DataLoader(String search) {
            this.search = search;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (devices != null && adapter != null) {
                ((TextView) getListView().getEmptyView()).setText("");
                devices.removeAll(devices);
                adapter.notifyDataSetChanged();
            } else {
                devices = new ArrayList<>();
            }
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Integer... params) {
            if (User.listAll(User.class).isEmpty()) {
                User.getUsers(getActivity(), R.xml.users);
                Device.getDevices(getActivity(), R.xml.devices);
            }
            switch (params[0]) {
                case ALL:
                    devices.addAll(Device.listAll(Device.class));
                    break;
                case ASSIGNED:
                    devices.addAll(Device.findAssignedDevices());
                    break;
                case FREE:
                    devices.addAll(Device.findFreeDevices());
                    break;
                case START:
                    devices.addAll(Device.listAll(Device.class));
                    break;
                case DEPARTMENT:
                    devices.addAll(Device.findByUserDepartment(search != null ? search : ""));
                    break;
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (devices != null) {
                devices.removeAll(devices);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            assignedDevicesButton.setVisibility(View.VISIBLE);
            freeDevicesButton.setVisibility(View.VISIBLE);
            departmentChooserButton.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new DevicesAdapter(getActivity(), devices);
                setListAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            if (devices.isEmpty()) {
                ((TextView) getListView().getEmptyView()).setText(getResources().getString(R.string.empty));
            }
        }
    }

}
