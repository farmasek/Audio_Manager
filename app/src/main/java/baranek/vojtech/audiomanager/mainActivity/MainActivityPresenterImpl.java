package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import baranek.vojtech.audiomanager.AlarmCollisionChecker;
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
        if (collisionWithTimerProfiles)
        {Toast.makeText(getContext(), "Časovač není možno zapnout, je v koklizi s jiným.", Toast.LENGTH_SHORT).show();
            rh.setTimerActivity(timerProfile,false);
        ret=false;}
        else{
            Toast.makeText(getContext(), "Časovač zapnut", Toast.LENGTH_SHORT).show();
            rh.setTimerActivity(timerProfile,true);
            ret=true;}

        return ret;



        //TODO LOGIC FOR SETTING TIMER ON
    }

    @Override
    public void setTimerProfileInActive(TimerProfile timerProfile) {

        //TODO LOGIC FOR SETTING TIMER OFF
        RealmHelper rh = new RealmHelper(getContext());
        rh.setTimerActivity(timerProfile,false);

    }
}
