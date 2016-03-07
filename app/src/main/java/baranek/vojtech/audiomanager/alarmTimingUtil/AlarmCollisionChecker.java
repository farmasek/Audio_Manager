package baranek.vojtech.audiomanager.alarmTimingUtil;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import baranek.vojtech.audiomanager.model.RealmHelper;
import baranek.vojtech.audiomanager.model.TimerProfile;
import io.realm.RealmResults;

/**
 * Created by Farmas on 13.12.2015.
 */
public class AlarmCollisionChecker {

    public static boolean isCollisionWithTimerProfiles(TimerProfile timerProfile, Context c) {
        boolean ret = false;
        RealmHelper realmHelper = new RealmHelper(c);

        //Checking, if there is saved timer with start time in collision
        RealmResults<TimerProfile> returnedTimersBetween = realmHelper.getQueryResBetween(timerProfile, getDaysArrayFromTimerProfile(timerProfile));
        if (returnedTimersBetween.size() > 0) {
            return true;
        }
        //Getting saved timers, not in collision for checking collision with saving timer interval
        RealmResults<TimerProfile> returnedTimers = realmHelper.getQueryResNotBetween(timerProfile, getDaysArrayFromTimerProfile(timerProfile));
        for (TimerProfile t : returnedTimers) {

            if (t.isKonZap()){

                //checking for start time of saving timer
                if (t.getZacCas()<=timerProfile.getZacCas() && t.getZacCas()+t.getCasDoKonce()>=timerProfile.getZacCas())
                    return true;
                //checking for end time of saving timer, same as start if off
                if (t.getZacCas()<=timerProfile.getZacCas()+timerProfile.getCasDoKonce() && t.getZacCas()+t.getCasDoKonce()>=timerProfile.getZacCas()+timerProfile.getCasDoKonce())
                    return true;
            }

        }
        //Check collision of saving timer, if last to next day
        int savingTimerEndTime = timerProfile.getZacCas()+timerProfile.getCasDoKonce();
        if(savingTimerEndTime>1440){
            savingTimerEndTime=savingTimerEndTime-1440;
        TimerProfile rightDaysProfile = new TimerProfile() ;
            //= moveDaysToRight(timerProfile);
            rightDaysProfile.setDny(moveDaysToRight(timerProfile).getDny());
            rightDaysProfile.setZacCas(0);
            rightDaysProfile.setCasDoKonce(savingTimerEndTime);
            rightDaysProfile.setKonZap(timerProfile.isKonZap());
            rightDaysProfile.setId(timerProfile.getId());

        RealmResults<TimerProfile> returnedTimersBetweenForNextDay = realmHelper.getQueryResBetween(rightDaysProfile, getDaysArrayFromTimerProfile(rightDaysProfile));
        if (returnedTimersBetweenForNextDay.size() > 0) {
            return true;
        }}

        //Check for collision of saved timers for next day
        List<TimerProfile> listOfNextDayTimers = new ArrayList<>();

        RealmResults<TimerProfile> returnedAllExceptOne = realmHelper.getQueryResAllExceptCurrent(timerProfile);
        for (TimerProfile savedTimer : returnedAllExceptOne
             ) {
            //Create new list for timers lasts for next day
            if (savedTimer.isKonZap()){
                int savedTimerEndTime = savedTimer.getZacCas()+ savedTimer.getCasDoKonce();
                if (savedTimerEndTime>1440 ){
                    savedTimerEndTime = savedTimerEndTime-1440;
                    TimerProfile nextDayTimer = new TimerProfile();
                    nextDayTimer.setZacCas(0);
                    nextDayTimer.setCasDoKonce(savedTimerEndTime);
                    nextDayTimer.setDny(moveDaysToRight(savedTimer).getDny());
                    nextDayTimer.setKonZap(savedTimer.isKonZap());
                    nextDayTimer.setId(savedTimer.getId());
                    listOfNextDayTimers.add(nextDayTimer);
                }
            }
        }
        for (TimerProfile nextDaySavedTimer:listOfNextDayTimers
             ) {
            //check for days match
            boolean dayCheck = false;
            for (String savedDay: getDaysArrayFromTimerProfile(nextDaySavedTimer)
                 ) {

                for (String savingDay: getDaysArrayFromTimerProfile(timerProfile)
                        ) {
                    if (savedDay.contains(savingDay)){
                        dayCheck=true;
                    }

                }

            }

            if (dayCheck){
                if (timerProfile.getZacCas()<=nextDaySavedTimer.getZacCas()+nextDaySavedTimer.getCasDoKonce()){
                    return true;
                }
                if (timerProfile.getZacCas()+timerProfile.getCasDoKonce()<=nextDaySavedTimer.getZacCas()+nextDaySavedTimer.getCasDoKonce()){
                    return true;
                }
            }


        }


        return false;
    }




    private static TimerProfile moveDaysToRight(TimerProfile timerProfile) {

        StringBuilder strDays = new StringBuilder();

        if (timerProfile.getDny().contains("M")) strDays.append("T");
        if (timerProfile.getDny().contains("T")) strDays.append("W");
        if (timerProfile.getDny().contains("W")) strDays.append("R");
        if (timerProfile.getDny().contains("R")) strDays.append("F");
        if (timerProfile.getDny().contains("F")) strDays.append("U");
        if (timerProfile.getDny().contains("U")) strDays.append("S");
        if (timerProfile.getDny().contains("S")) strDays.append("M");

        TimerProfile timerProfile1 = new TimerProfile();
        timerProfile1.setDny(strDays.toString());

        return timerProfile1;
    }

    private static TimerProfile moveDaysToLeft(TimerProfile timerProfile) {

        StringBuilder strDays = new StringBuilder();

        if (timerProfile.getDny().contains("M")) strDays.append("S");
        if (timerProfile.getDny().contains("T")) strDays.append("M");
        if (timerProfile.getDny().contains("W")) strDays.append("T");
        if (timerProfile.getDny().contains("R")) strDays.append("W");
        if (timerProfile.getDny().contains("F")) strDays.append("R");
        if (timerProfile.getDny().contains("U")) strDays.append("F");
        if (timerProfile.getDny().contains("S")) strDays.append("U");

        TimerProfile timerProfile1 = new TimerProfile();
        timerProfile1.setDny(strDays.toString());

        return timerProfile1;
    }

    public static String[] getDaysArrayFromTimerProfile(TimerProfile timerProfile) {

        List<String> listDays = new ArrayList<>();

        if (timerProfile.getDny().contains("M")) listDays.add("M");
        if (timerProfile.getDny().contains("T")) listDays.add("T");
        if (timerProfile.getDny().contains("W")) listDays.add("W");
        if (timerProfile.getDny().contains("R")) listDays.add("R");
        if (timerProfile.getDny().contains("F")) listDays.add("F");
        if (timerProfile.getDny().contains("U")) listDays.add("U");
        if (timerProfile.getDny().contains("S")) listDays.add("S");

        String[] ret = listDays.toArray(new String[listDays.size()]);
        return ret;

    }
}
