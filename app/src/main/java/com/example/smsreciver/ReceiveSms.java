package com.example.smsreciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveSms extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Pattern p = Pattern.compile("(|^)\\d{4}");

            Bundle bundle = intent.getExtras();
            SmsMessage[] msgS = null;
            String smsFrom;
            if (bundle != null){
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgS = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgS.length; i++){
                        msgS[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsFrom = msgS[i].getOriginatingAddress();
                        String msgBody = msgS[i].getMessageBody();

                        Matcher m = p.matcher(msgBody);
                        if(m.find()) {
                            mListener.messageReceived(m.group(0));
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
