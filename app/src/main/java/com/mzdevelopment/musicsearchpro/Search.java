package com.mzdevelopment.musicsearchpro;

import android.app.*;
import android.content.*;
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
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.adapter.CustomAdapter;
import com.mzdevelopment.musicsearchpro.controls.Controls;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.db.playlist_get_set;
import com.mzdevelopment.musicsearchpro.musicplayer.AudioPlayerActivity;
import com.mzdevelopment.musicsearchpro.service.SongService;
import com.mzdevelopment.musicsearchpro.ui.adapter.PlayListAdapter;
import com.mzdevelopment.musicsearchpro.util.Utilities;
import com.mzdevelopment.musicsearchpro.utility.MediaItem;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;
import com.mzdevelopment.musicsearchpro.utility.UtilFunctions;
import com.mzdevelopment.musicsearchpro.volley.MyVolleyHelper;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.*;
import org.json.*;

public class Search extends ActionBarActivity
{
    public static class Utility
    {

        public static void setDynamicHeight(ListView listview)
        {
            ListAdapter listadapter = listview.getAdapter();
            if(listadapter == null)
            {
                return;
            }
            int i = 0;
            int j = android.view.View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for(int k = 0; k < listadapter.getCount(); k++)
            {
                View view = listadapter.getView(k, null, listview);
                view.setLayoutParams(new android.view.ViewGroup.LayoutParams(-2, -2));
                view.measure(j, 0);
                i += view.getMeasuredHeight();
            }

            android.view.ViewGroup.LayoutParams layoutparams = listview.getLayoutParams();
            layoutparams.height = i + listview.getDividerHeight() * (-1 + listadapter.getCount());
            listview.setLayoutParams(layoutparams);
            listview.requestLayout();
        }

        public Utility()
        {
        }
    }


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
    TableLayout TableLayout01;
    private ActionBar actionBar;
    AppPreferences appPref;
    CustomAdapter customAdapter;
    DatabaseHelper db;
    String file_url;
    String file_url1;
    String flag;
    LinearLayout ll_main;
    RelativeLayout ll_top;
    ListView lstview;
    ListView lstview1;
    ListView lstview2;
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
    Handler mHandler;
    public InterstitialAd mInterstitialAd;
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
    ArrayList search_username;
    ArrayList song_id;
    ArrayList song_image;
    ArrayList song_title;
    ArrayList song_title1;
    ArrayList song_url;
    TextView tv_Norecords;
    TextView tv_cloud;
    TextView tv_device;
    ArrayList user_name;
    TextView view1;
    TextView view2;

    public Search()
    {
        mPosition = -1;
        mTitle = "";
        query = "";
        playlist = new ArrayList();
        Count = new ArrayList();
        song_title1 = new ArrayList();
        flag = "cloud";
        song_id = new ArrayList();
        song_title = new ArrayList();
        song_image = new ArrayList();
        song_url = new ArrayList();
        user_name = new ArrayList();
        search_song_id = new ArrayList();
        search_song_title = new ArrayList();
        search_song_image = new ArrayList();
        search_song_url = new ArrayList();
        search_username = new ArrayList();
        customAdapter = null;
        mHandler = new Handler();
    }

    private static void applyBlur()
    {
        iv_image.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw()
            {
                Search.iv_image.getViewTreeObserver().removeOnPreDrawListener(this);
                Search.iv_image.buildDrawingCache();
                Bitmap bitmap = Search.iv_image.getDrawingCache();
                if(android.os.Build.VERSION.SDK_INT >= 17)
                {
                    Search.blur(bitmap, Search.toplayout, 10F);
                } else
                {
                    BitmapDrawable bitmapdrawable = new BitmapDrawable(bitmap);
                    bitmapdrawable.setAlpha(127);
                    Search.toplayout.setBackgroundDrawable(bitmapdrawable);
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
                    Search.iv_pause.setVisibility(View.GONE);
                    Search.iv_play.setVisibility(View.VISIBLE);
                    Search.tv_now_playing.setVisibility(View.GONE);
                    return;
                } else
                {
                    Search.iv_pause.setVisibility(View.VISIBLE);
                    Search.iv_play.setVisibility(View.GONE);
                    Search.tv_now_playing.setVisibility(View.VISIBLE);
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

    private void clearList()
    {
        song_title.clear();
        song_image.clear();
        song_url.clear();
        song_id.clear();
        user_name.clear();
    }

    private void fetchData()
    {
        final ProgressDialog localProgressDialog = ProgressDialog.show(this, getResources().getText(R.string.app_name), getResources().getText(R.string.wait_message), true);
        localProgressDialog.show();
        JsonArrayRequest jsonarrayrequest = new JsonArrayRequest(0, appPref.getPopularUrl(), null, new com.android.volley.Response.Listener<JSONArray>() {
            public void onResponse(JSONArray jsonarray)
            {
                ArrayList arraylist;
                lstview.setVisibility(View.VISIBLE);
                tv_Norecords.setVisibility(View.GONE);
                clearList();
                arraylist = new ArrayList();
                if(jsonarray != null) {
                    try {
                        int i = 0;
                        while (i < jsonarray.length()) {
                            MediaItem mediaitem;
                            JSONObject jsonobject;
                            JSONObject jsonobject1;
                            mediaitem = new MediaItem();
                            jsonobject = jsonarray.getJSONObject(i);
                            jsonobject1 = jsonobject.getJSONObject("user");
                            if (jsonobject.has("stream_url") && !jsonobject.isNull("stream_url")) {
                                song_id.add(jsonobject.getString("id"));
                                song_url.add(jsonobject.getString("stream_url"));
                                mediaitem.setSongId(jsonobject.getString("id"));
                                mediaitem.setSongUrl(jsonobject.getString("stream_url"));
                                if (!jsonobject.getString("title").contains("-")) {
                                    song_title.add(jsonobject.getString("title").trim());
                                    user_name.add("Unknown");
                                    mediaitem.setSongTitle(jsonobject.getString("title").trim());
                                    mediaitem.setArtist("Unknown");
                                } else {
                                    String as[] = jsonobject.getString("title").split("-");
                                    song_title.add(as[1].trim());
                                    user_name.add(as[0].trim());
                                    mediaitem.setSongTitle(as[1].trim());
                                    mediaitem.setArtist(as[0].trim());
                                }
                                if (jsonobject.getString("artwork_url") != null && !String.valueOf(jsonobject.getString("artwork_url")).equals("")) {
                                    song_image.add(jsonobject.getString("artwork_url"));
                                    mediaitem.setSongImage(jsonobject.getString("artwork_url"));
                                } else {
                                    song_image.add(jsonobject1.getString("avatar_url"));
                                    mediaitem.setSongImage(jsonobject1.getString("avatar_url"));
                                }
                                arraylist.add(mediaitem);
                            }
                            i++;
                        }
                        if (localProgressDialog.isShowing()) {
                            localProgressDialog.dismiss();
                        }
                        PlayerConstants.SONGS_LIST_TEMP = arraylist;
                        fillData();
                        return;
                    } catch (JSONException jsonexception) {
                        if (localProgressDialog.isShowing()) {
                            localProgressDialog.dismiss();
                        }
                        jsonexception.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyerror)
            {
                if(localProgressDialog.isShowing())
                {
                    localProgressDialog.dismiss();
                }
            }
        }) {
            public Map getHeaders()
                throws AuthFailureError
            {
                return new HashMap();
            }
        };
        MyVolleyHelper.getIntance(this).addRequestToQueue(jsonarrayrequest);
    }

    private void fetchSearchData()
    {
        final ProgressDialog localProgressDialog = ProgressDialog.show(this, getResources().getText(R.string.app_name), getResources().getText(R.string.wait_message), true);
        localProgressDialog.show();
        try
        {
            JsonArrayRequest jsonarrayrequest = new JsonArrayRequest(0, appPref.getSearchUrl()+"&q="+URLEncoder.encode(query, "UTF-8"), null, new com.android.volley.Response.Listener<JSONArray>() {
                public void onResponse(JSONArray jsonarray)
                {
                    ArrayList arraylist;
                    clearList();
                    arraylist = new ArrayList();
                    if(jsonarray != null) {
                        try {
                            lstview.setVisibility(View.VISIBLE);
                            tv_Norecords.setVisibility(View.GONE);
                            int i = 0;
                            while (i < jsonarray.length()) {
                                MediaItem mediaitem;
                                JSONObject jsonobject;
                                JSONObject jsonobject1;
                                mediaitem = new MediaItem();
                                jsonobject = jsonarray.getJSONObject(i);
                                jsonobject1 = jsonobject.getJSONObject("user");
                                if (jsonobject.has("stream_url") && !jsonobject.isNull("stream_url")) {
                                    song_id.add(jsonobject.getString("id"));
                                    song_url.add(jsonobject.getString("stream_url"));
                                    mediaitem.setSongId(jsonobject.getString("id"));
                                    mediaitem.setSongUrl(jsonobject.getString("stream_url"));
                                    if (!jsonobject.getString("title").contains("-")) {
                                        song_title.add(jsonobject.getString("title").trim());
                                        user_name.add("Unknown");
                                        mediaitem.setSongTitle(jsonobject.getString("title").trim());
                                        mediaitem.setArtist("Unknown");
                                    } else {
                                        String as[] = jsonobject.getString("title").split("-");
                                        song_title.add(as[1].trim());
                                        user_name.add(as[0].trim());
                                        mediaitem.setSongTitle(as[1].trim());
                                        mediaitem.setArtist(as[0].trim());
                                    }
                                    if (jsonobject.getString("artwork_url") != null && !String.valueOf(jsonobject.getString("artwork_url")).equals("")) {
                                        song_image.add(jsonobject.getString("artwork_url"));
                                        mediaitem.setSongImage(jsonobject.getString("artwork_url"));
                                    } else {
                                        song_image.add(jsonobject1.getString("avatar_url"));
                                        mediaitem.setSongImage(jsonobject1.getString("avatar_url"));
                                    }
                                    arraylist.add(mediaitem);
                                }
                                i++;
                            }
                            if (localProgressDialog.isShowing()) {
                                localProgressDialog.dismiss();
                            }
                            PlayerConstants.SONGS_LIST_TEMP = arraylist;
                            fillData();
                        } catch (JSONException jsonexception) {
                            if (localProgressDialog.isShowing()) {
                                localProgressDialog.dismiss();
                            }
                            jsonexception.printStackTrace();
                        }
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyerror)
                {
                    if(localProgressDialog.isShowing())
                    {
                        localProgressDialog.dismiss();
                    }
                }
            }) {
                public Map getHeaders()
                    throws AuthFailureError
                {
                    return new HashMap();
                }
            };
            MyVolleyHelper.getIntance(this).addRequestToQueue(jsonarrayrequest);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void fillData()
    {
        if(PlayerConstants.SONGS_LIST_TEMP.size() <= 0)
        {
            TableLayout01.setVisibility(View.GONE);
        } else
        {
            TableLayout01.setVisibility(View.VISIBLE);
        }
        if(appPref.getFlag().equals("device"))
        {
            rl_main.setVisibility(View.GONE);
            ll_main.setVisibility(View.VISIBLE);
            customAdapter = new CustomAdapter(this, R.layout.custom_list, PlayerConstants.SONGS_LIST_TEMP, "search");
            lstview1.setAdapter(customAdapter);
            lstview1.setFastScrollEnabled(true);
            Utility.setDynamicHeight(lstview1);
            customAdapter = new CustomAdapter(this, R.layout.custom_list, PlayerConstants.SONGS_LIST_TEMP, "search");
            lstview2.setAdapter(customAdapter);
            lstview2.setFastScrollEnabled(true);
            Utility.setDynamicHeight(lstview2);
            return;
        } else
        {
            rl_main.setVisibility(View.VISIBLE);
            ll_main.setVisibility(View.GONE);
            customAdapter = new CustomAdapter(this, R.layout.custom_list, PlayerConstants.SONGS_LIST_TEMP, "search");
            lstview.setAdapter(customAdapter);
            lstview.setFastScrollEnabled(true);
            return;
        }
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

    private void requestNewInterstitial()
    {
        com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).build();
        mInterstitialAd.loadAd(adrequest);
    }

    public static void runOnUI(Runnable runnable)
    {
        UIHandler.post(runnable);
    }

    private void setListeners()
    {
        lstview1.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                Log.d("TAG", "TAG Tapped INOUT(IN)");
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = i;
                Log.d("TAG", "TAG Tapped INOUT(OUT)");
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
                Intent intent = new Intent(Search.this, AudioPlayerActivity.class);
                startActivity(intent);
            }
        });
        lstview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                Log.d("TAG", "TAG Tapped INOUT(IN)");
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = i;
                Log.d("TAG", "TAG Tapped INOUT(OUT)");
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
                Intent intent = new Intent(Search.this, AudioPlayerActivity.class);
                startActivity(intent);
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
                    ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongId();
                    String s = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongTitle();
                    String s1 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongImage();
                    ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getSongUrl();
                    String s2 = ((MediaItem)PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)).getArtist();
                    Search.imageLoader.DisplayImage(s1, Search.iv_image);
                    Search.imageLoader.DisplayImageBitmap(s1, Search.iv_round, "");
                    Search.tv_title.setText(s);
                    Search.tv_artist.setText(s2);
                    Search.applyBlur();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }

        });
    }

    public void addToPlaylist(final String title, final String image, final String url, final String id, final String username)
    {
        int i = getResources().getDimensionPixelSize(R.dimen.dp_12);
        int j = getResources().getDimensionPixelSize(R.dimen.sp_14);
        final Dialog dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.playlistpopup);
        dialog1.setTitle("Add to playlist");
        LinearLayout linearlayout = (LinearLayout)dialog1.findViewById(R.id.ll_main);
        TextView textview = new TextView(this);
        textview.setSingleLine();
        android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams(-1, -1);
        textview.setLayoutParams(layoutparams);
        textview.setPadding(i, i, i, i);
        textview.setTextSize(j);
        textview.setText("New");
        linearlayout.addView(textview);
        List list = db.getPlaylist();
        if(list.size() > 0)
        {
            playlist = new ArrayList();

            Iterator iterator = list.iterator();
            while(iterator.hasNext()) {
                final TextView input;
                playlist_get_set playlist_get_set1 = (playlist_get_set)iterator.next();
                TextView textview1 = new TextView(this);
                android.widget.LinearLayout.LayoutParams layoutparams1 = new android.widget.LinearLayout.LayoutParams(-1, 2);
                textview1.setBackgroundColor(getResources().getColor(R.color.artist_color));
                textview1.setLayoutParams(layoutparams1);
                linearlayout.addView(textview1);
                input = new TextView(this);
                input.setSingleLine();
                android.widget.LinearLayout.LayoutParams layoutparams2 = new android.widget.LinearLayout.LayoutParams(-1, -1);
                input.setLayoutParams(layoutparams2);
                input.setPadding(i, i, i, i);
                input.setTextSize(j);
                input.setText(playlist_get_set1.getPlaylist_name().toString());
                playlist.add(playlist_get_set1.getPlaylist_name().toString());
                linearlayout.addView(input);
                input.setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(View view)
                    {
                        if(db.getSongCount(id, input.getText().toString()) == 0)
                        {
                            db.insertResentSongs(id, title.replaceAll("'", ""), image, url, input.getText().toString().replaceAll("'", ""), username.replaceAll("'", ""));
                            Toast.makeText(getApplicationContext(), "song added to playlist", Toast.LENGTH_LONG).show();
                            dialog1.cancel();
                            return;
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "this song already added in your playlist", Toast.LENGTH_LONG).show();
                            dialog1.cancel();
                            return;
                        }
                    }
                });
                input.setOnTouchListener(new android.view.View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionevent)
                    {
                        input.setTextColor(0xff0000ff);
                        return false;
                    }
                });
            }
        }
        textview.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                dialog1.cancel();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Search.this);
                builder.setTitle("Playlist name");
                final EditText edittext = new EditText(Search.this);
                edittext.setSingleLine();
                android.widget.LinearLayout.LayoutParams layoutparams3 = new android.widget.LinearLayout.LayoutParams(-1, -1);
                edittext.setPadding(10, 10, 10, 10);
                edittext.setLayoutParams(layoutparams3);
                builder.setView(edittext);
                builder.setPositiveButton("save", new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        if(edittext.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(getApplicationContext(), "Please enter playlist name", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(playlist.indexOf(edittext.getText().toString()) == -1)
                        {
                            db.insertPlaylist(edittext.getText().toString().replaceAll("'", ""));
                            db.insertResentSongs(id, title.replaceAll("'", ""), image, url, edittext.getText().toString().replaceAll("'", ""), username.replaceAll("'", ""));
                            Toast.makeText(getApplicationContext(), "song added to playlist", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Playlist already exist", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("cancel", new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        dialoginterface.cancel();
                    }
                });
                builder.show();
            }
        });
        dialog1.show();
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
        song_title1.clear();
        Count.clear();
        List list = db.getPlaylist();
        if(list.size() > 0)
        {
            lstview2.setVisibility(View.VISIBLE);
            tv_Norecords.setVisibility(View.GONE);
            int i;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); Count.add(String.valueOf(i)))
            {
                playlist_get_set playlist_get_set1 = (playlist_get_set)iterator.next();
                song_title1.add(playlist_get_set1.getPlaylist_name().toString());
                i = db.getCount(playlist_get_set1.getPlaylist_name().toString());
            }

            String as[] = (String[])song_title1.toArray(new String[song_title1.size()]);
            String as1[] = (String[])Count.toArray(new String[Count.size()]);
            lstview2.setAdapter(new PlayListAdapter(this, as, "playlist", as1));
            Utility.setDynamicHeight(lstview2);
            return;
        } else
        {
            lstview2.setVisibility(View.GONE);
            return;
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.search_cloud1);
        setCustomTitle("Search");
        context = this;
        imageLoader = new ImageLoader(getApplicationContext());
        appPref = new AppPreferences(getApplicationContext());
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4934154249742370/7599294446");
        requestNewInterstitial();

        mHandler.postDelayed(new Runnable() {
            public void run()
            {
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
                mHandler.postDelayed(this, 0x75300L);
            }
        }, 0x3a980L);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed()
            {
                requestNewInterstitial();
            }

            public void onAdFailedToLoad(int i)
            {
            }

            public void onAdLoaded()
            {
            }
        });

        file_url = "http://api.soundcloud.com/tracks.json?";
        file_url1 = "http://api-v2.soundcloud.com/explore/";
        db = new DatabaseHelper(getApplicationContext());
        appPref.setFlag("cloud");
        lstview = (ListView)findViewById(0x102000a);
        lstview1 = (ListView)findViewById(R.id.list1);
        lstview2 = (ListView)findViewById(R.id.list2);
        tv_Norecords = (TextView)findViewById(R.id.tv_norecords);
        tv_cloud = (TextView)findViewById(R.id.tv_cloud);
        tv_device = (TextView)findViewById(R.id.tv_device);
        view1 = (TextView)findViewById(R.id.view1);
        view2 = (TextView)findViewById(R.id.view2);
        view1.setVisibility(View.VISIBLE);
        rl_main = (RelativeLayout)findViewById(R.id.rl_main);
        ll_main = (LinearLayout)findViewById(R.id.ll_main);
        TableLayout01 = (TableLayout)findViewById(R.id.TableLayout01);
        setDrawer();
        if(checkconnection())
        {
            fetchData();
        }
        tv_cloud.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                appPref.setFlag("cloud");
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                clearList();
                if(checkconnection())
                {
                    fetchData();
                }
            }
        });
        tv_device.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                appPref.setFlag("device");
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                clearList();
                PlayerConstants.SONGS_LIST_TEMP = db.getAllSongsNew();
                fillData();
                getPlayList();
            }
        });
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchview = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchview.setSearchableInfo(((SearchManager)getSystemService(SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchview.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String s)
            {
                if(appPref.getFlag().equals("device"))
                {
                    PlayerConstants.SONGS_LIST_TEMP = db.getAllSongsSearch(s);
                    fillData();
                }
                return true;
            }

            public boolean onQueryTextSubmit(String s)
            {
                if(appPref.getFlag().equals("cloud"))
                {
                    query = s;
                    if(checkconnection())
                    {
                        fetchSearchData();
                    }
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    protected void onDestroy()
    {
        super.onDestroy();
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

    protected void onPause()
    {
        super.onPause();
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
            changeButton();
        }
    }

    void saveDataone()
    {
        appPref.setIsUnlockAppPaid(1);
        mHandler.removeCallbacksAndMessages(null);
        mInterstitialAd.setAdListener(null);
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
            public void onClick(View view3)
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
                    Intent intent = new Intent(Search.this, PlayList.class);
                    intent.putExtra("pagename", "chart");
                    startActivity(intent);
                    finish();
                } else if(j == 1) {
                    Intent intent1 = new Intent(Search.this, Search.class);
                    startActivity(intent1);
                } else if(j == 2) {
                    Intent intent2 = new Intent(Search.this, RecentSongList.class);
                    intent2.putExtra("playlist", "recent");
                    startActivity(intent2);
                    finish();
                } else if(j == 3) {
                    Intent intent3 = new Intent(Search.this, Songs.class);
                    startActivity(intent3);
                    finish();
                } else if(j == 4) {
                    Intent intent4 = new Intent(Search.this, Artist.class);
                    startActivity(intent4);
                    finish();
                } else if(j == 5) {
                    Intent intent5 = new Intent(Search.this, PlayList.class);
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
