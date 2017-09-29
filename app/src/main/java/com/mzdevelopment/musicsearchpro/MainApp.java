package com.mzdevelopment.musicsearchpro;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class MainApp extends Application
{

    private static MainApp mObject;

    public MainApp()
    {
    }

    public static MainApp getInstance()
    {
        return mObject;
    }

    protected void attachBaseContext(Context context)
    {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public void onCreate()
    {
        mObject = this;
        super.onCreate();
    }
}
