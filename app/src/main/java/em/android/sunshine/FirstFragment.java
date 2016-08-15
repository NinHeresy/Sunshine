package em.android.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FirstFragment extends Fragment {

    ListView listView;
    ArrayAdapter<String> mForecastAdapter;

    public FirstFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //chamada do menu pelo fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fore_cast_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.refresh) {
            BuscaTudoNaAPI buscaTudo = new BuscaTudoNaAPI();
            buscaTudo.execute("524901");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_first, container, false);


        String[] data = {

                "Hoje - Doming - 29º ",
                "Segunda - feira - 29º",
                "Terça - feira - 30º",
                "Quarta - feira - 31º",
                "Quinta - feira - 28º",
                "Sexta - feira - 27º",
                "Sábado - 15º"

        };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.item_lista, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        weekForecast);

        listView = (ListView) rootView.findViewById(R.id.list_view_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;

    }

    public class BuscaTudoNaAPI extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = BuscaTudoNaAPI.class.getSimpleName();

        


        @Override
        protected Void doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String format = "json";
            String units = "metric";
            String appid = "427f62014a9ec86f4d52844e57f5cefd";

            int numDays = 7;

            try

            {

                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/city?";
                final String QUERY_PARAM = "id";
                final String FORMAT_PARAM = "mode";
                final String FORMAT_UNIT = "unit";
                final String DAYS_PARAM = "cnt";
                final String APP_ID = "APPID";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().
                        appendQueryParameter(QUERY_PARAM, params[0]).
                        appendQueryParameter(APP_ID, appid).
                        appendQueryParameter(FORMAT_PARAM, format).
                        appendQueryParameter(FORMAT_UNIT, units).
                        appendQueryParameter(DAYS_PARAM, Integer.toString(numDays)).
                        build();

                URL url = new URL(builtUri.toString());//("http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=427f62014a9ec86f4d52844e57f5cefd");
                Log.v(LOG_TAG, "Built URI" + builtUri.toString());


//                String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
//                URL url = new URL(baseUrl.concat(apiKey));

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
//                forecastJsonStr = buffer.toString();
//                Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);
            } catch (
                    IOException e
                    )

            {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally

            {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;


        }

        protected void onPostExecute(String[] result) {
            if (result != null) {
                mForecastAdapter.clear();
                for (String dayForecastStr : result) {
                    mForecastAdapter.add(dayForecastStr);
                }
            }
        }
    }//fim da classe asynktask
}//fim da classe firstfragment