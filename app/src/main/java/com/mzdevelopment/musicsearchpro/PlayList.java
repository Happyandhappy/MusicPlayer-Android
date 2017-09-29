package com.mzdevelopment.musicsearchpro;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.controls.Controls;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.db.playlist_get_set;
import com.mzdevelopment.musicsearchpro.service.SongService;
import com.mzdevelopment.musicsearchpro.ui.adapter.ChartListAdapter;
import com.mzdevelopment.musicsearchpro.ui.adapter.PlayListAdapter;
import com.mzdevelopment.musicsearchpro.utility.MediaItem;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;
import com.mzdevelopment.musicsearchpro.utility.UtilFunctions;
import java.util.*;

public class PlayList extends ActionBarActivity
{

    static final int RC_REQUEST = 10001;
    static final String TAG = "Drive";
    public static Handler UIHandler = new Handler(Looper.getMainLooper());
    static Context context;
    static ImageLoader imageLoader;
    static ImageView iv_image;
    static ImageView iv_pause;
    static ImageView iv_play;
    static ImageView iv_round;
    static RelativeLayout toplayout;
    static TextView tv_artist;
    static TextView tv_now_playing;
    static TextView tv_title;
    private final String COUNT = "count";
    private final String COUNTRY = "country";
    ArrayList Count;
    private final String FLAG = "flag";
    private ActionBar actionBar;
    AppPreferences appPref;
    DatabaseHelper db;
    int icons[] = {
        R.drawable.alternative_rock, R.drawable.ambient, R.drawable.classical, R.drawable.country, 
		R.drawable.dance_edm, R.drawable.dancehall, R.drawable.deephouse, R.drawable.disco, 
		R.drawable.drum_bass, R.drawable.dubstep, R.drawable.electronic, R.drawable.folk, 
		R.drawable.hip_hop_rap, R.drawable.house, R.drawable.indie, R.drawable.jazz_and_blues, 
		R.drawable.latin, R.drawable.metal, R.drawable.piano, R.drawable.pop, 
        R.drawable.r_b_soul, R.drawable.reggae, R.drawable.reggaeton, R.drawable.rock, 
		R.drawable.soundtrack, R.drawable.techno, R.drawable.trance, R.drawable.trap, 
		R.drawable.trip_hop, R.drawable.world
    };
    String image1;
    String image2;
    String image3;
    ArrayList imagelist;
    LinearLayout ll_main;
    RelativeLayout ll_top;
    ListView lstview;
    private SimpleAdapter mAdapter;
    String mCount[] = {
        "", "", "", "", "", "", "", "", "", ""
    };
    String mCountries[];
    private LinearLayout mDrawer;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    int mFlags[] = {
        R.drawable.menu_charts, R.drawable.menu_search, R.drawable.menu_recents, R.drawable.menu_songs, R.drawable.menu_artists, R.drawable.menu_playlists
    };
    private List mList;
    int mPosition;
    String mTitle;
    String pagename;
    ArrayList playlist;
    String query;
    RelativeLayout rl_main;
    ArrayList song_id;
    ArrayList song_image;
    ArrayList song_title;
    ArrayList song_url;
    TextView tv_Norecords;

    public PlayList()
    {
        song_id = new ArrayList();
        song_title = new ArrayList();
        song_image = new ArrayList();
        song_url = new ArrayList();
        imagelist = new ArrayList();
        mPosition = -1;
        mTitle = "";
        query = "";
        playlist = new ArrayList();
        Count = new ArrayList();
        image1 = "";
        image2 = "";
    }

    private static void applyBlur()
    {
        iv_image.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw()
            {
                PlayList.iv_image.getViewTreeObserver().removeOnPreDrawListener(this);
                PlayList.iv_image.buildDrawingCache();
                Bitmap bitmap = PlayList.iv_image.getDrawingCache();
                if(android.os.Build.VERSION.SDK_INT >= 17)
                {
                    PlayList.blur(bitmap, PlayList.toplayout, 10F);
                } else
                {
                    BitmapDrawable bitmapdrawable = new BitmapDrawable(bitmap);
                    bitmapdrawable.setAlpha(127);
                    PlayList.toplayout.setBackgroundDrawable(bitmapdrawable);
                }
                return true;
            }

        });
    }

    private static void blur(Bitmap bitmap, View view, float f)
    {
        System.currentTimeMillis();
        Bitmap bitmap1 = Bitmap.createBitmap((int)((float)view.getMeasuredWidth() / 8F), (int)((float)view.getMeasuredHeight() / 8F), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        canvas.translate((float)(-view.getLeft()) / 8F, (float)(-view.getTop()) / 8F);
        canvas.scale(1.0F / 8F, 1.0F / 8F);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        Bitmap bitmap2 = FastBlur.doBlur(bitmap1, (int)2.0F, true);
        view.setBackground(new BitmapDrawable(context.getResources(), bitmap2));
    }

    public static void changeButton()
    {
        runOnUI(new Runnable() {

            public void run()
            {
                if(PlayerConstants.SONG_PAUSED)
                {
                    PlayList.iv_pause.setVisibility(View.GONE);
                    PlayList.iv_play.setVisibility(View.VISIBLE);
                    PlayList.tv_now_playing.setVisibility(View.GONE);
                    return;
                } else
                {
                    PlayList.iv_pause.setVisibility(View.VISIBLE);
                    PlayList.iv_play.setVisibility(View.GONE);
                    PlayList.tv_now_playing.setVisibility(View.VISIBLE);
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

    public static Bitmap getclip(Bitmap bitmap)
    {
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap1.getWidth() / 2, bitmap1.getHeight() / 2, bitmap1.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap1, rect, rect, paint);
        return bitmap2;
    }

    public static void runOnUI(Runnable runnable)
    {
        UIHandler.post(runnable);
    }

    private static void updateUI()
    {
        runOnUI(new Runnable() {

            public void run()
            {
                try
                {
                    ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongId();
                    String s = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongTitle();
                    String s1 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongImage();
                    ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongUrl();
                    String s2 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getArtist();
                    PlayList.imageLoader.DisplayImage(s1, PlayList.iv_image);
                    PlayList.imageLoader.DisplayImageBitmap(s1, PlayList.iv_round, "");
                    PlayList.tv_title.setText(s);
                    PlayList.tv_artist.setText(s2);
                    PlayList.applyBlur();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }

        });
    }

    public void ClearList()
    {
        song_id.clear();
        song_title.clear();
        song_image.clear();
        song_url.clear();
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

    public void getPlayList()
    {
        ClearList();
        List list = db.getPlaylist();
        if(list.size() > 0)
        {
            lstview.setVisibility(View.VISIBLE);
            tv_Norecords.setVisibility(View.GONE);
            int i;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); Count.add(String.valueOf(i)))
            {
                playlist_get_set playlist_get_set1 = (playlist_get_set)iterator.next();
                song_title.add(playlist_get_set1.getPlaylist_name().toString());
                i = db.getCount(playlist_get_set1.getPlaylist_name().toString());
            }

            String as[] = (String[])song_title.toArray(new String[song_title.size()]);
            String as1[] = (String[])Count.toArray(new String[Count.size()]);
            lstview.setAdapter(new PlayListAdapter(this, as, "playlist", as1));
            return;
        } else
        {
            lstview.setVisibility(View.GONE);
            tv_Norecords.setVisibility(View.VISIBLE);
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.search);
        setCustomTitle("Search");
        context = this;
        imageLoader = new ImageLoader(getApplicationContext());
        appPref = new AppPreferences(getApplicationContext());
        lstview = (ListView)findViewById(0x102000a);
        tv_Norecords = (TextView)findViewById(R.id.tv_norecords);
        db = new DatabaseHelper(getApplicationContext());
        setDrawer();
        if(getIntent().getExtras() != null)
        {
            pagename = getIntent().getStringExtra("pagename");
            if(!pagename.equalsIgnoreCase("chart"))
            {
                setCustomTitle("Playlist");
                getPlayList();
                return;
            }
            setCustomTitle("Chart");
            ClearList();
            song_title.add("Alternative Rock");
            song_title.add("Ambient");
            song_title.add("Classical");
            song_title.add("Country");
            song_title.add("Dance & EDM");
            song_title.add("Dancehall");
            song_title.add("Deep House");
            song_title.add("Disco");
            song_title.add("Drum & Bass");
            song_title.add("Dubstep");
            song_title.add("Electronic");
            song_title.add("Folk & Singer-Songwriter");
            song_title.add("Hip Hop & Rap");
            song_title.add("House");
            song_title.add("Indie");
            song_title.add("Jazz & Blues");
            song_title.add("Latin");
            song_title.add("Metal");
            song_title.add("Piano");
            song_title.add("Pop");
            song_title.add("R&B & Soul");
            song_title.add("Reggae");
            song_title.add("Reggaeton");
            song_title.add("Rock");
            song_title.add("Soundtrack");
            song_title.add("Techno");
            song_title.add("Trance");
            song_title.add("Trap");
            song_title.add("Trip Hop");
            song_title.add("World");
            String as[] = (String[])song_title.toArray(new String[song_title.size()]);
            String as1[] = (String[])Count.toArray(new String[Count.size()]);
            lstview.setAdapter(new ChartListAdapter(this, as, "chart", as1, icons));
        }
    }

    protected void onResume()
    {
        super.onResume();
        if(UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext()))
            updateUI();
        changeButton();
    }

    void saveDataone()
    {
        appPref.setIsUnlockAppPaid(1);
        Toast.makeText(getBaseContext(), "Successful", Toast.LENGTH_LONG).show();
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
        ((ImageView)view.findViewById(R.id.iv_left)).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                if(mDrawerLayout.isDrawerOpen(mDrawer))
                    mDrawerLayout.closeDrawer(mDrawer);
                else
                    mDrawerLayout.openDrawer(mDrawer);
            }
        });
    }

    public void setDrawer()
    {
        mDrawer = (LinearLayout)findViewById(R.id.drawer);
        mCountries = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerList = (ListView)findViewById(R.id.drawer_list);
        ll_top = (RelativeLayout)findViewById(R.id.ll_top);
        toplayout = (RelativeLayout)findViewById(R.id.toplayout);
        iv_image = (ImageView)findViewById(R.id.iv_image);
        iv_round = (ImageView)findViewById(R.id.iv_round);
        tv_now_playing = (TextView)findViewById(R.id.tv_now_playing);
        tv_artist = (TextView)findViewById(R.id.tv_artist);
        tv_title = (TextView)findViewById(R.id.tv_title);
        iv_play = (ImageView)findViewById(R.id.iv_play);
        iv_pause = (ImageView)findViewById(R.id.iv_pause);
        iv_image.setImageResource(R.drawable.app_icon);
        applyBlur();
        Bitmap bitmap = getclip(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon));
        iv_round.setImageBitmap(bitmap);
        iv_pause.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Controls.pauseControl(getApplicationContext());
            }
        });
        iv_play.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Controls.playControl(getApplicationContext());
            }
        });
        mList = new ArrayList();
        for(int i = 0; i < 6; i++)
        {
            HashMap hashmap = new HashMap();
            hashmap.put("country", mCountries[i]);
            hashmap.put("count", mCount[i]);
            hashmap.put("flag", Integer.toString(mFlags[i]));
            mList.add(hashmap);
        }

        String as[] = {
            "flag", "country", "count"
        };
        int ai[] = {
            R.id.flag, R.id.country, R.id.count
        };
        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, as, ai);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view)
            {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View view)
            {
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int j, long l)
            {
                if(j == 0) {
                    Intent intent = new Intent(PlayList.this, PlayList.class);
                    intent.putExtra("pagename", "chart");
                    startActivity(intent);
                    finish();
                } else if(j == 1) {
                    Intent intent1 = new Intent(PlayList.this, Search.class);
                    startActivity(intent1);
                } else if(j == 2) {
                    Intent intent2 = new Intent(PlayList.this, RecentSongList.class);
                    intent2.putExtra("playlist", "recent");
                    startActivity(intent2);
                    finish();
                } else if(j == 3) {
                    Intent intent3 = new Intent(PlayList.this, Songs.class);
                    startActivity(intent3);
                    finish();
                } else if(j == 4) {
                    Intent intent4 = new Intent(PlayList.this, Artist.class);
                    startActivity(intent4);
                    finish();
                } else if(j == 5) {
                    Intent intent5 = new Intent(PlayList.this, PlayList.class);
                    intent5.putExtra("pagename", "playlist");
                    startActivity(intent5);
                    finish();
                }
                mDrawerLayout.closeDrawer(mDrawer);
            }
        });
        mDrawerList.setAdapter(mAdapter);
    }
}
