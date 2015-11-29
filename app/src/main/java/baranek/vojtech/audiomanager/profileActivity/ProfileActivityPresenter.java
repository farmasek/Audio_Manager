package baranek.vojtech.audiomanager.profileActivity;

import baranek.vojtech.audiomanager.model.TimerProfile;

/**
 * Created by farmas on 9.11.2015.
 */
public interface ProfileActivityPresenter {

    TimerProfile getDefaultTimerProfile();

    TimerProfile getSelectedTimerProfile(int id);

    void putIntoDatabase(TimerProfile timerProfile);

    void editIteminDatabase(TimerProfile timerProfile);

    void setEndTimeIfGood(TimerProfile timerProfile, int lEndTime, boolean turnOnEnd);

    void setStartTextString(TimerProfile timerProfile);

    void setEndTextString(TimerProfile timerProfile);

    void setEndStatus(TimerProfile timerProfile, boolean checkActive);

    void setSeekersRange();

    void closeRealm();

    /**
     * Set selected timer, default if new timer
     * @param id  - timer id, -1 if new timer
     * @return TimerProfile
     */
    TimerProfile getSelectedOrDefaultTimer(int id);

    /**
     * Save timer if new, edit if edited
     * @param timerProfile - data to save
     * @param id - -1 if new
     */
    void profileActivityButtonClick(TimerProfile timerProfile, int id);

    void setDefaultProfileView(int id);

    String[] getRezimy();

    void deleteTimer(int id);
}
