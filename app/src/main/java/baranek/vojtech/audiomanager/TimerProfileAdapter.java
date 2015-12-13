package baranek.vojtech.audiomanager;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import baranek.vojtech.audiomanager.mainActivity.MainActivityPresenter;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileHelper;
import io.realm.RealmList;
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
        holder.rvNazev.setText(timerProfile.getNazev());
        holder.rvZacTime.setText(TimerProfileHelper.getFormatedStartTime(timerProfile));
        holder.rvZacRez.setText(getTimeFromRez(timerProfile.getZacRez()));
        holder.rvZacIW.setImageDrawable(getDrawableForIw(timerProfile.getZacRez()));
        holder.rvSwitchZap.setChecked(timerProfile.isTimerZap());
        setVolumeTextview(holder.rvZacHlas, timerProfile.getZacRez(), timerProfile.getZacVyzvaneni(), timerProfile.getZacMedia(), timerProfile.getZacOzn(), timerProfile.getZacAlarm());
        holder.rvSwitchZap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    boolean b = mainActivityPresenter.setTimerProfileActive(timerProfile);
                    if (!b) holder.rvSwitchZap.setChecked(b);

                }else{
                    mainActivityPresenter.setTimerProfileInActive(timerProfile);
                }

            }
        });


        if(timerProfile.isKonZap()) {
            holder.rvKonTime.setVisibility(View.VISIBLE);
            holder.rvKonIW.setVisibility(View.VISIBLE);
            holder.rvKonRez.setVisibility(View.VISIBLE);
            holder.rvKonHlas.setVisibility(View.VISIBLE);
            holder.rvKonIW.setImageDrawable(getDrawableForIw(timerProfile.getKonRez()));
            holder.rvKonTime.setText(TimerProfileHelper.getFormatedEndTime(timerProfile));
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

        if (strDays.contains("M"))ret.append("Po ");
        if (strDays.contains("T"))ret.append("Út ");
        if (strDays.contains("W"))ret.append("St ");
        if (strDays.contains("R"))ret.append("Čt ");
        if (strDays.contains("F"))ret.append("Pá ");
        if (strDays.contains("U"))ret.append("So ");
        if (strDays.contains("S"))ret.append("Ne ");

        if(ret.toString().equals(""))
            ret.append("Nezadáno");

        return ret.toString();
    }

    private void setVolumeTextview(TextView rvZacHlas, int zacRez, int Vyz, int Med, int Ozn, int Alarm) {

        if (zacRez==0)
        {
           rvZacHlas.setText("Vyz - "+ Vyz + " / Méd - " + Med + " /Ozn - " +Ozn + " / Alarm - "+ Alarm);
        }
        else{
            rvZacHlas.setVisibility(View.GONE);
        }

    }

    private String getTimeFromRez(int zacRez) {

        String ret = null;
        switch (zacRez) {
            case 0:
                ret = "Hlasitý";
                break;

            case 1:
                ret = "Tichý";
                break;

            case 2:
                ret = "Vibrace";
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
