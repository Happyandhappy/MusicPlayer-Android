package com.mzdevelopment.musicsearchpro.volley;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;
import com.android.volley.*;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class MyVolleyHelper
{
    static class LruBitmapCache extends LruCache implements com.android.volley.toolbox.ImageLoader.ImageCache
    {

        public static int getCacheSize(Context context)
        {
            DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
            return 3 * (4 * (displaymetrics.widthPixels * displaymetrics.heightPixels));
        }

        public Bitmap getBitmap(String s)
        {
            return (Bitmap)get(s);
        }

        public void putBitmap(String s, Bitmap bitmap)
        {
            put(s, bitmap);
        }

        protected int sizeOf(Object obj, Object obj1)
        {
            return sizeOf((String)obj, (Bitmap)obj1);
        }

        protected int sizeOf(String s, Bitmap bitmap)
        {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }

        LruBitmapCache(int i)
        {
            super(i);
        }

        LruBitmapCache(Context context)
        {
            super(getCacheSize(context));
        }
    }


    private static Context mCtx = null;
    private static MyVolleyHelper mInstance = null;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    private MyVolleyHelper(Context context)
    {
        mRequestQueue = null;
        mImageLoader = null;
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(context));
    }

    public static MyVolleyHelper getIntance(Context context)
    {
        synchronized(MyVolleyHelper.class){
            MyVolleyHelper myvolleyhelper;
            if(mInstance == null)
            {
                mInstance = new MyVolleyHelper(context);
            }
            myvolleyhelper = mInstance;
            return myvolleyhelper;
        }
    }

    private RequestQueue getRequestQueue()
    {
        if(mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public String addParamsToUrl(Map map, String s)
    {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = map.entrySet().iterator();
        int i = 0;
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            stringbuilder.append((new StringBuilder()).append((String)entry.getKey()).append("=").toString());
            try
            {
                stringbuilder.append(URLEncoder.encode((String)entry.getValue(), "UTF-8"));
            }
            catch(UnsupportedEncodingException unsupportedencodingexception)
            {
                unsupportedencodingexception.printStackTrace();
            }
            if(i != -1 + map.size())
            {
                stringbuilder.append("&");
                i++;
            }
        } while(true);
        return (new StringBuilder()).append(s).append("?").append(stringbuilder.toString()).toString();
    }

    public void addRequestToQueue(Request request)
    {
        getRequestQueue().add(request);
    }

    public void cancelRequest(String s)
    {
        getRequestQueue().cancelAll(s);
    }

    public void clearCache(String s)
    {
        getRequestQueue().getCache().remove(s);
    }

    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }

}
