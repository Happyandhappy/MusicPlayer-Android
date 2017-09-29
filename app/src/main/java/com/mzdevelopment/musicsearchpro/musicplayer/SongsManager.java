package com.mzdevelopment.musicsearchpro.musicplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager
{
    class FileExtensionFilter
        implements FilenameFilter
    {
        public boolean accept(File file, String s)
        {
            return s.endsWith(".mp3") || s.endsWith(".MP3");
        }

        FileExtensionFilter()
        {
            super();
        }
    }


    final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList songsList;

    public SongsManager()
    {
        songsList = new ArrayList();
    }

    public ArrayList getPlayList()
    {
        File file = new File(MEDIA_PATH);
        if(file.listFiles(new FileExtensionFilter()).length > 0)
        {
            File afile[] = file.listFiles(new FileExtensionFilter());
            int i = afile.length;
            for(int j = 0; j < i; j++)
            {
                File file1 = afile[j];
                HashMap hashmap = new HashMap();
                hashmap.put("songTitle", file1.getName().substring(0, -4 + file1.getName().length()));
                hashmap.put("songPath", file1.getPath());
                songsList.add(hashmap);
            }

        }
        return songsList;
    }
}
