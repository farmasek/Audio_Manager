package baranek.vojtech.audiomanager.alarmTimingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import baranek.vojtech.audiomanager.model.TimerProfileKeys;

/**
 * Created by Farmas on 01.01.2016.
 */
public class BootCompletReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE).edit().putInt(TimerProfileKeys.KEY_ID, -1).apply();
            AlarmControl.runNextTimer(context);
        }
    }
}
