package baranek.vojtech.audiomanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import baranek.vojtech.audiomanager.mainActivity.MainActivityPresenter;
import baranek.vojtech.audiomanager.model.TimerProfile;
import io.realm.RealmResults;

/**
 * Created by Farmas on 27.11.2015.
 */
public class TimerViewHolder extends RecyclerView.ViewHolder {

    protected TextView vName;
    protected TextView vInfo;

    public TimerViewHolder(View itemView, final MainActivityPresenter mainActivityPresenter, final RealmResults<TimerProfile> timerProfileList) {
        super(itemView);

        vName = (TextView) itemView.findViewById(R.id.rv_itemNazev);
        vInfo = (TextView) itemView.findViewById(R.id.rv_itemsecLine);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mainActivityPresenter.startTimerProfileActivity(timerProfileList.get(getAdapterPosition()).getId());

            }
        });
    }
}
