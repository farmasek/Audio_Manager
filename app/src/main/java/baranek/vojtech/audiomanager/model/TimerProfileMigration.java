package baranek.vojtech.audiomanager.model;

import baranek.vojtech.audiomanager.model.TimerProfile;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.internal.Table;

/**
 * Created by Farmas on 13.12.2015.
 */
public class TimerProfileMigration implements RealmMigration {
    /**
     * Migration for new tables -- NOT USED --
     * @param realm
     * @param oldVersion
     * @param newVersion
     */

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {


        RealmSchema schema = realm.getSchema();


        if (oldVersion == 1)
        {
            RealmObjectSchema timerProfileSchema = schema.get("TimerProfile");
            timerProfileSchema.addField("isTimerZap", Boolean.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setBoolean("isTimerZap",false);
                        }
                    });
            oldVersion++;
        }
        if (oldVersion == 2)
        {   }

    }
}
