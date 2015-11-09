package baranek.vojtech.audiomanager;

/**
 * Created by farmas on 9.11.2015.
 */
public interface ProfileActivityPresenter {

    TimerProfile getDefaultTimerProfile();

    TimerProfile getSelectedTimerProfile();

    void putIntoDatabase(TimerProfile timerProfile);

    void setSeekersRange();

}
