package baranek.vojtech.audiomanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;

/**
 * Created by Farmas on 18.12.2015.
 */
public class AlarmControl {

    public static void runNextTimer(Context c){

        AlarmControlHelper alarmControlHelper = getNextTimer(c);

        runTimerReceiver(c,alarmControlHelper);
    }

    public static void runTimerReceiver(Context c, AlarmControlHelper alarmControlHelper) {

        TimerProfile nextTimer = alarmControlHelper.getTimerProfile();
        long receiverTime = alarmControlHelper.getNextAlarmTime();

        if (nextTimer!=null) {

            Calendar cal = Calendar.getInstance();
            long t = receiverTime - cal.getTimeInMillis();
            t= t/1000/60;
            Toast.makeText(c,"timer se spusti sa:"+t,Toast.LENGTH_SHORT).show();
            Log.i("ALARMCONTROL", "Next Timer in " + t + " id : " + nextTimer.getId());

            //if timer is already active, skip
            if (c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME,Context.MODE_PRIVATE).getInt(TimerProfileKeys.KEY_ID,-1)==-1){

                Log.i("ALARMCONTROL", "Timer is not active id: " + nextTimer.getId());
            //Start of timer
            Intent intent = new Intent(c, AlarmReceiver.class);
            intent.putExtra(TimerProfileKeys.KEY_ID, nextTimer.getId());
            intent.putExtra(TimerProfileKeys.KEY_ISITENDTIMER,false);

            PendingIntent pint = PendingIntent.getBroadcast(c, 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pint);
            alarmManager.set(AlarmManager.RTC, receiverTime, pint);


            //End of timer
            Intent intentKon = new Intent(c,AlarmReceiver.class);
            intentKon.putExtra(TimerProfileKeys.KEY_ID, nextTimer.getId());
            intentKon.putExtra(TimerProfileKeys.KEY_ISITENDTIMER, true);

            PendingIntent pintKon = PendingIntent.getBroadcast(c,100,intentKon,PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pintKon);

            if (nextTimer.isKonZap())
            {
                long endReceiverTime = receiverTime + nextTimer.getCasDoKonce()*60*1000;
                alarmManager.set(AlarmManager.RTC,endReceiverTime,pintKon);

            }



        }
        }
    }


    /**
     * Scan for active TimerProfiles and run the nearest to actual time
     * @param c context
     */


    public static AlarmControlHelper getNextTimer(Context c) {
        long receiverTime=0;
        Calendar cal = Calendar.getInstance();
        // Sunday = 1 ... Saturday = 7
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int dayCounter = 1;

        int currTimeInMinutes = cal.get(Calendar.HOUR_OF_DAY)*60*60 + cal.get(Calendar.MINUTE)*60+cal.get(Calendar.SECOND);
        String strDayOfWeek = getDayShortCutFromDayNumber(dayOfWeek);


        //Checking next Active timer, return null if no active
        TimerProfile timer = findTimersForDaySince(currTimeInMinutes/60, strDayOfWeek, c);

        //setting time to run receiver from now
        if(timer!=null)
        {
            receiverTime= cal.getTimeInMillis() + (timer.getZacCas()*60-currTimeInMinutes)*1000;
        }
        else{
            int h = 1440*60 - currTimeInMinutes;
            receiverTime=cal.getTimeInMillis() + h*1000;
        }

        //looking for another day, timer == null if no active timer, max days 7
        while (timer==null && dayCounter<8){
            dayOfWeek= addOneDay(dayOfWeek);
            strDayOfWeek = getDayShortCutFromDayNumber(dayOfWeek);
            timer=findTimersForDaySince(-1, strDayOfWeek, c);

            if(timer==null) {
                receiverTime = receiverTime + 24 * 60 * 60 * 1000;
            }
            else {
                receiverTime = receiverTime + timer.getZacCas()*60*1000;
            }
            dayCounter++;
        }

        return new AlarmControlHelper(receiverTime,timer);
    }


    /**
     * add one day, change to 1 if saturday (7)
     * @param day current day
     * @return next day
     */
    private static int addOneDay(int day){

        if (day==7)
        {
            day = 1;
        }
        else{
            day++;
        }

        return day;
    }

    /**
     * find active timerprofiles for particular day since particular time
     * @param currentTimeInMin current time in minutes - -1 if new day
     * @param today string searching day
     * @param c context
     * @return first timer, from database, null if no active timers
     */

    private static TimerProfile findTimersForDaySince(int currentTimeInMin, String today, Context c){

        RealmHelper realmHelper = new RealmHelper(c);

        TimerProfile firstTimer = realmHelper.getFirstTimerForDaySinceTime(currentTimeInMin, today);

        return firstTimer;


    }

    /**
     * Convert int days to String shortcuts
     * @param dayOfWeek integer of day 1-7
     * @return String of integer day
     */

    private static String getDayShortCutFromDayNumber(int dayOfWeek){

        String ret = null;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                ret="S";
                break;
            case Calendar.MONDAY:
                ret="M";
                break;
            case Calendar.TUESDAY:
                ret="T";
                break;
            case Calendar.WEDNESDAY:
                ret="W";
                break;
            case Calendar.THURSDAY:
                ret="R";
                break;
            case Calendar.FRIDAY:
                ret="F";
                break;
            case Calendar.SATURDAY:
                ret="U";
                break;
        }

        return ret;
    }

}
