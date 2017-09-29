package com.mzdevelopment.musicsearchpro;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.controls.Controls;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.db.recent_get_set;
import com.mzdevelopment.musicsearchpro.service.SongService;
import com.mzdevelopment.musicsearchpro.ui.adapter.AllSongsListAdapter;
import com.mzdevelopment.musicsearchpro.ui.adapter.ArtistListAdapter;
import com.mzdevelopment.musicsearchpro.utility.MediaItem;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;
import com.mzdevelopment.musicsearchpro.utility.UtilFunctions;
import java.util.*;

public class Artist extends ActionBarActivity
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
    String file_url;
    String flag;
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
    ArrayList playlist;
    String query;
    RelativeLayout rl_main;
    ArrayList search_song_id;
    ArrayList search_song_image;
    ArrayList search_song_title;
    ArrayList search_song_url;
    ArrayList search_user_name;
    ArrayList song_id;
    ArrayList song_image;
    ArrayList song_title;
    ArrayList song_url;
    TextView tv_Norecords;
    TextView tv_cloud;
    TextView tv_device;
    ArrayList user_name;

    public Artist()
    {
        mPosition = -1;
        mTitle = "";
        query = "";
        playlist = new ArrayList();
        flag = "cloud";
        song_id = new ArrayList();
        song_title = new ArrayList();
        song_image = new ArrayList();
        song_url = new ArrayList();
        Count = new ArrayList();
        user_name = new ArrayList();
        search_song_id = new ArrayList();
        search_song_title = new ArrayList();
        search_song_image = new ArrayList();
        search_song_url = new ArrayList();
        search_user_name = new ArrayList();
    }

    private static void applyBlur()
    {
        iv_image.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw()
            {
                Artist.iv_image.getViewTreeObserver().removeOnPreDrawListener(this);
                Artist.iv_image.buildDrawingCache();
                Bitmap bitmap = Artist.iv_image.getDrawingCache();
                if(android.os.Build.VERSION.SDK_INT >= 17)
                {
                    Artist.blur(bitmap, Artist.toplayout, 10F);
                } else {
                    BitmapDrawable bitmapdrawable = new BitmapDrawable(bitmap);
                    bitmapdrawable.setAlpha(127);
                    Artist.toplayout.setBackgroundDrawable(bitmapdrawable);
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
                    Artist.iv_pause.setVisibility(View.GONE);
                    Artist.iv_play.setVisibility(View.VISIBLE);
                    Artist.tv_now_playing.setVisibility(View.GONE);
                    return;
                } else
                {
                    Artist.iv_pause.setVisibility(View.VISIBLE);
                    Artist.iv_play.setVisibility(View.GONE);
                    Artist.tv_now_playing.setVisibility(View.VISIBLE);
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

    private void clearList()
    {
        song_title.clear();
        song_image.clear();
        song_url.clear();
        song_id.clear();
    }

    private void fillData()
    {
        String as[] = (String[])song_title.toArray(new String[song_title.size()]);
        String as1[] = (String[])song_image.toArray(new String[song_image.size()]);
        String as2[] = (String[])song_url.toArray(new String[song_url.size()]);
        String as3[] = (String[])song_id.toArray(new String[song_id.size()]);
        String as4[] = (String[])user_name.toArray(new String[user_name.size()]);
        String as5[] = (String[])Count.toArray(new String[Count.size()]);
        lstview.setAdapter(new ArtistListAdapter(this, as, as1, as2, as3, "artist", as4, as5));
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
                    Artist.imageLoader.DisplayImage(s1, Artist.iv_image);
                    Artist.imageLoader.DisplayImageBitmap(s1, Artist.iv_round, "");
                    Artist.tv_title.setText(s);
                    Artist.tv_artist.setText(s2);
                    Artist.applyBlur();
                } catch(Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
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

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.search);
        setCustomTitle("Artist");
        context = this;
        imageLoader = new ImageLoader(getApplicationContext());
        appPref = new AppPreferences(getApplicationContext());
        db = new DatabaseHelper(getApplicationContext());
        lstview = (ListView)findViewById(0x102000a);
        tv_Norecords = (TextView)findViewById(R.id.tv_norecords);
        tv_cloud = (TextView)findViewById(R.id.tv_cloud);
        tv_device = (TextView)findViewById(R.id.tv_device);
        setDrawer();
        clearList();
        List list = db.getGroupArtistSongs();
        if(list.size() > 0)
        {
            lstview.setVisibility(View.VISIBLE);
            tv_Norecords.setVisibility(View.GONE);
            int i;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); Count.add(String.valueOf(i)))
            {
                recent_get_set recent_get_set1 = (recent_get_set)iterator.next();
                song_id.add(recent_get_set1.getsong_id().toString());
                song_title.add(recent_get_set1.gettitle().toString());
                song_image.add(recent_get_set1.getimage());
                user_name.add(recent_get_set1.getUser_name());
                song_url.add(recent_get_set1.geturl());
                i = db.getArtistCount(recent_get_set1.getUser_name());
            }

            fillData();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchview = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchview.setSearchableInfo(((SearchManager)getSystemService(SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchview.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String s)
            {
                String as[] = (String[])song_title.toArray(new String[song_title.size()]);
                String as1[] = (String[])song_id.toArray(new String[song_id.size()]);
                String as2[] = (String[])song_image.toArray(new String[song_image.size()]);
                String as3[] = (String[])song_url.toArray(new String[song_url.size()]);
                String as4[] = (String[])user_name.toArray(new String[user_name.size()]);
                search_song_id.clear();
                search_song_title.clear();
                search_song_image.clear();
                search_song_url.clear();
                search_user_name.clear();
                for(int i = 0; i < as.length; i++)
                {
                    if(as[i].toString().toLowerCase().contains(s) || as4[i].toString().toLowerCase().contains(s))
                    {
                        search_song_id.add(as1[i]);
                        search_song_title.add(as[i]);
                        search_song_image.add(as2[i]);
                        search_song_url.add(as3[i]);
                        search_user_name.add(as4[i]);
                    }
                }

                if(search_song_id.size() == 0)
                {
                    tv_Norecords.setVisibility(View.VISIBLE);
                    lstview.setVisibility(View.GONE);
                } else
                {
                    tv_Norecords.setVisibility(View.GONE);
                    lstview.setVisibility(View.VISIBLE);
                    String as5[] = (String[])search_song_title.toArray(new String[search_song_title.size()]);
                    String as6[] = (String[])search_song_image.toArray(new String[search_song_image.size()]);
                    String as7[] = (String[])search_song_url.toArray(new String[search_song_url.size()]);
                    String as8[] = (String[])search_song_id.toArray(new String[search_song_id.size()]);
                    String as9[] = (String[])search_user_name.toArray(new String[search_user_name.size()]);
                    lstview.setAdapter(new AllSongsListAdapter(Artist.this, as5, as6, as7, as8, "search", as9));
                }
                return true;
            }

            public boolean onQueryTextSubmit(String s)
            {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        if(!mDrawerToggle.onOptionsItemSelected(menuitem)) {
            switch(menuitem.getItemId())
            {
                default:
                    return super.onOptionsItemSelected(menuitem);

                case R.id.action_search:
                    break;
            }
        }
        return true;
    }

    protected void onPostCreate(Bundle bundle)
    {
        super.onPostCreate(bundle);
        mDrawerToggle.syncState();
    }

    protected void onResume()
    {
        super.onResume();
        if(UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext()))
        {
            updateUI();
        }
        changeButton();
    }

    public boolean onSearchRequested()
    {
        return super.onSearchRequested();
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
                    Intent intent = new Intent(Artist.this, PlayList.class);
                    intent.putExtra("pagename", "chart");
                    startActivity(intent);
                    finish();
                } else if(j == 1) {
                    Intent intent1 = new Intent(Artist.this, Search.class);
                    startActivity(intent1);
                } else if(j == 2) {
                    Intent intent2 = new Intent(Artist.this, RecentSongList.class);
                    intent2.putExtra("playlist", "recent");
                    startActivity(intent2);
                    finish();
                } else if(j == 3) {
                    Intent intent3 = new Intent(Artist.this, Songs.class);
                    startActivity(intent3);
                    finish();
                } else if(j == 4) {
                    Intent intent4 = new Intent(Artist.this, Artist.class);
                    startActivity(intent4);
                    finish();
                } else if(j == 5) {
                    Intent intent5 = new Intent(Artist.this, PlayList.class);
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
