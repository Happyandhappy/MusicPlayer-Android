package com.mzdevelopment.musicsearchpro;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mzdevelopment.musicsearchpro.adapter.CustomAdapter;
import com.mzdevelopment.musicsearchpro.db.AppPreferences;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.db.playlist_get_set;
import com.mzdevelopment.musicsearchpro.util.Utilities;
import com.mzdevelopment.musicsearchpro.utility.MediaItem;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;
import com.mzdevelopment.musicsearchpro.volley.MyVolleyHelper;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.*;
import org.json.*;

public class Chart extends ActionBarActivity
{

    private ActionBar actionBar;
    AppPreferences appPref;
    CustomAdapter customAdapter;
    DatabaseHelper db;
    LinearLayout drawer;
    String file_url1;
    String flag;
    ImageView iv_image;
    ImageView iv_round;
    RelativeLayout ll_top;
    ListView lstview;
    int mPosition;
    String mTitle;
    ArrayList playlist;
    String query;
    ArrayList song_id;
    ArrayList song_image;
    ArrayList song_title;
    ArrayList song_url;
    String strcat;
    LinearLayout toplayout;
    TextView tv_Norecords;
    TextView tv_cloud;
    TextView tv_device;
    ArrayList user_name;
    TextView view1;
    TextView view2;

    public Chart()
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
        customAdapter = null;
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
        final ProgressDialog localProgressDialog;
        String s;
        localProgressDialog = ProgressDialog.show(this, getResources().getText(R.string.app_name), getResources().getText(R.string.wait_message), true);
        localProgressDialog.show();
        s = "";
        try {
            String s1 = URLEncoder.encode(strcat, "UTF-8");
            s = s1;
        }catch(Exception e){}
        JsonArrayRequest jsonarrayrequest = new JsonArrayRequest(0, (new StringBuilder()).append(appPref.getChartUrl()).append(s).toString(), null, new com.android.volley.Response.Listener<JSONArray>() {
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
                        localProgressDialog.dismiss();
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
            public Map getHeaders() throws AuthFailureError
            {
                return new HashMap();
            }
        };
        MyVolleyHelper.getIntance(this).addRequestToQueue(jsonarrayrequest);
    }

    private void fillData()
    {
        customAdapter = new CustomAdapter(this, R.layout.custom_list, PlayerConstants.SONGS_LIST_TEMP, "chart");
        lstview.setAdapter(customAdapter);
        lstview.setFastScrollEnabled(true);
    }

    public void addToPlaylist(final String title, final String image, final String url, final String id, final String user_name)
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
                            db.insertResentSongs(id, title.replaceAll("'", ""), image, url, input.getText().toString().replaceAll("'", ""), user_name.replaceAll("'", ""));
                            Toast.makeText(getApplicationContext(), "song added to playlist", Toast.LENGTH_LONG).show();
                            dialog1.cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), "This song already added in your playlist", Toast.LENGTH_LONG).show();
                            dialog1.cancel();
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Chart.this);
                builder.setTitle("Playlist name");
                final EditText edittext = new EditText(Chart.this);
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
                            db.insertResentSongs(id, title.replaceAll("'", ""), image, url, edittext.getText().toString().replaceAll("'", ""), user_name.replaceAll("'", ""));
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

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.search);
        setCustomTitle("Chart");
        db = new DatabaseHelper(getApplicationContext());
        appPref = new AppPreferences(this);
        lstview = (ListView)findViewById(0x102000a);
        tv_Norecords = (TextView)findViewById(R.id.tv_norecords);
        drawer = (LinearLayout)findViewById(R.id.drawer);
        drawer.setVisibility(View.GONE);
        if(getIntent().getExtras() != null)
        {
            strcat = getIntent().getStringExtra("strcat");
            file_url1 = "http://api-v2.soundcloud.com/explore/";
        }
        if(checkconnection())
        {
            fetchData();
        }
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
            public void onClick(View view3)
            {
                finish();
            }
        });
    }
}
