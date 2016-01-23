package baranek.vojtech.audiomanager.profileActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.MaterialDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.w3c.dom.Text;

import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileActivity extends AppCompatActivity implements ProfileActivityView {


    //-------Initialize components------//


    @Bind(R.id.et_TimerName)
    EditText etNazevCasovace;
    //---Zacatecni nastaveni---//
    @Bind(R.id.tvZacCas)
    TextView tvZacCas;
    @Bind(R.id.tvZacRez)
    TextView tvZacRez;
    @Bind(R.id.cvZacHlasitost)
    CardView cvZacHlasitost;
    @Bind(R.id.sliderZacAlarm)
    DiscreteSeekBar sliderZacAlarm;
    @Bind(R.id.sliderZacMedia)
    DiscreteSeekBar sliderZacMedia;
    @Bind(R.id.sliderZacVyzvan)
    DiscreteSeekBar sliderZacVyzvan;
    @Bind(R.id.sliderZacNot)
    DiscreteSeekBar sliderZacNot;
    @Bind(R.id.tvZacNotif)
    TextView tvZacNotif;
    //---Konecne nastaveni---//
    @Bind(R.id.tvKonCas)
    TextView tvKonCas;
    @Bind(R.id.tvKonRez)
    TextView tvKonRez;
    @Bind(R.id.cvKonHlasitost)
    CardView cvKonHlasitost;
    @Bind(R.id.sliderKonAlarm)
    DiscreteSeekBar sliderKonAlarm;
    @Bind(R.id.sliderKonMedia)
    DiscreteSeekBar sliderKonMedia;
    @Bind(R.id.sliderKonVyzvan)
    DiscreteSeekBar sliderKonVyzvan;
    @Bind(R.id.sliderKonNot)
    DiscreteSeekBar sliderKonNot;
    @Bind(R.id.tvKonNotif)
    TextView tvKonNotif;
    @Bind(R.id.chbKonec)
    CheckBox chbKonecAktiv;
    //---Opakokvani---//
    @Bind(R.id.tgbPo)
    ToggleButton tgbPo;
    @Bind(R.id.tgbUt)
    ToggleButton tgbUt;
    @Bind(R.id.tgbSt)
    ToggleButton tgbSt;
    @Bind(R.id.tgbCt)
    ToggleButton tgbCt;
    @Bind(R.id.tgbPa)
    ToggleButton tgbPa;
    @Bind(R.id.tgbSo)
    ToggleButton tgbSo;
    @Bind(R.id.tgbNe)
    ToggleButton tgbNe;

    @Bind(R.id.fab)
    FloatingActionButton fab;


    TimerProfile timer = new TimerProfile();
    ProfileActivityPresenterImpl profileActivityPresenter;
    //
    // id of TimerProfile, if -1 then new profile
    private int id = -1;
    private boolean visibility = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setToolbar();
        //Set default interface
        id = getIntent().getIntExtra(TimerProfileKeys.KEY_ID, -1);
        profileActivityPresenter = new ProfileActivityPresenterImpl(this);
        profileActivityPresenter.setDefaultProfileView(id);


        //Collect data and send to presenter

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileActivityPresenter.profileActivityButtonClick(collectTimerProfileDataFromViews(), id);

            }
        });

        chbKonecAktiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                profileActivityPresenter.setEndStatus(timer, isChecked);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        MenuItem menuItem = menu.findItem(R.id.prof_actionDelete);
        menuItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.prof_actionDelete);
        item.setVisible(visibility);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //Clicked menu option for delete current timer
            case R.id.prof_actionDelete: {
                profileActivityPresenter.deleteTimer(id);
                finishView();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        profileActivityPresenter.searchNextTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileActivityPresenter.closeRealm();
    }

    //-- Zacatek time settings --//
    @OnClick(R.id.tvZacCas)
    void zacTimeSet() {
        ProfileSetters.getTimeFromTimePicker(getFragmentManager(), timer, true, profileActivityPresenter);
    }

    //    Konec time settings //
    @OnClick(R.id.tvKonCas)
    void konTimeSet() {
        ProfileSetters.getTimeFromTimePicker(getFragmentManager(), timer, false, profileActivityPresenter);
    }

    //    Select Zacatecni rezim   //
    @OnClick(R.id.tvZacRez)
    void setTvZacRez() {
        new MaterialDialog.Builder(this)
                .title(R.string.select_mode_dialo_title)
                .items(R.array.sound_modes)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        if (i == -1)
                            i = timer.getZacRez();
                        timer.setZacRez(i);
                        ProfileSetters.setTvRezimAndShowCardView(timer.getZacRez(), profileActivityPresenter, tvZacRez, cvZacHlasitost);
                        return false;
                    }
                })
                .positiveText("Ok")
                .show();

    }


    //    Select koncovy rezim   //
    @OnClick(R.id.tvKonRez)
    void setTvKonRez() {
        new MaterialDialog.Builder(this)
                .title(R.string.select_mode_dialo_title)
                .items(R.array.sound_modes)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        if (i == -1)
                            i = timer.getKonRez();
                        timer.setKonRez(i);
                        ProfileSetters.setTvRezimAndShowCardView(timer.getKonRez(), profileActivityPresenter, tvKonRez, cvKonHlasitost);
                        return false;
                    }
                })
                .positiveText("Ok")
                .show();

    }

    /**
     * Set custom action bar for activity
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout cv = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        cv.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);

        // Setting  navigation color on post Lollipop devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void showData() {

        etNazevCasovace.setText(timer.getNazev());
        profileActivityPresenter.setStartTextString(timer);
        ProfileSetters.setTvRezimAndShowCardView(timer.getZacRez(), profileActivityPresenter, tvZacRez, cvZacHlasitost);
        ProfileSetters.setVolumeSliders(sliderZacVyzvan, sliderZacAlarm, sliderZacMedia, sliderZacNot, timer.getZacVyzvaneni(), timer.getZacAlarm(), timer.getZacMedia(), timer.getZacOzn());


        ProfileSetters.setTvRezimAndShowCardView(timer.getKonRez(), profileActivityPresenter, tvKonRez, cvKonHlasitost);
        chbKonecAktiv.setChecked(timer.isKonZap());
        ProfileSetters.setVolumeSliders(sliderKonVyzvan, sliderKonAlarm, sliderKonMedia, sliderKonNot, timer.getKonVyzvaneni(), timer.getKonAlarm(), timer.getKonMedia(), timer.getKonOzn());
        profileActivityPresenter.setEndTextString(timer);
        ProfileSetters.setDaysTgbs(tgbPo, tgbUt, tgbSt, tgbCt, tgbPa, tgbSo, tgbNe, timer.getDny());


    }

    @Override
    public TimerProfile collectTimerProfileDataFromViews() {

        timer.setNazev(etNazevCasovace.getText().toString());

        String daysFromTgbs = ProfileSetters.getDaysFromTgbs(tgbPo, tgbUt, tgbSt, tgbCt, tgbPa, tgbSo, tgbNe);
        timer.setDny(daysFromTgbs);

        ProfileSetters.getVolumeFromSliders(sliderZacAlarm, sliderZacMedia, sliderZacVyzvan, sliderZacNot, timer, true);
        ProfileSetters.getVolumeFromSliders(sliderKonAlarm, sliderKonMedia, sliderKonVyzvan, sliderKonNot, timer, false);

        return timer;

    }

    @Override
    public void setSeekersRange(int maxMedia, int maxRing, int maxAlarm, int maxNot) {

        SharedPreferences sharedPreferences = getSharedPreferences(TimerProfileKeys.KEY_PREFERENCENAME, Context.MODE_PRIVATE);
        boolean isNotifActiv = sharedPreferences.getBoolean(TimerProfileKeys.KEY_ISNOTIFACTIVE, true);

        sliderKonAlarm.setMax(maxAlarm);
        sliderKonMedia.setMax(maxMedia);
        sliderKonVyzvan.setMax(maxRing);

        sliderKonNot.setMax(maxNot);

        sliderZacAlarm.setMax(maxAlarm);
        sliderZacMedia.setMax(maxMedia);
        sliderZacVyzvan.setMax(maxRing);
        sliderZacNot.setMax(maxNot);

        if (!isNotifActiv) {

            tvZacNotif.setVisibility(View.GONE);
            tvKonNotif.setVisibility(View.GONE);

            sliderKonNot.setVisibility(View.GONE);
            sliderZacNot.setVisibility(View.GONE);
        } else {
            tvZacNotif.setVisibility(View.VISIBLE);
            tvKonNotif.setVisibility(View.VISIBLE);

            sliderKonNot.setVisibility(View.VISIBLE);
            sliderZacNot.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void setTimePickerStartTime(String time) {
        tvZacCas.setText(time);
    }

    @Override
    public void setTimePickerEndTime(String time) {
        tvKonCas.setText(time);
    }

    @Override
    public void setCheckboxStatus(boolean isActive) {
        chbKonecAktiv.setChecked(isActive);
    }

    @Override
    public void showErrorSnackBar(String errorMessage) {
        Snackbar.make(getCurrentFocus(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setViewTimerProfile(TimerProfile timerProfile) {
        timer = new TimerProfile(timerProfile);
    }

    @Override
    public void setMenuItemsVisible(boolean visibility) {
        invalidateOptionsMenu();
        this.visibility = visibility;
    }

    @Override
    public void setToolbarTitle(String title) {
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
    }

    @Override
    public void setFabIcon(int iconID) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), iconID, null);
        fab.setImageDrawable(drawable);
    }

    @Override
    public void finishView() {
        finish();
    }

}
