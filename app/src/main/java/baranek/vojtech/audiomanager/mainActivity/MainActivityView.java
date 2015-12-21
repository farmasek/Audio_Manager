package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Farmas on 27.11.2015.
 */
public interface MainActivityView {

    Context getContext();

    void startTimerProfileActivity(Intent i);

    void showToastMessage(String msg);
}
