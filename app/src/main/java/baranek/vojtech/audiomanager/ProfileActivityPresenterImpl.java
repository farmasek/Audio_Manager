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

    @Override
    public TimerProfile getDefaultTimerProfile() {
        TimerProfile timer = new TimerProfile(1, "Ukazkovy casovac", TimerProfile.getCasFromHodMin(15, 35),
                TimerProfile.getCasFromHodMin(17, 46), 0, 1, 1, 2, 3, 4, 5, 6, true, "MTW");
        return timer;
    }

    @Override
    public TimerProfile getSelectedTimerProfile() {
        return null;
    }

    @Override
    public void putIntoDatabase(TimerProfile timerProfile) {

    }

    @Override
    public void setSeekersRange() {

        AudioManager audioManager = (AudioManager) profileActivityView.getContext().getSystemService(Context.AUDIO_SERVICE);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        profileActivityView.setSeekersRange(max);

    }
}
