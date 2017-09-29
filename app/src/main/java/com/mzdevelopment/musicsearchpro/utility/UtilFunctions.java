package com.mzdevelopment.musicsearchpro.utility;

import android.app.ActivityManager;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.mzdevelopment.musicsearchpro.R;

import java.util.*;

public class UtilFunctions
{

    static String LOG_CLASS = "UtilFunctions";

    public UtilFunctions()
    {
    }

    public static boolean currentVersionSupportBigNotification()
    {
        return android.os.Build.VERSION.SDK_INT >= 16;
    }

    public static boolean currentVersionSupportLockScreenControls()
    {
        return android.os.Build.VERSION.SDK_INT >= 14;
    }

    public static Bitmap getAlbumart(Context context, Long long1)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        ParcelFileDescriptor parcelfiledescriptor;
        Bitmap bitmap;
        Bitmap bitmap1;
        try
        {
            Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), long1.longValue());
            parcelfiledescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        }
        catch(Error error)
        {
            return null;
        }
        catch(Exception exception)
        {
            return null;
        }
        bitmap = null;
        if(parcelfiledescriptor != null)
        {
            bitmap1 = BitmapFactory.decodeFileDescriptor(parcelfiledescriptor.getFileDescriptor(), null, options);
            bitmap = bitmap1;
        }
        return bitmap;
    }

    public static Bitmap getDefaultAlbumArt(Context context)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        Bitmap bitmap;
        try
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_album_art, options);
        }
        catch(Error error)
        {
            return null;
        }
        catch(Exception exception)
        {
            return null;
        }
        return bitmap;
    }

    public static String getDuration(long l)
    {
        long l1 = (l / 1000L) % 60L;
        long l2 = (l / 60000L) % 60L;
        long l3 = l / 0x36ee80L;
        String s;
        String s1;
        String s2;
        if(l1 < 10L)
        {
            s = (new StringBuilder()).append("0").append(l1).toString();
        } else
        {
            s = (new StringBuilder()).append("").append(l1).toString();
        }
        if(l2 < 10L)
        {
            s1 = (new StringBuilder()).append("0").append(l2).toString();
        } else
        {
            s1 = (new StringBuilder()).append("").append(l2).toString();
        }
        s2 = (new StringBuilder()).append("").append(l3).toString();
        if(l3 > 0L)
        {
            return (new StringBuilder()).append(s2).append(":").append(s1).append(":").append(s).toString();
        } else
        {
            return (new StringBuilder()).append(s1).append(":").append(s).toString();
        }
    }

    public static boolean isServiceRunning(String s, Context context)
    {
        for(Iterator iterator = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(0x7fffffff).iterator(); iterator.hasNext();)
        {
            if(s.equals(((android.app.ActivityManager.RunningServiceInfo)iterator.next()).service.getClassName()))
            {
                return true;
            }
        }

        return false;
    }

    public static ArrayList listOfSongs(Context context)
    {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, "is_music != 0", null, null);
        ArrayList arraylist = new ArrayList();
        cursor.moveToFirst();
        MediaItem mediaitem;
        for(; cursor.moveToNext(); arraylist.add(mediaitem))
        {
            mediaitem = new MediaItem();
            String s = cursor.getString(cursor.getColumnIndex("title"));
            String s1 = cursor.getString(cursor.getColumnIndex("artist"));
            cursor.getString(cursor.getColumnIndex("album"));
            cursor.getLong(cursor.getColumnIndex("duration"));
            cursor.getString(cursor.getColumnIndex("_data"));
            cursor.getLong(cursor.getColumnIndex("album_id"));
            cursor.getString(cursor.getColumnIndex("composer"));
            mediaitem.setSongTitle(s);
            mediaitem.setArtist(s1);
        }

        cursor.close();
        Log.d("SIZE", (new StringBuilder()).append("SIZE: ").append(arraylist.size()).toString());
        return arraylist;
    }

}
