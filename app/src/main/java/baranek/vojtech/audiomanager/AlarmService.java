package baranek.vojtech.audiomanager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

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


        TimerProfile timerProfile=realmHelper.getTimerProfileById(id);

        if (!isEnd){
            //Do stuff for Start timer
            Log.i("alarmSERVICE", "Received START service id : "+ intent.getIntExtra(TimerProfileKeys.KEY_ID,-1));

            //If timer has end on, set sharedpreferences ID as Active;
            if (timerProfile.isKonZap()){
              //  AlarmControl.runNextTimer(getApplicationContext());
                sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID,id).apply();
            }


        }
        if(isEnd){
            //Do stuff for End timer
            Log.i("alarmSERVICE", "Received END service id : "+ intent.getIntExtra(TimerProfileKeys.KEY_ID,-1));
            sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID,-1).apply();
            AlarmControl.runNextTimer(getApplicationContext());
        }


        //TODO Change ring modes etc
      /*  AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);*/


        //Find and run next timer


    }
}
