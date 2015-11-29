package baranek.vojtech.audiomanager.mainActivity;

import android.content.Intent;

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
        i.putExtra(TimerProfileKeys.KEY_ID,id);
       mainActivityView.startTimerProfileActivity(i);
    }
}
