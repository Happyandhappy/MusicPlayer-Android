package com.mzdevelopment.musicsearchpro.db;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences
{

    private static final String APP_SHARED_PREFS = "music";
    private SharedPreferences appSharedPrefs;
    private android.content.SharedPreferences.Editor prefsEditor;

    public AppPreferences(Context context)
    {
        appSharedPrefs = context.getSharedPreferences("music", 0);
        prefsEditor = appSharedPrefs.edit();
    }

    public void RemoveAllSharedPreference()
    {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public String getChartUrl()
    {
        return appSharedPrefs.getString("ChartUrl", "http://api.soundcloud.com/tracks.json?client_id=74b38ce3d412b6e25db87047c7bd4a4b&limit=50&offset=0&tags=");
    }

    public String getChartUrl1()
    {
        return appSharedPrefs.getString("ChartUrl1", "http://api-v2.soundcloud.com/explore/");
    }

    public String getChartUrl2()
    {
        return appSharedPrefs.getString("ChartUrl2", "?client_id=74b38ce3d412b6e25db87047c7bd4a4b&limit=50&offset=0");
    }

    public String getDeviceId()
    {
        return appSharedPrefs.getString("deviceid", "");
    }

    public String getFlag()
    {
        return appSharedPrefs.getString("flag", "");
    }

    public int getIsUnlockAppPaid()
    {
        return appSharedPrefs.getInt("IsUnlockAppPaid", 0);
    }

    public String getPopularUrl()
    {
        return appSharedPrefs.getString("PopularUrl", "http://api.soundcloud.com/tracks.json?client_id=74b38ce3d412b6e25db87047c7bd4a4b&tags=Popular%20Music&limit=50&offset=0");
    }

    public String getSearchUrl()
    {
        return appSharedPrefs.getString("SearchUrl", "http://api.soundcloud.com/tracks.json?client_id=74b38ce3d412b6e25db87047c7bd4a4b&limit=50");
    }

    public void setChartUrl(String s)
    {
        prefsEditor.putString("ChartUrl", s);
        prefsEditor.commit();
    }

    public void setChartUrl1(String s)
    {
        prefsEditor.putString("ChartUrl1", s);
        prefsEditor.commit();
    }

    public void setChartUrl2(String s)
    {
        prefsEditor.putString("ChartUrl2", s);
        prefsEditor.commit();
    }

    public void setDeviceId(String s)
    {
        prefsEditor.putString("deviceid", s);
        prefsEditor.commit();
    }

    public void setFlag(String s)
    {
        prefsEditor.putString("flag", s);
        prefsEditor.commit();
    }

    public void setIsUnlockAppPaid(int i)
    {
        prefsEditor.putInt("IsUnlockAppPaid", i);
        prefsEditor.commit();
    }

    public void setPopularUrl(String s)
    {
        prefsEditor.putString("PopularUrl", s);
        prefsEditor.commit();
    }

    public void setSearchUrl(String s)
    {
        prefsEditor.putString("SearchUrl", s);
        prefsEditor.commit();
    }
}
