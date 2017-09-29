package com.mzdevelopment.musicsearchpro.LasyList;

import android.graphics.Bitmap;
import android.util.Log;
import java.util.*;

public class MemoryCache
{

    private static final String TAG = "MemoryCache";
    private Map cache;
    private long limit;
    private long size;

    public MemoryCache()
    {
        cache = Collections.synchronizedMap(new LinkedHashMap(10, 1.5F, true));
        size = 0L;
        limit = 0xf4240L;
        setLimit(Runtime.getRuntime().maxMemory() / 4L);
    }

    private void checkSize()
    {
        Log.i("MemoryCache", (new StringBuilder()).append("cache size=").append(size).append(" length=").append(cache.size()).toString());
        if(size > limit)
        {
            Iterator iterator = cache.entrySet().iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                size = size - getSizeInBytes((Bitmap)entry.getValue());
                iterator.remove();
            } while(size > limit);
            Log.i("MemoryCache", (new StringBuilder()).append("Clean cache. New size ").append(cache.size()).toString());
        }
    }

    public void clear()
    {
        cache.clear();
    }

    public Bitmap get(String s)
    {
        if(!cache.containsKey(s))
        {
            return null;
        }
        Bitmap bitmap;
        try
        {
            bitmap = (Bitmap)cache.get(s);
        }
        catch(NullPointerException nullpointerexception)
        {
            return null;
        }
        return bitmap;
    }

    long getSizeInBytes(Bitmap bitmap)
    {
        if(bitmap == null)
        {
            return 0L;
        } else
        {
            return (long)(bitmap.getRowBytes() * bitmap.getHeight());
        }
    }

    public void put(String s, Bitmap bitmap)
    {
        try
        {
            if(cache.containsKey(s))
            {
                size = size - getSizeInBytes((Bitmap)cache.get(s));
            }
            cache.put(s, bitmap);
            size = size + getSizeInBytes(bitmap);
            checkSize();
            return;
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setLimit(long l)
    {
        limit = l;
        Log.i("MemoryCache", (new StringBuilder()).append("MemoryCache will use up to ").append((double)limit / 1024D / 1024D).append("MB").toString());
    }
}
