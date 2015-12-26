package baranek.vojtech.audiomanager;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import baranek.vojtech.audiomanager.model.TimerProfileKeys;

/**
 * Created by Farmas on 25.12.2015.
 */
public class MyPreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            PreferenceManager preferenceManager = getPreferenceManager();
            preferenceManager.setSharedPreferencesName(TimerProfileKeys.KEY_PREFERENCENAME);
            preferenceManager.setSharedPreferencesMode(MODE_PRIVATE);
            addPreferencesFromResource(R.xml.preference_layout);
        }
    }
}
