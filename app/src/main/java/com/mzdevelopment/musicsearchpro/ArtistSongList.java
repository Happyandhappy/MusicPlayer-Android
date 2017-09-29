package com.mzdevelopment.musicsearchpro;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.adapter.CustomAdapter;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.musicplayer.AudioPlayerActivity;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;
import java.util.ArrayList;

public class ArtistSongList extends ActionBarActivity
{

    private ActionBar actionBar;
    AppPreferences appPref;
    String artistname;
    CustomAdapter customAdapter;
    DatabaseHelper db;
    String file_url;
    String flag;
    ListView lstview;
    String mCountries[];
    int mPosition;
    String mTitle;
    ArrayList playlist;
    String query;
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

    public ArtistSongList()
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
        user_name = new ArrayList();
        search_song_id = new ArrayList();
        search_song_title = new ArrayList();
        search_song_image = new ArrayList();
        search_song_url = new ArrayList();
        search_user_name = new ArrayList();
        customAdapter = null;
    }

    private void clearList()
    {
        song_title.clear();
        song_image.clear();
        song_url.clear();
        song_id.clear();
    }

    private void setListeners()
    {
        lstview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                Log.d("TAG", "TAG Tapped INOUT(IN)");
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = i;
                Log.d("TAG", "TAG Tapped INOUT(OUT)");
                Intent intent = new Intent(ArtistSongList.this, AudioPlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void fillData(String s)
    {
        if(s.equals(""))
        {
            PlayerConstants.SONGS_LIST_TEMP = db.getArtistSongListNew(artistname);
        } else
        {
            PlayerConstants.SONGS_LIST_TEMP = db.getArtistSongListSearch(artistname, s.trim());
        }
        customAdapter = new CustomAdapter(this, R.layout.custom_list, PlayerConstants.SONGS_LIST_TEMP, "artist");
        lstview.setAdapter(customAdapter);
        lstview.setFastScrollEnabled(true);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.search);
        setCustomTitle("Artist");
        db = new DatabaseHelper(getApplicationContext());
        appPref = new AppPreferences(this);
        lstview = (ListView)findViewById(0x102000a);
        tv_Norecords = (TextView)findViewById(R.id.tv_norecords);
        if(getIntent().getExtras() != null)
        {
            artistname = getIntent().getStringExtra("user_name");
        }
        clearList();
        fillData("");
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchview = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchview.setSearchableInfo(((SearchManager)getSystemService(SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchview.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String s)
            {
                fillData(s);
                return true;
            }

            public boolean onQueryTextSubmit(String s)
            {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onSearchRequested()
    {
        return super.onSearchRequested();
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
