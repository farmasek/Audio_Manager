package baranek.vojtech.audiomanager.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.mainActivity.MainActivity;
import baranek.vojtech.audiomanager.model.TimerProfileHelper;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;

/**
 * Created by Farmas on 30.12.2015.
 */


public class NotificationHelper {

    private static int Notification_ID = 33;

    /**
     * Method for Show notification
     *
     * @param c           context
     * @param receiveTime time in millis for next volume change
     */
    public static void showNotification(Context c, long receiveTime) {

        String strNextTime = getStringForNextDayFromReceiveTime(receiveTime,c);

        SharedPreferences sharedPreferences = c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);
        boolean isEnable = sharedPreferences.getBoolean("isNotifEnable",true);
        if(isEnable){

        //Intent for Turn off one timer button
        Intent notifIntentVyp = new Intent(c, NotificationReceiver.class);
        notifIntentVyp.setAction(TimerProfileKeys.KEY_NOTIFVYPONETIMER);
        PendingIntent notifPIntVyp = PendingIntent.getBroadcast(c, 9, notifIntentVyp, PendingIntent.FLAG_UPDATE_CURRENT);


        //Intent for turn off all timers button
        Intent notifIntentVypAll = new Intent(c, NotificationReceiver.class);
        notifIntentVypAll.setAction(TimerProfileKeys.KEY_NOTIFVYPALLTIMERS);
        PendingIntent notifPIntentVypAll = PendingIntent.getBroadcast(c, 9, notifIntentVypAll, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent for Notification click
        Intent mainActivityIntent = new Intent(c, MainActivity.class);
        PendingIntent mainActivityNotifPInt = PendingIntent.getActivity(c, 9, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


       NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(c);
        notBuilder.setSmallIcon(R.mipmap.app_launcehr_custom);
        notBuilder.setContentTitle(c.getResources().getString(R.string.app_name));
        notBuilder.setContentText(strNextTime);
        notBuilder.setContentIntent(mainActivityNotifPInt);
        notBuilder.addAction(R.drawable.ic_skip_next_black_24dp, c.getString(R.string.vyp), notifPIntVyp);
        notBuilder.addAction(R.drawable.ic_clear_black_24dp, c.getString(R.string.vyp_vse), notifPIntentVypAll);


        //Preferences settings

            String notification_slideable = sharedPreferences.getString("notification_slideable", "0");
            if (notification_slideable.equals("0"))
            {
                notBuilder.setOngoing(true);
            }
            if(notification_slideable.equals("1"))
            {
                notBuilder.setOngoing(false);
            }


            if(!sharedPreferences.getBoolean("showNotifIcon",true)){
                notBuilder.setSmallIcon(android.R.color.transparent);
            }

            Notification notification = notBuilder.build();
            notification.priority=Notification.PRIORITY_LOW;
            NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(Notification_ID, notification);

    }}

    /**
     * Generate string from time in millis
     *
     * @param receiveTimeInMillis receive time in millis
     * @return String of next timer time
     */
    public static String getStringForNextDayFromReceiveTime(long receiveTimeInMillis, Context c) {
        String ret;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(receiveTimeInMillis);

        ret = c.getString(R.string.next_change) + getDayLongShortcut(cal.get(Calendar.DAY_OF_WEEK),c) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + TimerProfileHelper.getZeroBeforMinute(cal.get(Calendar.MINUTE));
        return ret;

    }

    /**
     * Get long shortcut for days of week
     *
     * @param dayOfWeek int day of week
     * @return string day of week
     */
    private static String getDayLongShortcut(int dayOfWeek, Context c) {

        String ret = null;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                ret = c.getString(R.string.Nedele);
                break;
            case Calendar.MONDAY:
                ret = c.getString(R.string.Pondeli);
                break;
            case Calendar.TUESDAY:
                ret = c.getString(R.string.Utery);
                break;
            case Calendar.WEDNESDAY:
                ret = c.getString(R.string.Streda);
                break;
            case Calendar.THURSDAY:
                ret = c.getString(R.string.Ctvrtek);
                break;
            case Calendar.FRIDAY:
                ret = c.getString(R.string.Patek);
                break;
            case Calendar.SATURDAY:
                ret = c.getString(R.string.Sobota);
                break;
        }

        return ret;
    }

    /**
     * Close actual notification
     *
     * @param c context
     */
    public static void closeNotification(Context c) {
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(Notification_ID);
    }
}
