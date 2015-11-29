package baranek.vojtech.audiomanager.profileActivity;

import android.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileHelper;

/**
 * Created by Farmas on 08.11.2015.
 */
public class ProfileSetters {
   private static TimerProfile timerCl = new TimerProfile();
    private static boolean zac;

    public static void getTimeFromTimePicker(FragmentManager frm, TimerProfile timer, boolean zacatek, final ProfileActivityPresenter profileActivityPresenter) {
        timerCl=timer;
        zac=zacatek;
        int min, hod;

        if (zacatek) {
            hod = timerCl.getZacCas() / 60;
            min = timerCl.getZacCas() % 60;
        } else {
            int lastEndTime = TimerProfileHelper.getLastTimeToEnd(timerCl);
            hod = lastEndTime / 60;
            min = lastEndTime % 60;
        }
        TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1, int i2) {
                if (zac) {
                    int lastTimeToEnd = TimerProfileHelper.getLastTimeToEnd(timerCl);
                    timerCl.setZacCas(TimerProfileHelper.getCasFromHodMin(i, i1));
                    profileActivityPresenter.setEndTimeIfGood(timerCl, lastTimeToEnd, timerCl.isKonZap());
                    profileActivityPresenter.setStartTextString(timerCl);

                } else {
                    profileActivityPresenter.setEndTimeIfGood(timerCl, TimerProfileHelper.getCasFromHodMin(i, i1), true);
                }
            }


        }, hod, min, true);
        tpd.show(frm, "TimePicker");



    }




    public static void setTvRezimAndShowCardView(int rezim, ProfileActivityPresenter profileActivityPresenter, TextView tvRezim, CardView cvHlasitost) {

        String [] rezimy = profileActivityPresenter.getRezimy();

        tvRezim.setText(rezimy[rezim]);
        if (rezim ==0)
            cvHlasitost.setVisibility(View.VISIBLE);
        else
            cvHlasitost.setVisibility(View.GONE);

    }

    public static void getVolumeFromSliders(DiscreteSeekBar sliderAlarm, DiscreteSeekBar sliderMedia, DiscreteSeekBar sliderVyzvan, DiscreteSeekBar sliderNotif, TimerProfile timer, boolean b) {
        // b true = zacatek //
        if (b){
            if (timer.getZacRez()==0){
                timer.setZacAlarm(sliderAlarm.getProgress());
                timer.setZacMedia(sliderMedia.getProgress());
                timer.setZacVyzvaneni(sliderVyzvan.getProgress());
                timer.setZacOzn(sliderNotif.getProgress());
            }
            else{
                timer.setZacAlarm(0);
                timer.setZacMedia(0);
                timer.setZacVyzvaneni(0);
                timer.setZacOzn(0);

            }

        }

        else{
            if (timer.getKonRez()==0){
                timer.setKonAlarm(sliderAlarm.getProgress());
                timer.setKonMedia(sliderMedia.getProgress());
                timer.setKonVyzvaneni(sliderVyzvan.getProgress());
                timer.setKonOzn(sliderNotif.getProgress());
            }
            else{
                timer.setKonAlarm(0);
                timer.setKonMedia(0);
                timer.setKonVyzvaneni(0);
                timer.setKonOzn(0);

            }


        }


    }

    public static String getDaysFromTgbs(ToggleButton tgbPo, ToggleButton tgbUt, ToggleButton tgbSt, ToggleButton tgbCt, ToggleButton tgbPa, ToggleButton tgbSo, ToggleButton tgbNe) {

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

       return stringBuffer.toString();


    }

    public static void setVolumeSliders (DiscreteSeekBar sliderVyzvan, DiscreteSeekBar sliderAlarm, DiscreteSeekBar sliderMedia,DiscreteSeekBar sliderNot, int vyzvan, int alarm, int media, int notif) {
        sliderVyzvan.setProgress(vyzvan);
        sliderMedia.setProgress(media);
        sliderAlarm.setProgress(alarm);
        sliderNot.setProgress(notif);

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
