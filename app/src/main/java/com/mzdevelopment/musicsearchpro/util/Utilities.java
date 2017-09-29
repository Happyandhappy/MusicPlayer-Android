package com.mzdevelopment.musicsearchpro.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities
{

    public Utilities()
    {
    }

    public static double ConvertToBearing(double d)
    {
        return (d + 360D) % 360D;
    }

    public static int DateDifference(Date date)
    {
        Date date1 = Calendar.getInstance().getTime();
        long l = date1.getTime() - date.getTime();
        long _tmp = (l / 1000L) % 60L;
        long l1 = (l / 60000L) % 60L;
        long _tmp1 = l / 0x36ee80L;
        int _tmp2 = (int)((date1.getTime() - date.getTime()) / 0x5265c00L);
        return (int)l1;
    }

    public static double GetBearingAngle(double d, double d1, double d2, double d3)
    {
        double d4 = deg2rad(d);
        double d5 = deg2rad(d1);
        double d6 = deg2rad(d2);
        double d7 = deg2rad(d3) - d5;
        return ConvertToBearing(rad2deg(Math.atan2(Math.sin(d7) * Math.cos(d6), Math.cos(d4) * Math.sin(d6) - Math.sin(d4) * Math.cos(d6) * Math.cos(d7))));
    }

    public static double GetDistance(double d, double d1, double d2, double d3)
    {
        double d4 = d1 - d3;
        return 1.62137D * (1.1515D * (60D * rad2deg(Math.acos(Math.sin(deg2rad(d)) * Math.sin(deg2rad(d2)) + Math.cos(deg2rad(d)) * Math.cos(deg2rad(d2)) * Math.cos(deg2rad(d4))))));
    }

    public static boolean checkNetworkConnection(Context context)
    {
        int i = 1;
        boolean flag = true;
        NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(i);
        NetworkInfo networkinfo1 = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        System.out.println((new StringBuilder()).append("wifi").append(networkinfo.isAvailable()).toString());
        System.out.println((new StringBuilder()).append("info").append(networkinfo1).toString());
        if((networkinfo1 == null || !networkinfo1.isConnected()) && !networkinfo.isAvailable())
        {
            i = 0;
        }
        if(i == 0)
        {
            flag = false;
        }
        if(i == 1)
        {
            flag = true;
        }
        return flag;
    }

    public static String convertGMTtoDate(Date date)
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMM dd, yyyy");
        TimeZone timezone = TimeZone.getDefault();
        simpledateformat.setTimeZone(timezone);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, timezone.getOffset(calendar.getTimeInMillis()));
        return simpledateformat.format(calendar.getTime());
    }

    public static String convertGMTtoDateTime(Date date)
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        TimeZone timezone = TimeZone.getDefault();
        simpledateformat.setTimeZone(timezone);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, timezone.getOffset(calendar.getTimeInMillis()));
        return simpledateformat.format(calendar.getTime());
    }

    public static String convertGMTtoDateTimeSearch(Date date)
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timezone = TimeZone.getDefault();
        simpledateformat.setTimeZone(timezone);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, timezone.getOffset(calendar.getTimeInMillis()));
        return simpledateformat.format(calendar.getTime());
    }

    public static double deg2rad(double d)
    {
        return (3.1415926535897931D * d) / 180D;
    }

    public static String getStringResourceByName(Context context, String s)
    {
        int i = context.getResources().getIdentifier(s, "string", context.getApplicationContext().getPackageName());
        Log.e("key", Integer.toString(i));
        return context.getString(i);
    }

    public static Bitmap getclip(Bitmap bitmap)
    {
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
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

    public static boolean isEmailValid(String s)
    {
        boolean flag = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}", Pattern.CASE_INSENSITIVE).matcher(s).matches();
        boolean flag1 = false;
        if(flag)
        {
            flag1 = true;
        }
        return flag1;
    }

    public static boolean isPasswordValid(String s)
    {
        boolean flag = Pattern.compile("^.{4,8}$", Pattern.CASE_INSENSITIVE).matcher(s).matches();
        boolean flag1 = false;
        if(flag)
        {
            flag1 = true;
        }
        return flag1;
    }

    public static boolean isUserNameValid(String s)
    {
        boolean flag = Pattern.compile("^[a-zA-Z0-9_-]{5,10}$", Pattern.CASE_INSENSITIVE).matcher(s).matches();
        boolean flag1 = false;
        if(flag)
        {
            flag1 = true;
        }
        return flag1;
    }

    public static boolean isValidCardNumber(String s)
    {
        boolean flag = Pattern.compile("^.{12,16}$", Pattern.CASE_INSENSITIVE).matcher(s).matches();
        boolean flag1 = false;
        if(flag)
        {
            flag1 = true;
        }
        return flag1;
    }

    public static boolean isValidDateDifference(Date date)
    {
        return (Calendar.getInstance().getTime().getTime() - date.getTime()) / 0x36ee80L <= 1L;
    }

    public static String pad(int i)
    {
        if(i >= 10)
        {
            return String.valueOf(i);
        } else
        {
            return (new StringBuilder()).append("0").append(String.valueOf(i)).toString();
        }
    }

    public static double rad2deg(double d)
    {
        return (180D * d) / 3.1415926535897931D;
    }

    public static Typeface setFonts(Context context, String s)
    {
        if(s.equals("1"))
        {
            return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
        } else
        {
            return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listview)
    {
        ListAdapter listadapter = listview.getAdapter();
        if(listadapter == null)
        {
            return;
        }
        int i = 0;
        for(int j = 0; j < listadapter.getCount(); j++)
        {
            View view = listadapter.getView(j, null, listview);
            view.measure(0, 0);
            i += view.getMeasuredHeight();
        }

        android.view.ViewGroup.LayoutParams layoutparams = listview.getLayoutParams();
        layoutparams.height = i + listview.getDividerHeight() * (-1 + listadapter.getCount());
        listview.setLayoutParams(layoutparams);
    }
}
