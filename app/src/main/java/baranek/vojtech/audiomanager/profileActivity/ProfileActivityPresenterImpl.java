package baranek.vojtech.audiomanager.profileActivity;

import android.content.Context;
import android.media.AudioManager;

import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmCollisionChecker;
import baranek.vojtech.audiomanager.alarmTimingUtil.AlarmControl;
import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.model.RealmHelper;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileHelper;

/**
 * Created by farmas on 9.11.2015.
 */
public class ProfileActivityPresenterImpl implements ProfileActivityPresenter {

    public ProfileActivityView profileActivityView;
    private RealmHelper realmHelper;
    private boolean searchNextTimer = false;

    public ProfileActivityPresenterImpl(ProfileActivityView profileActivityView) {

        this.profileActivityView = profileActivityView;
        realmHelper = new RealmHelper(this.profileActivityView.getContext());
    }

    /**
     * Create default timer for first usage
     *
     * @return - default timer
     */
    @Override
    public TimerProfile getDefaultTimerProfile() {
        TimerProfile timer = new TimerProfile(
                realmHelper.getNextRealmId(),
                profileActivityView.getContext().getString(R.string.default_timer_name),
                TimerProfileHelper.getCasFromHodMin(15, 35), 120, 0, 1, 5, 4, 3, 4, 5, 3, 3, 3,
                true, "MTW", false);
        return timer;
    }

    @Override
    public TimerProfile getSelectedTimerProfile(int id) {
        TimerProfile timerProfile = realmHelper.getTimerProfileById(id);
        return timerProfile;
    }

    @Override
    public void putIntoDatabase(TimerProfile timerProfile) {
        realmHelper.insertTimerProfileIntoRealm(timerProfile);
    }

    @Override
    public void editIteminDatabase(TimerProfile timerProfile) {

        realmHelper.editItemInRealm(timerProfile);

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
        String formatedEndTime = TimerProfileHelper.getFormatedEndTime(timerProfile,profileActivityView.getContext());
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
        int maxNot = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);


        profileActivityView.setSeekersRange(maxMedia, maxAlarm, maxRing, maxNot);

    }

    @Override
    public void closeRealm() {
        realmHelper.close();
    }

    @Override
    public TimerProfile getSelectedOrDefaultTimer(int id) {

        TimerProfile timerProfile;
        if (id == -1) {
            timerProfile = getDefaultTimerProfile();
        } else {

            timerProfile = getSelectedTimerProfile(id);
        }

        return timerProfile;
    }

    @Override
    public void profileActivityButtonClick(TimerProfile timerProfile, int id) {

        if (id!=-1){
         AlarmControl.turnOffTimerWithoutSearchingNext(id,profileActivityView.getContext());
            searchNextTimer=true;
        }

        timerProfile.setIsTimerZap(!AlarmCollisionChecker.isCollisionWithTimerProfiles(timerProfile, profileActivityView.getContext()));

        boolean run = false;
        if (id == -1) {
            if (timerProfile.getDny().equals("")) {
                profileActivityView.showErrorSnackBar(profileActivityView.getContext().getString(R.string.day_must_be_chosen));
            } else {
                putIntoDatabase(timerProfile);
                profileActivityView.finishView();
                run=true;

            }
        } else {
            if (timerProfile.getDny().equals("")) {
                profileActivityView.showErrorSnackBar(profileActivityView.getContext().getString(R.string.day_must_be_chosen));
            } else {
                editIteminDatabase(timerProfile);
                profileActivityView.finishView();
                run=true;

            }
        }

        if (timerProfile.isTimerZap()&&run) {
            searchNextTimer=false;
            AlarmControl.runNextTimer(profileActivityView.getContext());
        }
    }

    @Override
    public void setDefaultProfileView(int id) {
        setSeekersRange();
        if (id == -1) {
            profileActivityView.setMenuItemsVisible(false);
            profileActivityView.setToolbarTitle(profileActivityView.getContext().getResources().getString(R.string.str_create_profil));
            profileActivityView.setFabIcon(R.drawable.ic_done_white_24dp);

        } else {
            profileActivityView.setMenuItemsVisible(true);
            profileActivityView.setToolbarTitle(profileActivityView.getContext().getResources().getString(R.string.str_edit_profil));
            profileActivityView.setFabIcon(R.drawable.ic_save_white_24dp);
        }
        TimerProfile timerProfile = getSelectedOrDefaultTimer(id);
        profileActivityView.setViewTimerProfile(timerProfile);
        profileActivityView.showData();

    }

    @Override
    public String[] getRezimy() {
        return profileActivityView.getContext().getResources().getStringArray(R.array.sound_modes);
    }

    @Override
    public void deleteTimer(int id) {
        AlarmControl.turnOffTimer(id,profileActivityView.getContext());
        realmHelper.deleteTimerFromRealm(id);

    }

    @Override
    public void searchNextTimer() {
        if (searchNextTimer){
        searchNextTimer=false;
        AlarmControl.runNextTimer(profileActivityView.getContext());}
    }
}
