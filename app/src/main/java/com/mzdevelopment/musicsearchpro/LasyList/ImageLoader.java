package com.mzdevelopment.musicsearchpro.LasyList;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.widget.ImageView;

import com.mzdevelopment.musicsearchpro.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader
{
    class BitmapDisplayer
        implements Runnable
    {

        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public void run()
        {
            if(imageViewReused(photoToLoad))
            {
                return;
            }
            if(bitmap != null)
            {
                if(flag.equals("1"))
                {
                    bitmap = ImageLoader.getclip(bitmap);
                    photoToLoad.imageView.setImageBitmap(bitmap);
                    return;
                } else
                {
                    photoToLoad.imageView.setImageBitmap(bitmap);
                    return;
                }
            } else
            {
                photoToLoad.imageView.setImageResource(R.drawable.noimage);
                return;
            }
        }

        public BitmapDisplayer(Bitmap bitmap1, PhotoToLoad phototoload)
        {
            super();
            bitmap = bitmap1;
            photoToLoad = phototoload;
        }
    }

    private class PhotoToLoad
    {

        public ImageView imageView;
        public String url;

        public PhotoToLoad(String s, ImageView imageview)
        {
            super();
            url = s;
            imageView = imageview;
        }
    }

    class PhotosLoader implements Runnable
    {

        PhotoToLoad photoToLoad;

        public void run()
        {
            if(!imageViewReused(photoToLoad))
            {
                Bitmap bitmap = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bitmap);
                if(!imageViewReused(photoToLoad))
                {
                    BitmapDisplayer bitmapdisplayer = new BitmapDisplayer(bitmap, photoToLoad);
                    ((Activity)photoToLoad.imageView.getContext()).runOnUiThread(bitmapdisplayer);
                    return;
                }
            }
        }

        PhotosLoader(PhotoToLoad phototoload)
        {
            super();
            photoToLoad = phototoload;
        }
    }


    String Activity;
    ExecutorService executorService;
    FileCache fileCache;
    String flag;
    private Map imageViews;
    MemoryCache memoryCache;
    final int stub_id = R.drawable.noimage;

    public ImageLoader(Context context)
    {
        memoryCache = new MemoryCache();
        imageViews = Collections.synchronizedMap(new WeakHashMap());
        Activity = "";
        flag = "0";
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    private Bitmap decodeFile(File file)
    {
        int i;
        int j;
        int k;
        android.graphics.BitmapFactory.Options options1;
        try
        {
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            i = options.outWidth;
            j = options.outHeight;
            k = 1;
            while(i / 2 >= 70 && j / 2 >= 70) {
                i /= 2;
                j /= 2;
                k *= 2;
            }
            options1 = new android.graphics.BitmapFactory.Options();
            options1.inSampleSize = k;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, options1);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            return null;
        }
    }

    private Bitmap getBitmap(String s)
    {
        File file = fileCache.getFile(s);
        Bitmap bitmap = decodeFile(file);
        if(bitmap != null)
        {
            return bitmap;
        }
        Bitmap bitmap1;
        try
        {
            HttpURLConnection httpurlconnection = (HttpURLConnection)(new URL(s)).openConnection();
            httpurlconnection.setConnectTimeout(30000);
            httpurlconnection.setReadTimeout(30000);
            httpurlconnection.setInstanceFollowRedirects(true);
            java.io.InputStream inputstream = httpurlconnection.getInputStream();
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            Utils.CopyStream(inputstream, fileoutputstream);
            fileoutputstream.close();
            bitmap1 = decodeFile(file);
            return bitmap1;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    public static Bitmap getclip(Bitmap bitmap)
    {
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap1.getWidth() / 2, bitmap1.getHeight() / 2, bitmap1.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap1, rect, rect, paint);
        return bitmap2;
    }

    private void queuePhoto(String s, ImageView imageview)
    {
        PhotoToLoad phototoload = new PhotoToLoad(s, imageview);
        executorService.submit(new PhotosLoader(phototoload));
    }

    public void DisplayImage(String s, ImageView imageview)
    {
        imageViews.put(imageview, s);
        Bitmap bitmap = memoryCache.get(s);
        if(bitmap != null)
        {
            imageview.setImageBitmap(bitmap);
            return;
        } else
        {
            queuePhoto(s, imageview);
            imageview.setImageResource(R.drawable.noimage);
            return;
        }
    }

    public void DisplayImage(String s, ImageView imageview, String s1)
    {
        Activity = s1;
        imageViews.put(imageview, s);
        Bitmap bitmap = memoryCache.get(s);
        if(bitmap != null)
        {
            imageview.setImageBitmap(bitmap);
            return;
        } else
        {
            queuePhoto(s, imageview);
            imageview.setImageResource(R.drawable.noimage);
            return;
        }
    }

    public void DisplayImageBitmap(String s, ImageView imageview, String s1)
    {
        flag = "1";
        imageViews.put(imageview, s);
        Bitmap bitmap = memoryCache.get(s);
        if(bitmap != null)
        {
            imageview.setImageBitmap(getclip(bitmap));
            return;
        } else
        {
            queuePhoto(s, imageview);
            imageview.setImageResource(R.drawable.noimage);
            return;
        }
    }

    public void clearCache()
    {
        memoryCache.clear();
        fileCache.clear();
    }

    boolean imageViewReused(PhotoToLoad phototoload)
    {
        String s = (String)imageViews.get(phototoload.imageView);
        return s == null || !s.equals(phototoload.url);
    }

}
