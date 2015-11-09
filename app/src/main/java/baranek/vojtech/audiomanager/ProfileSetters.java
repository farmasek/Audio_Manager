package baranek.vojtech.audiomanager;

import android.app.FragmentManager;


import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import android.widget.ToggleButton;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Calendar;

/**
 * Created by Farmas on 08.11.2015.
 */
public class ProfileSetters {
    static TimerProfile timerCl = new TimerProfile();
    static boolean zac;
    static TextView tv;
    private static String[] rezimy = ProfileActivity.rezimy;

    public static void getTimeFromTimePicker(FragmentManager frm, TimerProfile timer, boolean zacatek, TextView tvToset) {
        timerCl=timer;
        zac=zacatek;
        tv= tvToset;
        Calendar cal = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1, int i2) {
                if (zac) {
                    timerCl.setZacCas(TimerProfile.getCasFromHodMin(i, i1));
                    setTextView(tv, timerCl.getZacCas());
                } else {
                    timerCl.setKonCas(TimerProfile.getCasFromHodMin(i, i1));
                    setTextView(tv, timerCl.getKonCas());
                }
            }


        }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true);
        tpd.show(frm, "TimePicker");



    }

    public static void setTextView(TextView tv, int cas) {

        tv.setText(TimerProfile.getCasFormatedString(cas));
    }


    public static void setTvRezimAndShowCardView(int rezim, TextView tvRezim, CardView cvHlasitost) {
        tvRezim.setText(rezimy[rezim]);
        if (rezim ==0)
            cvHlasitost.setVisibility(View.VISIBLE);
        else
            cvHlasitost.setVisibility(View.GONE);

    }

    public static void getVolumeFromSliders(DiscreteSeekBar sliderAlarm, DiscreteSeekBar sliderMedia, DiscreteSeekBar sliderVyzvan, TimerProfile timer, boolean b) {
        // b true = zacatek //
        if (b){
            if (timer.getZacRez()==0){
                timer.setZacAlarm(sliderAlarm.getProgress());
                timer.setZacMedia(sliderMedia.getProgress());
                timer.setZacVyzvaneni(sliderVyzvan.getProgress());}
            else{
                timer.setZacAlarm(0);
                timer.setZacMedia(0);
                timer.setZacVyzvaneni(0);

            }

        }

        else{
            if (timer.getKonRez()==0){
                timer.setKonAlarm(sliderAlarm.getProgress());
                timer.setKonMedia(sliderMedia.getProgress());
                timer.setKonVyzvaneni(sliderVyzvan.getProgress());
            }
            else{
                timer.setKonAlarm(0);
                timer.setKonMedia(0);
                timer.setKonVyzvaneni(0);

            }


        }


    }

    public static void getDaysFromTgbs(ToggleButton tgbPo, ToggleButton tgbUt, ToggleButton tgbSt, ToggleButton tgbCt, ToggleButton tgbPa, ToggleButton tgbSo, ToggleButton tgbNe, TimerProfile timer) {

        StringBuffer stringBuffer = new StringBuffer();

        if (tgbPo.isChecked())
            stringBuffer.append("M");   // -- Monday --//

        if (tgbUt.isChecked())
            stringBuffer.append("T");   // -- Tuesday --//

        if (tgbSt.isChecked())
            stringBuffer.append("W");   // -- Wednesday --//

        if (tgbCt.isChecked())
            stringBuffer.append("R");   // -- Thursday --//

        if (tgbPa.isChecked())
            stringBuffer.append("F");   // -- Friday --//

        if (tgbSo.isChecked())
            stringBuffer.append("U");   // -- Saturday --//

        if (tgbNe.isChecked())
            stringBuffer.append("S");   // -- Sunday --//

        timer.setDny(stringBuffer.toString());


    }

    public static void setVolumeSliders (DiscreteSeekBar sliderVyzvan, DiscreteSeekBar sliderAlarm, DiscreteSeekBar sliderMedia, int vyzvan, int alarm, int media) {
        sliderVyzvan.setProgress(vyzvan);
        sliderMedia.setProgress(media);
        sliderAlarm.setProgress(alarm);

    }

    public static void setDaysTgbs(ToggleButton tgbPo, ToggleButton tgbUt, ToggleButton tgbSt, ToggleButton tgbCt, ToggleButton tgbPa, ToggleButton tgbSo, ToggleButton tgbNe, String strDays) {

        if (strDays.contains("M"))tgbPo.setChecked(true);
        if (strDays.contains("T"))tgbUt.setChecked(true);
        if (strDays.contains("W"))tgbSt.setChecked(true);
        if (strDays.contains("R"))tgbCt.setChecked(true);
        if (strDays.contains("F"))tgbPa.setChecked(true);
        if (strDays.contains("U"))tgbSo.setChecked(true);
        if (strDays.contains("S"))tgbNe.setChecked(true);

    }
}
