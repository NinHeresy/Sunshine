package em.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.crash.FirebaseCrash;

import em.android.sunshine.sync.SunshineSyncAdapter;

public class  MainActivity extends AppCompatActivity {
    String mLocation;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocation = null;
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SecondFragment())
                    .commit();
        }
        FirebaseCrash.log("Activity created");
        SunshineSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuItem) {

        getMenuInflater().inflate(R.menu.main, menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){
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
    //private final String LOG_TAG = SecondFragment.FetchWeatherTask.class.getSimpleName();

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
    }

