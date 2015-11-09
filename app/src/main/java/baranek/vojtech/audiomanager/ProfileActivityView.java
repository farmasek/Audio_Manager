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

    void setSeekersRange(int max);

    Context getContext();

}
