package com.mzdevelopment.musicsearchpro.utility;

import android.os.Handler;
import java.util.ArrayList;

public class PlayerConstants
{

    public static Handler PLAY_PAUSE_HANDLER;
    public static Handler PROGRESSBAR_CHANGE_HANDLER;
    public static Handler PROGRESSBAR_HANDLER;
    public static ArrayList SONGS_LIST = new ArrayList();
    public static ArrayList SONGS_LIST_TEMP = new ArrayList();
    public static boolean SONG_CHANGED = false;
    public static Handler SONG_CHANGE_HANDLER;
    public static int SONG_NUMBER = 0;
    public static boolean SONG_PAUSED = true;
    public static boolean isRepeat = false;
    public static boolean isShuffle = false;

    public PlayerConstants()
    {
    }

}
