package baranek.vojtech.audiomanager.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import baranek.vojtech.audiomanager.R;
import baranek.vojtech.audiomanager.RealmHelper;
import baranek.vojtech.audiomanager.TimerProfileAdapter;
import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.profileActivity.ProfileActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerViewShowData)
    RecyclerView recyclerViewShowData;
    @Bind(R.id.fab)
    FloatingActionButton fabAddNewTimer;
    private RealmHelper realmHelper;
    private MainActivityPresenter mainActivityPresenter;
    private TimerProfileAdapter timerProfileAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
   timerProfileAdapter.notifyDataSetChanged();
    }

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



        fabAddNewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityPresenter.startTimerProfileActivity(-1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            RealmResults<TimerProfile> timerProfiles = realmHelper.getRealmResults();
            timerProfileAdapter = new TimerProfileAdapter(timerProfiles,mainActivityPresenter);
            recyclerViewShowData.setAdapter(timerProfileAdapter);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void startTimerProfileActivity(Intent i) {
        startActivity(i);
    }
}
