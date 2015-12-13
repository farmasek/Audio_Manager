package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;

import baranek.vojtech.audiomanager.model.TimerProfile;

/**
 * Created by Farmas on 27.11.2015.
 */
public interface MainActivityPresenter {

    void startTimerProfileActivity(int id);

    Context getContext();

    boolean setTimerProfileActive(TimerProfile timerProfile);

    void setTimerProfileInActive(TimerProfile timerProfile);
}
