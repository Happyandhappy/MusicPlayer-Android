package com.mzdevelopment.musicsearchpro;

import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.musicsearchpro.ui.adapter.NavDrawerListAdapter;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
{
    class DownloadFileFromURL extends AsyncTask
    {
        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((String[])aobj);
        }

        protected String doInBackground(String as[])
        {
            InputStream inputstream = null;
            try {
                URL url = new URL(as[0]);
                HttpURLConnection httpurlconnection;
                int i;
                inputstream = url.openStream();
                httpurlconnection = (HttpURLConnection) url.openConnection();
                i = httpurlconnection.getContentLength();
                if (httpurlconnection != null) {
                    FileOutputStream fileoutputstream;
                    byte abyte0[];
                    fileoutputstream = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString(), "FILE2.mp3"));
                    abyte0 = new byte[1024];
                    long l = 0L;
                    if (inputstream != null) {
                        while (true) {
                            int j = inputstream.read(abyte0);
                            if (j <= 0)
                                break;
                            l += j;
                            Integer ainteger[] = new Integer[1];
                            ainteger[0] = Integer.valueOf((int) ((100L * l) / (long) i));
                            publishProgress(ainteger);
                            fileoutputstream.write(abyte0, 0, j);
                        }
                    }
                    if (fileoutputstream != null)
                        fileoutputstream.close();
                }
                if (inputstream != null) {
                    try {
                        inputstream.close();
                    } catch (IOException ioexception4) {
                    }
                }
            }catch(Exception e){}
            return "";
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((String)obj);
        }

        protected void onPostExecute(String s)
        {
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        DownloadFileFromURL()
        {
            super();
        }
    }


    private final String DOWNSCALE_FILTER = "downscale_filter";
    ActionBar actionBar;
    private NavDrawerListAdapter adapter;
    private CheckBox downScale;
    private ImageView image;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private ArrayList navDrawerItems;
    private TypedArray navMenuIcons;
    private String navMenuTitles[];
    private TextView statusText;
    private TextView text;
    private TextView text1;

    public MainActivity()
    {
    }

    private void addCheckBoxes(ViewGroup viewgroup)
    {
        downScale = new CheckBox(this);
        android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.FrameLayout.LayoutParams(-2, -2);
        downScale.setLayoutParams(layoutparams);
        downScale.setText("Downscale before blur");
        downScale.setVisibility(View.VISIBLE);
        downScale.setTextColor(-1);
        downScale.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
            {
                applyBlur();
            }
        });
        viewgroup.addView(downScale);
    }

    private TextView addStatusText(ViewGroup viewgroup)
    {
        TextView textview = new TextView(this);
        textview.setLayoutParams(new android.view.ViewGroup.LayoutParams(-2, -2));
        textview.setTextColor(-1);
        viewgroup.addView(textview);
        return textview;
    }

    private void applyBlur()
    {
        image.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw()
            {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();
                Bitmap bitmap = image.getDrawingCache();
                if(android.os.Build.VERSION.SDK_INT >= 17)
                {
                    text1.setVisibility(View.GONE);
                    blur(bitmap, text, 10F);
                } else
                {
                    text1.setVisibility(View.VISIBLE);
                    BitmapDrawable bitmapdrawable = new BitmapDrawable(bitmap);
                    bitmapdrawable.setAlpha(127);
                    text.setBackgroundDrawable(bitmapdrawable);
                }
                return true;
            }
        });
    }

    private void blur(Bitmap bitmap, View view, float f)
    {
        long l = System.currentTimeMillis();
        float f1 = 1.0F;
        if(downScale.isChecked())
        {
            f1 = 8F;
            f = 10F;
        }
        Bitmap bitmap1 = Bitmap.createBitmap((int)((float)view.getMeasuredWidth() / f1), (int)((float)view.getMeasuredHeight() / f1), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        canvas.translate((float)(-view.getLeft()) / f1, (float)(-view.getTop()) / f1);
        canvas.scale(1.0F / f1, 1.0F / f1);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        Bitmap bitmap2 = FastBlur.doBlur(bitmap1, (int)f, true);
        view.setBackground(new BitmapDrawable(getResources(), bitmap2));
        statusText.setText((new StringBuilder()).append(System.currentTimeMillis() - l).append("ms").toString());
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.test);
        setCustomTitle("Music");
        image = (ImageView)findViewById(R.id.picture);
        text = (TextView)findViewById(R.id.text);
        text1 = (TextView)findViewById(R.id.text1);
        image.setImageResource(R.drawable.ic_launcher);
        statusText = addStatusText((ViewGroup)findViewById(R.id.controls));
        addCheckBoxes((ViewGroup)findViewById(R.id.controls));
        if(bundle != null)
        {
            downScale.setChecked(bundle.getBoolean("downscale_filter"));
        }
        applyBlur();
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        bundle.putBoolean("downscale_filter", downScale.isChecked());
        super.onSaveInstanceState(bundle);
    }

    protected void setCustomTitle(String s)
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ColorDrawable colordrawable = new ColorDrawable(Color.parseColor("#016db1"));
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setBackgroundDrawable(colordrawable);
        ((TextView)actionBar.getCustomView().findViewById(R.id.tv_title)).setText(s);
    }

    public String toString()
    {
        return "Fast blur";
    }





}
