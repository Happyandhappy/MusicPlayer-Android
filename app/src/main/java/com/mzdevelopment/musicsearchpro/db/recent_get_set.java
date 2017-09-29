package com.mzdevelopment.musicsearchpro.db;


public class recent_get_set
{

    public String image;
    public String song_id;
    public String title;
    public String url;
    public String user_name;

    public recent_get_set()
    {
    }

    public String getUser_name()
    {
        return user_name;
    }

    public String getimage()
    {
        return image;
    }

    public String getsong_id()
    {
        return song_id;
    }

    public String gettitle()
    {
        return title;
    }

    public String geturl()
    {
        return url;
    }

    public void setUser_name(String s)
    {
        user_name = s;
    }

    public void setimage(String s)
    {
        image = s;
    }

    public void setsong_id(String s)
    {
        song_id = s;
    }

    public void settitle(String s)
    {
        title = s;
    }

    public void seturl(String s)
    {
        url = s;
    }
}
