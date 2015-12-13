package baranek.vojtech.audiomanager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import baranek.vojtech.audiomanager.mainActivity.MainActivityPresenter;
import baranek.vojtech.audiomanager.model.TimerProfile;
import io.realm.RealmResults;

/**
 * Created by Farmas on 27.11.2015.
 */
public class TimerViewHolder extends RecyclerView.ViewHolder {

    protected TextView rvNazev;
    protected SwitchCompat rvSwitchZap;
    protected TextView rvDny;

    protected TextView rvZacTime;
    protected TextView rvZacRez;
    protected ImageView rvZacIW;
    protected TextView rvZacHlas;

    protected TextView rvKonTime;
    protected TextView rvKonRez;
    protected ImageView rvKonIW;
    protected TextView rvKonHlas;






    public TimerViewHolder(View itemView, final MainActivityPresenter mainActivityPresenter, final RealmResults<TimerProfile> timerProfileList) {
        super(itemView);

        rvNazev = (TextView) itemView.findViewById(R.id.rv_itemNazev);
        rvDny = (TextView) itemView.findViewById(R.id.rv_Dny);
        rvSwitchZap = (SwitchCompat) itemView.findViewById(R.id.rv_switchitemOn);


        rvZacTime = (TextView) itemView.findViewById(R.id.rv_ZacTime);
        rvZacRez = (TextView) itemView.findViewById(R.id.rvZacRezim);
        rvZacHlas= (TextView) itemView.findViewById(R.id.rv_zacHlasitost);
        rvZacIW = (ImageView) itemView.findViewById(R.id.rv_ZacIcon);

        rvKonTime = (TextView) itemView.findViewById(R.id.rv_KonTime);
        rvKonRez = (TextView) itemView.findViewById(R.id.rv_KonRez);
        rvKonHlas = (TextView) itemView.findViewById(R.id.rv_KonHlas);
        rvKonIW = (ImageView) itemView.findViewById(R.id.rv_KonIcon);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mainActivityPresenter.startTimerProfileActivity(timerProfileList.get(getAdapterPosition()).getId());

            }
        });
    }
}
