package com.mzdevelopment.musicsearchpro.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.Chart;
import com.mzdevelopment.musicsearchpro.LasyList.ImageLoader;
import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.db.DatabaseHelper;
import java.util.ArrayList;

public class ChartListAdapter extends ArrayAdapter
{
    static class ViewHolder
    {

        ImageView iv_image;
        public TextView texttitle;

        ViewHolder()
        {
        }
    }


    private final Activity context;
    String count[];
    DatabaseHelper db;
    int icons[];
    ArrayList image;
    public ImageLoader imageLoader;
    String pagename;
    String songTitle[];

    public ChartListAdapter(Activity activity, String as[], String s, String as1[], int ai[])
    {
        super(activity, R.layout.chartlist_row, as);
        image = new ArrayList();
        context = activity;
        songTitle = as;
        pagename = s;
        count = as1;
        icons = ai;
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
            view1 = context.getLayoutInflater().inflate(R.layout.chartlist_row, null, true);
            holder = new ViewHolder();
            holder.texttitle = (TextView)view1.findViewById(R.id.tv_title);
            holder.iv_image = (ImageView)view1.findViewById(R.id.iv_image);
            view1.setTag(holder);
            view1.setTag(R.id.title, holder.texttitle);
            view1.setTag(R.id.iv_image, holder.iv_image);
        } else
        {
            holder = (ViewHolder)view1.getTag();
        }
        holder.iv_image.setTag(Integer.valueOf(i));
        holder.iv_image.setImageResource(icons[Integer.parseInt(holder.iv_image.getTag().toString())]);
        holder.texttitle.setText(songTitle[i]);
        relativelayout = (RelativeLayout)holder.texttitle.getParent();
        relativelayout.setFocusable(true);
        relativelayout.setSelected(true);
        relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view2)
            {
                Intent intent = new Intent(context, Chart.class);
                intent.putExtra("strcat", holder.texttitle.getText().toString());
                context.startActivity(intent);
            }
        });
        return view1;
    }

}
