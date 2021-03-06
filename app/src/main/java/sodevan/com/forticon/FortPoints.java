package sodevan.com.forticon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.HashMap;

public class FortPoints extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {


    FirebaseDatabase database ;
    DatabaseReference reference ;
    ArrayList<FortpointObject> ar ;
    ListView lv ;
    TextView tv ;
    private BeaconManager beaconManager;
    public HashMap<String,Region> ssnRegionMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fort_points);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT)) ;
        beaconManager.bind(this);





        Iconify.with(new FontAwesomeModule());
        ImageView iv = (ImageView) findViewById(R.id.ims)  ;

        IconDrawable D = new IconDrawable(this , FontAwesomeIcons.fa_feed) ;
        iv.setImageDrawable(D);

        Intent recieve  = getIntent() ;
        final String Fortid =   recieve.getStringExtra("Fortid") ;
        String Fortname = recieve.getStringExtra("Fortname") ;

         tv = (TextView) findViewById(R.id.nameq) ;

        tv.setText(Fortname);
        lv = (ListView)findViewById(R.id.lv1);


        ar = new ArrayList<FortpointObject>() ;
        ssnRegionMap = new HashMap<>() ;


        database = FirebaseDatabase.getInstance() ;
        reference = database.getReference("Fortpoints").child(Fortid) ;

        FirebaseListAdapter<FortpointObject> adapter = new FirebaseListAdapter<FortpointObject>(this , FortpointObject.class ,  R.layout.fortpointsitem2, reference) {
            @Override
            protected void populateView(View v, FortpointObject model, int position) {




                ar.add(model);
           Context c =  FortPoints.this ;


                final String name = model.getName() ;
                String namespace = model.getNamespace() ;
                String instance = model.getInstance()  ;
                String photolink  = model.getPhotolink() ;
                String text = model.getText() ;

                TextView nametv = (TextView)v.findViewById(R.id.name);
                nametv.setText(name);


                Identifier namespaceid = Identifier.parse(namespace);
                Identifier instance1 = Identifier.parse(instance) ;
                 Region region =   new  Region(name ,namespaceid, instance1 ,null);
                try {
                    beaconManager.startMonitoringBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }



            }
        } ;

        lv.setAdapter(adapter);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        getMenuInflater().inflate(R.menu.fort_points, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
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
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {

            }

            @Override
            public void didExitRegion(Region region) {

            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

                tv.setText(region.getUniqueId());



            }
        });



    }
}
