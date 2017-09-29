package com.mzdevelopment.musicsearchpro.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.Chart;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.Search;
import com.mzdevelopment.musicsearchpro.musicplayer.MusicPlayerActivity;

public class SearchListAdapter extends ArrayAdapter
{
    static class ViewHolder
    {

        public ImageView iv_chkbox;
        public ImageView iv_image;
        public RelativeLayout ll_image;
        public TextView texttitle;
        public TextView textusername;

        ViewHolder()
        {
        }
    }


    private final Activity context;
    public ImageLoader imageLoader;
    String songTitle[];
    String song_id[];
    String song_image[];
    String song_url[];
    String strpage;
    String user_name[];

    public SearchListAdapter(Activity activity, String as[], String as1[], String as2[], String as3[], String s, String as4[])
    {
        super(activity, R.layout.search_list_row, as);
        context = activity;
        songTitle = as;
        song_image = as1;
        song_url = as2;
        song_id = as3;
        user_name = as4;
        strpage = s;
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public View getView(final int position, View view, ViewGroup viewgroup)
    {
        View view1 = view;
        ViewHolder viewholder;
        RelativeLayout relativelayout;
        if(view1 == null)
        {
            view1 = context.getLayoutInflater().inflate(R.layout.search_list_row, null, true);
            viewholder = new ViewHolder();
            viewholder.iv_image = (ImageView)view1.findViewById(R.id.iv_image);
            viewholder.texttitle = (TextView)view1.findViewById(R.id.title);
            viewholder.textusername = (TextView)view1.findViewById(R.id.username);
            viewholder.iv_chkbox = (ImageView)view1.findViewById(R.id.iv_checkBox);
            viewholder.ll_image = (RelativeLayout)view1.findViewById(R.id.ll_image);
            view1.setTag(viewholder);
            view1.setTag(R.id.iv_image, viewholder.iv_image);
            view1.setTag(R.id.title, viewholder.texttitle);
            view1.setTag(R.id.iv_checkBox, viewholder.iv_chkbox);
        } else
        {
            viewholder = (ViewHolder)view1.getTag();
        }
        viewholder.iv_chkbox.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                if(strpage.equals("chart"))
                {
                    ((Chart)context).addToPlaylist(songTitle[position], song_image[position], song_url[position], song_id[position], user_name[position]);
                    return;
                } else
                {
                    ((Search)context).addToPlaylist(songTitle[position], song_image[position], song_url[position], song_id[position], user_name[position]);
                    return;
                }
            }
        });
        viewholder.iv_image.setTag(Integer.valueOf(position));
        int _tmp = (int)(0.5F + 70F * context.getResources().getDisplayMetrics().density);
        imageLoader.DisplayImage(song_image[Integer.parseInt(viewholder.iv_image.getTag().toString())], viewholder.iv_image);
        viewholder.texttitle.setText(songTitle[position]);
        viewholder.textusername.setText(user_name[position]);
        relativelayout = (RelativeLayout)viewholder.ll_image.getParent();
        relativelayout.setFocusable(true);
        relativelayout.setSelected(true);
        relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("song_url", song_url[position]);
                intent.putExtra("song_id", song_id[position]);
                intent.putExtra("song_image", song_image[position]);
                intent.putExtra("song_title", songTitle[position]);
                intent.putExtra("username", user_name[position]);
                context.startActivity(intent);
            }
        });
        return view1;
    }

}
