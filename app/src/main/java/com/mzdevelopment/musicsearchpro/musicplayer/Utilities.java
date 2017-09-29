package com.mzdevelopment.musicsearchpro.musicplayer;


public class Utilities
{

    public Utilities()
    {
    }

    public int getProgressPercentage(long l, long l1)
    {
        Double.valueOf(0.0D);
        long l2 = (int)(l / 1000L);
        long l3 = (int)(l1 / 1000L);
        return Double.valueOf(100D * ((double)l2 / (double)l3)).intValue();
    }

    public String milliSecondsToTimer(long l)
    {
        String s = "";
        int i = (int)(l / 0x36ee80L);
        int j = (int)(l % 0x36ee80L) / 60000;
        int k = (int)((l % 0x36ee80L % 60000L) / 1000L);
        if(i > 0)
        {
            s = (new StringBuilder()).append(i).append(":").toString();
        }
        String s1;
        if(k < 10)
        {
            s1 = (new StringBuilder()).append("0").append(k).toString();
        } else
        {
            s1 = (new StringBuilder()).append("").append(k).toString();
        }
        return (new StringBuilder()).append(s).append(j).append(":").append(s1).toString();
    }

    public int progressToTimer(int i, int j)
    {
        int k = j / 1000;
        return 1000 * (int)(((double)i / 100D) * (double)k);
    }
}
