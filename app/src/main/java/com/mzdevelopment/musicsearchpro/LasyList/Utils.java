package com.mzdevelopment.musicsearchpro.LasyList;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils
{

    public Utils()
    {
    }

    public static void CopyStream(InputStream inputstream, OutputStream outputstream)
    {
        byte abyte0[] = new byte[1024];
        try
        {
            while(true) {
                int i = inputstream.read(abyte0, 0, 1024);
                if(i == -1)
                    return;
                outputstream.write(abyte0, 0, i);
            }
        }
        catch(Exception exception)
        {
        }
    }
}
