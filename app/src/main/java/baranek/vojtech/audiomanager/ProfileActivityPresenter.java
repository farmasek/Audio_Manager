package baranek.vojtech.audiomanager;

/**
 * Created by farmas on 9.11.2015.
 */
public interface ProfileActivityPresenter {

    TimerProfile getDefaultTimerProfile();

    TimerProfile getSelectedTimerProfile();

    void putIntoDatabase(TimerProfile timerProfile);

    void setEndTimeIfGood(TimerProfile timerProfile, int lEndTime, boolean turnOnEnd);

    void setStartTextString(TimerProfile timerProfile);

    void setEndTextString(TimerProfile timerProfile);

    void setEndStatus(TimerProfile timerProfile, boolean checkActive);

    void setSeekersRange();

}
