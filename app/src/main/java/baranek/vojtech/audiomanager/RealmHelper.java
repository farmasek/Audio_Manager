package baranek.vojtech.audiomanager;

import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Farmas on 26.11.2015.
 */
public class RealmHelper {

    private Realm realm=null;

    public RealmHelper() {
    if (realm ==null || realm.isClosed())
        realm = Realm.getDefaultInstance();
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

    public TimerProfile getTimerProfileById(int id ){

        TimerProfile timerProfile = realm.where(TimerProfile.class)
                .equalTo(TimerProfileKeys.KEY_ID, id)
                .findFirst();
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
}
