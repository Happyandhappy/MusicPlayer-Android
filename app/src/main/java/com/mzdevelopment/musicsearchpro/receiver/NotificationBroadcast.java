package com.mzdevelopment.musicsearchpro.receiver;

import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.mzdevelopment.musicsearchpro.controls.Controls;
import com.mzdevelopment.musicsearchpro.service.SongService;
import com.mzdevelopment.musicsearchpro.utility.PlayerConstants;

public class NotificationBroadcast extends BroadcastReceiver
{

    public static final String FINISH_AUDIOPLAYER = "com.mzdevelopment.musicsearchpro.finish";

    public NotificationBroadcast()
    {
    }

    public String ComponentName()
    {
        return getClass().getName();
    }

    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals("android.intent.action.MEDIA_BUTTON")) {
            KeyEvent keyevent = (KeyEvent)intent.getExtras().get("android.intent.extra.KEY_EVENT");
            if(keyevent.getAction() == 0) {
                switch(keyevent.getKeyCode())
                {
                    default:
                        return;

                    case 79: // 'O'
                    case 85: // 'U'
                        if(!PlayerConstants.SONG_PAUSED)
                        {
                            Controls.pauseControl(context);
                            return;
                        } else
                        {
                            Controls.playControl(context);
                            return;
                        }

                    case 87: // 'W'
                        Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
                        Controls.nextControl(context);
                        return;

                    case 88: // 'X'
                        Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
                        Controls.previousControl(context);
                        return;

                    case 86: // 'V'
                    case 126: // '~'
                    case 127: // '\177'
                        break;
                }
            }
        }else {
            if (intent.getAction().equals("com.mzdevelopment.musicsearchpro.play")) {
                Controls.playControl(context);
                return;
            }
            if (intent.getAction().equals("com.mzdevelopment.musicsearchpro.pause")) {
                Controls.pauseControl(context);
                return;
            }
            if (intent.getAction().equals("com.mzdevelopment.musicsearchpro.next")) {
                Controls.nextControl(context);
                return;
            }
            if (intent.getAction().equals("com.mzdevelopment.musicsearchpro.previous")) {
                Controls.previousControl(context);
                return;
            }
            if (intent.getAction().equals("com.mzdevelopment.musicsearchpro.delete")) {
                context.stopService(new Intent(context, SongService.class));
                return;
            }
        }
    }
}
