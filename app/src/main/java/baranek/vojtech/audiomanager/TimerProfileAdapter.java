package baranek.vojtech.audiomanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baranek.vojtech.audiomanager.mainActivity.MainActivityPresenter;
import baranek.vojtech.audiomanager.model.TimerProfile;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Farmas on 27.11.2015.
 */
public class TimerProfileAdapter extends RecyclerView.Adapter<TimerViewHolder>{

    private RealmResults<TimerProfile> timerProfileList;
    private MainActivityPresenter mainActivityPresenter;

    public TimerProfileAdapter(RealmResults<TimerProfile> timerProfileList, MainActivityPresenter mainActivityPresenter) {
        this.timerProfileList = timerProfileList;
        this.mainActivityPresenter= mainActivityPresenter;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_timer_item,parent,false);
        return new TimerViewHolder(view, mainActivityPresenter, timerProfileList);

    }

    @Override
    public void onBindViewHolder(TimerViewHolder holder, int position) {

        TimerProfile timerProfile = timerProfileList.get(position);
        holder.vName.setText(timerProfile.getNazev());
        holder.vInfo.setText(Integer.toString(timerProfile.getCasDoKonce()));


    }

    @Override
    public int getItemCount() {
        return timerProfileList.size();
    }
}
