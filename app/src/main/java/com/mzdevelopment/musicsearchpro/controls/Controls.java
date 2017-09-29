package com.mzdevelopment.musicsearchpro.controls;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.service.SongService;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;
import com.mzdevelopment.musicsearchpro.utility.UtilFunctions;
import java.util.ArrayList;

public class Controls
{

    static String LOG_CLASS = "Controls";

    public Controls()
    {
    }

    public static void nextControl(Context context)
    {
        if(!UtilFunctions.isServiceRunning(SongService.class.getName(), context))
        {
            return;
        }
        if(PlayerConstants.SONGS_LIST.size() > 0)
        {
            if(PlayerConstants.SONG_NUMBER < -1 + PlayerConstants.SONGS_LIST.size())
            {
                PlayerConstants.SONG_NUMBER = 1 + PlayerConstants.SONG_NUMBER;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else
            {
                PlayerConstants.SONG_NUMBER = 0;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }

    public static void pauseControl(Context context)
    {
        sendMessage(context.getResources().getString(R.string.pause));
    }

    public static void playControl(Context context)
    {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void previousControl(Context context)
    {
        if(!UtilFunctions.isServiceRunning(SongService.class.getName(), context))
        {
            return;
        }
        if(PlayerConstants.SONGS_LIST.size() > 0)
        {
            if(PlayerConstants.SONG_NUMBER > 0)
            {
                PlayerConstants.SONG_NUMBER = -1 + PlayerConstants.SONG_NUMBER;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else
            {
                PlayerConstants.SONG_NUMBER = -1 + PlayerConstants.SONGS_LIST.size();
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }

    private static void sendMessage(String s)
    {
        try
        {
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, s));
            return;
        }
        catch(Exception exception)
        {
            return;
        }
    }

}
