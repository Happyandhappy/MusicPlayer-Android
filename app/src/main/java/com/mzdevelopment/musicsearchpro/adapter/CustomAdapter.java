package com.mzdevelopment.musicsearchpro.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.android.gms.ads.InterstitialAd;
import com.mzdevelopment.musicsearchpro.Artist;
import com.mzdevelopment.musicsearchpro.Chart;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.*;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.musicplayer.AudioPlayerActivity;
import com.mzdevelopment.musicsearchpro.utility.MediaItem;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter
{
    private class ViewHolder
    {
        public ImageView iv_chkbox;
        public ImageView iv_image;
        public RelativeLayout ll_image;
        public TextView texttitle;
        public TextView textusername;

        private ViewHolder()
        {
            super();
        }
    }

    AppPreferences appPref;
    Context context;
    DatabaseHelper db;
    ViewHolder holder;
    ImageLoader imageLoader;
    LayoutInflater inflator;
    ArrayList listOfSongs;
    String strpage;

    public CustomAdapter(Context context1, int i, ArrayList arraylist, String s)
    {
        super(context1, i, arraylist);
        listOfSongs = arraylist;
        strpage = s;
        context = context1;
        inflator = LayoutInflater.from(context1);
        imageLoader = new ImageLoader(context1);
        appPref = new AppPreferences(context1);
        db = new DatabaseHelper(context1);
    }

    private void hideKeyboard(Button button)
    {
        ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(button.getWindowToken(), 0);
    }

    public View getView(final int position, View view, ViewGroup viewgroup)
    {
        View view1 = view;
        final MediaItem detail;
        if(view == null)
        {
            view1 = inflator.inflate(R.layout.all_songs_list_row, viewgroup, false);
            holder = new ViewHolder();
            holder.iv_image = (ImageView)view1.findViewById(R.id.iv_image);
            holder.texttitle = (TextView)view1.findViewById(R.id.title);
            holder.ll_image = (RelativeLayout)view1.findViewById(R.id.ll_image);
            holder.textusername = (TextView)view1.findViewById(R.id.user_name);
            holder.iv_chkbox = (ImageView)view1.findViewById(R.id.iv_checkBox);
            view1.setTag(holder);
            view1.setTag(R.id.iv_image, holder.iv_image);
            view1.setTag(R.id.title, holder.texttitle);
            view1.setTag(R.id.iv_checkBox, holder.iv_chkbox);
        } else
        {
            holder = (ViewHolder)view1.getTag();
        }
        detail = (MediaItem)listOfSongs.get(position);
        holder.iv_image.setTag(Integer.valueOf(position));
        imageLoader.DisplayImage(detail.getSongImage(), holder.iv_image);
        holder.texttitle.setText(detail.toString());
        holder.textusername.setText(detail.getArtist());
        if(strpage.equals("chart") || strpage.equals("search"))
        {
            holder.iv_chkbox.setVisibility(View.VISIBLE);
        } else
        {
            holder.iv_chkbox.setImageResource(R.drawable.ic_edit);
            holder.iv_chkbox.setVisibility(View.VISIBLE);
        }
        holder.iv_chkbox.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                if(strpage.equals("chart"))
                {
                    ((Chart)context).addToPlaylist(detail.getSongTitle(), detail.getSongImage(), detail.getSongUrl(), detail.getSongId(), detail.getArtist());
                    return;
                }
                if(strpage.equals("search"))
                {
                    ((Search)context).addToPlaylist(detail.getSongTitle(), detail.getSongImage(), detail.getSongUrl(), detail.getSongId(), detail.getArtist());
                    return;
                } else
                {
                    View view3 = LayoutInflater.from(context).inflate(R.layout.update_popup, null);
                    final AlertDialog alertdialog = (new android.app.AlertDialog.Builder(context)).create();
                    alertdialog.setView(view3);
                    alertdialog.setTitle("Update Info");
                    final EditText et_title = (EditText)view3.findViewById(R.id.et_title);
                    final EditText et_artist = (EditText)view3.findViewById(R.id.et_artist);
                    final Button btn_update = (Button)view3.findViewById(R.id.btn_update);
                    et_title.setText(detail.getSongTitle());
                    et_artist.setText(detail.getArtist());
                    btn_update.setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View view)
                        {
                            hideKeyboard(btn_update);
                            String s = et_title.getText().toString().trim();
                            String s1 = et_artist.getText().toString().trim();
                            db.updateSongInfo(detail.getSongId(), s, s1);
                            if(strpage.equals("songs"))
                                ((Songs)context).fillData("");
                            else if(strpage.equals("artist"))
                            {
                                Intent intent = new Intent(context, Artist.class);
                                context.startActivity(intent);
                            } else
                            if(strpage.equals("recent"))
                            {
                                ((RecentSongList)context).fillData("");
                            }
                            alertdialog.dismiss();
                        }
                    });
                    alertdialog.show();
                    alertdialog.getWindow().setLayout(-1, -2);
                    return;
                }
            }
        });
        ((View)holder.texttitle.getParent().getParent()).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                PlayerConstants.SONGS_LIST = PlayerConstants.SONGS_LIST_TEMP;
                Log.d("TAG", "TAG Tapped INOUT(IN)");
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = position;
                Log.d("TAG", "TAG Tapped INOUT(OUT)");
                if(strpage.equals("search") && ((Search)context).mInterstitialAd.isLoaded())
                {
                    ((Search)context).mInterstitialAd.show();
                }
                Intent intent = new Intent(context, AudioPlayerActivity.class);
                context.startActivity(intent);
            }
        });
        return view1;
    }

}
