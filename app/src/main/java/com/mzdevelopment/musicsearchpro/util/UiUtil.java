package com.mzdevelopment.musicsearchpro.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

public class UiUtil
{

    public static final String TAG = "UiUtil";
    private static final Hashtable fontCache = new Hashtable();

    public UiUtil()
    {
    }

    public static Typeface getFont(Context context, String s)
    {
        Typeface typeface1;
        synchronized(fontCache)
        {
            if(fontCache.get(s) == null)
            {
                Typeface typeface;
                typeface = Typeface.createFromAsset(context.getAssets(), (new StringBuilder()).append("fonts/").append(s).toString());
                fontCache.put(s, new SoftReference(typeface));
                return typeface;
            }
            SoftReference softreference = (SoftReference)fontCache.get(s);
            if(softreference.get() == null)
            {
                Typeface typeface;
                typeface = Typeface.createFromAsset(context.getAssets(), (new StringBuilder()).append("fonts/").append(s).toString());
                fontCache.put(s, new SoftReference(typeface));
                return typeface;
            }
            typeface1 = (Typeface)softreference.get();
        }
        return typeface1;
    }

    public static void setCustomFont(View view, Context context, AttributeSet attributeset, int ai[], int i)
    {
        TypedArray typedarray = context.obtainStyledAttributes(attributeset, ai);
        setCustomFont(view, context, typedarray.getString(i));
        typedarray.recycle();
    }

    private static boolean setCustomFont(View view, Context context, String s)
    {
        if(TextUtils.isEmpty(s))
        {
            return false;
        }
label0:
        {
            Typeface typeface = getFont(context, s);
            if(view instanceof TextView)
            {
                ((TextView)view).setTypeface(typeface);
                break label0;
            }
            try
            {
                ((Button)view).setTypeface(typeface);
            }
            catch(Exception exception)
            {
                Log.e("UiUtil", (new StringBuilder()).append("Could not get typeface: ").append(s).toString(), exception);
                return false;
            }
        }
        return true;
    }

}
