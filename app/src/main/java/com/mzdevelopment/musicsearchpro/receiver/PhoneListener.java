package com.mzdevelopment.musicsearchpro.receiver;

import android.content.Context;
import android.media.MediaPlayer;
import android.telephony.PhoneStateListener;
import android.util.Log;
import com.mzdevelopment.musicsearchpro.service.SongService;

public class PhoneListener extends PhoneStateListener
{

    private Context context;

    public PhoneListener(Context context1)
    {
        Log.i("CallRecorder", "PhoneListener constructor");
        context = context1;
    }

    public void onCallStateChanged(int i, String s)
    {
        Log.d("CallRecorder", (new StringBuilder()).append("PhoneListener::onCallStateChanged state:").append(i).append(" incomingNumber:").append(s).toString());
        switch (i) {
            default:
                break;
            case 0:
                Log.d("CallRecorder", "CALL_STATE_IDLE, stoping recording");
                if(SongService.mp != null)
                {
                    SongService.resumeSong(context);
                    return;
                }
                break;
            case 1:
                Log.d("CallRecorder", "CALL_STATE_RINGING");
                if(SongService.mp != null && SongService.mp.isPlaying())
                {
                    SongService.pauseSong(context);
                    return;
                }
                break;
            case 2:
                Log.d("CallRecorder", "CALL_STATE_OFFHOOK starting recording");
                if(SongService.mp != null && SongService.mp.isPlaying())
                {
                    SongService.pauseSong(context);
                    return;
                }
                break;
        }
    }
}
