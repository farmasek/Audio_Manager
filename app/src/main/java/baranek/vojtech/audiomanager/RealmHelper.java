package baranek.vojtech.audiomanager;

import android.content.Context;

import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Farmas on 26.11.2015.
 */
public class RealmHelper {

    private Realm realm=null;
    private   RealmConfiguration config;
    public RealmHelper(Context c) {

      config = new RealmConfiguration.Builder(c)
                .schemaVersion(4)
               // .migration(new TimerProfileMigration())
              .deleteRealmIfMigrationNeeded()
                .build();


    if (realm ==null || realm.isClosed())
        realm = Realm.getInstance(config);


    }

    public  void insertTimerProfileIntoRealm( final TimerProfile timerProfile){


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TimerProfile realmTimerProfile = realm.copyToRealm(timerProfile);
            }
        });

    }
    public  int getNextRealmId(){
        int nextId =0 ;
        boolean empty = realm.isEmpty();
        if (!empty)
        nextId= realm.where(TimerProfile.class).max("id").intValue()+1;

        return nextId;
    }


    public  RealmResults<TimerProfile> getRealmResults() {

        RealmResults<TimerProfile> realmResults;
        realmResults=realm.where(TimerProfile.class)
                .findAll();

        return realmResults;
    }


    public RealmResults<TimerProfile> getQueryResBetween(TimerProfile timerProfile, String[] strDays){

        int casDoKonce = timerProfile.getCasDoKonce();
        if (!timerProfile.isKonZap()){
            casDoKonce=0;
        }

        RealmResults<TimerProfile> realmResults;
        RealmQuery<TimerProfile> query = realm.where(TimerProfile.class);

        query.not().equalTo("id", timerProfile.getId());
        query.equalTo("isTimerZap", true);
        query.beginGroup();
        for (int i = 0; i < strDays.length; i++) {
            query.contains("dny", strDays[i]);

            if (i!=strDays.length-1)
                query.or();
        }
        query.endGroup();
        query.between("zacCas", timerProfile.getZacCas(), timerProfile.getZacCas() + casDoKonce);

        realmResults=query.findAll();
        return realmResults;
    }
    public RealmResults<TimerProfile> getQueryResNotBetween(TimerProfile timerProfile, String[] strDays){

        int casDoKonce = timerProfile.getCasDoKonce();
        if (!timerProfile.isKonZap()){
            casDoKonce=0;
        }

        RealmResults<TimerProfile> realmResults;
        RealmQuery<TimerProfile> query = realm.where(TimerProfile.class);

        query.not().equalTo("id", timerProfile.getId());
        query.equalTo("isTimerZap", true);
        query.beginGroup();
        for (int i = 0; i < strDays.length; i++) {
            query.contains("dny", strDays[i]);

            if (i!=strDays.length-1)
                query.or();
        }
        query.endGroup();
        query.not().between("zacCas", timerProfile.getZacCas(), timerProfile.getZacCas() + casDoKonce);

        realmResults=query.findAll();
        return realmResults;
    }
    public TimerProfile getFirstTimerForDaySinceTime (int currentTimeInMin, String today){

        TimerProfile realmResult=null;
        RealmQuery<TimerProfile> query = realm.where(TimerProfile.class);
        query.equalTo("isTimerZap", true);
        query.contains("dny", today);
        query.greaterThan("zacCas", currentTimeInMin);
        RealmResults<TimerProfile> realmResults = query.findAllSorted("zacCas", Sort.ASCENDING);
        if(!realmResults.isEmpty()){
            realmResult=realmResults.first();
        }

        return realmResult;
    }



    public TimerProfile getTimerProfileById(int id ){

    /*   RealmResults<TimerProfile> timerProfiles = realm.where(TimerProfile.class)
                .equalTo(TimerProfileKeys.KEY_ID, id)
                .findAllSorted("zacCas");
        TimerProfile timerProfile = timerProfiles.first();*/

        RealmQuery<TimerProfile> query = realm.where(TimerProfile.class);
        query.equalTo(TimerProfileKeys.KEY_ID, id);
        TimerProfile timerProfile = query.findFirst();
        return timerProfile;

    }

    public void close(){

        if (realm !=null || !realm.isClosed())
        realm.close();
    }

    public  void deleteTimerFromRealm(int id) {

        realm.beginTransaction();
        TimerProfile t = realm.where(TimerProfile.class)
                .equalTo(TimerProfileKeys.KEY_ID,id)
                .findFirst();
        t.removeFromRealm();

        realm.commitTransaction();

    }

    public void editItemInRealm(TimerProfile timerProfile) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(timerProfile);
        realm.commitTransaction();
    }

    public void setTimerActivity(TimerProfile timerProfile, boolean b) {

        realm.beginTransaction();
        TimerProfile retTimer = realm.where(TimerProfile.class)
                .equalTo(TimerProfileKeys.KEY_ID, timerProfile.getId())
                .findFirst();
        retTimer.setIsTimerZap(b);
        realm.commitTransaction();
    }
}
