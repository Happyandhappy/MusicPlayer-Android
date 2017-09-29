package com.mzdevelopment.musicsearchpro;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.util.Utilities;
import com.mzdevelopment.musicsearchpro.volley.MyVolleyHelper;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends Activity
{

    static final int RC_REQUEST = 10001;
    static final String TAG = "Drive";
    AppPreferences appPref;

    public Splash()
    {
    }

    private boolean checkconnection()
    {
        if(!Utilities.checkNetworkConnection(this))
        {
            showDialog(3);
            return false;
        } else
        {
            System.out.println("NETWORK CONNECTION AVAILABLE-------------");
            return true;
        }
    }

    private void fetchData()
    {
        JsonObjectRequest jsonobjectrequest = new JsonObjectRequest(0, "http://france-seo.com/Free%20Music%20Streamer/musicUrl.php", null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject jsonobject)
                    {
                        try
                        {
                            if(jsonobject == null)
                            {
                                appPref.setPopularUrl("http://api.soundcloud.com/tracks.json?client_id=74b38ce3d412b6e25db87047c7bd4a4b&tags=Popular%20Music&limit=50&offset=0");
                                appPref.setSearchUrl("http://api.soundcloud.com/tracks.json?client_id=74b38ce3d412b6e25db87047c7bd4a4b&limit=50");
                                appPref.setChartUrl("http://api.soundcloud.com/tracks.json?client_id=74b38ce3d412b6e25db87047c7bd4a4b&limit=50&offset=0&tags=");
                                appPref.setChartUrl1("http://api-v2.soundcloud.com/explore/");
                                appPref.setChartUrl2("?client_id=74b38ce3d412b6e25db87047c7bd4a4b&limit=50&offset=0");
                            }else {
                                String s = jsonobject.getString("popular");
                                String s1 = jsonobject.getString("search");
                                if (jsonobject.has("charts") && !jsonobject.isNull("charts")) {
                                    String s4 = jsonobject.getString("charts");
                                    appPref.setChartUrl(s4);
                                }
                                String s2 = jsonobject.getString("charts1");
                                String s3 = jsonobject.getString("charts2");
                                appPref.setPopularUrl(s);
                                appPref.setSearchUrl(s1);
                                appPref.setChartUrl1(s2);
                                appPref.setChartUrl2(s3);
                            }
                            Intent intent = new Intent(Splash.this, Search.class);
                            startActivity(intent);
                            finish();
                        }
                        catch(JSONException jsonexception)
                        {
                            jsonexception.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyerror)
                    {
                    }
                })
            {
                public Map getHeaders()
                    throws AuthFailureError
                {
                    return new HashMap();
                }
            };
        MyVolleyHelper.getIntance(this).addRequestToQueue(jsonobjectrequest);
    }

    void alert(String s)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(s);
        builder.setNeutralButton("OK", null);
        Log.d("Drive", (new StringBuilder()).append("Showing alert dialog: ").append(s).toString());
        builder.create().show();
    }

    void complain(String s)
    {
        Log.e("Drive", (new StringBuilder()).append("**** TrivialDrive Error: ").append(s).toString());
        alert((new StringBuilder()).append("Error: ").append(s).toString());
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.splash);
        appPref = new AppPreferences(this);
        if(checkconnection())
        {
            fetchData();
        }
    }

    protected Dialog onCreateDialog(int i)
    {
        AlertDialog alertdialog;
        switch(i)
        {
        default:
            return null;

        case 1: // '\001'
            return ProgressDialog.show(this, getResources().getText(R.string.app_name), getResources().getText(R.string.wait_message), true);

        case 2: // '\002'
            AlertDialog alertdialog1 = (new android.app.AlertDialog.Builder(this)).create();
            alertdialog1.setTitle("Title");
            alertdialog1.setMessage("No record found");
            alertdialog1.setButton("Close", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int j)
                {
                }
            });
            return alertdialog1;

        case 3: // '\003'
            alertdialog = (new android.app.AlertDialog.Builder(this)).create();
            break;
        }
        alertdialog.setTitle(R.string.app_name);
        alertdialog.setMessage(getResources().getString(R.string.Dialog_Network));
        alertdialog.setButton("Ok", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int j)
            {
                finish();
            }
        });
        return alertdialog;
    }
}
