package baranek.vojtech.audiomanager.alarmTimingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Farmas on 01.01.2016.
 */
public class BootCompletReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            AlarmControl.runNextTimer(context);
        }
    }
}
