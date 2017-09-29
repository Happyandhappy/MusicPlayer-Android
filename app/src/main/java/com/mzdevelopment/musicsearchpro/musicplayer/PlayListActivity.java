package com.mzdevelopment.musicsearchpro.musicplayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.mzdevelopment.musicsearchpro.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayListActivity extends ListActivity
{

    public ArrayList songsList;

    public PlayListActivity()
    {
        songsList = new ArrayList();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.playlist);
        ArrayList arraylist = new ArrayList();
        songsList = (new SongsManager()).getPlayList();
        for(int i = 0; i < songsList.size(); i++)
        {
            arraylist.add((HashMap)songsList.get(i));
        }

        setListAdapter(new SimpleAdapter(this, arraylist, R.layout.playlist_item, new String[] {
            "songTitle"
        }, new int[] {
            R.id.songTitle
        }));
        getListView().setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int j, long l)
            {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra("songIndex", j);
                setResult(100, intent);
                finish();
            }
        });
    }
}
