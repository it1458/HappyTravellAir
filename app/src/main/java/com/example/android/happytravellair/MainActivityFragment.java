package com.example.android.happytravellair;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mFlightsAdapter;

    public MainActivityFragment() {
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
}
