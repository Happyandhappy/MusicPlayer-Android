package com.mzdevelopment.musicsearchpro.musicplayer;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.FastBlur;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.Search;
import com.mzdevelopment.musicsearchpro.controls.Controls;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.receiver.NotificationBroadcast;
import com.mzdevelopment.musicsearchpro.service.SongService;
import com.mzdevelopment.musicsearchpro.utility.*;
import java.util.ArrayList;

public class AudioPlayerActivity extends ActionBarActivity
    implements android.widget.SeekBar.OnSeekBarChangeListener
{

    public static Handler UIHandler = new Handler(Looper.getMainLooper());
    static ImageButton btnBackward;
    static ImageButton btnForward;
    static ImageButton btnNext;
    static ImageButton btnPause;
    static ImageButton btnPlay;
    static ImageButton btnPrevious;
    static ImageButton btnRepeat;
    static ImageButton btnShuffle;
    static Context context;
    static DatabaseHelper db;
    static ImageView image;
    static ImageLoader imageLoader;
    static ImageView iv_song;
    static RelativeLayout rl_main;
    static SeekBar songProgressBar;
    static TextView textAlbumArtist;
    static TextView textBufferDuration;
    static TextView textComposer;
    static TextView textDuration;
    static TextView textNowPlaying;
    ActionBar actionBar;
    private NotificationBroadcast broadcastReceiver;
    int totalDuration;
    private Utilities utils;

    public AudioPlayerActivity()
    {
        broadcastReceiver = new NotificationBroadcast() {
            public void onReceive(Context context1, Intent intent)
            {
                if(intent.getAction().equals("com.mzdevelopment.musicsearchpro.delete"))
                {
                    context1.stopService(new Intent(context1, SongService.class));
                    Intent intent1 = new Intent(context1, Search.class);
                    intent1.setFlags(0x10000000);
                    context1.startActivity(intent1);
                    finish();
                }
            }
        };
    }

    private static void applyBlur()
    {
        image.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw()
            {
                AudioPlayerActivity.image.getViewTreeObserver().removeOnPreDrawListener(this);
                AudioPlayerActivity.image.buildDrawingCache();
                Bitmap bitmap = AudioPlayerActivity.image.getDrawingCache();
                if(android.os.Build.VERSION.SDK_INT >= 17)
                {
                    AudioPlayerActivity.blur(bitmap, AudioPlayerActivity.rl_main, 10F);
                } else
                {
                    BitmapDrawable bitmapdrawable = new BitmapDrawable(bitmap);
                    bitmapdrawable.setAlpha(127);
                    AudioPlayerActivity.rl_main.setBackgroundDrawable(bitmapdrawable);
                }
                return true;
            }

        });
    }

    private static void blur(Bitmap bitmap, View view, float f)
    {
        Bitmap bitmap1 = Bitmap.createBitmap((int)((float)view.getMeasuredWidth() / 8F), (int)((float)view.getMeasuredHeight() / 8F), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        canvas.translate((float)(-view.getLeft()) / 8F, (float)(-view.getTop()) / 8F);
        canvas.scale(1.0F / 8F, 1.0F / 8F);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        Bitmap bitmap2 = FastBlur.doBlur(bitmap1, (int)30F, true);
        view.setBackground(new BitmapDrawable(context.getResources(), bitmap2));
    }

    public static void changeButton()
    {
        runOnUI(new Runnable() {

            public void run()
            {
                if(PlayerConstants.SONG_PAUSED)
                {
                    AudioPlayerActivity.btnPause.setVisibility(View.GONE);
                    AudioPlayerActivity.btnPlay.setVisibility(View.VISIBLE);
                    return;
                } else
                {
                    AudioPlayerActivity.btnPause.setVisibility(View.VISIBLE);
                    AudioPlayerActivity.btnPlay.setVisibility(View.GONE);
                    return;
                }
            }

        });
    }

    public static void changeUI()
    {
        updateUI();
        changeButton();
    }

    private void getViews()
    {
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPause = (ImageButton)findViewById(R.id.btnPause);
        btnPlay = (ImageButton)findViewById(R.id.btnPlay);
        btnForward = (ImageButton)findViewById(R.id.btnForward);
        btnBackward = (ImageButton)findViewById(R.id.btnBackward);
        btnRepeat = (ImageButton)findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton)findViewById(R.id.btnShuffle);
        textNowPlaying = (TextView)findViewById(R.id.tv_titles);
        textAlbumArtist = (TextView)findViewById(R.id.tv_artist);
        rl_main = (RelativeLayout)findViewById(R.id.rl_main);
        iv_song = (ImageView)findViewById(R.id.iv_song);
        image = (ImageView)findViewById(R.id.picture);
        textComposer = (TextView)findViewById(R.id.textComposer);
        songProgressBar = (SeekBar)findViewById(R.id.songProgressBar);
        textBufferDuration = (TextView)findViewById(R.id.songCurrentDurationLabel);
        textDuration = (TextView)findViewById(R.id.songTotalDurationLabel);
        textNowPlaying.setSelected(true);
        textAlbumArtist.setSelected(true);
    }

    private void init()
    {
        getViews();
        setListeners();
        try
        {
            if(UtilFunctions.isServiceRunning(SongService.class.getName(), context))
            {
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }else {
                Intent intent = new Intent(context, SongService.class);
                context.startService(intent);
            }
            PlayerConstants.PROGRESSBAR_HANDLER = new Handler() {
                public void handleMessage(Message message)
                {
                    Integer ainteger[] = (Integer[])(Integer[])message.obj;
                    totalDuration = ainteger[1].intValue();
                    AudioPlayerActivity.textBufferDuration.setText(UtilFunctions.getDuration(ainteger[0].intValue()));
                    AudioPlayerActivity.textDuration.setText(UtilFunctions.getDuration(ainteger[1].intValue()));
                    AudioPlayerActivity.songProgressBar.setProgress(ainteger[2].intValue());
                }
            };
            updateUI();
            changeButton();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void runOnUI(Runnable runnable)
    {
        UIHandler.post(runnable);
    }

    private void setListeners()
    {
        btnPrevious.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Controls.previousControl(getApplicationContext());
            }
        });
        btnNext.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Controls.nextControl(getApplicationContext());
            }
        });
        btnPause.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Controls.pauseControl(getApplicationContext());
            }
        });
        btnPlay.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Controls.playControl(getApplicationContext());
            }
        });
        btnRepeat.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(PlayerConstants.isRepeat)
                {
                    PlayerConstants.isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    AudioPlayerActivity.btnRepeat.setImageResource(R.drawable.repeat);
                    return;
                } else
                {
                    PlayerConstants.isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    PlayerConstants.isShuffle = false;
                    AudioPlayerActivity.btnRepeat.setImageResource(R.drawable.repeat);
                    AudioPlayerActivity.btnShuffle.setImageResource(R.drawable.shuffle);
                    return;
                }
            }
        });
        btnShuffle.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(PlayerConstants.isShuffle)
                {
                    PlayerConstants.isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    AudioPlayerActivity.btnShuffle.setImageResource(R.drawable.shuffle);
                    return;
                } else
                {
                    PlayerConstants.isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    PlayerConstants.isRepeat = false;
                    AudioPlayerActivity.btnShuffle.setImageResource(R.drawable.shuffle);
                    AudioPlayerActivity.btnRepeat.setImageResource(R.drawable.repeat);
                    return;
                }
            }
        });
    }

    private static void updateUI()
    {
        runOnUI(new Runnable() {

            public void run()
            {
                try
                {
                    String s = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongId();
                    String s1 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongTitle();
                    String s2 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongImage();
                    String s3 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongUrl();
                    String s4 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getArtist();
                    AudioPlayerActivity.imageLoader.DisplayImage(s2, AudioPlayerActivity.image);
                    AudioPlayerActivity.imageLoader.DisplayImage(s2.replace("-large", "-t500x500"), AudioPlayerActivity.iv_song);
                    AudioPlayerActivity.db.deleteSong(s, "recent");
                    AudioPlayerActivity.db.insertResentSongs(s, s1.replaceAll("'", ""), s2, s3, "recent", s4.replaceAll("'", ""));
                    AudioPlayerActivity.textNowPlaying.setText(s1);
                    AudioPlayerActivity.textAlbumArtist.setText(s4);
                    AudioPlayerActivity.applyBlur();
                    return;
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }

        });
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.player);
        setCustomTitle("Album");
        context = this;
        db = new DatabaseHelper(getApplicationContext());
        imageLoader = new ImageLoader(getApplicationContext());
        utils = new Utilities();
        init();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        PlayerConstants.PROGRESSBAR_HANDLER.removeCallbacksAndMessages(null);
    }

    public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
    {
    }

    protected void onResume()
    {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.mzdevelopment.musicsearchpro.delete"));
    }

    public void onStartTrackingTouch(SeekBar seekbar)
    {
        PlayerConstants.PROGRESSBAR_CHANGE_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_CHANGE_HANDLER.obtainMessage(1, Integer.valueOf(0)));
    }

    public void onStopTrackingTouch(SeekBar seekbar)
    {
        PlayerConstants.PROGRESSBAR_CHANGE_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_CHANGE_HANDLER.obtainMessage(1, Integer.valueOf(0)));
        int i = utils.progressToTimer(seekbar.getProgress(), totalDuration);
        try
        {
            PlayerConstants.PROGRESSBAR_CHANGE_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_CHANGE_HANDLER.obtainMessage(0, Integer.valueOf(i)));
            return;
        }
        catch(Exception exception)
        {
            return;
        }
    }

    protected void setCustomTitle(String s)
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ColorDrawable colordrawable = new ColorDrawable(Color.parseColor("#4caf50"));
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setBackgroundDrawable(colordrawable);
        View view = actionBar.getCustomView();
        ((TextView)view.findViewById(R.id.tv_title)).setText(s);
        ImageView imageview = (ImageView)view.findViewById(R.id.iv_left);
        imageview.setImageResource(R.drawable.previous);
        imageview.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                finish();
            }
        });
    }
}
