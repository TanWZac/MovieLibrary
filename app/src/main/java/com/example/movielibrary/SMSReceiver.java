package com.example.movielibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER = "SMS_FILTER";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Use the Telephony class to extract the incoming messages from the intent
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for (int i = 0; i < messages.length; i++) {

            SmsMessage currentMessage = messages[i];
            String senderNum = currentMessage.getDisplayOriginatingAddress();
            String message = currentMessage.getDisplayMessageBody(); //for each new message send a broadcast contains the new message to MainActivity
            Toast.makeText(context, "Sender: " + senderNum + ", message: " + message, Toast.LENGTH_LONG).show();

            //The MainActivity has to tokenize the new message and update the UI
            Intent msgIntent = new Intent();    //intent is an object that can hold the os or other app activity and its data in uri form
            msgIntent.setAction(SMS_FILTER);    //describes the activity to start and carries any necessary data
            msgIntent.putExtra(SMS_MSG_KEY, message); // allow Android components to request functionality from other components of the Android system
            context.sendBroadcast(msgIntent);
        }
    }

}