package com.example.greg.gpssportraker.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.greg.gpssportraker.R;
import com.example.greg.gpssportraker.history.dummy.RouteContent;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.List;


public class HistoryListFragment extends ListFragment {


    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {
        public void onItemSelected(String id);

    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public HistoryListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ArrayList<RouteContent.RouteItem> rcf = new ArrayList<RouteContent.RouteItem>();

        long hlCsize = HistoryLocContainer.count(HistoryLocContainer.class);
        for (int i = 1; i <= hlCsize; i++){
            HistoryLocContainer hlC = HistoryLocContainer.findById(HistoryLocContainer.class, i);
            rcf.add(new RouteContent.RouteItem(Integer.toString(i),
                    hlC.getVehicle(),
                    hlC.getDatum(),
                    hlC.getDistance(),
                    hlC.getElevationGain(),
                    hlC.getMyroute(),
                    hlC.getPolylines(),
                    hlC.getMagassagok(),
                    hlC.getSpeeds(),
                    hlC.getAvgSpeed(),
                    hlC.getNumberOfElements(),
                    hlC.getDuration()));
            RouteContent.addItem(rcf.get(i-1));
        }

        HistoryAdapter hsa = new HistoryAdapter(getActivity().getApplicationContext(),rcf);
        setListAdapter(hsa);

        //setListAdapter(new ArrayAdapter<RouteContent.RouteItem>(getActivity().getApplicationContext(), R.layout.historyrow,RouteContent.ITEMS));
        //new ArrayAdapter<RouteContent.RouteItem>(getActivity().getApplicationContext(),)
        //HistoryAdapter historyAdapter = new HistoryAdapter(getActivity().getApplicationContext(),RouteContent.ITEMS);
        //setListAdapter(historyAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(RouteContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
