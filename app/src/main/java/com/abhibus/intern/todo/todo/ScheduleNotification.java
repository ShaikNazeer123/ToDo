package com.abhibus.intern.todo.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Shaik Nazeer on 22-06-2017.
 */

public class ScheduleNotification extends BroadcastReceiver {
    DataBase db;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        db = new DataBase(context);
        Log.e("Onreceive","1");
        Cursor res = db.getTasks(0);
        do{
            String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
            String dateTime = res.getString(4)+" "+res.getString(2);
            if(currentDateandTime.charAt(11)=='0'){
                currentDateandTime = currentDateandTime.substring(0, 11) + currentDateandTime.substring(11 + 1);
                if(currentDateandTime.charAt(13)=='0'){
                    currentDateandTime = currentDateandTime.substring(0, 13) + currentDateandTime.substring(13 + 1);;
                }
            }
            else if(currentDateandTime.charAt(14)=='0'){
                currentDateandTime = currentDateandTime.substring(0, 14) + currentDateandTime.substring(14 + 1);;
            }
            if(currentDateandTime.charAt(0)=='0'){
                currentDateandTime = currentDateandTime.substring(0, 0) + currentDateandTime.substring(0 + 1);
                if(currentDateandTime.charAt(2)=='0'){
                    currentDateandTime = currentDateandTime.substring(0, 2) + currentDateandTime.substring(2 + 1);;
                }
            }
            else if(currentDateandTime.charAt(3)=='0'){
                currentDateandTime = currentDateandTime.substring(0, 3) + currentDateandTime.substring(3 + 1);;
            }
            Log.e("Onreceive"," system "+currentDateandTime+" db "+dateTime);
            if(currentDateandTime.equals(dateTime)){
                notification(context,res.getString(1),res.getString(2)+" - " + res.getString(3));
                db.update(res.getInt(0));
                break;
            }
        }while(res.moveToNext());
        MainActivity.scheduleAlarm(context);

    }


    public void notification(Context context,String title,String body){
        NotificationCompat.Builder ncontent = (NotificationCompat.Builder) new NotificationCompat.Builder(context).setSmallIcon(R.drawable.add_image1).setContentTitle(title).setContentText(body);

        Intent resultIntent = new Intent(context,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        ncontent.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ncontent.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        ncontent.setSound(sound);
        ncontent.setSound ( Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getPackageName() + "/raw/notify"));
        manager.notify(0,ncontent.build());
    }

}
