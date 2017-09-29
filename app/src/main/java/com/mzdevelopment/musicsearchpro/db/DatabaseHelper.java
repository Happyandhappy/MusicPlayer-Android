package com.mzdevelopment.musicsearchpro.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mzdevelopment.musicsearchpro.utility.MediaItem;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public static final String DATABASE_NAME = "DBManager_freemusicstream";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_PLAYLISTID = "playlist_id";
    public static final String KEY_PLAYLISTNAME = "playlist_name";
    public static final String KEY_PLAYLIST_NAME = "playlist_name";
    public static final String KEY_SONG_ID = "songId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";
    public static final String KEY_USER_NAME = "user_name";
    public static final String TABLE_PLAYLIST = "tbl_playlist";
    public static final String TABLE_RESENT = "tbl_resent_song";

    public DatabaseHelper(Context context)
    {
        super(context, "DBManager_freemusicstream", null, 1);
    }

    public void deleteSong(String s, String s1)
    {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.beginTransaction();
        sqlitedatabase.execSQL((new StringBuilder()).append("delete from tbl_resent_song where songId ='").append(s).append("' AND ").append("playlist_name").append(" ='").append(s1).append("'").toString());
        sqlitedatabase.setTransactionSuccessful();
        sqlitedatabase.endTransaction();
        sqlitedatabase.close();
    }

    public List getAllSongs()
    {
        ArrayList arraylist = new ArrayList();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT  * FROM tbl_resent_song WHERE playlist_name !='recent'", null);
        if(cursor.moveToFirst())
        {
            do
            {
                recent_get_set recent_get_set1 = new recent_get_set();
                recent_get_set1.setsong_id(cursor.getString(1));
                recent_get_set1.settitle(cursor.getString(2));
                recent_get_set1.setimage(cursor.getString(3));
                recent_get_set1.seturl(cursor.getString(4));
                recent_get_set1.setUser_name(cursor.getString(6));
                arraylist.add(recent_get_set1);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public ArrayList getAllSongsNew()
    {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT  * FROM tbl_resent_song WHERE playlist_name !='recent'", null);
        ArrayList arraylist = new ArrayList();
        if(cursor.moveToFirst())
        {
            do
            {
                MediaItem mediaitem = new MediaItem();
                mediaitem.setSongId(cursor.getString(1));
                mediaitem.setSongTitle(cursor.getString(2));
                mediaitem.setSongImage(cursor.getString(3));
                mediaitem.setSongUrl(cursor.getString(4));
                mediaitem.setArtist(cursor.getString(6));
                arraylist.add(mediaitem);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public ArrayList getAllSongsSearch(String s)
    {
        String s1 = (new StringBuilder()).append("SELECT  * FROM tbl_resent_song WHERE playlist_name !='recent' AND (title LIKE  '" +
"%"
).append(s).append("%'").append(" OR ").append("user_name").append(" LIKE  '%").append(s).append("%')").toString();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery(s1, null);
        ArrayList arraylist = new ArrayList();
        if(cursor.moveToFirst())
        {
            do
            {
                MediaItem mediaitem = new MediaItem();
                mediaitem.setSongId(cursor.getString(1));
                mediaitem.setSongTitle(cursor.getString(2));
                mediaitem.setSongImage(cursor.getString(3));
                mediaitem.setSongUrl(cursor.getString(4));
                mediaitem.setArtist(cursor.getString(6));
                arraylist.add(mediaitem);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public int getArtistCount(String s)
    {
        String s1 = (new StringBuilder()).append("select * from tbl_resent_song where user_name ='").append(s).append("'").append(" And ").append("playlist_name").append(" !='").append("recent").append("'").toString();
        Cursor cursor = getWritableDatabase().rawQuery(s1, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    public List getArtistList()
    {
        ArrayList arraylist = new ArrayList();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT  DISTINCT user_name,title,image,songId,url FROM tbl_resent_song WHERE pla" +
"ylist_name !='recent'"
, null);
        if(cursor.moveToFirst())
        {
            do
            {
                recent_get_set recent_get_set1 = new recent_get_set();
                recent_get_set1.setsong_id(cursor.getString(3));
                recent_get_set1.settitle(cursor.getString(1));
                recent_get_set1.setimage(cursor.getString(2));
                recent_get_set1.seturl(cursor.getString(4));
                recent_get_set1.setUser_name(cursor.getString(0));
                arraylist.add(recent_get_set1);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public List getArtistSongList(String s)
    {
        ArrayList arraylist = new ArrayList();
        String s1 = (new StringBuilder()).append("SELECT  * FROM tbl_resent_song WHERE user_name ='").append(s).append("'").append(" And ").append("playlist_name").append(" !='").append("recent").append("'").toString();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery(s1, null);
        if(cursor.moveToFirst())
        {
            do
            {
                recent_get_set recent_get_set1 = new recent_get_set();
                recent_get_set1.setsong_id(cursor.getString(1));
                recent_get_set1.settitle(cursor.getString(2));
                recent_get_set1.setimage(cursor.getString(3));
                recent_get_set1.seturl(cursor.getString(4));
                recent_get_set1.setUser_name(cursor.getString(6));
                arraylist.add(recent_get_set1);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public ArrayList getArtistSongListNew(String s)
    {
        String s1 = (new StringBuilder()).append("SELECT  * FROM tbl_resent_song WHERE user_name ='").append(s).append("'").append(" And ").append("playlist_name").append(" !='").append("recent").append("'").toString();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery(s1, null);
        ArrayList arraylist = new ArrayList();
        if(cursor.moveToFirst())
        {
            do
            {
                MediaItem mediaitem = new MediaItem();
                mediaitem.setSongId(cursor.getString(1));
                mediaitem.setSongTitle(cursor.getString(2));
                mediaitem.setSongImage(cursor.getString(3));
                mediaitem.setSongUrl(cursor.getString(4));
                mediaitem.setArtist(cursor.getString(6));
                arraylist.add(mediaitem);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public ArrayList getArtistSongListSearch(String s, String s1)
    {
        String s2 = (new StringBuilder()).append("SELECT  * FROM tbl_resent_song WHERE user_name ='").append(s).append("'").append(" AND ").append("playlist_name").append(" !='").append("recent").append("'").append(" AND (").append("title").append(" LIKE  '%").append(s1).append("%'").append(" OR ").append("user_name").append(" LIKE  '%").append(s1).append("%')").toString();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery(s2, null);
        ArrayList arraylist = new ArrayList();
        if(cursor.moveToFirst())
        {
            do
            {
                MediaItem mediaitem = new MediaItem();
                mediaitem.setSongId(cursor.getString(1));
                mediaitem.setSongTitle(cursor.getString(2));
                mediaitem.setSongImage(cursor.getString(3));
                mediaitem.setSongUrl(cursor.getString(4));
                mediaitem.setArtist(cursor.getString(6));
                arraylist.add(mediaitem);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public int getCount(String s)
    {
        String s1 = (new StringBuilder()).append("select * from tbl_resent_song where playlist_name ='").append(s).append("'").toString();
        Cursor cursor = getWritableDatabase().rawQuery(s1, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    public List getGroupArtistSongs()
    {
        ArrayList arraylist = new ArrayList();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT  * FROM tbl_resent_song WHERE playlist_name !='recent' Group By user_name", null);
        if(cursor.moveToFirst())
        {
            do
            {
                recent_get_set recent_get_set1 = new recent_get_set();
                recent_get_set1.setsong_id(cursor.getString(1));
                recent_get_set1.settitle(cursor.getString(2));
                recent_get_set1.setimage(cursor.getString(3));
                recent_get_set1.seturl(cursor.getString(4));
                recent_get_set1.setUser_name(cursor.getString(6));
                arraylist.add(recent_get_set1);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public List getPlaylist()
    {
        ArrayList arraylist = new ArrayList();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery("SELECT  * FROM tbl_playlist", null);
        if(cursor.moveToFirst())
        {
            do
            {
                playlist_get_set playlist_get_set1 = new playlist_get_set();
                playlist_get_set1.setPlaylist_name(cursor.getString(1));
                arraylist.add(playlist_get_set1);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public List getRecentList(String s)
    {
        ArrayList arraylist = new ArrayList();
        String s1 = (new StringBuilder()).append("SELECT  * FROM tbl_resent_song WHERE playlist_name ='").append(s).append("'").append(" Order by ").append("id").append(" DESC").toString();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery(s1, null);
        if(cursor.moveToFirst())
        {
            do
            {
                recent_get_set recent_get_set1 = new recent_get_set();
                recent_get_set1.setsong_id(cursor.getString(1));
                recent_get_set1.settitle(cursor.getString(2));
                recent_get_set1.setimage(cursor.getString(3));
                recent_get_set1.seturl(cursor.getString(4));
                recent_get_set1.setUser_name(cursor.getString(6));
                arraylist.add(recent_get_set1);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public ArrayList getRecentListNew(String s)
    {
        String s1 = (new StringBuilder()).append("SELECT  * FROM tbl_resent_song WHERE playlist_name ='").append(s).append("'").append(" Order by ").append("id").append(" DESC").toString();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery(s1, null);
        ArrayList arraylist = new ArrayList();
        if(cursor.moveToFirst())
        {
            do
            {
                MediaItem mediaitem = new MediaItem();
                mediaitem.setSongId(cursor.getString(1));
                mediaitem.setSongTitle(cursor.getString(2));
                mediaitem.setSongImage(cursor.getString(3));
                mediaitem.setSongUrl(cursor.getString(4));
                mediaitem.setArtist(cursor.getString(6));
                arraylist.add(mediaitem);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public ArrayList getRecentListSearch(String s)
    {
        String s1 = (new StringBuilder()).append("SELECT  * FROM tbl_resent_song WHERE playlist_name ='recent' AND (title LIKE  '%").append(s).append("%'").append(" OR ").append("user_name").append(" LIKE  '%").append(s).append("%')").toString();
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery(s1, null);
        ArrayList arraylist = new ArrayList();
        if(cursor.moveToFirst())
        {
            do
            {
                MediaItem mediaitem = new MediaItem();
                mediaitem.setSongId(cursor.getString(1));
                mediaitem.setSongTitle(cursor.getString(2));
                mediaitem.setSongImage(cursor.getString(3));
                mediaitem.setSongUrl(cursor.getString(4));
                mediaitem.setArtist(cursor.getString(6));
                arraylist.add(mediaitem);
            } while(cursor.moveToNext());
        }
        cursor.close();
        sqlitedatabase.close();
        return arraylist;
    }

    public int getSongCount(String s, String s1)
    {
        String s2 = (new StringBuilder()).append("select * from tbl_resent_song where songId ='").append(s).append("'").append(" AND ").append("playlist_name").append(" ='").append(s1).append("'").toString();
        Cursor cursor = getWritableDatabase().rawQuery(s2, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    public void insertPlaylist(String s)
    {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.beginTransaction();
        sqlitedatabase.execSQL((new StringBuilder()).append("insert into tbl_playlist (playlist_name) values ('").append(s).append("')").toString());
        sqlitedatabase.setTransactionSuccessful();
        sqlitedatabase.endTransaction();
        sqlitedatabase.close();
    }

    public void insertResentSongs(String s, String s1, String s2, String s3, String s4, String s5)
    {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.beginTransaction();
        sqlitedatabase.execSQL((new StringBuilder()).append("insert into tbl_resent_song (songId,title,image,url,playlist_name,user_name) val" +
"ues ('"
).append(s).append("','").append(s1).append("','").append(s2).append("','").append(s3).append("','").append(s4).append("','").append(s5).append("')").toString());
        sqlitedatabase.setTransactionSuccessful();
        sqlitedatabase.endTransaction();
        sqlitedatabase.close();
    }

    public void onCreate(SQLiteDatabase sqlitedatabase)
    {
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS tbl_resent_song(id  INTEGER PRIMARY KEY AUTOINCREMENT" +
",songId TEXT,title TEXT,image TEXT,url TEXT,playlist_name TEXT,user_name TEXT)"
);
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS tbl_playlist(playlist_id  INTEGER PRIMARY KEY AUTOINC" +
"REMENT,playlist_name TEXT)"
);
    }

    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS tbl_resent_song");
        sqlitedatabase.execSQL("DROP TABLE IF EXISTS tbl_playlist");
        onCreate(sqlitedatabase);
    }

    public void updateCountfamily(int i)
    {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.beginTransaction();
        sqlitedatabase.execSQL((new StringBuilder()).append("update tbl_user_contacts set family_count=").append(i).toString());
        sqlitedatabase.setTransactionSuccessful();
        sqlitedatabase.endTransaction();
        sqlitedatabase.close();
    }

    public void updateSongInfo(String s, String s1, String s2)
    {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.beginTransaction();
        sqlitedatabase.execSQL((new StringBuilder()).append("update tbl_resent_song set title = '").append(s1).append("'").append(" , ").append("user_name").append(" = '").append(s2).append("'").append(" WHERE ").append("songId").append(" ='").append(s).append("'").toString());
        sqlitedatabase.setTransactionSuccessful();
        sqlitedatabase.endTransaction();
        sqlitedatabase.close();
    }
}
