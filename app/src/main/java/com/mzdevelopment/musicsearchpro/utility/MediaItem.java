package com.mzdevelopment.musicsearchpro.utility;


public class MediaItem
{

    String artist;
    String id;
    String image;
    String title;
    String url;

    public MediaItem()
    {
    }

    public String getArtist()
    {
        return artist;
    }

    public String getSongId()
    {
        return id;
    }

    public String getSongImage()
    {
        return image;
    }

    public String getSongTitle()
    {
        return title;
    }

    public String getSongUrl()
    {
        return url;
    }

    public void setArtist(String s)
    {
        artist = s;
    }

    public void setSongId(String s)
    {
        id = s;
    }

    public void setSongImage(String s)
    {
        image = s;
    }

    public void setSongTitle(String s)
    {
        title = s;
    }

    public void setSongUrl(String s)
    {
        url = s;
    }

    public String toString()
    {
        return title;
    }
}
