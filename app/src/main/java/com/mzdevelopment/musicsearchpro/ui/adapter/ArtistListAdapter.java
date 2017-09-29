package com.mzdevelopment.musicsearchpro.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.ArtistSongList;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.musicplayer.MusicPlayerActivity;

public class ArtistListAdapter extends ArrayAdapter
{
    static class ViewHolder
    {

        public ImageView iv_image;
        public RelativeLayout ll_image;
        public TextView textcount;
        public TextView texttitle;
        public TextView username;

        ViewHolder()
        {
        }
    }


    private final Activity context;
    String count[];
    public ImageLoader imageLoader;
    String page_name;
    String songTitle[];
    String song_id[];
    String song_image[];
    String song_url[];
    String username[];

    public ArtistListAdapter(Activity activity, String as[], String as1[], String as2[], String as3[], String s, String as4[], 
            String as5[])
    {
        super(activity, R.layout.all_songs_list_row, as);
        context = activity;
        songTitle = as;
        song_image = as1;
        song_url = as2;
        song_id = as3;
        username = as4;
        page_name = s;
        count = as5;
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public View getView(final int position, View view, ViewGroup viewgroup)
    {
        View view1 = view;
        ViewHolder viewholder;
        RelativeLayout relativelayout;
        if(view1 == null)
        {
            view1 = context.getLayoutInflater().inflate(R.layout.all_songs_list_row, null, true);
            viewholder = new ViewHolder();
            viewholder.iv_image = (ImageView)view1.findViewById(R.id.iv_image);
            viewholder.texttitle = (TextView)view1.findViewById(R.id.title);
            viewholder.username = (TextView)view1.findViewById(R.id.user_name);
            viewholder.textcount = (TextView)view1.findViewById(R.id.tv_count);
            viewholder.ll_image = (RelativeLayout)view1.findViewById(R.id.ll_image);
            view1.setTag(viewholder);
            view1.setTag(R.id.iv_image, viewholder.iv_image);
            view1.setTag(R.id.title, viewholder.texttitle);
        } else
        {
            viewholder = (ViewHolder)view1.getTag();
        }
        viewholder.iv_image.setTag(Integer.valueOf(position));
        imageLoader.DisplayImage(song_image[position], viewholder.iv_image);
        viewholder.texttitle.setText(songTitle[position]);
        viewholder.username.setText(username[position]);
        viewholder.username.setVisibility(View.VISIBLE);
        if(page_name.equals("artist"))
        {
            viewholder.texttitle.setVisibility(View.GONE);
            viewholder.textcount.setVisibility(View.VISIBLE);
            viewholder.textcount.setText((new StringBuilder()).append("  - ").append(count[position]).append("tracks").toString());
        }
        relativelayout = (RelativeLayout)viewholder.ll_image.getParent();
        relativelayout.setFocusable(true);
        relativelayout.setSelected(true);
        relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                if(page_name.equals("artist"))
                {
                    Intent intent = new Intent(context, ArtistSongList.class);
                    intent.putExtra("user_name", username[position]);
                    context.startActivity(intent);
                    return;
                } else
                {
                    Intent intent1 = new Intent(context, MusicPlayerActivity.class);
                    intent1.putExtra("song_url", song_url[position]);
                    intent1.putExtra("song_id", song_id[position]);
                    intent1.putExtra("song_image", song_image[position]);
                    intent1.putExtra("song_title", songTitle[position]);
                    intent1.putExtra("user_name", username[position]);
                    context.startActivity(intent1);
                    return;
                }
            }
        });
        return view1;
    }

}
