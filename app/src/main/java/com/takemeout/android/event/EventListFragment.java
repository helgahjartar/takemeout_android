package com.takemeout.android.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.takemeout.android.R;
import com.takemeout.android.event.projections.EventOverviewProjection;
import java.sql.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Vector;

public class EventListFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_list, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void displayEvents(EventOverviewProjection[] events) {
        HashMap<Date,Vector<Integer>> map = getDateIndexMap(events);

        for (Date d : map.keySet()) {
            LinearLayout list = (LinearLayout) view.findViewById(R.id.entry_layout);
            String date = DateFormat.getDateInstance().format(d);
            TextView dateTextView = getDateTextView();
            dateTextView.setText(date);

            LinearLayout ll = getDateTextViewContainer();
            ll.addView(dateTextView);
            list.addView(ll);

            for (int i : map.get(d)) {
                EventListEntryFragment entry = EventListEntryFragment.newInstance(events[i]);
                getChildFragmentManager().beginTransaction().add(R.id.entry_layout, entry).commit();
                getChildFragmentManager().executePendingTransactions();
            }
        }
    }

    private LinearLayout getDateTextViewContainer() {
        LinearLayout ll = new LinearLayout(view.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, -1, 0, -1);
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (16*scale + 0.5f);
        ll.setPadding(dpAsPixels, 0, dpAsPixels, 0);
        ll.setLayoutParams(params);
        ll.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.orange));
        return ll;
    }

    private TextView getDateTextView() {
        TextView textView = new TextView(view.getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setTextSize(24);
        textView.setPadding(10,10,10,10);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
        return textView;
    }

    private HashMap<Date,Vector<Integer>> getDateIndexMap(EventOverviewProjection[] events) {
        HashMap<Date,Vector<Integer>> map = new HashMap<>();
        for (int i = 0; i < events.length; i++) {
            if (!map.containsKey(events[i].getTime())) map.put(events[i].getTime(), new Vector<Integer>());
            map.get(events[i].getTime()).add(i);
        }
        return map;
    }
}
