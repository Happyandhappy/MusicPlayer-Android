package com.mzdevelopment.musicsearchpro.receiver;

import android.content.*;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallBroadcastReceiver extends BroadcastReceiver
{

    public CallBroadcastReceiver()
    {
    }

    public void onReceive(Context context, Intent intent)
    {
        Log.d("CallRecorder", (new StringBuilder()).append("CallBroadcastReceiver::onReceive got Intent: ").append(intent.toString()).toString());
        if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
        {
            String s = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
            Log.d("CallRecorder", (new StringBuilder()).append("CallBroadcastReceiver intent has EXTRA_PHONE_NUMBER: ").append(s).toString());
        }
        PhoneListener phonelistener = new PhoneListener(context);
        ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).listen(phonelistener, 32);
    }
}
