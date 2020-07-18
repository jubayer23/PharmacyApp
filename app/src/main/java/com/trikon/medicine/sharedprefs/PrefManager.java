package com.trikon.medicine.sharedprefs;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.trikon.medicine.BuildConfig;
import com.trikon.medicine.appdata.MydApplication;
import com.trikon.medicine.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by jubayer on 6/6/2017.
 */


public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static Gson GSON = new Gson();
    // Sharedpref file name
    private static final String PREF_NAME = BuildConfig.APPLICATION_ID;

    private static final String KEY_USER = "user";

    private static final String KEY_RECEIVED_CARD_OBJ = "received_card_obj";
    private static final String KEY_LOG = "key_log";
    private static final String KEY_EMAIL_CACHE = "key_email_cache";
    private static final String KEY_ONGOING_ORDER = "key_ongoing_order";
    private static final String KEY_NOTI_ORDER_NUMBER = "KEY_NOTI_ORDER_NUMBER";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    public void setEmailCache(String obj) {
        editor = pref.edit();

        editor.putString(KEY_EMAIL_CACHE, obj);

        // commit changes
        editor.commit();
    }
    public String getEmailCache() {
        return pref.getString(KEY_EMAIL_CACHE,"");
    }

    public void setLog(String obj) {
        editor = pref.edit();

        editor.putString(KEY_LOG, obj);

        // commit changes
        editor.commit();
    }
    public String getLog() {
        return pref.getString(KEY_LOG,"");
    }



    public void setUser(User obj) {
        editor = pref.edit();

        String json = MydApplication.gson.toJson(obj);

        editor.putString(KEY_USER, json);

        // commit changes
        editor.commit();
    }

    public User getUser() {
        String json = pref.getString(KEY_USER, "");

        if (!json.isEmpty()) {
            return MydApplication.gson.fromJson(json, User.class);
        } else
            return null;
    }


    /*public void setOnGoingOrder(PendingOrder obj) {
        editor = pref.edit();

        String json = MydApplication.gson.toJson(obj);

        editor.putString(KEY_ONGOING_ORDER, json);

        // commit changes
        editor.commit();
    }

    public PendingOrder getOnGoingOrder() {
        String json = pref.getString(KEY_ONGOING_ORDER, "");

        if (!json.isEmpty()) {
            return MydApplication.gson.fromJson(json, PendingOrder.class);
        } else
            return null;
    }*/


    public ArrayList<Integer> getNotiOrderNumber() {

        ArrayList<Integer> shops = new ArrayList<>();

        String gson = pref.getString(KEY_NOTI_ORDER_NUMBER, "");

        if (gson.isEmpty()) return shops;

        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        shops = GSON.fromJson(gson, type);

        return shops;
    }


    public void setNotiOrderNumber(ArrayList<Integer> obj) {
        editor = pref.edit();

        editor.putString(KEY_NOTI_ORDER_NUMBER, GSON.toJson(obj));

        // commit changes
        editor.commit();
    }


}