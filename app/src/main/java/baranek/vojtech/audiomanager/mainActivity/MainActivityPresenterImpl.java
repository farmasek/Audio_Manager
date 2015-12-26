package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmCollisionChecker;
import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmControl;
import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import baranek.vojtech.audiomanager.profileActivity.ProfileActivity;

/**
 * Created by Farmas on 27.11.2015.
 */
public class MainActivityPresenterImpl implements MainActivityPresenter {

    private MainActivityView mainActivityView;

    public MainActivityPresenterImpl(MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
    }

    @Override
    public void startTimerProfileActivity(int id) {
        Intent i = new Intent(mainActivityView.getContext(), ProfileActivity.class);
        i.putExtra(TimerProfileKeys.KEY_ID, id);
        mainActivityView.startTimerProfileActivity(i);
    }

    @Override
    public Context getContext() {

        return mainActivityView.getContext();
    }

    @Override
    public boolean setTimerProfileActive(TimerProfile timerProfile) {
        RealmHelper rh = new RealmHelper(getContext());
        boolean ret;
        boolean collisionWithTimerProfiles = AlarmCollisionChecker.isCollisionWithTimerProfiles(timerProfile, getContext());
        if (collisionWithTimerProfiles) {
            mainActivityView.showToastMessage("Časovač není možno zapnout, je v koklizi s jiným.");
            rh.setTimerActivity(timerProfile, false);
            ret = false;
        } else {

            rh.setTimerActivity(timerProfile, true);
            //Start next Timer
            AlarmControl.runNextTimer(getContext());
          //  mainActivityView.showToastMessage("Časovač zapnut");
            ret = true;
        }

        return ret;

    }

    @Override
    public void setTimerProfileInActive(TimerProfile timerProfile) {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME,Context.MODE_PRIVATE);

        RealmHelper rh = new RealmHelper(getContext());
        rh.setTimerActivity(timerProfile, false);
        //if turning off active timer, change active id
        if (timerProfile.getId()== sharedPreferences.getInt(TimerProfileKeys.KEY_ID,-1))
        sharedPreferences.edit().putInt(TimerProfileKeys.KEY_ID,-1).apply();
        AlarmControl.runNextTimer(getContext());

    }
}
