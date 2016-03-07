package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmCollisionChecker;
import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmControl;
import baranek.vojtech.audiomanager.model.RealmHelper;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileHelper;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import baranek.vojtech.audiomanager.profileActivity.ProfileActivity;

/**
 * Created by Farmas on 27.11.2015.
 */
public class MainActivityPresenterImpl implements MainActivityPresenter {

    private MainActivityView mainActivityView;

    public MainActivityPresenterImpl(MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
        //Show dialog for first start or update
        SharedPreferences sharedPreferences = mainActivityView.getContext().getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);
        //change number for every update
        if (sharedPreferences.getInt(TimerProfileKeys.KEY_FIRSTRUN,0)==0)
        {
            AlarmControl.runNextTimer(getContext());
            showWelcomeDialog();
            //Create new timer for first application start
           if(sharedPreferences.getInt(TimerProfileKeys.KEY_FIRSTRUN,0)==0){
            _createDefaultTimer();
        }

        }
        //Change version for update, 0 = first start
        sharedPreferences.edit().putInt(TimerProfileKeys.KEY_FIRSTRUN,1).apply();
    }

    private void _createDefaultTimer() {
        RealmHelper realmHelper = new RealmHelper(getContext());
        TimerProfile timer = new TimerProfile(realmHelper.getNextRealmId(), mainActivityView.getContext().getString(R.string.default_timer_name), TimerProfileHelper.getCasFromHodMin(15, 35), 120, 0, 1, 1, 2, 3, 4, 5, 6, 5, 4, true, "MTW", false);
        realmHelper.insertTimerProfileIntoRealm(timer);
    }

    /**
     * Start Timer profile activity
     * @param id -1 for new profile, other for editing
     */
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

            ret = true;
        }

        return ret;

    }

    @Override
    public void setTimerProfileInActive(int id) {

        AlarmControl.turnOffTimer(id,getContext());

    }

    @Override
    public void showWelcomeDialog() {
        mainActivityView.showWelcomeDialog(R.string.update_string);
    }

    @Override
    public Intent openEmailIntent() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:vojtabaranek@gmail.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getContext().getString(R.string.app_name));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            return intent;
        }
        return null;
    }
}
