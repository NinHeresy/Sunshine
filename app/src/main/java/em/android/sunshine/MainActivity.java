package em.android.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.crash.FirebaseCrash;

import em.android.sunshine.adapter.ForecastRecyclerAdapter;
import em.android.sunshine.sync.SunshineSyncAdapter;
import em.android.sunshine.utility.Utility;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static em.android.sunshine.SecondFragment.LOG_TAG;


public class MainActivity extends AppCompatActivity implements SecondFragment.Callback {
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    boolean mTwoPane;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mLocation = Utility.getPreferredLocation(this);

        if (findViewById(R.id.container_fragment) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, new DetailActvityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }

        } else {
            mTwoPane = false;
        }

//        SecondFragment forecastFragment =  ((SecondFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.container_fragment));

//        FirebaseCrash.log("Activity created");
        SunshineSyncAdapter.initializeSyncAdapter(this);

        if (!checkPlayServices()) {
            // This is where we could either prompt a user that they should install
            // the latest version of Google Play Services, or add an error snackbar
            // that some features won't be available.
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "Esse aparelho não suporta push notifications.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuItem) {

        getMenuInflater().inflate(R.menu.main, menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SetingsActivity.class);
            startActivity(intent);
            return true;

        }
//        if(id == R.id.abre_mapa){
//            abreLocalizacaoNoMapa();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
            SecondFragment ff = (SecondFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            if (null != ff) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }


//    private void abreLocalizacaoNoMapa(){
//        SharedPreferences sharedPrefs =
//                PreferenceManager.getDefaultSharedPreferences(this);
//        String location = sharedPrefs.getString(
//                getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
//
//        // Using the URI scheme for showing a location found on a map.  This super-handy
//        // intent can is detailed in the "Common Intents" page of Android's developer site:
//        // http://developer.android.com/guide/components/intents-common.html#Maps
//        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
//                .appendQueryParameter("q", location)
//                .build();
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(geoLocation);
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            Log.d(LOG_TAG, "Não foi encontrada  " + location);
//        }
//    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailActvityFragment.DETAIL_URI, contentUri);

            DetailActvityFragment fragment = new DetailActvityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActvity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }
}

