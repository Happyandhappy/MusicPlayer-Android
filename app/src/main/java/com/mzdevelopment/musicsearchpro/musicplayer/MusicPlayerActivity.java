package com.mzdevelopment.musicsearchpro.musicplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.MediaPlayer;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.FastBlur;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.db.recent_get_set;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject;

public class MusicPlayerActivity extends ActionBarActivity
    implements android.media.MediaPlayer.OnCompletionListener, android.widget.SeekBar.OnSeekBarChangeListener
{
    class playMusic extends AsyncTask
    {

        ProgressDialog localProgressDialog;

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        protected JSONObject doInBackground(Void avoid[])
        {
            try
            {
                mp.reset();
                mp.setAudioStreamType(3);
                mp.setDataSource((new StringBuilder()).append(song_url).append("?client_id=4346c8125f4f5c40ad666bacd8e96498").toString());
                mp.prepare();
                mp.start();
            }
            catch(Exception exception)
            {
                return null;
            }
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((JSONObject)obj);
        }

        protected void onPostExecute(JSONObject jsonobject)
        {
            super.onPostExecute(jsonobject);
            localProgressDialog.dismiss();
            btnPlay.setImageResource(R.drawable.pause);
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
            updateProgressBar();
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            localProgressDialog = ProgressDialog.show(MusicPlayerActivity.this, getResources().getText(R.string.app_name), getResources().getText(R.string.wait_message), true);
            localProgressDialog.show();
        }

        playMusic()
        {
            super();
            localProgressDialog = null;
        }
    }


    ActionBar actionBar;
    private ImageButton btnBackward;
    private ImageButton btnForward;
    private ImageButton btnNext;
    private ImageButton btnPlay;
    private ImageButton btnPrevious;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private int currentSongIndex;
    DatabaseHelper db;
    ImageView image;
    public ImageLoader imageLoader;
    private boolean isRepeat;
    private boolean isShuffle;
    ImageView iv_song;
    private Handler mHandler;
    private Runnable mUpdateTimeTask;
    private MediaPlayer mp;
    String pagename;
    RelativeLayout rl_main;
    private int seekBackwardTime;
    private int seekForwardTime;
    private TextView songCurrentDurationLabel;
    int songIndex;
    private SongsManager songManager;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songTotalDurationLabel;
    String song_id;
    ArrayList song_id1;
    ArrayList song_image1;
    String song_img;
    String song_title;
    ArrayList song_title1;
    String song_url;
    ArrayList song_url1;
    private ArrayList songsList;
    TextView tv_artist;
    TextView tv_title;
    ArrayList user_name1;
    String username;
    private Utilities utils;

    public MusicPlayerActivity()
    {
        mHandler = new Handler();
        seekForwardTime = 5000;
        seekBackwardTime = 5000;
        currentSongIndex = 0;
        isShuffle = false;
        isRepeat = false;
        songsList = new ArrayList();
        pagename = "";
        song_id1 = new ArrayList();
        song_title1 = new ArrayList();
        song_image1 = new ArrayList();
        song_url1 = new ArrayList();
        user_name1 = new ArrayList();
        mUpdateTimeTask = new Runnable() {
            public void run()
            {
                long l = mp.getDuration();
                long l1 = mp.getCurrentPosition();
                songTotalDurationLabel.setText((new StringBuilder()).append("").append(utils.milliSecondsToTimer(l)).toString());
                songCurrentDurationLabel.setText((new StringBuilder()).append("").append(utils.milliSecondsToTimer(l1)).toString());
                int i = utils.getProgressPercentage(l1, l);
                songProgressBar.setProgress(i);
                mHandler.postDelayed(this, 100L);
            }
        };
    }

    private void applyBlur()
    {
        image.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw()
            {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();
                Bitmap bitmap = image.getDrawingCache();
                if(android.os.Build.VERSION.SDK_INT >= 17)
                {
                    blur(bitmap, rl_main, 10F);
                } else
                {
                    BitmapDrawable bitmapdrawable = new BitmapDrawable(bitmap);
                    bitmapdrawable.setAlpha(127);
                    rl_main.setBackgroundDrawable(bitmapdrawable);
                }
                return true;
            }
        });
    }

    private void blur(Bitmap bitmap, View view, float f)
    {
        System.currentTimeMillis();
        Bitmap bitmap1 = Bitmap.createBitmap((int)((float)view.getMeasuredWidth() / 8F), (int)((float)view.getMeasuredHeight() / 8F), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        canvas.translate((float)(-view.getLeft()) / 8F, (float)(-view.getTop()) / 8F);
        canvas.scale(1.0F / 8F, 1.0F / 8F);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        Bitmap bitmap2 = FastBlur.doBlur(bitmap1, (int)30F, true);
        view.setBackground(new BitmapDrawable(getResources(), bitmap2));
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        if(j == 100)
        {
            currentSongIndex = intent.getExtras().getInt("songIndex");
            playSong(currentSongIndex);
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    public void onCompletion(MediaPlayer mediaplayer)
    {
        if(isRepeat)
        {
            playSong(currentSongIndex);
            return;
        }
        if(isShuffle)
        {
            currentSongIndex = 0 + (new Random()).nextInt(1 + (0 + (-1 + song_id1.size())));
            playSong(currentSongIndex);
            return;
        }
        if(currentSongIndex < -1 + song_id1.size())
        {
            playSong(1 + currentSongIndex);
            currentSongIndex = 1 + currentSongIndex;
            return;
        } else
        {
            playSong(0);
            currentSongIndex = 0;
            return;
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.player);
        setCustomTitle("Album");
        btnPlay = (ImageButton)findViewById(R.id.btnPlay);
        btnForward = (ImageButton)findViewById(R.id.btnForward);
        btnBackward = (ImageButton)findViewById(R.id.btnBackward);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        btnRepeat = (ImageButton)findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton)findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar)findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView)findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView)findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView)findViewById(R.id.songTotalDurationLabel);
        iv_song = (ImageView)findViewById(R.id.iv_song);
        tv_title = (TextView)findViewById(R.id.tv_titles);
        tv_artist = (TextView)findViewById(R.id.tv_artist);
        rl_main = (RelativeLayout)findViewById(R.id.rl_main);
        image = (ImageView)findViewById(R.id.picture);
        db = new DatabaseHelper(getApplicationContext());
        imageLoader = new ImageLoader(getApplicationContext());
        if(getIntent().getExtras() != null)
        {
            song_url = getIntent().getStringExtra("song_url");
            song_id = getIntent().getStringExtra("song_id");
            song_title = getIntent().getStringExtra("song_title");
            song_img = getIntent().getStringExtra("song_image");
            username = getIntent().getStringExtra("username");
            pagename = getIntent().getStringExtra("pagename");
            currentSongIndex = getIntent().getIntExtra("songIndex", 0);
        }
        List list = db.getAllSongs();
        if(list.size() > 0)
        {
            recent_get_set recent_get_set1;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); user_name1.add(recent_get_set1.getUser_name()))
            {
                recent_get_set1 = (recent_get_set)iterator.next();
                song_id1.add(recent_get_set1.getsong_id().toString());
                song_title1.add(recent_get_set1.gettitle().toString());
                song_image1.add(recent_get_set1.getimage());
                song_url1.add(recent_get_set1.geturl());
            }

        }
        tv_title.setText(song_title);
        tv_artist.setText(username);
        imageLoader.DisplayImage(song_img.replace("-large", "-t500x500"), iv_song);
        db.deleteSong(song_id, "recent");
        db.insertResentSongs(song_id, song_title.replaceAll("'", ""), song_img, song_url, "recent", username.replaceAll("'", ""));
        imageLoader.DisplayImage(song_img, image);
        applyBlur();
        mp = new MediaPlayer();
        songManager = new SongsManager();
        utils = new Utilities();
        songProgressBar.setOnSeekBarChangeListener(this);
        mp.setOnCompletionListener(this);
        songsList = songManager.getPlayList();
        btnPlay.setImageResource(R.drawable.pause);
        (new playMusic()).execute(new Void[0]);
        btnPlay.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(mp.isPlaying())
                {
                    if(mp != null)
                    {
                        mp.pause();
                        btnPlay.setImageResource(R.drawable.icon_play_button);
                    }
                } else if(mp != null) {
                    mp.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
            }
        });
        btnForward.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                int i = mp.getCurrentPosition();
                if(i + seekForwardTime <= mp.getDuration())
                {
                    mp.seekTo(i + seekForwardTime);
                    return;
                } else
                {
                    mp.seekTo(mp.getDuration());
                    return;
                }
            }
        });
        btnBackward.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                int i = mp.getCurrentPosition();
                if(i - seekBackwardTime >= 0)
                    mp.seekTo(i - seekBackwardTime);
                else
                    mp.seekTo(0);
            }
        });
        btnNext.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(currentSongIndex < -1 + song_id1.size())
                {
                    playSong(1 + currentSongIndex);
                    currentSongIndex = 1 + currentSongIndex;
                    return;
                } else
                {
                    playSong(0);
                    currentSongIndex = 0;
                    return;
                }
            }
        });
        btnPrevious.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(currentSongIndex > 0)
                {
                    playSong(-1 + currentSongIndex);
                    currentSongIndex = -1 + currentSongIndex;
                    return;
                } else
                {
                    playSong(-1 + song_id1.size());
                    currentSongIndex = -1 + song_id1.size();
                    return;
                }
            }
        });
        btnRepeat.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(isRepeat)
                {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.repeat);
                    return;
                } else
                {
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.repeat);
                    btnShuffle.setImageResource(R.drawable.shuffle);
                    return;
                }
            }
        });
        btnShuffle.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(isShuffle)
                {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.shuffle);
                    return;
                } else
                {
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.shuffle);
                    btnRepeat.setImageResource(R.drawable.repeat);
                    return;
                }
            }
        });
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
    {
    }

    public void onStartTrackingTouch(SeekBar seekbar)
    {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    public void onStopTrackingTouch(SeekBar seekbar)
    {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int i = mp.getDuration();
        int j = utils.progressToTimer(seekbar.getProgress(), i);
        mp.seekTo(j);
        updateProgressBar();
    }

    public void playSong(int i)
    {
        try
        {
            mp.reset();
            mp.setAudioStreamType(3);
            if(!pagename.equals("device"))
            {
                mp.setDataSource((new StringBuilder()).append(song_url).append("?client_id=74b38ce3d412b6e25db87047c7bd4a4b").toString());
            }else {
                mp.setDataSource((new StringBuilder()).append((String) song_url1.get(i)).append("?client_id=74b38ce3d412b6e25db87047c7bd4a4b").toString());
                tv_title.setText((CharSequence) song_title1.get(i));
                tv_artist.setText((CharSequence) user_name1.get(i));
                imageLoader.DisplayImage(((String) song_image1.get(i)).replace("-large", "-t500x500"), iv_song);
                db.deleteSong((String) song_id1.get(i), "recent");
                db.insertResentSongs((String) song_id1.get(i), ((String) song_title1.get(i)).replaceAll("'", ""), (String) song_image1.get(i), (String) song_url1.get(i), "recent", ((String) user_name1.get(i)).replaceAll("'", ""));
                imageLoader.DisplayImage((String) song_image1.get(i), image);
                applyBlur();
            }
            mp.prepare();
            mp.start();
            btnPlay.setImageResource(R.drawable.pause);
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
            updateProgressBar();
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            illegalargumentexception.printStackTrace();
        }
        catch(IllegalStateException illegalstateexception)
        {
            illegalstateexception.printStackTrace();
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
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
                mHandler.removeCallbacks(mUpdateTimeTask);
                mp.release();
                finish();
            }
        });
    }

    public void updateProgressBar()
    {
        mHandler.postDelayed(mUpdateTimeTask, 100L);
    }

/*
    static int access$402(MusicPlayerActivity musicplayeractivity, int i)
    {
        musicplayeractivity.currentSongIndex = i;
        return i;
    }

*/



/*
    static boolean access$502(MusicPlayerActivity musicplayeractivity, boolean flag)
    {
        musicplayeractivity.isRepeat = flag;
        return flag;
    }

*/




/*
    static boolean access$702(MusicPlayerActivity musicplayeractivity, boolean flag)
    {
        musicplayeractivity.isShuffle = flag;
        return flag;
    }

*/


}
