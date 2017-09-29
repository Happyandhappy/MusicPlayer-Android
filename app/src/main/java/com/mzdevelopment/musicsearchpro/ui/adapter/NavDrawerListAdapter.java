package com.mzdevelopment.musicsearchpro.ui.adapter;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.mzdevelopment.musicsearchpro.NavDrawerItem;
import com.mzdevelopment.musicsearchpro.R;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter
{

    private Context context;
    private ArrayList navDrawerItems;

    public NavDrawerListAdapter(Context context1, ArrayList arraylist)
    {
        context = context1;
        navDrawerItems = arraylist;
    }

    public int getCount()
    {
        return navDrawerItems.size();
    }

    public Object getItem(int i)
    {
        return navDrawerItems.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        if(view == null)
        {
            view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_layout, null);
        }
        ((ImageView)view.findViewById(R.id.icon)).setImageResource(((NavDrawerItem)navDrawerItems.get(i)).getIcon());
        if(!((NavDrawerItem)navDrawerItems.get(i)).getCounterVisibility());
        return view;
    }
}
