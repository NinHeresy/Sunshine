package em.android.sunshine.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import em.android.sunshine.R;
import em.android.sunshine.SecondFragment;
import em.android.sunshine.data.WeatherContract;
import em.android.sunshine.utility.Utility;

/**
 * Created by emanu on 22/12/2016.
 */

    public class ForecastRecyclerAdapter extends CursorAdapter {

        private static final int VIEW_TYPE_COUNT = 2;
        private static final int VIEW_TYPE_TODAY = 0;
        private static final int VIEW_TYPE_FUTURE_DAY = 1;

        // Flag to determine if we want to use a separate view for "today".
        private boolean mUseTodayLayout = true;

    public ForecastRecyclerAdapter(FragmentActivity activity) {
        super();
    }

    /**
         * Cache of the children views for a forecast list item.
         */
        public static class ViewHolder {
            public final ImageView mIconView;
            public final TextView mDateView;
            public final TextView mDescriptionView;
            public final TextView mHighTempView;
            public final TextView mLowTempView;

            public ViewHolder(View view) {
                mIconView = (ImageView) view.findViewById(R.id.list_item_icon);
                mDateView = (TextView) view.findViewById(R.id.list_item_date_textview);
                mDescriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
                mHighTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
                mLowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
            }
        }

        public ForecastRecyclerAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // Choose the layout type
            int viewType = getItemViewType(cursor.getPosition());
            int layoutId = -1;
            switch (viewType) {
                case VIEW_TYPE_TODAY: {
                    layoutId = R.layout.list_item_forecast_today;
                    break;
                }
                case VIEW_TYPE_FUTURE_DAY: {
                    layoutId = R.layout.item_lista_2;
                    break;
                }
            }

            View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            ViewHolder viewHolder = (ViewHolder) view.getTag();
            int weatherId = cursor.getInt(SecondFragment.COL_WEATHER_CONDITION_ID);
            int defaultImage;

            switch (getItemViewType(cursor.getPosition())) {
                case VIEW_TYPE_TODAY:
                    defaultImage = Utility.getArtResourceForWeatherCondition(weatherId);
                    break;
                default:
                    defaultImage = Utility.getIconResourceForWeatherCondition(weatherId);
            }

            if (Utility.usingLocalGraphics(mContext)) {
                viewHolder.mIconView.
                        setImageResource(defaultImage);
            } else {
                Glide.with(mContext)
                        .load(Utility.getArtUrlForWeatherCondition(mContext, weatherId))
                        .error(defaultImage)
                        .crossFade()
                        .into(viewHolder.mIconView);
            }

            // Read date from cursor
            long dateInMillis = cursor.getLong(SecondFragment.COL_WEATHER_DATE);
            // Find TextView and set formatted date on it
            viewHolder.mDateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

            // Read weather forecast from cursor
            String description = Utility.getStringForWeatherCondition(mContext, weatherId);
            // Find TextView and set weather forecast on it
            viewHolder.mDescriptionView.setText(description);
            viewHolder.mDescriptionView.setContentDescription(mContext.getString(R.string.a11y_forecast, description));

            // For accessibility, we don't want a content description for the icon field
            // because the information is repeated in the description view and the icon
            // is not individually selectable

            // Read high temperature from cursor
            double high = mCursor.getDouble(SecondFragment.COL_WEATHER_MAX_TEMP);
            String highString = Utility.formatTemperature(mContext, high);
            viewHolder.mHighTempView.setText(highString);
            viewHolder.mHighTempView.setContentDescription(mContext.getString(R.string.a11y_high_temp, highString));

            // Read low temperature from cursor
            double low = mCursor.getDouble(SecondFragment.COL_WEATHER_MIN_TEMP);
            String lowString = Utility.formatTemperature(mContext, low);
            viewHolder.mLowTempView.setText(lowString);
            viewHolder.mLowTempView.setContentDescription(mContext.getString(R.string.a11y_low_temp, lowString));
        }

        public void setUseTodayLayout(boolean useTodayLayout) {
            mUseTodayLayout = useTodayLayout;
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }
    }