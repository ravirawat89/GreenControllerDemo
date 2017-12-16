package com.netcommlabs.greencontroller.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kumar on 10/30/2017.
 */

public class MySharedPreference {

    private static MySharedPreference object;
    public static final String MyPREFERENCES = "greenContrllerPrefs";
    private SharedPreferences sharedpreferences;
    private Context mContext;
    private String keySetMacAd = "macAddkey";


    public static MySharedPreference getInstance(Context mContext) {
        if (object == null) {
            object = new MySharedPreference(mContext);
        }
        return object;
    }

    public MySharedPreference(Context mContext) {
        this.mContext = mContext;
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }


    //************************** getter setter for string data ***************************
    public void setStringData(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key) {
        return sharedpreferences.getString(key, null);
    }

    //******************************** getter setter for boolean data ***********************************
    public void setBooleanData(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBooleanData(String key) {
        return sharedpreferences.getBoolean(key, false);
    }

    public void setConnectedDvcMacAdd(String macAdd) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(this.keySetMacAd, macAdd);
        editor.commit();
    }

    public String getConnectedDvcMacAdd() {
        return sharedpreferences.getString(this.keySetMacAd, null);
    }
}
