package baranek.vojtech.audiomanager.alarmTimingUtil;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.model.TimerProfile;
import io.realm.RealmResults;

/**
 * Created by Farmas on 13.12.2015.
 */
public class AlarmCollisionChecker {

    public static boolean isCollisionWithTimerProfiles(TimerProfile timerProfile, Context c){
        boolean ret = false;
        int casDoKonce = timerProfile.getCasDoKonce();
        if (!timerProfile.isKonZap()){
            casDoKonce=0;
        }

        int konCasTestedTimer = timerProfile.getZacCas() +casDoKonce;

        RealmHelper realmHelper= new RealmHelper(c);
        RealmResults<TimerProfile> returnedTimersBetween = realmHelper.getQueryResBetween(timerProfile, getDaysArrayFromTimerProfile(timerProfile));
        if (returnedTimersBetween.size()>0){
            return true;
        }

        RealmResults<TimerProfile> returnedTimers = realmHelper.getQueryResNotBetween(timerProfile, getDaysArrayFromTimerProfile(timerProfile));
        for (TimerProfile t: returnedTimers) {

            if (t.isKonZap()){

                int tcasKon = t.getZacCas()+t.getCasDoKonce();
                //test start in interval
                if (t.getZacCas()<= timerProfile.getZacCas() && timerProfile.getZacCas() <= tcasKon) {
                    ret = true;
                }
                //test end in interval
                if (t.getZacCas()<= konCasTestedTimer && konCasTestedTimer <= tcasKon) {
                    ret = true;
                }
                //test for if interval goes to next day
                if(konCasTestedTimer>=1440){
                    int endTimeNextDay = konCasTestedTimer-1440;

                    if(t.getZacCas()>=0 && t.getZacCas()<=endTimeNextDay){
                        ret=true;
                    }
                    if(tcasKon>=0 && tcasKon<=endTimeNextDay){
                        ret=true;
                    }


                }

                //Test if returned timer goes to next day
                if(tcasKon>=1440){
                    int endTimeNextDay = tcasKon-1440;
                    //test for start
                    if(timerProfile.getZacCas()>=0 && timerProfile.getZacCas()<=endTimeNextDay){
                        ret=true;
                    }
                    if(konCasTestedTimer>=0 && konCasTestedTimer<=endTimeNextDay){
                        ret=true;
                    }

                }

            }else{
                if(konCasTestedTimer>=1440){
                    int endTimeNextDay = konCasTestedTimer-1440;

                    if(t.getZacCas()>=0 && t.getZacCas()<=endTimeNextDay){
                        ret=true;
                    }

                }

            }

        }


        return ret;
    }

    public static String[] getDaysArrayFromTimerProfile(TimerProfile timerProfile) {

        List<String> listDays = new ArrayList<>();

        if (timerProfile.getDny().contains("M"))listDays.add("M");
        if (timerProfile.getDny().contains("T"))listDays.add("T");
        if (timerProfile.getDny().contains("W"))listDays.add("W");
        if (timerProfile.getDny().contains("R"))listDays.add("R");
        if (timerProfile.getDny().contains("F"))listDays.add("F");
        if (timerProfile.getDny().contains("U"))listDays.add("U");
        if (timerProfile.getDny().contains("S"))listDays.add("S");

        String[] ret = listDays.toArray(new String[listDays.size()]);
        return ret;

    }
}
