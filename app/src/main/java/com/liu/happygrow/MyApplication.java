package com.liu.happygrow;

import android.app.Application;

import com.liu.happygrow.colorUi.util.SharedPreferencesMgr;

/**
 * Created by Liu on 2016/3/10.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesMgr.init(this, "derson");
    }
}
