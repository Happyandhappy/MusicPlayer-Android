package com.mzdevelopment.musicsearchpro.LasyList;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class FileCache
{

    private File cacheDir;

    public FileCache(Context context)
    {
        if(Environment.getExternalStorageState().equals("mounted"))
        {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "LazyList");
        } else
        {
            cacheDir = context.getCacheDir();
        }
        if(!cacheDir.exists())
        {
            cacheDir.mkdirs();
        }
    }

    public void clear()
    {
        File afile[] = cacheDir.listFiles();
        if(afile != null)
        {
            int i = afile.length;
            int j = 0;
            while(j < i) 
            {
                afile[j].delete();
                j++;
            }
        }
    }

    public File getFile(String s)
    {
        String s1 = String.valueOf(s.hashCode());
        return new File(cacheDir, s1);
    }
}
