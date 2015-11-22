package baranek.vojtech.audiomanager;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by farmas on 9.11.2015.
 */
public class ProfileActivityPresenterImpl implements ProfileActivityPresenter {

    private ProfileActivityView profileActivityView;

    public ProfileActivityPresenterImpl(ProfileActivityView profileActivityView) {

        this.profileActivityView = profileActivityView;
    }

    /**
     * Create default timer for first usage
     *
     * @return - default timer
     */
    @Override
    public TimerProfile getDefaultTimerProfile() {
        TimerProfile timer = new TimerProfile(1, "Ukazkovy casovac", TimerProfileHelper.getCasFromHodMin(15, 35), 120, 0, 1, 1, 2, 3, 4, 5, 6, true, "MTW");
        return timer;
    }

    @Override
    public TimerProfile getSelectedTimerProfile() {
        return null;
    }

    @Override
    public void putIntoDatabase(TimerProfile timerProfile) {


    }

    /**
     * Check if end is bigger than start time.
     *
     * @param timerProfile
     * @param lEndTime     - end time from dialog
     */
    @Override
    public void setEndTimeIfGood(TimerProfile timerProfile, int lEndTime, boolean turnOnEnd) {

        int zacCas = timerProfile.getZacCas();

        timerProfile.setCasDoKonce(TimerProfileHelper.getCalculatedTimeToEnd(zacCas, lEndTime));
        setEndTextString(timerProfile);
        setEndStatus(timerProfile, turnOnEnd);

    }

    /**
     * Set formatted String into textview for start time
     *
     * @param timerProfile
     */
    @Override
    public void setStartTextString(TimerProfile timerProfile) {
        String formatedStartTime = TimerProfileHelper.getFormatedStartTime(timerProfile);
        profileActivityView.setTimePickerStartTime(formatedStartTime);
    }

    /**
     * Set formatted String into Textview for end time
     *
     * @param timerProfile
     */
    @Override
    public void setEndTextString(TimerProfile timerProfile) {
        String formatedEndTime = TimerProfileHelper.getFormatedEndTime(timerProfile);
        profileActivityView.setTimePickerEndTime(formatedEndTime);

    }

    /**
     * Set end status based on time
     *
     * @param timerProfile
     * @param checkActive  - true if set to active
     */
    @Override
    public void setEndStatus(TimerProfile timerProfile, boolean checkActive) {

        if (checkActive) {
            if (timerProfile.getCasDoKonce() == 0) {
                profileActivityView.showErrorSnackBar(profileActivityView.getContext().getString(R.string.same_time_error));
                timerProfile.setKonZap(false);
                profileActivityView.setCheckboxStatus(false);
            } else {
                timerProfile.setKonZap(true);
                profileActivityView.setCheckboxStatus(true);
            }

        } else {
            timerProfile.setKonZap(false);
            profileActivityView.setCheckboxStatus(false);

        }

    }


    /**
     * Set Sliders range
     */
    @Override
    public void setSeekersRange() {

        AudioManager audioManager = (AudioManager) profileActivityView.getContext().getSystemService(Context.AUDIO_SERVICE);
        int maxMedia = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int maxAlarm = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        int maxRing = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);


        profileActivityView.setSeekersRange(maxMedia, maxAlarm, maxRing);

    }
}
