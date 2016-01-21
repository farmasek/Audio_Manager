package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import baranek.vojtech.audiomanager.R;
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
            mainActivityView.showToastMessage(getContext().getString(R.string.timer_collision));
            rh.setTimerActivity(timerProfile.getId(), false);
            ret = false;
        } else {

            rh.setTimerActivity(timerProfile.getId(), true);
            //Start next Timer
            AlarmControl.runNextTimer(getContext());
          //  mainActivityView.showToastMessage("Časovač zapnut");
            ret = true;
        }

        return ret;

    }

    @Override
    public void setTimerProfileInActive(int id) {

        AlarmControl.turnOffTimer(id,getContext());

    }
}
