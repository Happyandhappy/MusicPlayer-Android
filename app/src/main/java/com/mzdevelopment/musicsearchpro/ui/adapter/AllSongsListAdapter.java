package com.mzdevelopment.musicsearchpro.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.musicplayer.MusicPlayerActivity;

public class AllSongsListAdapter extends ArrayAdapter
{
    static class ViewHolder
    {

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
    String pagename;
    String songTitle[];
    String song_id[];
    String song_image[];
    String song_url[];
    String user_name[];

    public AllSongsListAdapter(Activity activity, String as[], String as1[], String as2[], String as3[], String s, String as4[])
    {
        super(activity, R.layout.all_songs_list_row, as);
        context = activity;
        songTitle = as;
        song_image = as1;
        song_url = as2;
        song_id = as3;
        user_name = as4;
        imageLoader = new ImageLoader(activity.getApplicationContext());
        pagename = s;
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
            viewholder.ll_image = (RelativeLayout)view1.findViewById(R.id.ll_image);
            viewholder.textusername = (TextView)view1.findViewById(R.id.user_name);
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
                intent.putExtra("pagename", "device");
                intent.putExtra("songIndex", position);
                context.startActivity(intent);
            }
        });
        return view1;
    }

}
