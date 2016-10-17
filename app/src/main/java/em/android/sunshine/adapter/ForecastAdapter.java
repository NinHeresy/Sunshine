package em.android.sunshine.adapter;

/**
 * Created by emanu on 11/10/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import em.android.sunshine.R;

import em.android.sunshine.utility.Utility;
import em.android.sunshine.SecondFragment;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


//    private String formatHighLows(double high, double low) {
//        boolean isMetric = Utility.isMetric(mContext);
//        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
//        return highLowStr;
//    }



    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
//    private String convertCursorRowToUXFormat(Cursor cursor) {
//        String highAndLow = formatHighLows(
//                cursor.getDouble(SecondFragment.COL_WEATHER_MAX_TEMP),
//                cursor.getDouble(SecondFragment.COL_WEATHER_MIN_TEMP));
//
//        return Utility.formatDate(cursor.getLong(SecondFragment.COL_WEATHER_DATE)) +
//                " - " + cursor.getString(SecondFragment.COL_WEATHER_DESC) +
//                " - " + highAndLow;
//    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(SecondFragment.COL_WEATHER_ID);
        // Use placeholder image for now
        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        iconView.setImageResource(R.drawable.ic_menu_gallery);

        long dateInMillis = cursor.getLong(SecondFragment.COL_WEATHER_DATE);
        TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        String description = cursor.getString(SecondFragment.COL_WEATHER_DESC);
        TextView descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        descriptionView.setText(description);


        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(SecondFragment.COL_WEATHER_MAX_TEMP);
        TextView highView = (TextView) view.findViewById(R.id.list_item_high_textview);
        highView.setText(Utility.formatTemperature(high, isMetric));

        double low = cursor.getDouble(SecondFragment.COL_WEATHER_MIN_TEMP);
        TextView lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
        lowView.setText(Utility.formatTemperature(low, isMetric));
    }
    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_2, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */

}

