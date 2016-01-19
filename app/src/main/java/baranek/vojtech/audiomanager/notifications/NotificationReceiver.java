package baranek.vojtech.audiomanager.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Calendar;

import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmControl;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import io.realm.RealmResults;

/**
 * Created by Farmas on 26.12.2015.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        switch (action) {

            case TimerProfileKeys.KEY_NOTIFVYPONETIMER:
                //Notification click for turn off one timerprofile
                turnOffOneTimer(context);

                refreshMainActivityIfActive(context);
                break;

            case TimerProfileKeys.KEY_NOTIFVYPALLTIMERS:
                //Notification click for turn off all timerprofiles
                turnOffAllTimers(context);
                refreshMainActivityIfActive(context);
                NotificationHelper.closeNotification(context);
                break;

        }
    }

    private void turnOffAllTimers(Context context) {

        RealmHelper realmHelper = new RealmHelper(context);
        RealmResults<TimerProfile> allZapTimerProfiles = realmHelper.getAllZapTimerProfiles();

        for (int i = 0; i< allZapTimerProfiles.size();i++) {
            realmHelper.setTimerActivityAssync(allZapTimerProfiles.get(i).getId(), false, context);
        }
        AlarmControl.stopBothTimers(context);
    }

    private void refreshMainActivityIfActive(Context context) {
        Intent i = new Intent(TimerProfileKeys.INTENT_FILTER_KEY);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }

    private void turnOffOneTimer(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(TimerProfileKeys.KEY_ID, -1);
        //if timer is active
        if (-1 != id) {
            AlarmControl.turnOffTimerAssync(id, context);
        } else {
            //If there is no timerprofile active, then find next and turn off
            Calendar cal = Calendar.getInstance();
            int currTimeInMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
            TimerProfile timer;
            // Sunday = 1 ... Saturday = 7
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            String strDayOfWeek = AlarmControl.getDayShortCutFromDayNumber(dayOfWeek);
            int dayCounter = 1;
            timer = AlarmControl.findTimersForDaySince(currTimeInMinutes / 60, strDayOfWeek, context);

            //looking for another day, timer == null if no active timer, max days 7
            while (timer == null && dayCounter < 8) {
                dayOfWeek = AlarmControl.addOneDay(dayOfWeek);
                strDayOfWeek = AlarmControl.getDayShortCutFromDayNumber(dayOfWeek);
                timer = AlarmControl.findTimersForDaySince(-1, strDayOfWeek, context);
                dayCounter++;
            }
            //if there is next timer, turn him off
            if (timer != null) {
                AlarmControl.turnOffTimerAssync(timer.getId(), context);
            }

        }

    }
}
