package baranek.vojtech.audiomanager.mainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import baranek.vojtech.audiomanager.MyPreferenceActivity;
import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.TimerProfileAdapter;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.model.TimerProfileKeys;
import baranek.vojtech.audiomanager.volumeChangeManager.AndroidProfileChanger;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements MainActivityView {
       //NavigationView.OnNavigationItemSelectedListener

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerViewShowData)
    RecyclerView recyclerViewShowData;
    @Bind(R.id.fab)
    FloatingActionButton fabAddNewTimer;
    @Bind(R.id.fabLoud)
    FloatingActionButton fabLoud;
    @Bind(R.id.fabSilent)
    FloatingActionButton fabSilent;
    @Bind(R.id.fabVibrate)
    FloatingActionButton fabVibrate;
    private RealmHelper realmHelper;
    private MainActivityPresenter mainActivityPresenter;
    private TimerProfileAdapter timerProfileAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmHelper.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(refreshRecyclerFromNotifRcvr);
    }

    @Override
    protected void onResume() {
        super.onResume();
   timerProfileAdapter.notifyDataSetChanged();
        LocalBroadcastManager.getInstance(this).registerReceiver(refreshRecyclerFromNotifRcvr,new IntentFilter(TimerProfileKeys.INTENT_FILTER_KEY));
    }

    private BroadcastReceiver refreshRecyclerFromNotifRcvr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             timerProfileAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realmHelper= new RealmHelper(getApplicationContext());
        mainActivityPresenter = new MainActivityPresenterImpl(this);

        recyclerViewShowData.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        recyclerViewShowData.setLayoutManager(llm);

        RealmResults<TimerProfile> timerProfiles = realmHelper.getRealmResults();


         timerProfileAdapter = new TimerProfileAdapter(timerProfiles,mainActivityPresenter);
        recyclerViewShowData.setAdapter(timerProfileAdapter);

        // Setting  navigation color on post Lollipop devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        //Fab button clicks handlers

        //plus
        fabAddNewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityPresenter.startTimerProfileActivity(-1);
            }
        });
        //Silent
        fabSilent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidProfileChanger.setSilentMode(getContext());
            }
        });
        //Vibration
        fabVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               AndroidProfileChanger.setVibrateMode(getContext());
            }
        });
        //Normal
        fabLoud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Add Default settings
               AndroidProfileChanger.setNormalMode(getContext(),5,5,5,5,true);
            }
        });

        //Navigation drawer for future features
/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

  /*  @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent i = new Intent (this, MyPreferenceActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_email) {
            Intent intent = mainActivityPresenter.openEmailIntent();
            if (intent!=null){
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Navigation drawer for future features
/*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {

        } else if (id == R.id.nav_gallery) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void startTimerProfileActivity(Intent i) {
        startActivity(i);
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWelcomeDialog(int msg) {

        new MaterialDialog.Builder(this)
                .title(R.string.welcome_dialog_title)
                .content(getText(msg))
                .positiveText("Ok")
                .show();

    }
}
