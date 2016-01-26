package baranek.vojtech.audiomanager.alarmTimingUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Calendar;

import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.notifications.NotificationHelper;
import baranek.vojtech.audiomanager.model.TimerProfileHelper;
import baranek.vojtech.audiomanager.volumeChangeManager.AlarmReceiver;
import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import baranek.vojtech.audiomanager.volumeChangeManager.AndroidProfileChanger;

/**
 * Created by Farmas on 18.12.2015.
 */
public class AlarmControl {

    public static void runNextTimer(Context c) {

        NotificationHelper.closeNotification(c);
        AlarmControlHelper alarmControlHelper = getNextTimer(c);

        runTimerReceiver(c, alarmControlHelper);

    }

    public static void runTimerReceiver(Context c, AlarmControlHelper alarmControlHelper) {

        TimerProfile nextTimer = alarmControlHelper.getTimerProfile();
        long receiverTime = alarmControlHelper.getNextAlarmTime();


        if (nextTimer != null) {


            //if timer is already active, skip
            if (c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE).getInt(TimerProfileKeys.KEY_ID, -1) == -1) {

                stopBothTimers(c);

                startStartTimer(c, nextTimer, receiverTime);


                startEndTimer(c, nextTimer, receiverTime);

                long timeToGo = receiverTime - Calendar.getInstance().getTimeInMillis();
                Toast.makeText(c, convertTimeToReceiveToString(timeToGo,c), Toast.LENGTH_SHORT).show();

            }
        }

        if (nextTimer==null){
            stopBothTimers(c);
        }
    }

    public static void stopBothTimers(Context c) {

        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent intentKon = new Intent(c, AlarmReceiver.class);
        PendingIntent pintKon = PendingIntent.getBroadcast(c, 100, intentKon, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pintKon);
        Intent intent = new Intent(c, AlarmReceiver.class);
        PendingIntent pint = PendingIntent.getBroadcast(c, 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pint);

    }

    private static void startEndTimer(Context c, TimerProfile nextTimer, long receiverTime) {

        //End of timer
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        Intent intentKon = new Intent(c, AlarmReceiver.class);
        intentKon.putExtra(TimerProfileKeys.KEY_ID, nextTimer.getId());
        intentKon.putExtra(TimerProfileKeys.KEY_ISITENDTIMER, true);

        PendingIntent pintKon = PendingIntent.getBroadcast(c, 100, intentKon, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pintKon);

        //Start new end alarm only if end is on
        if (nextTimer.isKonZap()) {
            long endReceiverTime = receiverTime + nextTimer.getCasDoKonce() * 60 * 1000;
            alarmManager.set(AlarmManager.RTC_WAKEUP, endReceiverTime, pintKon);

            //Save end ReceiverTime for notifications
            SharedPreferences sharedPreferences = c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putLong(TimerProfileKeys.KEY_KONTIMERECEIVEINMILLIS, endReceiverTime).apply();

        }


    }

    private static void startStartTimer(Context c, TimerProfile nextTimer, long receiverTime) {
        //Start of timer
        Intent intent = new Intent(c, AlarmReceiver.class);
        intent.putExtra(TimerProfileKeys.KEY_ID, nextTimer.getId());
        intent.putExtra(TimerProfileKeys.KEY_ISITENDTIMER, false);

        PendingIntent pint = PendingIntent.getBroadcast(c, 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pint);
        alarmManager.set(AlarmManager.RTC_WAKEUP, receiverTime, pint);

        //Notification

        NotificationHelper.showNotification(c, receiverTime);
    }


    /**
     * Scan for active TimerProfiles and run the nearest to actual time
     * Run TimerProfile if there is active at this time
     *
     * @param c context
     * @return next timer and time to start in millis
     */

    public static AlarmControlHelper getNextTimer(Context c) {
        long receiverTime = 0;
        Calendar cal = Calendar.getInstance();
        TimerProfile timer;

        // Sunday = 1 ... Saturday = 7
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int dayCounter = 1;


        int currTimeInMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
        String strDayOfWeek = getDayShortCutFromDayNumber(dayOfWeek);


        timer = findIfThereIsTimerForThisTime(currTimeInMinutes / 60, strDayOfWeek, c);
        //run timer if there is active timer for actual time

        if (timer != null) {

            cal.set(Calendar.HOUR_OF_DAY, timer.getZacCas() / 60);
            cal.set(Calendar.MINUTE, timer.getZacCas() % 60);
            receiverTime = cal.getTimeInMillis() + timer.getCasDoKonce() * 60 * 1000;

            AndroidProfileChanger.changeVolumeProfile(c, timer.getId(), true);

            startEndTimer(c, timer, receiverTime);
            SharedPreferences sharedPreferences = c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID, timer.getId()).apply();
            sharedPreferences.edit().putLong(TimerProfileKeys.KEY_KONTIMERECEIVEINMILLIS, receiverTime);
            NotificationHelper.showNotification(c, receiverTime);


        } else {
            //Checking next Active timer, return null if no active
            timer = findTimersForDaySince(currTimeInMinutes / 60, strDayOfWeek, c);

            //setting time to run receiver from now
            if (timer != null) {
                receiverTime = cal.getTimeInMillis() + (timer.getZacCas() * 60 - currTimeInMinutes) * 1000;
            } else {
                int h = 1440 * 60 - currTimeInMinutes;
                receiverTime = cal.getTimeInMillis() + h * 1000;
            }

            //looking for another day, timer == null if no active timer, max days 7
            while (timer == null && dayCounter < 8) {
                dayOfWeek = addOneDay(dayOfWeek);
                strDayOfWeek = getDayShortCutFromDayNumber(dayOfWeek);
                timer = findTimersForDaySince(-1, strDayOfWeek, c);

                if (timer == null) {
                    receiverTime = receiverTime + 24 * 60 * 60 * 1000;
                } else {
                    receiverTime = receiverTime + timer.getZacCas() * 60 * 1000;
                }
                dayCounter++;
            }
        }
        return new AlarmControlHelper(receiverTime, timer);
    }

    /**
     * Find in database if there is active timer for this moment
     *
     * @param currTimeInMinutes current time in minutes - -1 if new day
     * @param today             string searching day
     * @param c                 context
     * @return
     */
    private static TimerProfile findIfThereIsTimerForThisTime(int currTimeInMinutes, String today, Context c) {
        RealmHelper realmHelper = new RealmHelper(c);

        TimerProfile returnTimer = null;
        TimerProfile firstTimer = realmHelper.getFirstTimerBeforeThisMoment(currTimeInMinutes, today);

        if (firstTimer != null) {
            if (firstTimer.getZacCas() + firstTimer.getCasDoKonce() > currTimeInMinutes && firstTimer.isKonZap())
                returnTimer = firstTimer;
        }

        return returnTimer;
    }


    /**
     * add one day, change to 1 if saturday (7)
     *
     * @param day current day
     * @return next day
     */
    public static int addOneDay(int day) {

        if (day == 7) {
            day = 1;
        } else {
            day++;
        }

        return day;
    }

    /**
     * find active timerprofiles for particular day since particular time
     *
     * @param currentTimeInMin current time in minutes - -1 if new day
     * @param today            string searching day
     * @param c                context
     * @return first timer, from database, null if no active timers
     */

    public static TimerProfile findTimersForDaySince(int currentTimeInMin, String today, Context c) {

        RealmHelper realmHelper = new RealmHelper(c);

        TimerProfile firstTimer = realmHelper.getFirstTimerForDaySinceTime(currentTimeInMin, today);

        return firstTimer;


    }

    /**
     * Convert int days to String shortcuts
     *
     * @param dayOfWeek integer of day 1-7
     * @return String of integer day
     */

    public static String getDayShortCutFromDayNumber(int dayOfWeek) {

        String ret = null;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                ret = "S";
                break;
            case Calendar.MONDAY:
                ret = "M";
                break;
            case Calendar.TUESDAY:
                ret = "T";
                break;
            case Calendar.WEDNESDAY:
                ret = "W";
                break;
            case Calendar.THURSDAY:
                ret = "R";
                break;
            case Calendar.FRIDAY:
                ret = "F";
                break;
            case Calendar.SATURDAY:
                ret = "U";
                break;
        }

        return ret;
    }


    /**
     * Calculate time to start new timer and convert to string
     *
     * @param timeToReceiveInMilis - time to receive in milisecconds
     * @return String of time to start
     */

    public static String convertTimeToReceiveToString(long timeToReceiveInMilis, Context c) {
        int hod, min;
        int timeToReceiveInMinutes = (int) (timeToReceiveInMilis / 1000 / 60);
        min = timeToReceiveInMinutes % 60;
        hod = timeToReceiveInMinutes / 60;
        String ret;
        if (hod==0 && min ==0){
            ret = c.getString(R.string.next_change_from_now) + c.getString(R.string.less_than_min);
        }
        else {
           ret = c.getString(R.string.next_change_from_now) + hod + ":" + TimerProfileHelper.getZeroBeforMinute(min);
        }
        return ret;

    }

    public static void turnOffTimer(int id, Context c) {
        turnOffTimerWithoutSearchingNext(id, c);
        AlarmControl.runNextTimer(c);
    }

    public static void turnOffTimerAssync(int id, Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);

        RealmHelper rh = new RealmHelper(c);
        rh.setTimerActivityAssyncAndStartNext(id, false, c);
        //if turning off active timer, change active id
        if (id == sharedPreferences.getInt(TimerProfileKeys.KEY_ID, -1))
            sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID, -1).apply();
    }

    public static void turnOffTimerWithoutSearchingNext(int id, Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);

        RealmHelper rh = new RealmHelper(c);
        rh.setTimerActivity(id, false);
        //if turning off active timer, change active id
        if (id == sharedPreferences.getInt(TimerProfileKeys.KEY_ID, -1))
            sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID, -1).apply();

    }
}
