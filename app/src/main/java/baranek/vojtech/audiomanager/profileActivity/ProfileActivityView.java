package baranek.vojtech.audiomanager.profileActivity;

import android.content.Context;

import baranek.vojtech.audiomanager.model.TimerProfile;
import io.realm.Realm;

/**
 * Created by Farmas on 08.11.2015.
 */
public interface ProfileActivityView {

    /**
     * Show timer profile
     */
    void showData();

    TimerProfile collectTimerProfileDataFromViews();

    void setSeekersRange(int maxMedia, int maxRing, int maxAlarm, int maxNot);

    void setTimePickerStartTime(String time);

    void setTimePickerEndTime(String time);

    void setCheckboxStatus(boolean isActive);

    void showErrorSnackBar(String errorMessage);

    Context getContext();

    void setViewTimerProfile(TimerProfile timerProfile );

    void setMenuItemsVisible(boolean visibility);

    void setToolbarTitle(String title);

    void setFabIcon(int iconID);

    void finishView();



}
