package baranek.vojtech.audiomanager;

import baranek.vojtech.audiomanager.model.TimerProfile;

/**
 * Created by Farmas on 20.12.2015.
 */
public class AlarmControlHelper {

    private TimerProfile timerProfile;
    private long nextAlarmTime;

    public AlarmControlHelper(long nextAlarmTime, TimerProfile timerProfile) {
        this.nextAlarmTime = nextAlarmTime;
        this.timerProfile = timerProfile;
    }

    public TimerProfile getTimerProfile() {
        return timerProfile;
    }

    public void setTimerProfile(TimerProfile timerProfile) {
        this.timerProfile = timerProfile;
    }

    public long getNextAlarmTime() {
        return nextAlarmTime;
    }

    public void setNextAlarmTime(long nextAlarmTime) {
        this.nextAlarmTime = nextAlarmTime;
    }
}
