package com.example.android.happytravellair;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FlightFragment extends Fragment {

    private ArrayAdapter<String> mFlightsAdapter;

    public FlightFragment() {
    }

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.flightfragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchFLightTask flightTask = new FetchFLightTask();
            flightTask.execute("94043");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] data = {
                "A to B - 23/02/45",
                "C to B - 23/02/45",
                "B to C - 23/02/45",
                "A to D - 23/02/45",
                "D to B - 23/02/45",
                "A to B - 20/02/45",
                "B to A - 20/02/45",
                "A to B - 25/02/45",
                "A to D - 26/02/45",
                "___",
                "A to B - 23/02/45",
                "C to B - 23/02/45",
                "B to C - 23/02/45",
                "A to D - 23/02/45",
                "D to B - 23/02/45",
        };
        List<String> flights = new ArrayList<String>(Arrays.asList(data));
        mFlightsAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_flight,
                R.id.list_item_flight_textview,
                flights);

        ListView listview = (ListView) rootView.findViewById(R.id.listview_flights);
        listview.setAdapter(mFlightsAdapter);

        return rootView;
    }

    public class FetchFLightTask extends AsyncTask<String, Void, String[]>{

        private final String LOG_TAG = FetchFLightTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            String zipCode = params[0];
            if(zipCode.length() == 0)
                return null;

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String flightJsonStr = null;

            String format = "json";
            String units = "metric";
            int numOfDays = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FLIGHT_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "appid";

                Uri builtUri = Uri.parse(FLIGHT_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, zipCode)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numOfDays))
                        .appendQueryParameter(APPID_PARAM,"?").build();
                        /*.appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numOfDays)*/


                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI: " + builtUri.toString());

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
                flightJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Json String: " + flightJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
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

            /*try{
                return getWeatherDataFromJson(flightJsonStr, numOfDays);
            }catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }*/
            return null;
        }

        /*@Override
        protected void onPostExecute(String[] result) {
            if(result != null) {
                mFlightsAdapter.clear();
                for(String s: result){
                    mFlightsAdapter.add(s);
                }
            }
        }*/
    }
}
