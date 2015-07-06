package com.dmitryzheltko.initapp.Models;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.orm.SugarRecord;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry.zheltko on 3/25/2015.
 */
public class Device extends SugarRecord<Device> implements Serializable {
    User user;
    String model;
    String manufacturer;
    String photoURL;

    public static void getDevices(Context context, int xmlId) {
        ArrayList<Device> devices = new ArrayList<>();
        XmlResourceParser xpp = context.getResources().getXml(xmlId);
        try {
            Device device = null;
            xpp.next();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = xpp.getName();
                    if ("device".equalsIgnoreCase(name)) {
                        device = new Device();
                        devices.add(device);
                    } else if ("photourl".equalsIgnoreCase(name)) {
                        xpp.next();
                        device.setPhotoURL(xpp.getText());
                    } else if ("manufacturer".equalsIgnoreCase(name)) {
                        xpp.next();
                        device.setManufacturer(xpp.getText());
                    } else if ("model".equalsIgnoreCase(name)) {
                        xpp.next();
                        device.setModel(xpp.getText());
                    } else if ("user_id".equalsIgnoreCase(name)) {
                        xpp.next();
                        Long id = Long.valueOf(xpp.getText());
                        if (id != -1) {
                            device.setUser(User.findById(User.class, id));
                        }
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Device.deleteAll(Device.class);
        Device.saveInTx(devices);
    }

    public static List<Device> findByUserDepartment(String search) {
        return Device.findWithQuery(Device.class, "Select device.photo_url, device.manufacturer, device.model, device.user from device join user on user.department like ? where device.user = user.id",
                "%" + search + "%");
    }

    public static List<Device> findByUserId(long id) {
        return Device.find(Device.class, "user = ?", String.valueOf(id));
    }

    public static List<Device> findAssignedDevices() {
        return Device.find(Device.class, "user > ?", "0");
    }

    public static List<Device> findFreeDevices() {
        return Device.find(Device.class, "user = ?", "0");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
