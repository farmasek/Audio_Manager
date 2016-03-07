package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileHelper;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import io.realm.RealmResults;

/**
 * Created by Farmas on 27.11.2015.
 */
public class TimerProfileAdapter extends RecyclerView.Adapter<TimerViewHolder> {

    private RealmResults<TimerProfile> timerProfileList;
    private MainActivityPresenter mainActivityPresenter;

    public TimerProfileAdapter(RealmResults<TimerProfile> timerProfileList, MainActivityPresenter mainActivityPresenter) {
        this.timerProfileList = timerProfileList;
        this.mainActivityPresenter = mainActivityPresenter;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_timer_item, parent, false);
        return new TimerViewHolder(view, mainActivityPresenter, timerProfileList);

    }

    @Override
    public void onBindViewHolder(final TimerViewHolder holder, int position) {


        final TimerProfile timerProfile = timerProfileList.get(position);
         holder.rvNazev.setText(timerProfile.getNazev() );
        holder.rvZacTime.setText(TimerProfileHelper.getFormatedStartTime(timerProfile));
        holder.rvZacRez.setText(getTimeFromRez(timerProfile.getZacRez()));
        holder.rvZacIW.setImageDrawable(getDrawableForIw(timerProfile.getZacRez()));
        holder.rvSwitchZap.setChecked(timerProfile.isTimerZap());
        setVolumeTextview(holder.rvZacHlas, timerProfile.getZacRez(), timerProfile.getZacVyzvaneni(), timerProfile.getZacMedia(), timerProfile.getZacOzn(), timerProfile.getZacAlarm());
        holder.rvSwitchZap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Control for automatic change while initialize
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        boolean b = mainActivityPresenter.setTimerProfileActive(timerProfile);
                        if (!b) holder.rvSwitchZap.setChecked(b);


                    } else {
                        mainActivityPresenter.setTimerProfileInActive(timerProfile.getId());

                    }
                }

            }
        });


        if(timerProfile.isKonZap()) {
            holder.rvKonTime.setVisibility(View.VISIBLE);
            holder.rvKonIW.setVisibility(View.VISIBLE);
            holder.rvKonRez.setVisibility(View.VISIBLE);
            holder.rvKonHlas.setVisibility(View.VISIBLE);
            holder.rvKonIW.setImageDrawable(getDrawableForIw(timerProfile.getKonRez()));
            holder.rvKonTime.setText(TimerProfileHelper.getFormatedEndTime(timerProfile, mainActivityPresenter.getContext()));
            holder.rvKonRez.setText(getTimeFromRez(timerProfile.getKonRez()));
            setVolumeTextview(holder.rvKonHlas, timerProfile.getKonRez(), timerProfile.getKonVyzvaneni(), timerProfile.getKonMedia(), timerProfile.getKonOzn(), timerProfile.getKonAlarm());
        }
        else
        {
            holder.rvKonTime.setVisibility(View.GONE);
            holder.rvKonIW.setVisibility(View.GONE);
            holder.rvKonRez.setVisibility(View.GONE);
            holder.rvKonHlas.setVisibility(View.GONE);
        }
        holder.rvDny.setText(getDaysString(timerProfile.getDny()));
        //    holder.vInfo.setText(Integer.toString(timerProfile.getCasDoKonce()));


    }

    private String getDaysString(String strDays) {

        StringBuilder ret = new StringBuilder();

        if (strDays.contains("M"))
            ret.append(mainActivityPresenter.getContext().getString(R.string.Pondeli)).append(" ");
        if (strDays.contains("T"))
            ret.append(mainActivityPresenter.getContext().getString(R.string.Utery)).append(" ");
        if (strDays.contains("W"))
            ret.append(mainActivityPresenter.getContext().getString(R.string.Streda)).append(" ");
        if (strDays.contains("R"))
            ret.append(mainActivityPresenter.getContext().getString(R.string.Ctvrtek)).append(" ");;
        if (strDays.contains("F"))
            ret.append(mainActivityPresenter.getContext().getString(R.string.Patek)).append(" ");
        if (strDays.contains("U"))
            ret.append(mainActivityPresenter.getContext().getString(R.string.Sobota)).append(" ");
        if (strDays.contains("S"))
            ret.append(mainActivityPresenter.getContext().getString(R.string.Nedele)).append(" ");

        if(ret.toString().equals(""))
            ret.append(mainActivityPresenter.getContext().getString(R.string.nezadan_den));

        return ret.toString();
    }

    private void setVolumeTextview(TextView rvZacHlas, int zacRez, int Vyz, int Med, int Ozn, int Alarm) {

        SharedPreferences sharedPreferences = mainActivityPresenter.getContext().getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);


        if (zacRez==0)
        {

            StringBuilder text = new StringBuilder();
            text.append(mainActivityPresenter.getContext().getString(R.string.short_vyzv)).append(Vyz).append(" / ");
            text.append(mainActivityPresenter.getContext().getString(R.string.short_med)).append(Med).append(" / ");

          if (sharedPreferences.getBoolean(TimerProfileKeys.KEY_ISNOTIFACTIVE, true))
            text.append(mainActivityPresenter.getContext().getString(R.string.short_ozn)).append(Ozn).append(" / ");

            text.append(mainActivityPresenter.getContext().getString(R.string.short_alarm)).append(Alarm);

            rvZacHlas.setVisibility(View.VISIBLE);
           rvZacHlas.setText(text.toString());
        }
        else{
            rvZacHlas.setVisibility(View.GONE);
        }

    }

    private String getTimeFromRez(int zacRez) {

        String ret = null;
        switch (zacRez) {
            case 0:
                ret = mainActivityPresenter.getContext().getString(R.string.vlastni_rezim);
                break;

            case 1:
                ret = mainActivityPresenter.getContext().getString(R.string.tichy_rezim);
                break;

            case 2:
                ret = mainActivityPresenter.getContext().getString(R.string.vibrace_rezim);
                break;
        }
        return  ret;


    }

    private Drawable getDrawableForIw(int zacRez) {

        Drawable ret = null;

        switch (zacRez) {
            case 0:
                ret = ResourcesCompat.getDrawable(mainActivityPresenter.getContext().getResources(), R.drawable.ic_volume_up_grey_600_24dp, null);
                break;

            case 1:
                ret = ResourcesCompat.getDrawable(mainActivityPresenter.getContext().getResources(), R.drawable.ic_volume_off_grey_600_24dp, null);
                break;

            case 2:
                ret = ResourcesCompat.getDrawable(mainActivityPresenter.getContext().getResources(), R.drawable.ic_vibration_grey_600_24dp, null);
                break;
        }
        return  ret;
    }

    @Override
    public int getItemCount() {
        return timerProfileList.size();
    }
}
