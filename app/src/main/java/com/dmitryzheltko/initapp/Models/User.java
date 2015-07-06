package com.dmitryzheltko.initapp.Models;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.orm.SugarRecord;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dmitry.zheltko on 3/25/2015.
 */
public class User extends SugarRecord<User> implements Serializable {
    String name;
    String department;
    String photoURL;

    public static void getUsers(Context context, int xmlId) {
        ArrayList<User> users = new ArrayList<>();
        XmlResourceParser xpp = context.getResources().getXml(xmlId);
        try {
            User user = null;
            xpp.next();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = xpp.getName();
                    if ("user".equalsIgnoreCase(name)) {
                        user = new User();
                        users.add(user);
                    } else if ("name".equalsIgnoreCase(name)) {
                        xpp.next();
                        user.setName(xpp.getText());
                    } else if ("department".equalsIgnoreCase(name)) {
                        xpp.next();
                        user.setDepartment(xpp.getText());
                    } else if ("photourl".equalsIgnoreCase(name)) {
                        xpp.next();
                        user.setPhotoURL(xpp.getText());
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User.deleteAll(User.class);
        User.saveInTx(users);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
