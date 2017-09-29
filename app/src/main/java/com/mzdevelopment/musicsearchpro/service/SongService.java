package com.mzdevelopment.musicsearchpro.service;

import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.*;
import android.os.*;
import android.util.Log;
import android.widget.RemoteViews;
import com.mzdevelopment.musicsearchpro.*;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.musicplayer.AudioPlayerActivity;
import com.mzdevelopment.musicsearchpro.receiver.NotificationBroadcast;
import com.mzdevelopment.musicsearchpro.util.Utilities;
import com.mzdevelopment.musicsearchpro.utility.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SongService extends Service
    implements android.media.AudioManager.OnAudioFocusChangeListener
{
    public class DownloadImagesTask extends AsyncTask
    {
        private Bitmap download_Image(String s)
        {
            Bitmap bitmap = null;
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inSampleSize = 1;
            try
            {
                InputStream inputstream = getHttpConnection(s);
                bitmap = BitmapFactory.decodeStream(inputstream, null, options);
                inputstream.close();
            }
            catch(IOException ioexception)
            {
                ioexception.printStackTrace();
                return bitmap;
            }
            return bitmap;
        }

        private InputStream getHttpConnection(String s)
            throws IOException
        {
            java.net.URLConnection urlconnection = (new URL(s)).openConnection();
            HttpURLConnection httpurlconnection;
            int i;
            InputStream inputstream;
            InputStream inputstream1;
            try
            {
                httpurlconnection = (HttpURLConnection)urlconnection;
                httpurlconnection.setRequestMethod("GET");
                httpurlconnection.connect();
                i = httpurlconnection.getResponseCode();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
                return null;
            }
            inputstream = null;
            if(i == 200)
            {
                inputstream1 = httpurlconnection.getInputStream();
                inputstream = inputstream1;
            }
            return inputstream;
        }

        protected Bitmap doInBackground(Void avoid[])
        {
            return download_Image(imgUrl.replace("-large", "-t500x500"));
        }

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        protected void onPostExecute(Bitmap bitmap)
        {
            try {
                if (bitmap != null) {
                    bmp = bitmap;
                    updateNotification();
                    UpdateMetadata(data);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Bitmap)obj);
        }

        public DownloadImagesTask()
        {
            super();
        }
    }

    private class MainTask extends TimerTask
    {
        public void run()
        {
            handler.sendEmptyMessage(0);
        }

        private MainTask()
        {
            super();
        }

    }


    public static final String NOTIFY_DELETE = "com.mzdevelopment.musicsearchpro.delete";
    public static final String NOTIFY_NEXT = "com.mzdevelopment.musicsearchpro.next";
    public static final String NOTIFY_PAUSE = "com.mzdevelopment.musicsearchpro.pause";
    public static final String NOTIFY_PLAY = "com.mzdevelopment.musicsearchpro.play";
    public static final String NOTIFY_PREVIOUS = "com.mzdevelopment.musicsearchpro.previous";
    private static boolean currentVersionSupportBigNotification = false;
    private static boolean currentVersionSupportLockScreenControls = false;
    public static MediaPlayer mp;
    private static Thread playPauseThread;
    private static RemoteControlClient remoteControlClient;
    private static Timer timer;
    String LOG_CLASS;
    int NOTIFICATION_ID;
    AppPreferences appPrefs;
    AudioManager audioManager;
    Bitmap bmp;
    MediaItem data;
    private final Handler handler = new Handler() {
        public void handleMessage(Message message)
        {
            if(SongService.mp != null && isMediaPrepared)
            {
                int i = (100 * SongService.mp.getCurrentPosition()) / SongService.mp.getDuration();
                Integer ainteger1[] = new Integer[3];
                ainteger1[0] = Integer.valueOf(SongService.mp.getCurrentPosition());
                ainteger1[1] = Integer.valueOf(SongService.mp.getDuration());
                ainteger1[2] = Integer.valueOf(i);
                PlayerConstants.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, ainteger1));
                return;
            }
            try
            {
                Integer ainteger[] = new Integer[3];
                ainteger[0] = Integer.valueOf(0);
                ainteger[1] = Integer.valueOf(0);
                ainteger[2] = Integer.valueOf(0);
                PlayerConstants.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, ainteger));
                return;
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            return;
        }
    };
    String imgUrl;
    boolean isMediaPrepared;
    Bitmap mDummyAlbumArt;
    private Handler mHandler;
    private Runnable mUpdateTimeTask;
    private ComponentName remoteComponentName;
    Thread thread;

    public SongService()
    {
        LOG_CLASS = "SongService";
        NOTIFICATION_ID = 1111;
        bmp = null;
        imgUrl = "";
        mHandler = new Handler();
        isMediaPrepared = false;
        mUpdateTimeTask = new Runnable() {
            public void run()
            {
                try
                {
                    if(SongService.mp != null)
                    {
                        int i = (100 * SongService.mp.getCurrentPosition()) / SongService.mp.getDuration();
                        Integer ainteger[] = new Integer[3];
                        ainteger[0] = Integer.valueOf(SongService.mp.getCurrentPosition());
                        ainteger[1] = Integer.valueOf(SongService.mp.getDuration());
                        ainteger[2] = Integer.valueOf(i);
                        PlayerConstants.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, ainteger));
                    }
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
                mHandler.postDelayed(this, 100L);
            }
        };
    }

    private void RegisterRemoteClient()
    {
        remoteComponentName = new ComponentName(getApplicationContext(), (new NotificationBroadcast()).ComponentName());
        try
        {
            if(remoteControlClient == null)
            {
                audioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                intent.setComponent(remoteComponentName);
                remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(this, 0, intent, 0));
                audioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(189);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void UpdateMetadata(MediaItem mediaitem)
    {
        android.media.RemoteControlClient.MetadataEditor metadataeditor;
        try
        {
            if(remoteControlClient == null)
            {
                return;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return;
        }
        metadataeditor = remoteControlClient.editMetadata(true);
        metadataeditor.putString(2, mediaitem.getArtist());
        metadataeditor.putString(7, mediaitem.getSongTitle());
        if(bmp == null)
            mDummyAlbumArt = BitmapFactory.decodeResource(getResources(), R.drawable.default_album_art);
        else
            mDummyAlbumArt = bmp;
        metadataeditor.putBitmap(100, mDummyAlbumArt);
        metadataeditor.apply();
        audioManager.requestAudioFocus(this, 3, 1);
    }

    private void newNotification()
    {
        String s;
        Notification notification;
        s = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongTitle();
        RemoteViews remoteviews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        RemoteViews remoteviews1 = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);
        notification = (new android.support.v4.app.NotificationCompat.Builder(getApplicationContext())).setSmallIcon(R.drawable.ic_music).setContentTitle(s).build();
        setListeners(remoteviews);
        setListeners(remoteviews1);
        notification.contentView = remoteviews;
        if(currentVersionSupportBigNotification)
        {
            notification.bigContentView = remoteviews1;
        }
        imgUrl = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongImage();
        if(Utilities.checkNetworkConnection(getApplicationContext()))
        {
            (new DownloadImagesTask()).execute(new Void[0]);
        }
        try {
            if (bmp == null) {
                notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
                if (currentVersionSupportBigNotification) {
                    notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
                }
            } else {
                notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, bmp);
                if (currentVersionSupportBigNotification) {
                    notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, bmp);
                }
            }
        }catch(Exception e){}
        if(PlayerConstants.SONG_PAUSED)
        {
            notification.contentView.setViewVisibility(R.id.btnPause, 8);
            notification.contentView.setViewVisibility(R.id.btnPlay, 0);
            if(currentVersionSupportBigNotification)
            {
                notification.bigContentView.setViewVisibility(R.id.btnPause, 8);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, 0);
            }
        } else
        {
            notification.contentView.setViewVisibility(R.id.btnPause, 0);
            notification.contentView.setViewVisibility(R.id.btnPlay, 8);
            if(currentVersionSupportBigNotification)
            {
                notification.bigContentView.setViewVisibility(R.id.btnPause, 0);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, 8);
            }
        }
        notification.contentView.setTextViewText(R.id.textSongName, s);
        notification.contentView.setTextColor(R.id.textSongName, getResources().getColor(R.color.action_bar));
        if(currentVersionSupportBigNotification)
        {
            notification.bigContentView.setTextViewText(R.id.textSongName, s);
            notification.bigContentView.setTextColor(R.id.textSongName, getResources().getColor(R.color.action_bar));
        }
        notification.flags = 2 | notification.flags;
        startForeground(NOTIFICATION_ID, notification);
    }

    public static void pauseSong(Context context)
    {
        if(mp != null && mp.isPlaying())
        {
            mp.pause();
        }
    }

    private void playSong(String s, MediaItem mediaitem)
    {
        try
        {
            isMediaPrepared = false;
            if(currentVersionSupportLockScreenControls)
            {
                RegisterRemoteClient();
                UpdateMetadata(mediaitem);
                remoteControlClient.setPlaybackState(3);
            }
            mp.reset();
            mp.setAudioStreamType(3);
            mp.setDataSource((new StringBuilder()).append(s).append(appPrefs.getChartUrl2().split("&")[0]).toString());
            mp.prepare();
            mp.start();
            timer.scheduleAtFixedRate(new MainTask(), 0L, 100L);
            return;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public static void resumeSong(Context context)
    {
        if(mp != null)
        {
            mp.start();
        }
    }

    private void updateNotification()
    {
        String s;
        Notification notification;
        s = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongTitle();
        RemoteViews remoteviews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        RemoteViews remoteviews1 = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);
        notification = (new android.support.v4.app.NotificationCompat.Builder(getApplicationContext())).setSmallIcon(R.drawable.ic_music).setContentTitle(s).build();
        setListeners(remoteviews);
        setListeners(remoteviews1);
        notification.contentView = remoteviews;
        if(currentVersionSupportBigNotification)
        {
            notification.bigContentView = remoteviews1;
        }
        try {
            if (bmp == null) {
                notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
                if (currentVersionSupportBigNotification) {
                    notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
                }
            } else {
                notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, bmp);
                if (currentVersionSupportBigNotification) {
                    notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, bmp);
                }
            }
        }catch(Exception e){}
        if(PlayerConstants.SONG_PAUSED)
        {
            notification.contentView.setViewVisibility(R.id.btnPause, 8);
            notification.contentView.setViewVisibility(R.id.btnPlay, 0);
            if(currentVersionSupportBigNotification)
            {
                notification.bigContentView.setViewVisibility(R.id.btnPause, 8);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, 0);
            }
        } else
        {
            notification.contentView.setViewVisibility(R.id.btnPause, 0);
            notification.contentView.setViewVisibility(R.id.btnPlay, 8);
            if(currentVersionSupportBigNotification)
            {
                notification.bigContentView.setViewVisibility(R.id.btnPause, 0);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, 8);
            }
        }
        notification.contentView.setTextViewText(R.id.textSongName, s);
        notification.contentView.setTextColor(R.id.textSongName, getResources().getColor(R.color.action_bar));
        if(currentVersionSupportBigNotification)
        {
            notification.bigContentView.setTextViewText(R.id.textSongName, s);
            notification.bigContentView.setTextColor(R.id.textSongName, getResources().getColor(R.color.action_bar));
        }
        notification.flags = 2 | notification.flags;
        startForeground(NOTIFICATION_ID, notification);
    }

    public void onAudioFocusChange(int i)
    {
    }

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onCreate()
    {
        mp = new MediaPlayer();
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        appPrefs = new AppPreferences(getApplicationContext());
        currentVersionSupportBigNotification = UtilFunctions.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilFunctions.currentVersionSupportLockScreenControls();
        timer = new Timer();
        mp.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaplayer)
            {
                isMediaPrepared = true;
            }
        });
        mp.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaplayer)
            {
                isMediaPrepared = false;
                try {
                    if (!PlayerConstants.isRepeat) {
                        if (PlayerConstants.isShuffle)
                            PlayerConstants.SONG_NUMBER = 0 + (new Random()).nextInt(1 + (0 + (-1 + PlayerConstants.SONGS_LIST.size())));
                        else if (PlayerConstants.SONG_NUMBER < -1 + PlayerConstants.SONGS_LIST.size())
                            PlayerConstants.SONG_NUMBER = 1 + PlayerConstants.SONG_NUMBER;
                        else
                            PlayerConstants.SONG_NUMBER = 0;
                    }
                    data = (MediaItem) PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
                    if (SongService.currentVersionSupportLockScreenControls) {
                        RegisterRemoteClient();
                    }
                    String s = data.getSongUrl();
                    playSong(s, data);
                    newNotification();
                }catch(Exception e){}
            }
        });
        super.onCreate();
    }

    public void onDestroy()
    {
        if(mp != null)
        {
            mp.stop();
            mp = null;
        }
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
        if(thread != null)
        {
            thread.interrupt();
        }
        if(playPauseThread != null)
        {
            playPauseThread.interrupt();
        }
        remoteControlClient = null;
        mHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int j)
    {
        thread = new Thread(new Runnable() {
            public void run()
            {
                try
                {
                    data = (MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
                    if(SongService.currentVersionSupportLockScreenControls)
                    {
                        RegisterRemoteClient();
                    }
                    String s = data.getSongUrl();
                    playSong(s, data);
                    newNotification();
                    return;
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        });
        thread.start();
        PlayerConstants.SONG_CHANGE_HANDLER = new Handler(new android.os.Handler.Callback() {
            public boolean handleMessage(Message message)
            {
                Runnable runnable = new Runnable() {
                    public void run()
                    {
                        Iterator iterator;
                        isMediaPrepared = false;
                        try {
                            data = (MediaItem) PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
                            String s = data.getSongUrl();
                            newNotification();
                            playSong(s, data);
                            iterator = ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningTasks(10).iterator();
                            android.app.ActivityManager.RunningTaskInfo runningtaskinfo;
                            while (iterator.hasNext()) {
                                runningtaskinfo = (android.app.ActivityManager.RunningTaskInfo) iterator.next();
                                if (runningtaskinfo.topActivity.getClassName().equals(Search.class))
                                    Search.changeUI();
                                else if (runningtaskinfo.topActivity.getClassName().equals(Artist.class))
                                    Artist.changeUI();
                                else if (runningtaskinfo.topActivity.getClassName().equals(PlayList.class))
                                    PlayList.changeUI();
                                else if (runningtaskinfo.topActivity.getClassName().equals(RecentSongList.class))
                                    RecentSongList.changeUI();
                                else if (runningtaskinfo.topActivity.getClassName().equals(Songs.class))
                                    Songs.changeUI();
                            }
                            AudioPlayerActivity.changeUI();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                thread = new Thread(runnable);
                thread.start();
                return false;
            }
        });
        PlayerConstants.PLAY_PAUSE_HANDLER = new Handler(new android.os.Handler.Callback() {
            public boolean handleMessage(Message message)
            {
                final String s = (String)message.obj;
                if(SongService.mp == null)
                {
                    return false;
                } else
                {
                    Runnable runnable = new Runnable() {
                        public void run()
                        {
                            try {
                                if (s.equalsIgnoreCase(getResources().getString(R.string.play))) {
                                    PlayerConstants.SONG_PAUSED = false;
                                    if (SongService.currentVersionSupportLockScreenControls) {
                                        RegisterRemoteClient();
                                        SongService.remoteControlClient.setPlaybackState(3);
                                    }
                                    SongService.mp.start();
                                } else if (s.equalsIgnoreCase(getResources().getString(R.string.pause))) {
                                    PlayerConstants.SONG_PAUSED = true;
                                    if (SongService.currentVersionSupportLockScreenControls) {
                                        SongService.remoteControlClient.setPlaybackState(2);
                                    }
                                    SongService.mp.pause();
                                }
                                updateNotification();
                                Iterator iterator = ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningTasks(10).iterator();
                                android.app.ActivityManager.RunningTaskInfo runningtaskinfo;
                                while (iterator.hasNext()) {
                                    runningtaskinfo = (android.app.ActivityManager.RunningTaskInfo) iterator.next();
                                    if (runningtaskinfo.topActivity.getClassName().equals(Search.class))
                                        Search.changeButton();
                                    else if (runningtaskinfo.topActivity.getClassName().equals(Artist.class))
                                        Artist.changeButton();
                                    else if (runningtaskinfo.topActivity.getClassName().equals(PlayList.class))
                                        PlayList.changeButton();
                                    else if (runningtaskinfo.topActivity.getClassName().equals(RecentSongList.class))
                                        RecentSongList.changeButton();
                                    else if (runningtaskinfo.topActivity.getClassName().equals(Songs.class))
                                        Songs.changeButton();
                                }
                                AudioPlayerActivity.changeButton();
                                Log.d("TAG", (new StringBuilder()).append("TAG Pressed: ").append(s).toString());
                            }catch(Exception e){}
                        }
                    };
                    thread = new Thread(runnable);
                    thread.start();
                    return false;
                }
            }
        });
        return START_STICKY;
    }

    public void setListeners(RemoteViews remoteviews)
    {
        try
        {
            Intent intent = new Intent("com.mzdevelopment.musicsearchpro.previous");
            Intent intent1 = new Intent("com.mzdevelopment.musicsearchpro.delete");
            Intent intent2 = new Intent("com.mzdevelopment.musicsearchpro.pause");
            Intent intent3 = new Intent("com.mzdevelopment.musicsearchpro.next");
            Intent intent4 = new Intent("com.mzdevelopment.musicsearchpro.play");
            remoteviews.setOnClickPendingIntent(R.id.btnPrevious, PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0x8000000));
            remoteviews.setOnClickPendingIntent(R.id.btnDelete, PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0x8000000));
            remoteviews.setOnClickPendingIntent(R.id.btnPause, PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, 0x8000000));
            remoteviews.setOnClickPendingIntent(R.id.btnNext, PendingIntent.getBroadcast(getApplicationContext(), 0, intent3, 0x8000000));
            remoteviews.setOnClickPendingIntent(R.id.btnPlay, PendingIntent.getBroadcast(getApplicationContext(), 0, intent4, 0x8000000));
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void updateProgressBar()
    {
        mHandler.postDelayed(mUpdateTimeTask, 100L);
    }
}
