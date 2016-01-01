package baranek.vojtech.audiomanager.volumeChangeManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import baranek.vojtech.audiomanager.model.TimerProfileKeys;

/**
 * Created by Farmas on 20.12.2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("RECEIVER","received afafa");
        int id = intent.getIntExtra(TimerProfileKeys.KEY_ID, -1);

        if (!intent.getBooleanExtra(TimerProfileKeys.KEY_ISITENDTIMER, false)) {
            //Start service for Start settings
            Intent i = new Intent(context, AlarmService.class);
            i.putExtra(TimerProfileKeys.KEY_ID, id);
            i.putExtra(TimerProfileKeys.KEY_ISITENDTIMER, false);
            context.startService(i);
        }
        if (intent.getBooleanExtra(TimerProfileKeys.KEY_ISITENDTIMER, false)) {
            //Start service for End settings
            Intent i = new Intent(context, AlarmService.class);
            i.putExtra(TimerProfileKeys.KEY_ID, id);
            i.putExtra(TimerProfileKeys.KEY_ISITENDTIMER, true);
            context.startService(i);
        }


    }
}
