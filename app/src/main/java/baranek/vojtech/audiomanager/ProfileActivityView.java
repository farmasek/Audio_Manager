package baranek.vojtech.audiomanager;

import android.content.Context;

/**
 * Created by Farmas on 08.11.2015.
 */
public interface ProfileActivityView {

    /**
     * Show timer profile
     */
    void showData(TimerProfile timerProfile);

    void setSeekersRange(int maxMedia, int maxRing, int maxAlarm);

    void setTimePickerStartTime(String time);

    void setTimePickerEndTime(String time);

    void setCheckboxStatus(boolean isActive);

    void showErrorSnackBar(String errorMessage);

    Context getContext();

}
