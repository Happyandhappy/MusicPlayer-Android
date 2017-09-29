package com.mzdevelopment.musicsearchpro.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.Chart;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.RecentSongList;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import com.mzdevelopment.musicsearchpro.db.recent_get_set;
import java.util.*;

public class PlayListAdapter extends ArrayAdapter
{
    static class ViewHolder
    {

        ImageView iv_image1;
        ImageView iv_image2;
        ImageView iv_image3;
        public TextView textcount;
        public TextView texttitle;

        ViewHolder()
        {
        }
    }


    private final Activity context;
    String count[];
    DatabaseHelper db;
    ArrayList image;
    public ImageLoader imageLoader;
    String pagename;
    String songTitle[];

    public PlayListAdapter(Activity activity, String as[], String s, String as1[])
    {
        super(activity, R.layout.playlist_row, as);
        image = new ArrayList();
        context = activity;
        songTitle = as;
        pagename = s;
        count = as1;
        imageLoader = new ImageLoader(activity.getApplicationContext());
        db = new DatabaseHelper(activity.getApplicationContext());
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        View view1 = view;
        final ViewHolder holder;
        RelativeLayout relativelayout;
        if(view1 == null)
        {
            view1 = context.getLayoutInflater().inflate(R.layout.playlist_row, null, true);
            holder = new ViewHolder();
            holder.texttitle = (TextView)view1.findViewById(R.id.tv_title);
            holder.textcount = (TextView)view1.findViewById(R.id.tv_count);
            holder.iv_image1 = (ImageView)view1.findViewById(R.id.iv_image1);
            holder.iv_image2 = (ImageView)view1.findViewById(R.id.iv_image2);
            holder.iv_image3 = (ImageView)view1.findViewById(R.id.iv_image3);
            view1.setTag(holder);
            view1.setTag(R.id.title, holder.texttitle);
        } else
        {
            holder = (ViewHolder)view1.getTag();
        }
        if(pagename.equals("playlist"))
        {
            List list = db.getRecentList(songTitle[i]);
            image.clear();
            recent_get_set recent_get_set1;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); image.add(recent_get_set1.getimage()))
            {
                recent_get_set1 = (recent_get_set)iterator.next();
            }

            int j = image.size();
            if(j > 2)
            {
                holder.iv_image1.setVisibility(View.VISIBLE);
                holder.iv_image2.setVisibility(View.VISIBLE);
                holder.iv_image3.setVisibility(View.VISIBLE);
                imageLoader.DisplayImageBitmap((String)image.get(0), holder.iv_image1, "1");
                imageLoader.DisplayImageBitmap((String)image.get(1), holder.iv_image2, "1");
                imageLoader.DisplayImageBitmap((String)image.get(2), holder.iv_image3, "1");
            } else
            if(j > 1)
            {
                holder.iv_image1.setVisibility(View.VISIBLE);
                holder.iv_image2.setVisibility(View.VISIBLE);
                holder.iv_image3.setVisibility(View.GONE);
                imageLoader.DisplayImageBitmap((String)image.get(0), holder.iv_image1, "1");
                imageLoader.DisplayImageBitmap((String)image.get(1), holder.iv_image2, "1");
            } else
            {
                holder.iv_image1.setVisibility(View.VISIBLE);
                holder.iv_image2.setVisibility(View.GONE);
                holder.iv_image3.setVisibility(View.GONE);
                imageLoader.DisplayImageBitmap((String)image.get(0), holder.iv_image1, "1");
            }
        } else
        {
            holder.iv_image1.setVisibility(View.VISIBLE);
            holder.iv_image2.setVisibility(View.GONE);
            holder.iv_image3.setVisibility(View.GONE);
        }
        if(pagename.equals("playlist"))
        {
            holder.texttitle.setText(songTitle[i]);
            holder.textcount.setText(count[i]);
        } else
        {
            holder.texttitle.setText(songTitle[i]);
            holder.textcount.setVisibility(View.GONE);
        }
        relativelayout = (RelativeLayout)holder.texttitle.getParent();
        relativelayout.setFocusable(true);
        relativelayout.setSelected(true);
        relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                if(pagename.equals("playlist"))
                {
                    Intent intent = new Intent(context, RecentSongList.class);
                    intent.putExtra("playlist", holder.texttitle.getText().toString());
                    context.startActivity(intent);
                    return;
                } else
                {
                    Intent intent1 = new Intent(context, Chart.class);
                    intent1.putExtra("strcat", holder.texttitle.getText().toString());
                    context.startActivity(intent1);
                    return;
                }
            }
        });
        return view1;
    }

}
