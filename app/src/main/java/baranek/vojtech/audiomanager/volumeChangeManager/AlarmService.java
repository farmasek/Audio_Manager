package baranek.vojtech.audiomanager.volumeChangeManager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import baranek.vojtech.audiomanager.notifications.NotificationHelper;
import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmControl;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;

/**
 * Created by Farmas on 19.12.2015.
 */
public class AlarmService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AlarmService() {
        super("Alarm service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int id = intent.getIntExtra(TimerProfileKeys.KEY_ID, -1);
        boolean isEnd = intent.getBooleanExtra(TimerProfileKeys.KEY_ISITENDTIMER, false);
        RealmHelper realmHelper = new RealmHelper(getApplicationContext());

        SharedPreferences sharedPreferences = getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);


        TimerProfile timerProfile = realmHelper.getTimerProfileById(id);

        if (!isEnd) {
            //Do stuff for Start timer
            AndroidProfileChanger.changeVolumeProfile(getApplicationContext(), id, true);

            //If timer has end on, set sharedpreferences ID as Active;
            if (timerProfile.isKonZap()) {
                //  AlarmControl.runNextTimer(getApplicationContext());
                sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID, id).apply();
               NotificationHelper.showNotification(getApplicationContext(), sharedPreferences.getLong(TimerProfileKeys.KEY_KONTIMERECEIVEINMILLIS, 0));
            }


        }
        if (isEnd) {
            //Do stuff for End timer
            AndroidProfileChanger.changeVolumeProfile(getApplicationContext(), id, false);
            sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID, -1).apply();
            AlarmControl.runNextTimer(getApplicationContext());
        }


    }
}
