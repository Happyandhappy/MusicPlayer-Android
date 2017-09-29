package com.mzdevelopment.musicsearchpro;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import java.io.IOException;
import java.util.Timer;

public class PlayerService extends Service
{

    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    public static Intent broadcastIntent;
    public static Timer timer;
    private android.content.SharedPreferences.Editor editor;
    String file;
    public int i;
    int min;
    private MediaPlayer mp;
    private SharedPreferences preferences;
    int sec;

    public PlayerService()
    {
        i = 0;
        min = 59;
        sec = 60;
        file = "";
    }

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onCreate()
    {
        mp = new MediaPlayer();
        mp.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaplayer)
            {
                mediaplayer.stop();
            }
        });
    }

    public void play(String s)
    {
        try
        {
            file = s;
            mp.reset();
            mp.setDataSource(s);
            mp.prepare();
            mp.start();
            return;
        }
        catch(IllegalStateException illegalstateexception)
        {
            illegalstateexception.printStackTrace();
            mp.release();
            mp = new MediaPlayer();
            mp.reset();
            return;
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            illegalargumentexception.printStackTrace();
            return;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }
}
