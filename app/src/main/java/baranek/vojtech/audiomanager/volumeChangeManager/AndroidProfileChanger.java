package baranek.vojtech.audiomanager.volumeChangeManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;

/**
 * Created by Farmas on 22.12.2015.
 */
public class AndroidProfileChanger {

    /**
     * Change volume profile based on saved settings
     * @param c - context
     * @param id - active TimerProfile id
     * @param isZac - true if start time, false if end time
     */
    public static void changeVolumeProfile(Context c, int id, boolean isZac){
        RealmHelper realmHelper = new RealmHelper(c);
        TimerProfile timerProfile = realmHelper.getTimerProfileById(id);

        if (isZac)
        {
           int mode = timerProfile.getZacRez();

            switch (mode){
                case 0:
                    setNormalMode(c,timerProfile.getZacMedia(), timerProfile.getZacVyzvaneni(),timerProfile.getZacAlarm(),timerProfile.getZacOzn(),false);
                    break;
                case 1:
                    setSilentMode(c);
                    break;
                case 2:
                    setVibrateMode(c);
                    break;
            }

        }else
        {
            int mode = timerProfile.getKonRez();

            switch (mode){
                case 0:
                    setNormalMode(c,timerProfile.getKonMedia(), timerProfile.getKonVyzvaneni(),timerProfile.getKonAlarm(),timerProfile.getKonOzn(),false);
                    break;
                case 1:
                    setSilentMode(c);
                    break;
                case 2:
                    setVibrateMode(c);
                    break;
            }
        }

    }


    public static void setNormalMode(Context c, int volMed, int volRing, int volAlarm, int volNot,boolean setOnlyMode){

        SharedPreferences sharedPreferences = c.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);
        boolean isNotifActiv = sharedPreferences.getBoolean(TimerProfileKeys.KEY_ISNOTIFACTIVE,true);

        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        if (!setOnlyMode){
        audioManager.setStreamVolume(AudioManager.STREAM_RING, volRing, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volMed,0);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,volAlarm,0);
        if(isNotifActiv)
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,volNot,0);}
    }

    public static void setVibrateMode(Context c){
         AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
    public static void setSilentMode(Context c){
        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

}
